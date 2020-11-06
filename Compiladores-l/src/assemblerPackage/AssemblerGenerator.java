package assemblerPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import codeGenerationPackage.Operand;
import codeGenerationPackage.SymbolsTable;
import codeGenerationPackage.Triplet;
import codeGenerationPackage.TripletsManager;
import utilitiesPackage.ErrorReceiver;

public class AssemblerGenerator {
	
	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/testingPackage/";
	//public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	
	public static final String FILENAME = "codigo1.txt";

	private int variablesAuxCounter;
	private RegistersManager rm;
	
	static BufferedWriter code;

	
	SymbolsTable st;
	TripletsManager tm;
	//String outfilepath;
	
	public AssemblerGenerator(SymbolsTable st,TripletsManager tm /*,String outfilepath*/) {
		this.st=st;
		this.tm=tm;
		this.variablesAuxCounter = 0;
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
		String source1 = getSource(t.getFirstOperand());
		String source2 = getSource(t.getSecondOperand());
		String reg = rm.getHalfRegister();

		code.write("MOV " + reg + "," + source1);
		code.newLine();
		code.write("MUL " + reg + "," + source2);
		code.newLine();
		code.write("MOV @aux" + (++variablesAuxCounter) + "," + reg);
		code.newLine();
	}


	public Triplet getTripletAssociated(Operand op){
		String r = op.getRef();
		int tripletNumber = Integer.valueOf(r.substring(1,r.length()-2));
		System.out.println("EL TRIPLETE ASOCIADO ES:" + tripletNumber);
		return tm.getTriplet(tripletNumber);
	}

	public String getSource(Operand op){
		String source = op.getRef();

		if(op.getOperandType().equals(Operand.TRIPLET_POINTER))
			source = getTripletAssociated(op).getResultLocation();

		if(source.contains(":"))
				source =  "_" + source;

		return source;
	}
}
