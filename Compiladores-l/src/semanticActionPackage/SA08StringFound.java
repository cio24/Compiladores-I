package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.Symbol;
import usefulClassesPackage.Constants;

public class SA08StringFound extends SemanticAction {

	public SA08StringFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(){
		//ingresar en la talba de simbolos
		lexicalAnalyzer.addNextCharacter();
		String lexeme = lexicalAnalyzer.getCurrentLexeme();
		Symbol symbol = new Symbol(lexeme,Symbol._STRING);
		lexicalAnalyzer.getSymbolsTable().addSymbol(lexeme,symbol);
		lexicalAnalyzer.getYylval().sval = lexeme;
		lexicalAnalyzer.setTokenId(Constants.CSTRING);
	}

}