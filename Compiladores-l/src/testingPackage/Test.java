package testingPackage;

import lexicalAnalyzerPackage.Parser;
import assemblerPackage.AssemblerGenerator;
import utilitiesPackage.ErrorReceiver;

import java.io.IOException;

public class Test {

	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/testingPackage/testCases/";
	//public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\testCases\\";
	
	public static final String FILENAME = "testCase5.txt";

	
	public static void main(String[] args) throws IOException {
		Test test = new Test();	
	}
	
	public Test() throws IOException {
		test();
	}
	
	public void test() throws IOException {
		Parser p = new Parser(BASE_PATH + FILENAME);
		//p.yydebug = true;
		p.parse();
		p.la.getSymbolsTable().print();
		System.out.println(p.tm);
		if(!ErrorReceiver.hasErrors)
			System.out.println( "Compilacion exitosa");
		else
			System.out.println( "Compilacion fallida");

		String outAssemblerFile = FILENAME.substring(0,FILENAME.indexOf(".")) + ".asm";
		AssemblerGenerator ag= new AssemblerGenerator(p.la.getSymbolsTable(),p.tm, outAssemblerFile);
		ag.createAssembler();
		
		/*

		String nombre;
		int token = 0;
		while(token != -1) {
			token = lexicalAnalyzer.yylex();			
			nombre = Constants.getConstantName(token);
			if(nombre == null)
				System.out.println("Token: "+(char)token);
			else
				System.out.println("Token: "+nombre);
			System.out.println("***********");
		}
		System.out.println("Fin de archivo!");
				*/
	}
	
}
