package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA15GreaterThanFound extends SemanticAction{

	public SA15GreaterThanFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		lexicalAnalyzer.setTokenId((int)'>');
	}

}
