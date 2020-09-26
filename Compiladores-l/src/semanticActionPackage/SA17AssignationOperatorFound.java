package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA17AssignationOperatorFound extends SemanticAction{

	public SA17AssignationOperatorFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		lexicalAnalyzer.setTokenId((int)'=');
	}
	
}
