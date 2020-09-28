package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error2UnexpectedChar extends SemanticAction{

	public Error2UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] ERROR léxico: Mala definición.");
	}

}
