package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.ErrorReceiver;

public class Error3UnexpectedChar extends SemanticAction{

	public Error3UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		String error ="Caracter invalido o inesperado: ";
		String read = Character.toString((char)lexicalAnalyzer.getLastCharacterReadAscii()).replace("\n", "\\n").replace("\t","\\t");
		error +=  "'" + read + "'.";
		ErrorReceiver.displayError(ErrorReceiver.ERROR,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,error);

	}

}
