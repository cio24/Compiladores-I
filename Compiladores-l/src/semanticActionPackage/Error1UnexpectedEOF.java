package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error1UnexpectedEOF extends SemanticAction {

	public Error1UnexpectedEOF(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.setTokenId('~'); 
		System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Lexical error: Unexpected end of file.");
	}

}
