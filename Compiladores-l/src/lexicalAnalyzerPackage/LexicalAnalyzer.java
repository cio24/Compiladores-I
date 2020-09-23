package lexicalAnalyzerPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import semanticActionPackage.SemanticAction;

public class LexicalAnalyzer {

	private TransitionMatrix transitionMatrix;
	private ReturnableBufferedReader fileReader;
	private String lexem;
	private SymbolTable st;
	public KeywordTable reservedKeywords;
	private char lastCharacterRead;
	private int tokenId;
	private int currentState;

	public LexicalAnalyzer(String codePath) throws FileNotFoundException {
		reservedKeywords = new KeywordTable();
		transitionMatrix = new TransitionMatrix(this);
		st = new SymbolTable();
		fileReader = new ReturnableBufferedReader(
				new InputStreamReader(new FileInputStream(codePath), Charset.forName("UTF-8")));
		tokenId = -1;	
	}

	public int yylex() {
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
		System.out.println(lexem);
		
		if(tokenId == (int)'~')
			return -1;
		return tokenId;				

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

	public int getCurrentLine() {
		return fileReader.getCurrentLine();
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
