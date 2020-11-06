package assemblerPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import codeGenerationPackage.*;
import utilitiesPackage.ErrorReceiver;

public class AssemblerGenerator {
	
	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/testingPackage/";
	//public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	
	public static final String FILENAME = "codigo1.txt";

	public static int variablesAuxCounter;
	private RegistersManager rm;
	
	public static BufferedWriter code;

	
	public static SymbolsTable st;
	public static TripletsManager tm;
	//String outfilepath;
	
	public AssemblerGenerator(SymbolsTable st,TripletsManager tm /*,String outfilepath*/) {
		this.st=st;
		this.tm=tm;
		variablesAuxCounter = 0;
		rm = new RegistersManager();
		//this.outfilepath = outfilepath;
	}
	
	public void createAssemblerFile() throws IOException {
		if (/*!ErrorReceiver.hasErrors*/true)
		{
				File assembler = new File(BASE_PATH+FILENAME/*outfilepath*/); ///No vale la pena hacer una clase para esto
				FileOutputStream fos = new FileOutputStream(assembler);
				code = new BufferedWriter(new OutputStreamWriter(fos));
			 
				generateAssembler();
			 
				code.close();

		}
	}
	
	public void generateAssembler() throws IOException {
		generateHeader(); // Genera la primer parte del archivo con todas las librerias y sintaxis requerida
		generateDataSection(); // Genera la seccion donde se vuelca toda la información de la tabla de simbolos
		generateCodeSection(); // Genera la seccion donde se vuelca el codigo
	}
	
	public void writeMov(String dest,String src) throws IOException {
		code.write("MOV "+dest+","+src);
		code.newLine();
	}
	
	public void generateDataSection() throws IOException {
		
	};
	
	public void generateCodeSection() throws IOException {

		for(Triplet t: tm.triplets) {
			String operator = t.getOperator();
			String type = t.getDataType();
			switch (operator) {
				case "+":
					switch (type) {
						case "ULONGINT":
							mulInteger(t);
							break;
						case "DOUBLE":
							break;
					}
					break;

				case "-":
					switch (type) {
						case "ULONGINT":
							break;
						case "DOUBLE":
							break;
					}
					break;

				case "*":
					switch (type) {
						case "ULONGINT":
							break;
						case "DOUBLE":
							break;
					}
					break;

				case "/":
					switch (type) {
						case "ULONGINT":
						case "DOUBLE":
					}
				case "<":
					switch (type) {
						case "ULONGINT":
						case "DOUBLE":
					}
				case "<=":
					switch (type) {
						case "ULONGINT":
						case "DOUBLE":
					}
				case ">":
					switch (type) {
						case "ULONGINT":
						case "DOUBLE":
					}
				case ">=":
					switch (type) {
						case "ULONGINT":
						case "DOUBLE":
					}
				case "==":
					switch (type) {
						case "ULONGINT":
						case "DOUBLE":
					}
				case "=":
				case "OUT":
				case "BF":
				case "BT":
				case "BI":
				case "PDB":
				case "PDE":
				case "PC":
				case "label":
			}
		}
	};
	
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
		
	}

	public void mulInteger(Triplet t) throws IOException {

		//los souce pueden ser un string con una variable o un string con una constante
		//la variable puede ser una que guarde un resultado de un triplet
		String source1 = getSource(t.getFirstOperand());
		String source2 = getSource(t.getSecondOperand());

		//antes de esto se tiene que ver si esta libre y sino, liberarlo y guardar lo que tenia
		//en una variable auxiliar
		Register registerA = rm.getRegister("A");
		Register registerD = rm.getRegister("D");
		if(!registerA.isFree())
			rm.saveValue(registerA);

		if(!registerD.isFree())
			rm.saveValue(registerD);

		//codigo assembler xD
		code.write("MOV " + registerA.getEntire() + "," + source1);
		code.newLine();
		code.write("MUL " + registerA.getEntire() + "," + source2);
		code.newLine();

		//restauramos los registros de haberse guardado
		rm.restore(registerA);
		rm.restore(registerD);

		//guardar el resultado de la operación en el triplet
		t.setResultLocation(registerA.getEntire());
	}

	public String getSource(Operand op){
		String source = op.getRef();

		if(op.getOperandType().equals(Operand.TRIPLET_POINTER))
			source = getTripletAssociated(op).getResultLocation();
		else if(source.contains(":"))
			source =  "_" + source;

		return source;
	}

	public Triplet getTripletAssociated(Operand op){
		String r = op.getRef();
		int tripletNumber = Integer.valueOf(r.substring(1,r.length()-2));
		System.out.println("EL TRIPLETE ASOCIADO ES:" + tripletNumber);
		return tm.getTriplet(tripletNumber);
	}

}
