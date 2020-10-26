package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.ErrorReceiver;

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
			ErrorReceiver.displayError(ErrorReceiver.ERROR,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,"no hay palabra reservada \"" + lexeme + "\".");
			lexicalAnalyzer.setNextState(0);
		}
			
		
			
	}

}
