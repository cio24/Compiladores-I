package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA02AddNextChar extends SemanticAction{

	public SA02AddNextChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.addNextCharacter();
	}

}
