package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

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
		Double number=Double.parseDouble(snumber);
		
		if (number<Double.parseDouble("1.7976931348623157e+308") && number>Double.parseDouble("2.2250738585072014e-308")) {
			lexicalAnalyzer.setTokenId(402);
			//Ingresar en la tabla de simbolos
			}
		else 
		{
			System.out.println("Error lexico en linea:"+lexicalAnalyzer.getCurrentLine()+". Valor de constante doble fuera de rango");
			lexicalAnalyzer.setNextState(0);
		}
	}
	
}
