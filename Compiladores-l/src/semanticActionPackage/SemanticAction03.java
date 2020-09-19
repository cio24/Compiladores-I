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
			////Tenemos que poder acceder a la tabla de simbolos o como hacemos desde aca?			
		}
			
	}

}
