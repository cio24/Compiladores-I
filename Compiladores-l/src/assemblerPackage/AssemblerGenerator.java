package assemblerPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codeGenerationPackage.*;

public class AssemblerGenerator {
	
	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/testingPackage/";
	public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	//public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	

	public static final String FILENAME = "codigo1.txt";
	private static final String ADD = "ADD";
	private static final String SUB = "SUB";
	private static final String MUL = "MUL";
	private static final String DIV = "DIV";

	public static int variablesAuxCounter;
	//public static BufferedWriter code; DEPRECATED
	private RegistersManager rm;
	private HashMap<String,String> outVars;

	
	public static SymbolsTable st;
	public static TripletsManager tm;
	//String outfilepath;
	
	//Contenedores de codigo assembler que luego se volcaran al archivo
	public static List<String> headerSection; //Seccion de datos del archivo assembler
	
	public static List<String> dataSection; //Seccion de datos del archivo assembler
	
	public static List<List<String>> procList; //La lista de codigos de procedimientos (incluido el main)
	public int actualProcList;
	
	public static List<String> actualCode; //Apunta al procedimiento/main al cual se est� generando codigo actualmente

	
	public AssemblerGenerator(SymbolsTable st,TripletsManager tm /*,String outfilepath*/) {
		AssemblerGenerator.st =st;
		AssemblerGenerator.tm =tm;
		variablesAuxCounter = 0;
		rm = new RegistersManager();
		
		this.outVars = new HashMap<>();
		
		headerSection = new ArrayList<>();
		
		dataSection = new ArrayList<>();
		
		procList = new ArrayList<>();
		procList.add(new ArrayList<>()); //El primer procedimiento seria el main.
		
		actualCode = procList.get(0); 
		actualProcList = 0;
		
		//this.outfilepath = outfilepath;
	}
	
	public void createAssembler() throws IOException {
		if (/*!ErrorReceiver.hasErrors*/true) {
			generateHeader(); // Genera la primer parte del archivo con todas las librerias y sintaxis requerida
			generateCodeSection(); // Genera la seccion donde se vuelca el codigo
			generateDataSection(); // Genera la seccion donde se vuelca toda la informaci�n de la tabla de simbolos
			
			putCodeIntoFile();
		}
	}

	public void generateHeader() throws IOException {

		headerSection.add(".386");
		headerSection.add(".model flat, stdcall");
		headerSection.add("option casemap :none");
		headerSection.add("include \\masm32\\include\\windows.inc");
		headerSection.add("include \\masm32\\include\\kernel32.inc");
		headerSection.add("include \\masm32\\include\\user32.inc");
		headerSection.add("includelib \\masm32\\lib\\kernel32.lib");
		headerSection.add("includelib \\masm32\\lib\\user32.lib");	
	}

	public void generateDataSection(){
		dataSection.add(".data");

		//declaramos una variable que vamos a usar como titulo de todas las ventanas de los mensajes que se impriman
		stringDeclaration("outTitle","OUT message");

		//declaramos una variable por cada OUT que se haga y le asignamos el mensaje
		declareOUTVars();
	};

	public void generateCodeSection() throws IOException {
		actualCode.add(".code");
		actualCode.add("start:");

		for(Triplet t: tm.triplets)
			switch (t.getOperator()){
				case "-":
					subOpInt(t);
					break;
				case "+":
				case "*":
					commutativeOpInt(t);
					break;
				case "/":
					divOnInt(t);
					break;
				case "=":
					assignmentOnInt(t);
					break;
				case "==":
				case "<=":
				case "<":
				case ">":
				case ">=":
					writeIntComparison(t);
					break;
				case "OUT":
					writeOUT(t);
					break;
				case "BF":
				case "BT":
					writeIntConJump(t);
					break;
				case "BI":
					writeUnJump(t);
					break;
				case "PC":
					break;
				case "PDB": {
					procList.add(new ArrayList<>());
					actualProcList++;
					actualCode = procList.get(actualProcList);
					actualCode.add("LABEL DEL PROCEDIMIENTO "+t.getFirstOperand().getRef());
				}
					break;
				case "PDE": {
					actualCode.add("RETURN DEL PROCEDIMIENTO "+t.getFirstOperand().getRef());
					actualProcList--;
					actualCode = procList.get(actualProcList);
				}
					break;
				default:
					//son los labels!!
					writeLabel(t);
			}

		actualCode.add("end start");
	};

	public void stringDeclaration(String varName, String valueAssigned){
		dataSection.add(varName + " db \"" + valueAssigned + "\", 0");
	}

	public String removeApostrophes(String outMessage){
		return outMessage.substring(1,outMessage.length()-1);
	}

	public void writeMov(String dest,String src) throws IOException {
		actualCode.add("MOV "+dest+","+src);
	}

	public void writeOp(String opt, String op1Name, String op2Name) throws IOException {
		actualCode.add( opt + " " + op1Name + ", " + op2Name);
	}

	public void writeOp(String opt, String opName) throws IOException {
		actualCode.add( opt + " " + opName);
	}
	
	public void nonCommutativeOpInt(Triplet t) throws IOException {
		// Obtengo los operandos del triplete
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();
		
		// Extraigo los nombres que usare para representarlos en assembler 
		// (constantes para las constantes, variables precedidas por "_" y registros para los punteros a tripletes)
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm); 
		
		// Valor de operación
		String opt = getAssemblerOpt(t.getOperator());
		
		if(opt.equals("SUB")) {
			// Si es resta debo acomodarlo de manera para que el primer operando sea un registro 
			if (!op1.isPointer()) {
				//obtenemos un registro para pasarle el valor del operando uno
				Register reg = rm.getEntireRegisterFree();
				//generamos el assembler
				actualCode.add( "MOV " + reg.getEntire() + ", " + op1Name);
				op1Name = reg.getEntire();
				reg.setFree(false);
				// ESTO QUEDA op1Name = REGISTRO op2Name = NO REGISTRO
			} 
		} else {
			// Si es division entonces me aseguro que el primer operando se guarde en EAX, que siempre lo tenemos libre
			// Para poder hacer el div			
			actualCode.add( "MOV EAX, " + op1Name);
			actualCode.add( "MOV EDX, 0");
			
			// Libero el registro que usaba op1 ya que lo pase a EAX 
			if (op1.isPointer())
				rm.getRegister(op1Name).setFree(true);				

			// Ahora el primer operando es EAX
			op1Name = "EAX";
		}
			
		if (opt.equals("SUB")) {
			//Generalmos el assembler la operaci�n
			actualCode.add( opt + " " + op1Name + ", " + op2Name);
			
			if(op2.isPointer()) 
				rm.getRegister(op2Name).setFree(true);
		}
		else
		{
			if (op2.isImmediate(st))
			{
				Register r = rm.getEntireRegisterFree();
				actualCode.add( "MOV " + r.getEntire() +", "+ op2Name);
				r.setFree(false);
				op2Name = r.getEntire();
			}
			
			actualCode.add( "DIV " + op2Name);

			if(!op2.isVar()) 
				rm.getRegister(op2Name).setFree(true);
		}
			

		// Debo guardar el resultado en otro registro para liberar EAX
		if (opt.equals("DIV")) {
			// Pido el libre y genero el mov
			Register r = rm.getEntireRegisterFree();
			actualCode.add( "MOV " + r.getEntire() +", EAX");
			
			// Marco como usado el nuevo registro
			op1Name =  r.getEntire();
			r.setFree(false);
			// Libero EAX por las dudas
			rm.getRegister("EAX").setFree(true);
	
		}
		
		//finalmente guardamos el resultado en el terceto
		t.setResultLocation(op1Name);		
		// Libero el segundo si era registro
	}

	public void subOpInt(Triplet t) throws IOException {

		// Obtengo los operandos del triplet
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();

		// Extraigo los nombres que usare para representar los operandos en assembler
		// (constantes para las constantes, variables precedidas por "_" y registros para los punteros a tripletes)
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);

		// como es una resta debeo asegurarme de que el primer operando sea un registro
		if (!op1.isPointer()) {

			//obtenemos un registro para pasarle el valor del primer operando
			Register reg = rm.getEntireRegisterFree();
			writeMov(reg.getEntire(),op1Name);

			//ahora nuestro primer operando es el registro
			op1Name = reg.getEntire();
			reg.setFree(false);

			// ESTO QUEDA op1Name = REGISTRO op2Name = NO REGISTRO
		}

		//Generalmos el assembler la operacion
		writeOp("SUB", op1Name, op1Name);

		//si el segundo operando era un registro lo liberamos
		if(op2.isPointer())
			rm.getRegister(op2Name).setFree(true);

		//finalmente guardamos el resultado en el terceto
		t.setResultLocation(op1Name);
	}

	public void divOnInt(Triplet t) throws IOException {

		// Obtengo los operandos del triplet
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();

		// Extraigo los nombres que usare para representarlos en assembler
		// (constantes para las constantes, variables precedidas por "_" y registros para los punteros a tripletes)
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);

		// como es una division guardo el primer operando en EAX, que siempre lo tenemos libre
		writeMov("EAX",op1Name);
		writeMov("EDX", "0");

		// si el primer operando era un registro lo libero ya que lo pase a EAX
		if (op1.isPointer())
			rm.getRegister(op1Name).setFree(true);

		// Ahora el primer operando es EAX
		op1Name = "EAX";

		//si el segundo operando es un valor inmediato lo movemos a un registro
		if (op2.isImmediate(st)){
			Register r = rm.getEntireRegisterFree();
			writeMov(r.getEntire(),op2Name);
			r.setFree(false);

			//ahora el segundo operando es el registro
			op2Name = r.getEntire();
		}

		//finalmente generamos el assembler de la division
		writeOp("DIV",op2Name);

		//de ser un registro el operando dos lo liberamos porque ya hicimos la operación
		if(op2.isImmediate(st) || op2.isPointer())
			rm.getRegister(op2Name).setFree(true);

		// Debo guardar el resultado de la division en otro registro para mantener libre EAX
		Register r = rm.getEntireRegisterFree();
		writeMov(r.getEntire(),"EAX");

		// Marco como usado el nuevo registro
		op1Name =  r.getEntire();
		r.setFree(false);

		// Libero EAX por las dudas
		rm.getRegister("EAX").setFree(true);

		//finalmente guardamos el resultado en el terceto
		t.setResultLocation(op1Name);
	}

	public void declareOUTVars(){
		int outCounter = 1;
		for(String symbolKey: st.getAll()){

			//obtengo el simbolo asociado a la key
			Symbol s = st.getSymbol(symbolKey);

			//si es un string significa que se quiere imprimir con un OUT
			if(s.getDataType().equals(Symbol._STRING_TYPE)){

				//generamos el asembler que declara una nueva variable para el out y le asigna el string
				stringDeclaration("out" + outCounter, removeApostrophes(s.getLexeme()));

				//guardamos en un mapa el nombre de la variable asociada al string para poder usarlo cuando se quiera imprimir
				outVars.put(s.getLexeme(),"out" + outCounter++);
			}
		}
	}

	public void writeOUT(Triplet t) throws IOException {

		//obtenemos el nombre de la variable que contiene el mensaje a mostrar
		String varName = outVars.get(t.getFirstOperand().getRef());

		//generamos el assembler para mostrar el mensaje en pantalla
		actualCode.add("invoke MessageBox, NULL, addr outTitle, addr " + varName +", MB_OK");
		actualCode.add("invoke ExitProcess, 0");
	}
	
	public void commutativeOpInt(Triplet t) throws IOException{
		// Obtengo los operandos del triplete
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();
		
		// Extraigo los nombres que usare para representarlos en assembler 
		// (constantes para las constantes, variables precedidas por "_" y registros para los punteros a tripletes)
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);
		
		// Valor de operación
		String opt = getAssemblerOpt(t.getOperator());
		
		// Si es suma debo acomodarlo de manera para que el primer operando sea un registro 
		if(opt.equals("ADD")) {
			// Si ninguno es registro, entonces muevo el primer operando a un registro 
			// y utilizo este registro como primer operando de la suma
			if (!op1.isPointer() && !op2.isPointer()) {
				//obtenemos un registro para pasarle el valor del operando uno
				Register reg = rm.getEntireRegisterFree();
					
				//generamos el assembler
				actualCode.add( "MOV " + reg.getEntire() + ", " + op1Name);
				op1Name = reg.getEntire();
				reg.setFree(false);
				// ESTO QUEDA op1Name = REGISTRO op2Name = NO REGISTRO
			} 
			// Si el registro esta en el segundo lugar, les cambio el orden
			else if (!op1.isPointer() && op2.isPointer()) {
				// Switcheo los operandos si el segundo es registro y el primero no		
				Operand opAux = op1;
				op1 = op2;
				op2 = opAux;
				String aux = op1Name;
				op1Name = op2Name;
				op2Name = aux;			
			}
		} else {
			// Si es multiplicacion entonces me aseguro que el primer operando se guarde en EAX, que siempre lo tenemos libre
			// Para poder hacer el mul			
			actualCode.add( "MOV EAX, " + op1Name);
			
			// Libero el registro que usaba op1 ya que lo pase a EAX 
			if (op1.isPointer())
				rm.getRegister(op1Name).setFree(true);				

			// Ahora el primer operando es EAX
			op1Name = "EAX";
		}
			
		//Generalmos el assembler la operaci�n
		actualCode.add( opt + " " + op1Name + ", " + op2Name);

		// ADebo guardar el resultado en otro registro para liberar EAX
		if (opt.equals("MUL")) {
			// Pido el libre y genero el mov
			Register r = rm.getEntireRegisterFree();
			actualCode.add( "MOV " + r.getEntire() +", EAX");
			
			// Marco como usado el nuevo registro
			op1Name =  r.getEntire();
			r.setFree(false);
			// Libero EAX por las dudas
			rm.getRegister("EAX").setFree(true);		
		}
		
		//finalmente guardamos el resultado en el terceto
		t.setResultLocation(op1Name);		
		// Libero el segundo si era registro
		if(op2.isPointer()) 
			rm.getRegister(op2Name).setFree(true);
		
	}
	
	public void assignmentOnInt(Triplet t) throws IOException {
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();
		
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm); 
		
		// Si ambos son variables		
		boolean bothVars = op1.isVar() && op2.isVar();
		
		// Tenemos que mover la segunda var a un registro y despues hacer la asignacion
		if(bothVars){
			Register r = rm.getEntireRegisterFree();
			// Muevo la var al registro libre
			actualCode.add("MOV " + r.getEntire() + ", " + op2Name);
			// Guardo como nuevo operando 2 este registro
			op2Name = r.getEntire();	
			
			// NO LO SETEO COMO OCUPADO PORQUE SE QUE SI O SI LO LIBERO EN LA SIGUIENTE LINEA (me ahorro esos dos pasos)
		}			

		//finalmente hacemos la asignacion
		writeMov(op1Name,op2Name);
		
		// Si el segundo era un registro lo libero
		if(op2.isPointer()) 
			rm.getRegister(op2Name).setFree(true);
		
		
	}	
	
	private boolean isConmutative(String opt) {
		return opt.equals("+") || opt.equals("*");
	}

	private void writeIntComparison(Triplet t) throws IOException {
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();

		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);

		//el primer operando no puede ser inmediato, asi que lo pasamos a un registro
		if(op1.isImmediate(st)){
			Register r = rm.getEntireRegisterFree();
			writeMov(r.getEntire(),op2Name);
			r.setFree(false);
			op1Name = r.getEntire();
		}

		//generamos el assembler
		actualCode.add("CMP " + op1Name + ", " + op2Name);

		//liberamos el registro donde se guardaba el primer operando, ya que el resultado de la comparacion se ve reflejado en algunos flags
		rm.getRegister(op1Name).setFree(true);

		//si el segundo operando era un registro lo liberamos
		if(op2.isPointer())
			rm.getRegister(op2Name).setFree(true);

		//como el resultado quedo guardado en los flags tampoco hace falta guardarlo en el terceto
	}

	private void writeIntConJump(Triplet t) throws IOException {

		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();
		Triplet tCondition;

		if(t.getOperator().equals("BF")){
			tCondition = tm.getTriplet(Integer.parseInt(op1.getRef()));
			switch (tCondition.getOperator()){
				case "<":
					actualCode.add( "JNB " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case "<=":
					actualCode.add( "JNBE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case ">":
					actualCode.add( "JNA " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case">=":
					actualCode.add( "JNAE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case "==":
					actualCode.add( "JNEE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
			}
		}
		else{
			//es un salto pro true (BT) para los LOOP UNTIL
			tCondition = tm.getTriplet(Integer.parseInt(op1.getRef()));
			switch (tCondition.getOperator()){
				case "<":
					actualCode.add( "JB " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case "<=":
					actualCode.add( "JNA " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case ">":
					actualCode.add( "JA " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case">=":
					actualCode.add( "JNB " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case "==":
					actualCode.add( "JE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
			}

		}
	}

	private void writeLabel(Triplet t) throws IOException {
		actualCode.add(t.getOperator() + ":");
	}

	private String getAssemblerOpt(String opt) {
		switch(opt){
			case "+": return ADD;
			case "-": return SUB;
			case "*": return MUL;
			case "/": return DIV;
		}
		return "unknownOperator";
	}

	private void writeUnJump(Triplet t) throws IOException {
		actualCode.add( "JMP " + t.getFirstOperand().getRef());
	}
	
	public void putCodeIntoFile() throws IOException {
		BufferedWriter code;
		File assembler = new File(BASE_PATH+FILENAME/*outfilepath*/); ///No vale la pena hacer una clase para esto
		FileOutputStream fos;
		fos = new FileOutputStream(assembler);
		code = new BufferedWriter(new OutputStreamWriter(fos));

		code.write("----------->COMIENZO HEADER");
		code.newLine();
		
		for (int i = 0; i<headerSection.size();i++) {
			code.write(headerSection.get(i));
			code.newLine();
		}
		
		code.write("----------->COMIENZO DATA");
		code.newLine();

		for (int i = 0; i<dataSection.size();i++) {
			code.write(dataSection.get(i));
			code.newLine();
		}
		
		code.write("----------->COMIENZO PROCEDIMIENTOS");
		code.newLine();

		for (int i = procList.size()-1; i>0; i--)
		{
			List<String> thisList=procList.get(i);
			for (int j=0; j<thisList.size(); j++) {
				code.write(thisList.get(j));
				code.newLine();
			}
		}
		
		code.write("----------->COMIENZO MAIN");
		code.newLine();
		
		for (int i=0; i<procList.get(0).size(); i++) {
			code.write(procList.get(0).get(i));
			code.newLine();
		}
	
		code.close();
	}

}
