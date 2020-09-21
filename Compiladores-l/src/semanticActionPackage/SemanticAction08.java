package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.Constants;

public class SemanticAction08 extends SemanticAction {

	public SemanticAction08(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		//ingresar en la talba de simbolos
		
		lexicalAnalyzer.setTokenId(Constants.CONSTANTE_CADENA_CARACTERES);
	}

}