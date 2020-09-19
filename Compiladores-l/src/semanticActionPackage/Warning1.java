package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Warning1 extends SemanticAction{
	
	
	public Warning1(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		System.out.println("Warning Lexico en linea " + lexicalAnalyzer.getCurrentLine() + ": Caracter inesperado dentro de comentario");
	}

}
