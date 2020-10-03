package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.Constants;


public class SA14GreaterThanOrEqFound extends SemanticAction {

	public SA14GreaterThanOrEqFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		lexicalAnalyzer.setTokenId(Constants.GREATEQUAL);
	}

}