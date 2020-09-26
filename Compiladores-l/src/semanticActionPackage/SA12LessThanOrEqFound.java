package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefullClassesPackage.Constants;


public class SA12LessThanOrEqFound extends SemanticAction {

	public SA12LessThanOrEqFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		lexicalAnalyzer.setTokenId(Constants.COMPARADOR_MEN_IGUAL);
	}

}