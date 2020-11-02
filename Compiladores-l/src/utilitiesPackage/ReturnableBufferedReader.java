package utilitiesPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ReturnableBufferedReader extends BufferedReader {

	public ReturnableBufferedReader(Reader in, int sz) {
		super(in, sz);
		// TODO Auto-generated constructor stub
		lastCharacterRead=0;
		currentLine=1;
		lastCharacterWasReturned=false;
	}
	
	public ReturnableBufferedReader(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
		lastCharacterRead=0;
		currentLine=1;
		lastCharacterWasReturned=false;
	}
	
	private int currentLine;
	
	private int lastCharacterRead;
	
	private boolean lastCharacterWasReturned;
	
	public void returnLastCharacter() {
		lastCharacterWasReturned=true;
	}
	
	public int readNextCharacter() throws IOException {
		if (lastCharacterWasReturned)
			lastCharacterWasReturned=false;
		else {
			if ((char)lastCharacterRead=='\n')
				currentLine++;
			lastCharacterRead=this.read();	
		}
		return lastCharacterRead;
	}
	
	public int getCurrentLine() {
		return currentLine;
	}
		
}
