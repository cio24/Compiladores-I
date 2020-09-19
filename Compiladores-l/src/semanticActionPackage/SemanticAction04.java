package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction04 extends SemanticAction {

	public SemanticAction04(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		lexicalAnalyzer.setTokenId((int)'/');
	}

}
