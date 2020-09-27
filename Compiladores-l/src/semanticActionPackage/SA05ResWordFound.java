package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SA05ResWordFound extends SemanticAction{

	public SA05ResWordFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String lexeme = lexicalAnalyzer.getCurrentLexem();
		int keywordtoken = lexicalAnalyzer.reservedKeywords.getKeywordToken(lexeme);
		if (keywordtoken!=-1) {
			System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Reserved word \"" + lexeme + "\" found.");
			lexicalAnalyzer.setTokenId(keywordtoken);
		}
		else
		{
			System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Lexical error: there's no reserved word called \"" + lexeme + "\".");	
			lexicalAnalyzer.setNextState(0);
		}
			
		
			
	}

}
