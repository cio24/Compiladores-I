package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.ParserVal;
import lexicalAnalyzerPackage.Symbol;
import usefullClassesPackage.Constants;

public class SA08StringFound extends SemanticAction {

	public SA08StringFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(){
		//ingresar en la talba de simbolos
		String lexeme = lexicalAnalyzer.getCurrentLexem();
		Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine());
		lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
		lexicalAnalyzer.yylval.obj = symbol;
		lexicalAnalyzer.yylval.sval = lexeme;
		lexicalAnalyzer.setTokenId(Constants.CSTRING);
	}

}