package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import symbolPackage.Symbol;
import usefulClassesPackage.Constants;
import usefulClassesPackage.ErrorReceiver;

public class SA03IdFound extends SemanticAction {

	public SA03IdFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String lexeme = lexicalAnalyzer.getCurrentLexeme();

		if (lexeme.length() > 20){
			ErrorReceiver.displayError(ErrorReceiver.WARNING,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,"el identificador ("+lexeme+") es demasiado largo, se trunco a 20 caracteres.");
			lexeme = lexeme.substring(0,20);
		}

		System.out.println("Se encontró el lexema: " + lexeme);
		Symbol s = new Symbol(lexeme,Symbol._IDENTIFIER_LEXEME);
		lexicalAnalyzer.setTokenId(Constants.ID);
		lexicalAnalyzer.getSymbolsTable().addSymbol(lexeme,s);
		lexicalAnalyzer.getYylval().sval = lexeme;
	}
}
