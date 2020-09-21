package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.Constants;

public class SemanticAction18 extends SemanticAction {

	public SemanticAction18(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		lexicalAnalyzer.setTokenId(Constants.COMPARADOR_DISTINTO);
	}

}