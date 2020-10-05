package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Warning01UnexpectedCharInComment extends SemanticAction{
	
	
	public Warning01UnexpectedCharInComment(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		System.out.println("[Linea " + lexicalAnalyzer.getCurrentLine() + "] WARNING lexico: Caracter inesperado ("+(char)lexicalAnalyzer.getLastCharacterReadAscii()+") dentro de un comentario.");
	}

}
