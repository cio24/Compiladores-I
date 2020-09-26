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
		int keywordtoken = lexicalAnalyzer.reservedKeywords.getKeywordToken(lexicalAnalyzer.getCurrentLexem());
		if (keywordtoken!=-1)
			lexicalAnalyzer.setTokenId(keywordtoken);
		else
		{
			System.out.println("Error lexico: la palabra reservada "
								+ "\"" + lexicalAnalyzer.getCurrentLexem() + "\" no existe (linea " + lexicalAnalyzer.getCurrentLine() + ")");	
			lexicalAnalyzer.setNextState(0);
		}
			
		
			
	}

}
