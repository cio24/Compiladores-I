package lexicalAnalyzerPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import codeGenerationPackage.SymbolsTable;
import utilitiesPackage.Constants;
import utilitiesPackage.ReturnableBufferedReader;

public class LexicalAnalyzer {

	public final static char END_OF_FILE_CHAR = '~';
	public final static int NO_TOKEN_FOUND_VALUE = -1;

	private int tokenId;
	private int currentState;
	private char lastCharacterRead;
	private String lexeme;
	private TransitionMatrix transitionMatrix;
	private ReturnableBufferedReader fileReader;
	private SymbolsTable symbolsTable;
	private KeywordsTable reservedKeywords;
	private ParserVal yylval;

    //CONSTRUCTOR

	public LexicalAnalyzer(String codePath) throws FileNotFoundException {
		reservedKeywords = new KeywordsTable();
		transitionMatrix = new TransitionMatrix(this);
		symbolsTable = new SymbolsTable();
		fileReader = new ReturnableBufferedReader(
				new InputStreamReader(new FileInputStream(codePath), Charset.forName("UTF-8")));
		tokenId = -1;
	}


	//GET TOKEN METHOD

	public int yylex(ParserVal yylval) {

		this.yylval = yylval; //como es un atributo publico puede ser modificado por una accion semantica
		lexeme = ""; //
		currentState = 0;

		tokenId = NO_TOKEN_FOUND_VALUE;

		while (tokenId == NO_TOKEN_FOUND_VALUE) {

			int characterCode = -1;
			try {
				// siempre se va a encontar un token, por lo menos el token de final de archivo
				characterCode = fileReader.readNextCharacter();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//si encontramos el caracter /r lo salteamos
			if(characterCode == 13)
				continue;

			if(characterCode == -1)
				lastCharacterRead = END_OF_FILE_CHAR;
			else // guardo el caracter leido por si lo usa una accion semantica
				lastCharacterRead = (char) characterCode;

			int savedState=currentState;

			// se obtiene el siguiente estado
			currentState = transitionMatrix.getNextState(currentState, lastCharacterRead);

			/*
			ejecuto la accion semantica correspondiente al estado actual y el caracter leido
			 notese que si la accion semantica encontro un token, tiene que usar el metodo de setTokenId();
			 esta accion semantica podria detectar un error, por lo que cambiaria el currentState al estado 0
			 para poder seguir buscando un token valido
			 */
			transitionMatrix.getSemanticAction(savedState, lastCharacterRead).execute();

			/*
			cuando se actualiza el currentState, si se pasa al estado final (-1) se supone que se enconto un token
			entonces una accion semantica tuvo que actualizar el tokenId y no tendria que volver a entrar en el while, xq sino se romperia
			ya que estaria tratando de entrar en una posicion invalida de la matriz
			 */
		}

		if(tokenId == (int) END_OF_FILE_CHAR)
			return -1;

		//System.out.println("    [Linea " + this.getCurrentLine() + "] Token \""  +getTokenString(tokenId) + "\" encontrado"); ***YA NO NECESITAMOS IMPRIMIR ESTO***

		return tokenId;

	}


	//GETTERS

	public KeywordsTable getReservedKeywords() {
		return reservedKeywords;
	}


	public ParserVal getYylval() {
		return yylval;
	}

	public SymbolsTable getSymbolsTable() {
		return symbolsTable;
	}

	public char getLastCharacterRead() {
		return lastCharacterRead;
	}

	public String getCurrentLexeme() {
		return lexeme;
	}

	public int getLastCharacterReadAscii() {
		return lastCharacterRead;
	}

	public String getTokenString(int tokenId) {
		String name = Constants.getConstantName(tokenId);
		if(name == null)
			return Character.toString((char)tokenId);
		else
			return name;

	}

	public String getCurrentLine() {
		int line = fileReader.getCurrentLine();
		if(line <= 9)
			switch(line){
				case 0: return "00";
				case 1: return "01";
				case 2: return "02";
				case 3: return "03";
				case 4: return "04";
				case 5: return "05";
				case 6: return "06";
				case 7: return "07";
				case 8: return "08";
				default: return "09";
			}

		return String.valueOf(fileReader.getCurrentLine());
	}


	//SETTERS

	public void setNextState(int nextState) {
		currentState = nextState;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}


	//OTHERS

	public void initializeLexeme() {
		lexeme = "";
	}

	public void addNextCharacter() {
		lexeme = lexeme + lastCharacterRead;
	}

	public void returnLastCharacterRead() {
		fileReader.returnLastCharacter();
	}
	
}
