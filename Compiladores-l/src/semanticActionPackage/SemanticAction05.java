package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction05 extends SemanticAction{

	public SemanticAction05(LexicalAnalyzer lexicalAnalyzer) {
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
			System.out.println("Error lexico en linea:"+lexicalAnalyzer.getCurrentLine()+". La palabra reservada no es valida");	
			lexicalAnalyzer.setNextState(0);
		}
			
		
			
	}

}
