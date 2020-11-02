package semanticActionPackage;

import java.math.BigDecimal;
import lexicalAnalyzerPackage.LexicalAnalyzer;
import codeGenerationPackage.Symbol;
import utilitiesPackage.Constants;
import utilitiesPackage.ErrorReceiver;

public class SA06LongConstantFound extends SemanticAction {

	public SA06LongConstantFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		String lexeme = lexicalAnalyzer.getCurrentLexeme();
		BigDecimal value = new BigDecimal(lexeme);

		if ((Constants.MIN_RANGE_ULONGINT.compareTo(value)<0 || Constants.MIN_RANGE_ULONGINT.compareTo(value)==0)  && value.compareTo(Constants.MAX_RANGE_ULONGINT)<0)
				{
			lexicalAnalyzer.setTokenId(Constants.CONSTANT);
			
			//agregar a la tabla de simbolos
			Symbol s = new Symbol(lexeme,Symbol._CONSTANT_LEXEME,Symbol._ULONGINT_TYPE);
			lexicalAnalyzer.getSymbolsTable().addSymbol(lexeme,s);
			lexicalAnalyzer.getYylval().sval = lexeme;
		}
		else {
			ErrorReceiver.displayError(ErrorReceiver.ERROR,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,"constante ("+lexeme+") de tipo ULONGINT fuera de rango.");
			lexicalAnalyzer.setNextState(0);
		}
	}

}
