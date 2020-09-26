package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA13LessThanFound extends SemanticAction{

	public SA13LessThanFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		lexicalAnalyzer.setTokenId((int)'<');
	}

}
