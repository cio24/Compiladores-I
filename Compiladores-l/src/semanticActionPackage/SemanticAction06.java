package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

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
			lexicalAnalyzer.setTokenId(402);
			//agregar a la tabla de simbolos
		}
		else {
			System.out.print("Error lexico en linea:"+lexicalAnalyzer.getCurrentLine()+". Constante larga sin signo fuera de rango");
			lexicalAnalyzer.setNextState(0);
		}
		}

}
