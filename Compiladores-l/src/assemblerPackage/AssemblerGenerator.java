package assemblerPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import codeGenerationPackage.*;

public class AssemblerGenerator {
	
	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/testingPackage/";
	//public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	

	public static final String FILENAME = "codigo1.txt";
	private static final String ADD = "ADD";
	private static final String SUB = "SUB";
	private static final String MUL = "MUL";
	private static final String DIV = "DIV";

	public static int variablesAuxCounter;
	private RegistersManager rm;
	public static BufferedWriter code;

	
	public static SymbolsTable st;
	public static TripletsManager tm;
	//String outfilepath;
	
	public AssemblerGenerator(SymbolsTable st,TripletsManager tm /*,String outfilepath*/) {
		AssemblerGenerator.st =st;
		AssemblerGenerator.tm =tm;
		variablesAuxCounter = 0;
		rm = new RegistersManager();
		//this.outfilepath = outfilepath;
	}
	
	public void createAssemblerFile() throws IOException {
		if (/*!ErrorReceiver.hasErrors*/true) {
			File assembler = new File(BASE_PATH+FILENAME/*outfilepath*/); ///No vale la pena hacer una clase para esto
			FileOutputStream fos = new FileOutputStream(assembler);
			code = new BufferedWriter(new OutputStreamWriter(fos));
			generateAssembler();
			code.close();
		}
	}
	
	public void generateAssembler() throws IOException {
		generateHeader(); // Genera la primer parte del archivo con todas las librerias y sintaxis requerida
		generateCodeSection(); // Genera la seccion donde se vuelca el codigo
		generateDataSection(); // Genera la seccion donde se vuelca toda la informaci�n de la tabla de simbolos
	}

	public void generateHeader() throws IOException {

		code.write(".386");
		code.newLine();
		code.write(".model flat, stdcall");
		code.newLine();
		code.write("option casemap :none");
		code.newLine();
		code.write("include \\masm32\\include\\windows.inc");
		code.newLine();
		code.write("include \\masm32\\include\\kernel32.inc");
		code.newLine();
		code.write("include \\masm32\\include\\user32.inc");
		code.newLine();
		code.write("includelib \\masm32\\lib\\kernel32.lib");
		code.newLine();
		code.write("includelib \\masm32\\lib\\user32.lib");
		code.newLine();
		code.newLine();

	}

	public void generateCodeSection() throws IOException {
		for(Triplet t: tm.triplets)
			if(t.getOperator().equals("="))
				assignmentOnInt(t);
			else if(isConmutative(t.getOperator()))
				commutativeOpInt(t);
			else if(t.getOperator().equals("/"))
				divOnInt(t);
			else if(t.getOperator().equals("-"))
				subOpInt(t);
	};

	public void generateDataSection() throws IOException {
		
	};

	public void writeMov(String dest,String src) throws IOException {
		code.write("MOV "+dest+","+src);
		code.newLine();
	}

	public void writeOp(String opt, String op1Name, String op2Name) throws IOException {
		code.write( opt + " " + op1Name + ", " + op2Name);
		code.newLine();
	}

	public void writeOp(String opt, String opName) throws IOException {
		code.write( opt + " " + opName);
		code.newLine();
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
				code.write( "MOV " + reg.getEntire() + ", " + op1Name);
				code.newLine();
				op1Name = reg.getEntire();
				reg.setFree(false);
				// ESTO QUEDA op1Name = REGISTRO op2Name = NO REGISTRO
			} 
		} else {
			// Si es division entonces me aseguro que el primer operando se guarde en EAX, que siempre lo tenemos libre
			// Para poder hacer el div			
			code.write( "MOV EAX, " + op1Name);
			code.newLine();
			code.write( "MOV EDX, 0");
			code.newLine();
			
			// Libero el registro que usaba op1 ya que lo pase a EAX 
			if (op1.isPointer())
				rm.getRegister(op1Name).setFree(true);				

			// Ahora el primer operando es EAX
			op1Name = "EAX";
		}
			
		if (opt.equals("SUB")) {
			//Generalmos el assembler la operaci�n
			code.write( opt + " " + op1Name + ", " + op2Name);
			code.newLine();
			
			if(op2.isPointer()) 
				rm.getRegister(op2Name).setFree(true);
		}
		else
		{
			if (op2.isImmediate(st))
			{
				Register r = rm.getEntireRegisterFree();
				code.write( "MOV " + r.getEntire() +", "+ op2Name);
				code.newLine();
				r.setFree(false);
				op2Name = r.getEntire();
			}
			
			code.write( "DIV " + op2Name);
			code.newLine();		

			if(!op2.isVar()) 
				rm.getRegister(op2Name).setFree(true);
		}
			

		// Debo guardar el resultado en otro registro para liberar EAX
		if (opt.equals("DIV")) {
			// Pido el libre y genero el mov
			Register r = rm.getEntireRegisterFree();
			code.write( "MOV " + r.getEntire() +", EAX");
			code.newLine();
			
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
				code.write( "MOV " + reg.getEntire() + ", " + op1Name);
				code.newLine();
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
			code.write( "MOV EAX, " + op1Name);
			code.newLine();
			
			// Libero el registro que usaba op1 ya que lo pase a EAX 
			if (op1.isPointer())
				rm.getRegister(op1Name).setFree(true);				

			// Ahora el primer operando es EAX
			op1Name = "EAX";
		}
			
		//Generalmos el assembler la operaci�n
		code.write( opt + " " + op1Name + ", " + op2Name);
		code.newLine();

		// ADebo guardar el resultado en otro registro para liberar EAX
		if (opt.equals("MUL")) {
			// Pido el libre y genero el mov
			Register r = rm.getEntireRegisterFree();
			code.write( "MOV " + r.getEntire() +", EAX");
			code.newLine();
			
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
		
		// Tenemos que mover la segunda var a un registro y depsues hacer la asignacion
		if(bothVars){
			Register r = rm.getEntireRegisterFree();
			// Muevo la var al registro libre
			AssemblerGenerator.code.write("MOV " + r.getEntire() + ", " + op2Name);
			code.newLine();
			// Guardo como nuevo operando 2 este registro
			op2Name = r.getEntire();	
			
			// NO LO SETEO COMO OCUPADO PORQUE SE QUE SI O SI LO LIBERO EN LA SIGUIENTE LINEA (me ahorro esos dos pasos)
		}			

		//finalmente hacemos la asignaci�n
		AssemblerGenerator.code.write("MOV " + op1Name + ", " + op2Name);
		code.newLine();		
		
		// Si el segundo era un registro lo libero
		if(op2.isPointer()) 
			rm.getRegister(op2Name).setFree(true);
		
		
	}	
	
	private boolean isConmutative(String opt) {
		return opt.equals("+") || opt.equals("*");
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

}
