package lexicalAnalyzerPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import usefulClassesPackage.Constants;

public class LexicalAnalyzer {

	private TransitionMatrix transitionMatrix;
	private ReturnableBufferedReader fileReader;
	private final static char END_OF_FILE_CHAR = '~';
	private final static int NO_TOKEN_FOUND_VALUE = -1;
	private String lexeme;
	private SymbolsTable symbolsTable;
	private KeywordTable reservedKeywords;
	private char lastCharacterRead;
	private int tokenId;
	private int currentState;
	private ParserVal yylval;

	public KeywordTable getReservedKeywords() {
		return reservedKeywords;
	}

	public ParserVal getYylval() {
		return yylval;
	}

	public SymbolsTable getSymbolsTable() {
		return symbolsTable;
	}

	public LexicalAnalyzer(String codePath) throws FileNotFoundException {
		reservedKeywords = new KeywordTable();
		transitionMatrix = new TransitionMatrix(this);
		symbolsTable = new SymbolsTable();
		fileReader = new ReturnableBufferedReader(
				new InputStreamReader(new FileInputStream(codePath), Charset.forName("UTF-8")));
		tokenId = -1;	
	}

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
		
		System.out.println("    [Linea " + this.getCurrentLine() + "] Token \""  +getTokenString(tokenId) + "\" encontrado");
		
		return tokenId;				

	}
	
	public String getTokenString(int token) {
		String nombre = Constants.getConstantName(token);
		if(nombre == null)
			return Character.toString((char)token);
		else
			return nombre;
		
	}

	public char getLastCharactedRead() {
		return lastCharacterRead;
	}

	public void initializeLexem() {
		lexeme = "";
	}

	public void addNextCharacter() {
		//despu�s vamos a ver si esto funciona bien xD
		lexeme = lexeme + lastCharacterRead; /// Falta definir esto
	}

	public String getCurrentLexem() {
		return lexeme;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

	public String getCurrentLine() {
		int line = fileReader.getCurrentLine();
		String s;
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

	public void returnLastCharacterRead() {
		fileReader.returnLastCharacter();
	}

	public int getLastCharacter() {
		return lastCharacterRead;
	}
	
	public void setNextState(int ns) {
		currentState=ns;
	}
	
}
