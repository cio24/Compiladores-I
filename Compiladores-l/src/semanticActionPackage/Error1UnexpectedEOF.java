package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error1UnexpectedEOF extends SemanticAction {

	public Error1UnexpectedEOF(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.setTokenId('~'); 
		System.out.println("[Linea " + lexicalAnalyzer.getCurrentLine() + "] ERROR lexico: Fin de archivo inesperado.");
	}

}
