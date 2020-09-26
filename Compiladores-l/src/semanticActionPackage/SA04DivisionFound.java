package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA04DivisionFound extends SemanticAction {

	public SA04DivisionFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		lexicalAnalyzer.setTokenId((int)'/');
	}

}
