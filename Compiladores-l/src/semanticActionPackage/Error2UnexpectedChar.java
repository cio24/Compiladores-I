package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Error2UnexpectedChar extends SemanticAction{

	public Error2UnexpectedChar(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		lexicalAnalyzer.returnLastCharacterRead();
		String error = "[Line " + lexicalAnalyzer.getCurrentLine() + "] ERROR léxico: Mala definición.";
		String read = Character.toString((char)lexicalAnalyzer.getLastCharacter()).replace("\n", "\\n").replace("\t","\\t");
		error += " Carácter que genero la interrupción: '" + read + "'.";
		if (lexicalAnalyzer.getCurrentLexem() != "") {
			error += " Lexema leído: " + lexicalAnalyzer.getCurrentLexem();
		}
		System.out.println(error);
	}

}
