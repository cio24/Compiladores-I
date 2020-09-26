package semanticActionPackage;

import java.math.BigDecimal;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.ParserVal;
import lexicalAnalyzerPackage.Symbol;
import usefullClassesPackage.Constants;

public class SA07DoubleConstantFound extends SemanticAction {

	public SA07DoubleConstantFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		String lexem=lexicalAnalyzer.getCurrentLexem();
		String snumber=lexem.replace('d','e');
		BigDecimal number= new BigDecimal(snumber);

		lexicalAnalyzer.returnLastCharacterRead();
		if (number.compareTo(Constants.MAX_RANGE_DOUBLE) < 0  && Constants.MIN_RANGE_DOUBLE.compareTo(number)< 0) {
			lexicalAnalyzer.setTokenId(Constants.CONSTANTE_NUMERICA);
			//Ingresar en la tabla de simbolos
			String lexeme = lexicalAnalyzer.getCurrentLexem();
			Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine());
			lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
			lexicalAnalyzer.yylval.obj = symbol;
			lexicalAnalyzer.yylval.sval = lexeme;
			}
		else 
		{
			System.out.println("Error lexico: valor de constante doble fuera de rango (linea " + lexicalAnalyzer.getCurrentLine());
			lexicalAnalyzer.setNextState(0);
		}
	}
	
}
