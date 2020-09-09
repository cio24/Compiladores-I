import java.util.Vector;

public class TransitionMatrix {
	
	private Vector<Vector<Integer>> transitionMatrix;
	private Vector<Vector<SemanticAction>> semanticMatrix;
	private final static int STATES = 17;
	private final static int CHAR_SETS = 19;
	
	public TransitionMatrix() {
		
		transitionMatrix = new Vector<Vector<Integer>>(STATES);
		semanticMatrix = new Vector<Vector<SemanticAction>>(STATES);
		
	    for(int i=0;i<STATES;i++){
	    	transitionMatrix.add(new Vector<Integer>(CHAR_SETS));
	    	semanticMatrix.add(new Vector<SemanticAction>(CHAR_SETS));
	    }
		
	}
	
	public int getId(char c) {
		
		switch(c){
			case ' ':
			case '\t':
				return 0;
			case '\n':
				return 1;
			case '/':
				return 2;
			case '%':
				return 3;
			case 'd':
				return 4;
			case '_':
				return 5;
			case '>':
				return 6;
			case '<':
				return 7;
			case '!':
				return 8;
			case '=':
				return 9;
			case '+':
				return 10;
			case '-':
				return 11;
			case '*':
				return 12;
			case '\'':
				return 13;
			case '.':
				return 14;
			case '$':
				return 15;
		}
		
		int charType = Character.getType(c);
        if (charType == Character.LOWERCASE_LETTER)
        	return 16;
        if (charType == Character.UPPERCASE_LETTER)
        	return 17;
		 if(Character.isDigit(c))
			 return 18;
			
		return -1;
	}
	
}
