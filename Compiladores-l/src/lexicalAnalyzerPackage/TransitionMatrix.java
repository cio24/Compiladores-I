package lexicalAnalyzerPackage;
import java.util.Vector;

import semanticActionPackage.SemanticAction;

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
	    
	    Vector<Integer> v = transitionMatrix.get(0);
	    v.insertElementAt(0, 0);
	    v.insertElementAt(1, 0);
	    v.insertElementAt(2, 1);
	    v.insertElementAt(3, -1);
	    v.insertElementAt(4, 4);
	    v.insertElementAt(5, -1);
	    v.insertElementAt(6, 14);
	    v.insertElementAt(7, 13);
	    v.insertElementAt(8, 16);
	    v.insertElementAt(9, 15);
	    v.insertElementAt(10, -1);
	    v.insertElementAt(11, -1);
	    v.insertElementAt(12, -1);
	    v.insertElementAt(13, 7);
	    v.insertElementAt(14, 10);
	    v.insertElementAt(15, -1);
	    v.insertElementAt(16, 4);
	    v.insertElementAt(17, 5);
	    v.insertElementAt(18, 6);
	    
	    v = transitionMatrix.get(1);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 3)
	    		v.insertElementAt(i,2);
    		else
    			v.insertElementAt(i, -1);
	    }
	    
	    v = transitionMatrix.get(2);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 3)
	    		v.insertElementAt(i, 3);
	    	else if(i == 15)
	    		v.insertElementAt(i, -1);
	    	else
	    		v.insertElementAt(i,2);
	    }
	    
	    v = transitionMatrix.get(3);
	 
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 2)
	    		v.insertElementAt(i, 0);
	    	else if(i == 3)
	    		v.insertElementAt(i, 3);
	    	else if(i == 15)
	    		v.insertElementAt(i, -1);
	    	else
	    		v.insertElementAt(i,2);
	    }
	    
	    v = transitionMatrix.get(4);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 4 || i == 5 || i == 16 || i == 18)
	    		v.insertElementAt(i, 4);
	    	else
	    		v.insertElementAt(i,-1);
	    }
	    
	    v = transitionMatrix.get(5);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 5 || i == 17)
	    		v.insertElementAt(i, 5);
	    	else
	    		v.insertElementAt(i,-1);
	    }
	    
	    v = transitionMatrix.get(6);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 14)
	    		v.insertElementAt(i, 8);
	    	else if(i == 18)
	    		v.insertElementAt(i, 6);
	    	else
	    		v.insertElementAt(i,-1);
	    }
	    
	    v = transitionMatrix.get(7);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 1 || i == 13)
	    		v.insertElementAt(i, -1);
	    	else
	    		v.insertElementAt(i,7);
	    }
	    
	    v = transitionMatrix.get(8);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 4)
	    		v.insertElementAt(i, 9);
	    	if(i == 18)
	    		v.insertElementAt(i, 8);
	    	else
	    		v.insertElementAt(i,-1);
	    }
	    
	    v = transitionMatrix.get(9);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 10 || i == 11)
	    		v.insertElementAt(i, 11);
	    	else
	    		v.insertElementAt(i,-1);
	    }
	    
	    v = transitionMatrix.get(10);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 18)
	    		v.insertElementAt(i, 8);
	    	else
	    		v.insertElementAt(i,-1);
	    }
	    
	    v = transitionMatrix.get(11);
	    Vector<Integer> vv = transitionMatrix.get(12);
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 18) {
	    		v.insertElementAt(i, 12);
	    		vv.insertElementAt(i, 12);
	    	}
	    	else {
	    		v.insertElementAt(i,-1);
	    		vv.insertElementAt(i,-1);
	    		
	    	}
	    }
	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	for(int j = 13; j < STATES; j++)
	    	transitionMatrix.get(j).insertElementAt(i,-1);
	    }
		
	}
	
	private int getId(char c) {
		
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
	
	public int getNextState(int currentState, char c) {
		return transitionMatrix.get(currentState).get(this.getId(c));
	}
	
	public SemanticAction getSemanticAction(int actualState, char c) {
		return semanticMatrix.get(actualState).get(getId(c));
	}
}
