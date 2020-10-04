package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.ParserVal;
import lexicalAnalyzerPackage.Symbol;
import usefulClassesPackage.Constants;

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
			Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine(),Symbol._ID);
			lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
			lexicalAnalyzer.yylval.sval = lexeme;
		}
		else
		{
			String lexeme_truc=lexeme.substring(0,20);
			System.out.println("[Linea " + lexicalAnalyzer.getCurrentLine() + "] WARNING lexico: el identificador ("+lexeme+") es demasiado largo, se trunco a 20 caracteres.");
			
			//Agregar a la tabla de simbolos si no esta
			if(lexicalAnalyzer.symbolsTable.getSymbol(lexeme_truc) == null) {
				Symbol symbol = new Symbol(lexeme_truc,lexicalAnalyzer.getCurrentLine(),Symbol._ID);
				lexicalAnalyzer.symbolsTable.addSymbol(lexeme_truc,symbol);
				lexicalAnalyzer.yylval.sval = lexeme_truc;
			}

		}
		lexicalAnalyzer.setTokenId(Constants.ID);

			
	}

}
