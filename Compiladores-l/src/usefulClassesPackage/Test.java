package usefulClassesPackage;

import java.io.IOException;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Test {
	public static final String TEST1_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/usefulClassesPackage/test.txt";

	public Test() throws IOException {
		LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(TEST1_PATH);
		
		String nombre;
		int token = 0;
		while(token != (int)'~') {
			token = lexicalAnalyzer.getNextToken();			
			nombre = Constants.getConstantName(token);
			if(nombre == null)
				System.out.println("Token: "+(char)token);
			else
				System.out.println("Token: "+nombre);
		}
		System.out.println("Fin de archivo!");
	}
	
}
