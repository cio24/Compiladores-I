package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA19EOFFound extends SemanticAction{

	public SA19EOFFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.setTokenId('~');
	}
	
}
