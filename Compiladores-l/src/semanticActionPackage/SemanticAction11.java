package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction11 extends SemanticAction{

	public SemanticAction11(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.setTokenId((int)lexicalAnalyzer.getLastCharactedRead());
	}
	
}
