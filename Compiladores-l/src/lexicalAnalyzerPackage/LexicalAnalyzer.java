package lexicalAnalyzerPackage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class LexicalAnalyzer {
	
	private TransitionMatrix transitionMatrix;
	private ReturnableBufferedReader bufferedReader;
	private int currentState;
	private String tokenString;
	
	
	public LexicalAnalyzer(String codePath) throws FileNotFoundException{
		transitionMatrix = new TransitionMatrix();
		bufferedReader = new ReturnableBufferedReader(
			    new InputStreamReader(
				        new FileInputStream(codePath),
				        Charset.forName("UTF-8")));
		
		
	}

	public String getNextToken() {
		/*
		char c;
		tokenString = "";
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
	*/
		return null;
	}

	public void initializeLexem() {
		// TODO Auto-generated method stub
		
	}

	public void addNextCharacter() {
		// TODO Auto-generated method stub
		
	}
	
}
