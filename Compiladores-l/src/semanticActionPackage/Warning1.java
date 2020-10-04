package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Warning1 extends SemanticAction{
	
	
	public Warning1(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		System.out.println("[Linea " + lexicalAnalyzer.getCurrentLine() + "] WARNING lexico: Caracter inesperado ("+(char)lexicalAnalyzer.getLastCharacter()+") dentro de un comentario.");
	}

}
