package semanticActionPackage;

import java.math.BigDecimal;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.Constants;

public class SemanticAction07 extends SemanticAction {

	public SemanticAction07(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		String lexem=lexicalAnalyzer.getCurrentLexem();
		String snumber=lexem.replace('d','e');
		BigDecimal number= new BigDecimal(snumber);
		
		if (number.compareTo(Constants.MAX_RANGE_DOUBLE) < 0  && Constants.MIN_RANGE_DOUBLE.compareTo(number)< 0) {
			lexicalAnalyzer.setTokenId(Constants.CONSTANTE_NUMERICA);
			//Ingresar en la tabla de simbolos
			}
		else 
		{
			System.out.println("Error lexico en linea:"+lexicalAnalyzer.getCurrentLine()+". Valor de constante doble fuera de rango");
			lexicalAnalyzer.setNextState(0);
		}
	}
	
}
