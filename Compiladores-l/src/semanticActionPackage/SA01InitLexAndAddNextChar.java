package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA01InitLexAndAddNextChar extends SemanticAction {
	
	public SA01InitLexAndAddNextChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.initializeLexeme();
		lexicalAnalyzer.addNextCharacter();
	}
}
