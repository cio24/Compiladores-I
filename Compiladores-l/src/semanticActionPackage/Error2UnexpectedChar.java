package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import utilitiesPackage.ErrorReceiver;

public class Error2UnexpectedChar extends SemanticAction{

	public Error2UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String error = "Mala definicion.";
		String read = Character.toString((char)lexicalAnalyzer.getLastCharacterReadAscii()).replace("\n", "\\n").replace("\t","\\t");
		error += " Caracter que genero la interrupcion: '" + read + "'.";
		if (lexicalAnalyzer.getCurrentLexeme() != "") {
			error += " Lexema leido: " + lexicalAnalyzer.getCurrentLexeme();
		}
		ErrorReceiver.displayError(ErrorReceiver.ERROR,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,error);

	}

}
