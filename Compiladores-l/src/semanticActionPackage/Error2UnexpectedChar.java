package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error2UnexpectedChar extends SemanticAction{

	public Error2UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String error = "[Linea " + lexicalAnalyzer.getCurrentLine() + "] ERROR lexico: Mala definicion.";
		String read = Character.toString((char)lexicalAnalyzer.getLastCharacterReadAscii()).replace("\n", "\\n").replace("\t","\\t");
		error += " Caracter que genero la interrupcion: '" + read + "'.";
		if (lexicalAnalyzer.getCurrentLexeme() != "") {
			error += " Lexema leido: " + lexicalAnalyzer.getCurrentLexeme();
		}
		System.out.println(error);
	}

}
