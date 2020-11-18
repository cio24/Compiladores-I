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
	//public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	

	public static final String FILENAME = "codigo1.txt";

	public static int variablesAuxCounter;
	//public static BufferedWriter code; DEPRECATED
	private RegistersManager rm;
	private int outCounter;

	
	public static SymbolsTable st;
	public static TripletsManager tm;
	//String outfilepath;
	
	//Contenedores de codigo assembler que luego se volcaran al archivo
	public static List<String> headerSection; //Seccion de datos del archivo assembler
	
	public static List<String> dataSection; //Seccion de datos del archivo assembler
	
	public static List<List<String>> procList; //La lista de codigos de procedimientos (incluido el main)
	public int actualProcList;
	
	public static List<String> actualCode; //Apunta al procedimiento/main al cual se est� generando codigo actualmente
	
	public String procLabelPrefix = "PROCLabel"; 

	public int procLabelNumberCounter; //Cuenta la cantidad de labels de procedimiento usados para identificarlos unicamente
	
	HashMap<String, String> procLabels; //Registra los labels asociados a cada nombre de procedimiento


	public AssemblerGenerator(SymbolsTable st,TripletsManager tm /*,String outfilepath*/) {
		AssemblerGenerator.st =st;
		AssemblerGenerator.tm =tm;
		variablesAuxCounter = 0;
		this.outCounter = 0;
		rm = new RegistersManager();
		
		headerSection = new ArrayList<>();
		
		dataSection = new ArrayList<>();
		
		procList = new ArrayList<>();
		procList.add(new ArrayList<>()); //El primer procedimiento seria el main.
		
		actualCode = procList.get(0); 
		actualProcList = 0;
		
		procLabelNumberCounter = 0; 
		
		procLabels = new HashMap<String, String>();
		
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

	public void putCodeIntoFile() throws IOException { //Itera por todas las listas de codigo en orden para ponerlas en el archivo de codigo assembler
		BufferedWriter code;
		File assembler = new File(BASE_PATH+FILENAME/*outfilepath*/); ///No vale la pena hacer una clase para esto
		FileOutputStream fos;
		fos = new FileOutputStream(assembler);
		code = new BufferedWriter(new OutputStreamWriter(fos));

		for (int i = 0; i<headerSection.size();i++) {
			code.write(headerSection.get(i));
			code.newLine();
		}

		code.newLine();

		code.write(".DATA");
		code.newLine();
		code.newLine();

		for (int i = 0; i<dataSection.size();i++) {
			code.write(dataSection.get(i));
			code.newLine();
		}

		code.newLine();

		code.write(".CODE");
		code.newLine();
		code.newLine();

		for (int i = procList.size()-1; i>0; i--) //Se hace en este orden para que los procedimientos mas "adentro" en el anidamiento queden arriba
		{
			List<String> thisList=procList.get(i);
			for (int j=0; j<thisList.size(); j++) {
				code.write(thisList.get(j));
				code.newLine();
			}
		}

		code.newLine();

		code.write(".START");
		code.newLine();
		code.newLine();

		for (int i=0; i<procList.get(0).size(); i++) {
			code.write(procList.get(0).get(i));
			code.newLine();
		}

		code.newLine();

		code.write("END START");
		code.close();
	}

	public void generateHeader() {
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
		//declaramos una variable que vamos a usar como titulo de todas las ventanas de los mensajes que se impriman
		dataSection.add("outTitle" + " db \"" + "OUT message" + "\", 0");
	};

	public void generateCodeSection() {

		for(Triplet t: tm.triplets)
			switch (t.getOperator()){
				case "-":
					if(t.getDataType().equals(Symbol._ULONGINT_TYPE))
						subOpInt(t);
					else
						writeAritOpOnDouble(t);
					break;
				case "+":
				case "*":
					if(t.getDataType().equals(Symbol._ULONGINT_TYPE))
						commutativeOpInt(t);
					else
						writeAritOpOnDouble(t);
					break;
				case "/":
					if(t.getDataType().equals(Symbol._ULONGINT_TYPE))
						divOnInt(t);
					else
						writeAritOpOnDouble(t);
					break;
				case "=":
					if(t.getDataType().equals(Symbol._ULONGINT_TYPE))
						assignmentOnInt(t);
					else
						assignmentOnDouble(t);
					break;
				case "==":
				case "<=":
				case "<":
				case ">":
				case ">=":
					if(t.getDataType().equals(Symbol._ULONGINT_TYPE))
						writeIntComparison(t);
					else
						writeDoubleComparison(t);
					break;
				case "OUT":
					writeOUT(t);
					break;
				case "BF":
				case "BT":
					writeConJump(t);
					break;
				case "BI":
					actualCode.add( "JMP " + t.getFirstOperand().getRef());
					break;
				case "PC":
					//Se consulta la label del procedimiento siendo llamado (siempre debe existir esta label, ya que necesariamente se paso por  
					//la declaracion del procedimiento antes de poder realizar su llamada)
					String labelOfProcedure = procLabels.get(t.getFirstOperand().getRef());
					//Se llama a la label adquirida
					actualCode.add("CALL "+labelOfProcedure);
					break;
				case "PDB":

					//Se inicializa una nueva label para el procedimiento siendo declarado
					String label = procLabelPrefix+procLabelNumberCounter; 
					procLabelNumberCounter++;
					//Se registra la nueva label para el procedimiento
					procLabels.put(t.getFirstOperand().getRef(),label);

					
					procList.add(new ArrayList<>()); //Se genera una nueva lista de codigo para este procedimiento
					actualProcList++;
					actualCode = procList.get(actualProcList);
					actualCode.add(label+":");
					break;
				case "PDE":
					actualCode.add("RET");
					actualProcList--;
					actualCode = procList.get(actualProcList);
					break;
				default:
					//son los labels!!
					actualCode.add(t.getOperator() + ":");
			}

	};

	//############## CADENAS ##############

	public String removeApostrophes(String outMessage){
		return outMessage.substring(1,outMessage.length() - 1);
	}

	public void writeOUT(Triplet t) {

		//Creamos una variable a la cual le vamos a asignar el valor del string
		String varName = "@out" + ++outCounter;

		//generamos el asembler que declara la nueva variable para el out y se le asigna el string
		dataSection.add(varName + " db \"" + removeApostrophes(t.getFirstOperand().getRef()) + "\", 0");


		//generamos el assembler para mostrar el mensaje en pantalla
		actualCode.add("invoke MessageBox, NULL, addr outTitle, addr " + varName +", MB_OK");
		actualCode.add("invoke ExitProcess, 0");
	}


	//############## ENTEROS ##############

	public void subOpInt(Triplet t) {

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
			actualCode.add("MOV " + reg.getEntire() + "," + op1Name);

			//ahora nuestro primer operando es el registro
			op1Name = reg.getEntire();
			reg.setFree(false);

			// ESTO QUEDA op1Name = REGISTRO op2Name = NO REGISTRO
		}

		//Generalmos el assembler la operacion
		actualCode.add( "SUB" + " " + op1Name + ", " + op2Name);

		//si el segundo operando era un registro lo liberamos
		if(op2.isPointer())
			rm.getRegister(op2Name).setFree(true);

		//finalmente guardamos el resultado en el terceto
		t.setResultLocation(op1Name);
	}

	public void divOnInt(Triplet t) {

		// Obtengo los operandos del triplet
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();

		// Extraigo los nombres que usare para representarlos en assembler
		// (constantes para las constantes, variables precedidas por "_" y registros para los punteros a tripletes)
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);

		// como es una division guardo el primer operando en EAX, que siempre lo tenemos libre
		actualCode.add("MOV " + "EAX" + "," + op1Name);
		actualCode.add("MOV " + "EDX" + "," + "0");

		// si el primer operando era un registro lo libero ya que lo pase a EAX
		if (op1.isPointer())
			rm.getRegister(op1Name).setFree(true);

		// Ahora el primer operando es EAX
		op1Name = "EAX";

		//si el segundo operando es un valor inmediato lo movemos a un registro
		if (op2.isImmediate(st)){
			Register r = rm.getEntireRegisterFree();
			actualCode.add("MOV " + r.getEntire() + "," + op1Name);
			r.setFree(false);

			//ahora el segundo operando es el registro
			op2Name = r.getEntire();
		}

		//finalmente generamos el assembler de la division
		actualCode.add( "DIV" + " " + op2Name);

		//de ser un registro el operando dos lo liberamos porque ya hicimos la operación
		if(op2.isImmediate(st) || op2.isPointer())
			rm.getRegister(op2Name).setFree(true);

		// Debo guardar el resultado de la division en otro registro para mantener libre EAX
		Register r = rm.getEntireRegisterFree();
		actualCode.add("MOV " + r.getEntire() + "," + "EAX");

		// Marco como usado el nuevo registro
		op1Name =  r.getEntire();
		r.setFree(false);

		// Libero EAX por las dudas
		rm.getRegister("EAX").setFree(true);

		//finalmente guardamos el resultado en el terceto
		t.setResultLocation(op1Name);
	}
	
	public void commutativeOpInt(Triplet t) {
		// Obtengo los operandos del triplete
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();
		
		// Extraigo los nombres que usare para representarlos en assembler 
		// (constantes para las constantes, variables precedidas por "_" y registros para los punteros a tripletes)
		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);
		
		// Valor de operacion
		String opt = getAssemblerOpt(t.getOperator(),t.getDataType());
		
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
	
	public void assignmentOnInt(Triplet t) {
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
		actualCode.add("MOV " + op1Name + "," + op2Name);
		
		// Si el segundo era un registro lo libero
		if(op2.isPointer()) 
			rm.getRegister(op2Name).setFree(true);
		
		
	}

	private void writeIntComparison(Triplet t) {
		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();

		String op1Name = op1.getAssemblerReference(tm);
		String op2Name = op2.getAssemblerReference(tm);

		//el primer operando no puede ser inmediato, asi que lo pasamos a un registro
		if(op1.isImmediate(st)){
			Register r = rm.getEntireRegisterFree();
			actualCode.add("MOV " + r.getEntire() + "," + op2Name);

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


	//############## FLOTANTES ##############

	public void writeAritOpOnDouble(Triplet t) {

		//si es un inmediato se obtiene el nombre de una nueva variable auxiliar donde se guarda el resultado
		//sino se obtiene el assemblerReference
		String op1Name  = getDoubleVarName(t.getFirstOperand());
		String op2Name  = getDoubleVarName(t.getSecondOperand());

		//cargo en el ST de la pila lo que hay que la variable del operando 1
		actualCode.add("FLD " + op1Name);

		String opt = getAssemblerOpt(t.getOperator(),t.getDataType());

		//sumo, resto, mul o div el tope de la pila con el operando dos y queda gurdado en el tope de la pila
		actualCode.add(opt + " " + op2Name);

		//guardo el resultado en una variable auxiliar que se guarda en la tabla de simbolos
		String result = getNewVarAux();
		actualCode.add("FST " + result);

	}

	public String getDoubleVarName(Operand op) {
		String opName  = op.getAssemblerReference(tm);

		//como no vi que haya instrucciones para flotantes inmediatos, se crea una variable
		// para guardar c/u de estos y se guarda en la tabla de simbolos
		if(op.isImmediate(st))
			opName = immediateDoubleToVar(opName);

		return opName;
	}

	public String immediateDoubleToVar(String immediate) {
		String opName = "@aux" + ++variablesAuxCounter;

		//guardo la nueva variable en la tabla de simbolos
		st.addSymbol(opName,new Symbol(opName,Symbol._IDENTIFIER_LEXEME,Symbol._DOUBLE_TYPE,Symbol._VARIABLE_USE));

		//declaro la variable en el assembler y le asigno el inmediato
		dataSection.add(opName + " DD " + immediate);

		//retorno el nombre que le di a la variable
		return opName;
	}

	public void assignmentOnDouble(Triplet t) {
		//si es un inmediato se obtiene el nombre de una nueva variable auxiliar donde se guarda el resultado
		//sino se obtiene el assemblerReference
		String op2Name  = getDoubleVarName(t.getFirstOperand());

		//cargo en el ST de la pila lo que hay que la variable del operando 2
		actualCode.add("FLD " + op2Name);

		//guardo el resultado en la variable del operando 1
		actualCode.add("FST " + t.getFirstOperand().getAssemblerReference(tm));
	}

	public void writeDoubleComparison(Triplet t) {

		//si es un inmediato se obtiene el nombre de una nueva variable auxiliar donde se guarda el resultado
		//sino se obtiene el assemblerReference
		String op1Name  = getDoubleVarName(t.getFirstOperand());
		String op2Name  = getDoubleVarName(t.getSecondOperand());

		//cargo en el ST de la pila lo que hay que la variable del operando 1
		actualCode.add("FLD " + op1Name);

		//comparo el operando 2 con el operando 1
		actualCode.add("FCOM " + op2Name);

		//almacena la palabra de estado en memoria usando una variable auxiliar que se guarda en la tabla de simbolos
		String varAux = getNewVarAux("mem2bytes");
		actualCode.add("FSTSW " + varAux);
		actualCode.add("MOV " + "AX," + varAux);
		actualCode.add("SAHF");
	}

	//############## OTROS ##############

	private void writeConJump(Triplet t) {

		Operand op1 = t.getFirstOperand();
		Operand op2 = t.getSecondOperand();
		Triplet tCondition;

		if(t.getOperator().equals("BF")){
			tCondition = tm.getTriplet(Integer.parseInt(op1.getRef()));
			switch (tCondition.getOperator()){
				case "<":
					if(t.getOperator().equals("BF"))
						actualCode.add( "JNB " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					else //BT
						actualCode.add( "JB " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case "<=":
					if(t.getOperator().equals("BF"))
						actualCode.add( "JNBE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					else //BT
						actualCode.add( "JNA " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case ">":
					if(t.getOperator().equals("BF"))
						actualCode.add( "JNA " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					else //BT
						actualCode.add( "JA " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case">=":
					if(t.getOperator().equals("BF"))
						actualCode.add( "JNAE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					else //BT
						actualCode.add( "JNB " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					break;
				case "==":
					if(t.getOperator().equals("BF"))
						actualCode.add( "JNEE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
					else //BT
						actualCode.add( "JE " + tm.getTriplet(Integer.parseInt(op2.getRef())).getOperator());
			}
		}
	}

	public String getAssemblerOpt(String opt,String dataType) {
		switch(opt){
			case "+":
				if(dataType.equals(Symbol._ULONGINT_TYPE))
					return "ADD";
				return "FADD";
			case "-":
				if(dataType.equals(Symbol._ULONGINT_TYPE))
					return "SUB";
				return "FSUB";
			case "*":
				if(dataType.equals(Symbol._ULONGINT_TYPE))
					return "MUL";
				return "FMUL";
			case "/":
				if(dataType.equals(Symbol._ULONGINT_TYPE))
					return "DIV";
				return "FDIV";
		}
		return "unknownOperator";
	}

	public String getNewVarAux(String suffix){
		String varName = "@aux" + suffix + ++variablesAuxCounter;
		st.addSymbol(varName,new Symbol(varName,Symbol._IDENTIFIER_LEXEME,Symbol._DOUBLE_TYPE,Symbol._VARIABLE_USE));
		return varName;
	}

	public String getNewVarAux(){
		return getNewVarAux("");
	}


}
