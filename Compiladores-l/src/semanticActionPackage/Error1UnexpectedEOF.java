package semanticActionPackage;
import lexicalAnalyzerPackage.LexicalAnalyzer;
import utilitiesPackage.ErrorReceiver;

public class Error1UnexpectedEOF extends SemanticAction {

	public Error1UnexpectedEOF(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.setTokenId('~');
		ErrorReceiver.displayError(ErrorReceiver.ERROR,lexicalAnalyzer.getCurrentLine(),ErrorReceiver.LEXICO,"fin de archivo inesperado");
	}

}
