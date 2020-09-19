package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction01 extends SemanticAction {
	
	public SemanticAction01(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.initializeLexem();
		lexicalAnalyzer.addNextCharacter();
	}
}
