package semanticActionPackage;

import java.math.BigDecimal;
import lexicalAnalyzerPackage.LexicalAnalyzer;
import codeGenerationPackage.Symbol;
import utilitiesPackage.Constants;
import utilitiesPackage.ErrorReceiver;

public class SA07DoubleConstantFound extends SemanticAction {

	public SA07DoubleConstantFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		String lexem = lexicalAnalyzer.getCurrentLexeme();
		String snumber = lexem.replace('d','e');
		BigDecimal number = new BigDecimal(snumber);

		lexicalAnalyzer.returnLastCharacterRead();
		if (number.compareTo(Constants.MAX_RANGE_DOUBLE) < 0  && Constants.MIN_RANGE_DOUBLE.compareTo(number)< 0) {
			lexicalAnalyzer.setTokenId(Constants.CONSTANT);
			// Utilizamos como lexema de los double el valor pasado a string del BigDecimal, para unificar los 2.0515 con los 0.20515d+1
			String lexeme = new String(number.toString());
			//Ingresar en la tabla de simbolos
			Symbol s = new Symbol(lexeme,Symbol._CONSTANT_LEXEME,Symbol._DOUBLE_TYPE);
			lexicalAnalyzer.getSymbolsTable().addSymbol(lexeme,s);
			lexicalAnalyzer.getYylval().sval = lexeme;
			}
		else 
		{
			ErrorReceiver.displayError(ErrorReceiver.ERROR,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,"constante ("+lexem+") de tipo DOUBLE fuera de rango.");
			lexicalAnalyzer.setNextState(0);
		}
	}
	
}
