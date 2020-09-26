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
			System.out.println("Advertencia: el nombre del identificador se ha truncado porque supera los 20 caracteres (linea "+ lexicalAnalyzer.getCurrentLine());
			//Agregar a la tabla de simbolos si no esta
		}
		lexicalAnalyzer.setTokenId(Constants.IDENTIFICADOR);

			
	}

}
