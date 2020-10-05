package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error3UnexpectedChar extends SemanticAction{

	public Error3UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		String error = "[Linea " + lexicalAnalyzer.getCurrentLine() + "] ERROR lexico: Caracter invalido o inesperado: ";
		String read = Character.toString((char)lexicalAnalyzer.getLastCharacterReadAscii()).replace("\n", "\\n").replace("\t","\\t");
		error +=  "'" + read + "'.";
		System.out.println(error);
	}

}
