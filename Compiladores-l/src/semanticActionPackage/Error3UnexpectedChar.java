package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error3UnexpectedChar extends SemanticAction{

	public Error3UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] ERROR léxico: Carácter inválido o inesperado.");
	}

}
