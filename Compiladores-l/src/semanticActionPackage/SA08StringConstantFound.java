package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import codeGenerationPackage.Symbol;
import utilitiesPackage.Constants;

public class SA08StringConstantFound extends SemanticAction {

	public SA08StringConstantFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(){
		//ingresar en la talba de simbolos
		lexicalAnalyzer.addNextCharacter();
		String lexeme = lexicalAnalyzer.getCurrentLexeme();
		Symbol s= new Symbol(lexeme,Symbol._CONSTANT_LEXEME,Symbol._STRING_TYPE);
		lexicalAnalyzer.getSymbolsTable().addSymbol(lexeme,s);
		lexicalAnalyzer.getYylval().sval = lexeme;
		lexicalAnalyzer.setTokenId(Constants.CSTRING);
	}

}