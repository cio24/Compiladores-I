package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error1 extends SemanticAction {

	public Error1(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.setTokenId('~'); 
		System.out.println("Error Lexico en linea " + lexicalAnalyzer.getCurrentLine() + ": Fin de archivo inesperado");
	}

}
