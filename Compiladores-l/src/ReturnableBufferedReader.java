import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ReturnableBufferedReader extends BufferedReader {

	public ReturnableBufferedReader(Reader in, int sz) {
		super(in, sz);
		// TODO Auto-generated constructor stub
		lastCharacterRead=0;
		
		lastCharacterWasReturned=false;
	}
	
	public ReturnableBufferedReader(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
		lastCharacterRead=0;
		
		lastCharacterWasReturned=false;
	}
	
	private int lastCharacterRead;
	
	private boolean lastCharacterWasReturned;
	
	public void returnLastCharacter() {
		lastCharacterWasReturned=true;
	}
	
	public int readNextCharacter() throws IOException {
		if (lastCharacterWasReturned)
			lastCharacterWasReturned=false;
		else
			lastCharacterRead=this.read();
		return lastCharacterRead;
	}
		
}
