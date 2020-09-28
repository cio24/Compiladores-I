package lexicalAnalyzerPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import semanticActionPackage.SemanticAction;
import usefulClassesPackage.Constants;

public class LexicalAnalyzer {

	private TransitionMatrix transitionMatrix;
	private ReturnableBufferedReader fileReader;
	private String lexem;
	public SymbolsTable symbolsTable;
	public KeywordTable reservedKeywords;
	private char lastCharacterRead;
	private int tokenId;
	private int currentState;
	public ParserVal yylval;
	AtomicReference<ParserVal> reference;

	public LexicalAnalyzer(String codePath) throws FileNotFoundException {
		reservedKeywords = new KeywordTable();
		transitionMatrix = new TransitionMatrix(this);
		symbolsTable = new SymbolsTable();
		fileReader = new ReturnableBufferedReader(
				new InputStreamReader(new FileInputStream(codePath), Charset.forName("UTF-8")));
		tokenId = -1;	
	}

	public int yylex(AtomicReference<ParserVal> reference) {
		this.reference = reference;
		this.yylval = new ParserVal();
		lexem = "";
		currentState = 0;
		
		// seteamos tokenId en -1 para cuando se le pida un nuevo token alanalizador l�xico
		tokenId = -1;
		
		while (tokenId < 0) {
			
			int characterCode = -1;
			try {
				// siempre se va a encontar un token, por lo menos el token de final de archivo
				characterCode = fileReader.readNextCharacter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(characterCode == 13)
				continue;
			
			if(characterCode == -1)
				lastCharacterRead = '~';
			else
				lastCharacterRead = (char) characterCode; // guardo el caracter le�do por si lo usa una acci�n sem�ntica
		
			int savedState=currentState;
			// paso al siguiente estado
			currentState = transitionMatrix.getNextState(currentState, lastCharacterRead);

			// ejecuto la acci�n sem�ntica correspondiente al estado actual y el caracter le�do
			// notese que si la acci�n sem�ntica encontro un token, tiene que usar el m�todo de setTokenId();
			
			transitionMatrix.getSemanticAction(savedState, lastCharacterRead).execute();

				
			//cuando se actualiza el currentState, si se pasa al estado final (-1) se supone que se encontr� un token
			//entonces se tuvo que actualizad el tokenId y no tendr�a que volver a entrar en el while, xq sino se romperia
			//ya que estar�a tratando de entrar en una posici�n invalida de la matriz
		}
		//if (lexem.length()>0) System.out.println(lexem);
		reference.set(yylval);
		if(tokenId == (int)'~')
			return -1;
		
		System.out.println("-- LEX (Line " + this.getCurrentLine() + ") Token \""  +getTokenString(tokenId) + "\" found");
		
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
		lexem = "";
	}

	public void addNextCharacter() {
		//despu�s vamos a ver si esto funciona bien xD
		lexem = lexem + lastCharacterRead; /// Falta definir esto
	}

	public String getCurrentLexem() {
		return lexem;
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
