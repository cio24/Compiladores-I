package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import utilitiesPackage.ErrorReceiver;

public class Warning01UnexpectedCharInComment extends SemanticAction{
	
	
	public Warning01UnexpectedCharInComment(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		ErrorReceiver.displayError(ErrorReceiver.WARNING,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,"Caracter inesperado ("+(char)lexicalAnalyzer.getLastCharacterReadAscii()+") dentro de un comentario.");
	}

}
