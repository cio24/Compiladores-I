package semanticActionPackage;

import java.math.BigDecimal;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.Symbol;
import usefulClassesPackage.Constants;

public class SA06LongConstantFound extends SemanticAction {

	public SA06LongConstantFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		String lexem=lexicalAnalyzer.getCurrentLexeme();
		//Integer value=Integer.parseInt(lexem);
		BigDecimal value=new BigDecimal(lexem);

		if ((Constants.MIN_RANGE_ULONGINT.compareTo(value)<0 || Constants.MIN_RANGE_ULONGINT.compareTo(value)==0)  && value.compareTo(Constants.MAX_RANGE_ULONGINT)<0)
				{
			lexicalAnalyzer.setTokenId(Constants.CONSTANT);
			
			//agregar a la tabla de simbolos
			String lexeme = lexicalAnalyzer.getCurrentLexeme();
			Symbol symbol = new Symbol(lexeme,Symbol._ULONGINT);
			lexicalAnalyzer.getSymbolsTable().addSymbol(lexeme,symbol);
			lexicalAnalyzer.getYylval().sval = lexeme;
		}
		else {
			System.out.println("[Linea " + lexicalAnalyzer.getCurrentLine() + "] ERROR lexico: constante ("+lexem+") de tipo ULONGINT fuera de rango.");
			lexicalAnalyzer.setNextState(0);
		}
	}

}
