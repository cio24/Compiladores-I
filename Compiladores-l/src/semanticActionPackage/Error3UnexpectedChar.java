package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error3UnexpectedChar extends SemanticAction{

	public Error3UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		String error = "[Line " + lexicalAnalyzer.getCurrentLine() + "] ERROR léxico: Carácter inválido o inesperado: ";
		String read = Character.toString((char)lexicalAnalyzer.getLastCharacter()).replace("\n", "\\n").replace("\t","\\t");
		error +=  "'" + read + "'.";
		System.out.println(error);
	}

}
