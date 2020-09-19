package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error2 extends SemanticAction{

	public Error2(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		System.out.println("Error Lexico en linea " + lexicalAnalyzer.getCurrentLine() + ": Caracter inesperado");
	}

}
