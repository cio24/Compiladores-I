package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.Constants;

public class SemanticAction06 extends SemanticAction {

	public SemanticAction06(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		String lexem=lexicalAnalyzer.getCurrentLexem();
		Integer value=Integer.parseInt(lexem);
		if (value>=0 && value<=Math.pow(2, 32)-1) {
			lexicalAnalyzer.setTokenId(Constants.CONSTANTE_NUMERICA);
			//agregar a la tabla de simbolos
		}
		else {
			System.out.print("Error lexico en linea:"+lexicalAnalyzer.getCurrentLine()+". Constante larga sin signo fuera de rango");
			lexicalAnalyzer.setNextState(0);
		}
	}

}
