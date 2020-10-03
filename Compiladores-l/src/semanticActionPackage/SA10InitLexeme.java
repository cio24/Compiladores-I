package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA10InitLexeme extends SemanticAction{

	public SA10InitLexeme(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.initializeLexem();
	}

}
