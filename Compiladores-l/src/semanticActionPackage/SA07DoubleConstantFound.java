package semanticActionPackage;

import java.math.BigDecimal;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.ParserVal;
import lexicalAnalyzerPackage.Symbol;
import usefulClassesPackage.Constants;

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
			lexicalAnalyzer.setTokenId(Constants.CONSTANT);
			// Utilizamos como lexema de los double el valor pasado a string del BigDecimal, para unificar los 2.0515 con los 0.20515d+1
			String lexeme = new String(number.toString());
			//Ingresar en la tabla de simbolos
			Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine(),Symbol._DOUBLE);
			lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
			lexicalAnalyzer.yylval.sval = lexeme;
			}
		else 
		{
			System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Lexical error: DOUBLE out of range.");
			lexicalAnalyzer.setNextState(0);
		}
	}
	
}
