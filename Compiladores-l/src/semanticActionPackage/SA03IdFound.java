package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.ParserVal;
import lexicalAnalyzerPackage.Symbol;
import usefullClassesPackage.Constants;

public class SA03IdFound extends SemanticAction {

	public SA03IdFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String lexeme = lexicalAnalyzer.getCurrentLexem();
		if (lexeme.length()<=20) {
			Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine());
			lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
			lexicalAnalyzer.yylval.obj = symbol;
			lexicalAnalyzer.yylval.sval = lexeme;
		}
		else
		{
			lexeme=lexeme.substring(0,20);
			System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Warning: the identifier is too long, it has been truncated to 20 characters.");
			
			//Agregar a la tabla de simbolos si no esta
			if(lexicalAnalyzer.symbolsTable.getSymbol(lexeme) == null) {
				Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine());
				lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
				lexicalAnalyzer.yylval.obj = symbol;
				lexicalAnalyzer.yylval.sval = lexeme;
			}

		}
		lexicalAnalyzer.setTokenId(Constants.ID);

			
	}

}
