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
		String lexeme = lexicalAnalyzer.getCurrentLexeme();
		int keywordtoken = lexicalAnalyzer.getReservedKeywords().getTokenValue(lexeme);
		if (keywordtoken!=-1) {
			//System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Reserved word \"" + lexeme + "\" found.");
			lexicalAnalyzer.setTokenId(keywordtoken);
		}
		else
		{
			System.out.println("[Linea " + lexicalAnalyzer.getCurrentLine() + "] ERROR lexico: no hay palabra reservada \"" + lexeme + "\".");	
			lexicalAnalyzer.setNextState(0);
		}
			
		
			
	}

}
