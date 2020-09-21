package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction19 extends SemanticAction{

	public SemanticAction19(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.setTokenId('~'); ///Supongo que -1 es el token de fin de archivo
	}
	
}
