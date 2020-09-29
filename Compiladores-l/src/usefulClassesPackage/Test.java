package usefulClassesPackage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import lexicalAnalyzerPackage.Parser;
import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Test {

	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/usefulClassesPackage/";
	public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\usefulClassesPackage\\";
	//public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\usefulClassesPackage\\";
	
	public Test() throws IOException {
		test("program4.txt");
		/*
		System.out.println("TEST CADENAS: ");
		test("cadenas.txt");
		System.out.println("* * * * * * * * * * * * * *  * * * * *\n");
		System.out.println("TEST COMENTARIOS: ");
		test("comentarios.txt");
		System.out.println("* * * * * * * * * * * * * *  * * * * *\n");
		System.out.println("TEST DOUBLES: ");
		test("doubles.txt");
		System.out.println("* * * * * * * * * * * * * *  * * * * *\n");
		System.out.println("TEST IDENTIFICADORES: ");
		test("identificadores.txt");
		System.out.println("* * * * * * * * * * * * * *  * * * * *\n");
		System.out.println("TEST PALABRAS RESERVADAS: ");
		test("palabras_reservadas.txt");
		*/
	}
	
	public void test(String fileName) throws IOException {
		Parser p = new Parser(BASE_PATH + fileName);
		//p.yydebug = false;
		p.parse();
		p.la.symbolsTable.print();
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
