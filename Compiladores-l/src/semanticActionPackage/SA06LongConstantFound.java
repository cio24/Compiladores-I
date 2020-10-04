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
		String lexem=lexicalAnalyzer.getCurrentLexem();
		//Integer value=Integer.parseInt(lexem);
		BigDecimal value=new BigDecimal(lexem);

		if ((Constants.MIN_RANGE_ULONGINT.compareTo(value)<0 || Constants.MIN_RANGE_ULONGINT.compareTo(value)==0)  && value.compareTo(Constants.MAX_RANGE_ULONGINT)<0)
				{
			lexicalAnalyzer.setTokenId(Constants.CONSTANT);
			
			//agregar a la tabla de simbolos
			String lexeme = lexicalAnalyzer.getCurrentLexem();
			Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine(),Symbol._ULONGINT);
			lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
			lexicalAnalyzer.yylval.sval = lexeme;
		}
		else {
			System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] ERROR léxico: ULONGINT fuera de rango.");
			lexicalAnalyzer.setNextState(0);
		}
	}

}
