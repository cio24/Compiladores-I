package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class SemanticAction03 extends SemanticAction {

	public SemanticAction03(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String lexem = lexicalAnalyzer.getCurrentLexem();
		if (lexem.length()<=20) {
			//Agregar a la tabla de simbolos si no esta
		}
		else
		{
			lexem=lexem.substring(0,20);
			System.out.println("Advertencia en linea:"+lexicalAnalyzer.getCurrentLine()+". Nombre de identificador supera los 20 caracteres: identificador truncado");
			//Agregar a la tabla de simbolos si no esta
		}
		lexicalAnalyzer.setTokenId(401);

			
	}

}
