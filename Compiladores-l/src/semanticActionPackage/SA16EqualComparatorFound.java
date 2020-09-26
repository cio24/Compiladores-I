package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefullClassesPackage.Constants;


public class SA16EqualComparatorFound extends SemanticAction {

	public SA16EqualComparatorFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		lexicalAnalyzer.setTokenId(Constants.COMPARADOR_IGUAL);
	}

}