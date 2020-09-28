package semanticActionPackage;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.Parser;
import lexicalAnalyzerPackage.ParserVal;
import lexicalAnalyzerPackage.Symbol;
import usefullClassesPackage.Constants;

public class SA06LongConstantFound extends SemanticAction {

	public SA06LongConstantFound(LexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		String lexem=lexicalAnalyzer.getCurrentLexem();
		Integer value=Integer.parseInt(lexem);
		if (value>=0 && value<=Math.pow(2, 32)-1) {
			lexicalAnalyzer.setTokenId(Constants.CONSTANT);
			
			//agregar a la tabla de simbolos
			String lexeme = lexicalAnalyzer.getCurrentLexem();
			Symbol symbol = new Symbol(lexeme,lexicalAnalyzer.getCurrentLine(),Symbol._ULONGINT);
			lexicalAnalyzer.symbolsTable.addSymbol(lexeme,symbol);
			lexicalAnalyzer.yylval.sval = lexeme;
		}
		else {
			System.out.println("[Line " + lexicalAnalyzer.getCurrentLine() + "] Lexical error: ULONGINT out of range.");
			lexicalAnalyzer.setNextState(0);
		}
	}

}
