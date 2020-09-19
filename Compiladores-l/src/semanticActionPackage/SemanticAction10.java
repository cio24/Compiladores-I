package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction10 extends SemanticAction{

	public SemanticAction10(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.initializeLexem();
	}

}
