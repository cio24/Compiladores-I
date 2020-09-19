package lexicalAnalyzerPackage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class LexicalAnalyzer {
	
	private TransitionMatrix transitionMatrix;
	private ReturnableBufferedReader fileReader;
	private int currentState;
	private String lexem;
	private SymbolTable st;
	private KeywordTable reservedKeywords;
	
	
	public LexicalAnalyzer(String codePath) throws FileNotFoundException{
		transitionMatrix = new TransitionMatrix();
		fileReader = new ReturnableBufferedReader(
			    new InputStreamReader(
				        new FileInputStream(codePath),
				        Charset.forName("UTF-8")));
		
		
	}

	public String getNextToken() {

		char c;
		lexem = "";
		currentState = 0;
		int tokenId;

		
		while (currentState != -1) {
			  int characterCode = bufferedReader.readNextCharacter();
			  if(characterCode == -1)
				  return null;
			  c = (char) characterCode;
			  int nextState = transitionMatrix.getNextState(currentState, c);
			  SemanticAction semanticAction = transitionMatrix.getSemanticAction(currentState,c);
			  if(semantic)
		}
		return null;
	
	}

	public void initializeLexem() {
		lexem="";
	}

	public void addNextCharacter() {
		lexem=lexem+lastCharacterRead; ///Falta definir esto
	}
	
	public String getCurrentLexem() {
		return lexem;
	}
	
	public void setToken(int to) {
		
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
	
}
