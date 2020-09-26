package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA11SingleCharTokenFound extends SemanticAction{

	public SA11SingleCharTokenFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.setTokenId((int)lexicalAnalyzer.getLastCharactedRead());
	}
	
}
