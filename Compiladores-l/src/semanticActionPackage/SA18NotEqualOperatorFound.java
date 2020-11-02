package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import utilitiesPackage.Constants;

public class SA18NotEqualOperatorFound extends SemanticAction {

	public SA18NotEqualOperatorFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		lexicalAnalyzer.setTokenId(Constants.NEQUAL);
	}

}