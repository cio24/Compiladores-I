package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Warning1 extends SemanticAction{
	
	
	public Warning1(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Warning: Unexpected character inside a comment.");
	}

}
