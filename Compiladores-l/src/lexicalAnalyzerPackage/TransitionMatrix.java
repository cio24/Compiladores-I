package lexicalAnalyzerPackage;
import java.util.Vector;
import semanticActionPackage.*;

public class TransitionMatrix {
	
	private Vector<Vector<Integer>> transitionMatrix;
	private Vector<Vector<SemanticAction>> semanticMatrix;
	private final static int STATES = 19;
	private final static int CHAR_SETS = 22;
	private final static int FINAL_STATE = -1;
	@SuppressWarnings("unused")
	private LexicalAnalyzer lexicalAnalyzer;
	
	public TransitionMatrix(LexicalAnalyzer lexicalAnalyzer) {
		
		transitionMatrix = new Vector<Vector<Integer>>(STATES);
		semanticMatrix = new Vector<Vector<SemanticAction>>(STATES);
		this.lexicalAnalyzer = lexicalAnalyzer;
		
	    for(int i=0;i<STATES;i++){
	    	transitionMatrix.add(new Vector<Integer>(CHAR_SETS));
	    	semanticMatrix.add(new Vector<SemanticAction>(CHAR_SETS));
	    }
	    
	    //a continuación se agrega la transición de los estados
	    
	    //transición del estado 0
	    Vector<Integer> v = transitionMatrix.get(0);
	    v.insertElementAt(0,0);
	    v.insertElementAt(0,1);
	    v.insertElementAt(1,2);
	    v.insertElementAt(0,3);
	    v.insertElementAt(4,4);
	    v.insertElementAt(0,5);
	    v.insertElementAt(14,6);
	    v.insertElementAt(13,7);
	    v.insertElementAt(16,8);
	    v.insertElementAt(15,9);
	    v.insertElementAt(FINAL_STATE,10);
	    v.insertElementAt(FINAL_STATE,11);
	    v.insertElementAt(7,12);
	    v.insertElementAt(10,13);
	    v.insertElementAt(FINAL_STATE,14);
	    v.insertElementAt(4,15);
	    v.insertElementAt(4,16);
	    v.insertElementAt(4,17);
	    v.insertElementAt(5,18);
	    v.insertElementAt(6,19);
	    v.insertElementAt(0,20);
	    
	    //transición del estado 1
	    v = transitionMatrix.get(1);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 3)
	    		v.insertElementAt(2,i);
    		else
    			v.insertElementAt(FINAL_STATE,i); ////Cio chequea esto
	    }
	    
	    //transición del estado 2
	    v = transitionMatrix.get(2);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 3)
	    		v.insertElementAt(3,i);
	    	else if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else
	    		v.insertElementAt(2,i);
	    }
	    
	    //transición del estado 3
	    v = transitionMatrix.get(3);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 2)
	    		v.insertElementAt(0,i);
	    	else if(i == 3)
	    		v.insertElementAt(3,i);
	    	else if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else
	    		v.insertElementAt(2,i);
	    }
	    
	    //transición del estado 4
	    v = transitionMatrix.get(4);	    
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 4 || i == 5 || i == 15 || i == 16 || i == 17 || i == 19)
	    		v.insertElementAt(4,i);
	    	else
	    		v.insertElementAt(FINAL_STATE,i);
	    }
	    
	    //transición del estado 5
	    v = transitionMatrix.get(5);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 5 || i == 18)
	    		v.insertElementAt(5,i);
	    	else
	    		v.insertElementAt(-1,i);
	    }
	    
	    //transición del estado 6
	    v = transitionMatrix.get(6);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 5)
	    		v.insertElementAt(17,i);
	    	else if(i == 13)
	    		v.insertElementAt(8,i);
	    	else if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else if(i == 19)
	    		v.insertElementAt(6,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //transición del estado 7
	    v = transitionMatrix.get(7);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 1 || i == 20)
	    		v.insertElementAt(0,i);
	    	else if(i == 12 || i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else
	    		v.insertElementAt(7,i);
	    }
	    
	    //transición del estado 8
	    v = transitionMatrix.get(8);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 4)
	    		v.insertElementAt(9,i);
	    	if(i == 19)
	    		v.insertElementAt(8,i);
	    	else
	    		v.insertElementAt(FINAL_STATE,i);
	    }
	    
	    //transición del estado 9
	    v = transitionMatrix.get(9);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 10)
	    		v.insertElementAt(11,i);
	    	else if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //transición del estado 10
	    v = transitionMatrix.get(10);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else if(i == 19)
	    		v.insertElementAt(8,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //transición del estado 11
	    v = transitionMatrix.get(11);
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else if(i == 19)
	    		v.insertElementAt(12,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //transición del estado 12
	    v = transitionMatrix.get(12);
	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 19)
	    		v.insertElementAt(12,i);
	    	else
	    		v.insertElementAt(FINAL_STATE,i);
	    }
	    
	    //transición del estado 13, 14 y 15
	    for(int i = 0; i < CHAR_SETS; i++) {
	    	for(int j = 13; j <= 15; j++)
	    	transitionMatrix.get(j).insertElementAt(FINAL_STATE,i);
	    }
	    
	    //transición del estado 16
	    v = transitionMatrix.get(16);
	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 9 || i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //transición del estado 17
	    v = transitionMatrix.get(17);
	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 14)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else if(i == 16)
	    		v.insertElementAt(8,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //transición del estado 18
	    v = transitionMatrix.get(18);
	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 14 || i == 15)
	    		v.insertElementAt(FINAL_STATE,i);
	    	else
	    		v.insertElementAt(0,i);
	    }
	    
	    //*****ACCIONES SEMANTICAS*****//
	    SemanticAction semanticAction01 = new SemanticAction01(lexicalAnalyzer);
	    SemanticAction semanticAction02 = new SemanticAction02(lexicalAnalyzer);
	    SemanticAction semanticAction03 = new SemanticAction03(lexicalAnalyzer);
	    SemanticAction semanticAction04 = new SemanticAction04(lexicalAnalyzer);
	    SemanticAction semanticAction05 = new SemanticAction05(lexicalAnalyzer);
	    SemanticAction semanticAction06 = new SemanticAction06(lexicalAnalyzer);
	    SemanticAction semanticAction07 = new SemanticAction07(lexicalAnalyzer);
	    SemanticAction semanticAction08 = new SemanticAction08(lexicalAnalyzer);
	    SemanticAction semanticAction10 = new SemanticAction10(lexicalAnalyzer);
	    SemanticAction semanticAction11 = new SemanticAction11(lexicalAnalyzer);
	    SemanticAction semanticAction12 = new SemanticAction12(lexicalAnalyzer);
	    SemanticAction semanticAction13 = new SemanticAction13(lexicalAnalyzer);
	    SemanticAction semanticAction14 = new SemanticAction14(lexicalAnalyzer);
	    SemanticAction semanticAction15 = new SemanticAction15(lexicalAnalyzer);
	    SemanticAction semanticAction16 = new SemanticAction16(lexicalAnalyzer);
	    SemanticAction semanticAction17 = new SemanticAction17(lexicalAnalyzer);
	    SemanticAction semanticAction18 = new SemanticAction18(lexicalAnalyzer);
	    SemanticAction semanticAction19 = new SemanticAction19(lexicalAnalyzer);
	    
	    SemanticAction error1 = new Error1(lexicalAnalyzer);
	    SemanticAction error2 = new Error2(lexicalAnalyzer);
	    SemanticAction warning1 = new Warning1(lexicalAnalyzer);
	    SemanticAction semanticActionNone = new SemanticActionNone(lexicalAnalyzer);
	    
	    for(int i = 0; i < STATES; i++)
	    	for(int j = 0; j < CHAR_SETS; j++)
	    		semanticMatrix.get(i).insertElementAt(semanticActionNone,j);
	    
	   //acciones semánticas para el estado 0
	    Vector<SemanticAction> w=semanticMatrix.get(0);

	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 3 || i==5 || i==20)
	    		w.insertElementAt(error2,i);
	    	else if(i==4 || i==13 || (i>=15 && i<=19))
	    		w.insertElementAt(semanticAction01, i);
	    	else if (i==10 || i==11)
	    		w.insertElementAt(semanticAction11, i);
	    	else if (i==12)
	    		w.insertElementAt(semanticAction10,i);
	    	else if (i==14)
	    		w.insertElementAt(semanticAction19,i);
	    }
	    
	    //acciones semánticas para el estado 1
	    w=semanticMatrix.get(1);

	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 3)
				continue;
			w.insertElementAt(semanticAction04,i);
	    }
	    
	    //acciones semánticas para el estado 2
	    w=semanticMatrix.get(2);

	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 14)
				w.insertElementAt(error1,i);
			if(i == 20)
				w.insertElementAt(warning1,i);
	    }
	    
	    //acciones semánticas para el estado 3
	    w=semanticMatrix.get(3);

	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 14)
				w.insertElementAt(error1,i);
			else if(i == 20)
				w.insertElementAt(warning1,i);
	    }
	    
	    //acciones semánticas para el estado 4
	    w=semanticMatrix.get(4);

	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 4 || i == 5 || i == 15 || i == 16 || i == 17 || i == 19)
				w.insertElementAt(semanticAction02,i);
			else
				w.insertElementAt(semanticAction03,i);
	    }
	    
	    //acciones semánticas para el estado 5
	    w=semanticMatrix.get(5);

	    for(int i = 0; i < CHAR_SETS; i++) {
			if(i == 5 || i == 18)
				w.insertElementAt(semanticAction02,i);
			else
				w.insertElementAt(semanticAction05,i);
	    }
	    
	    //acciones semánticas para el estado 6
	    w=semanticMatrix.get(6);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 5)
	    		continue;
	    	else if(i == 13 || i == 19)
				w.insertElementAt(semanticAction02,i);
			else if(i == 14)
				w.insertElementAt(error1,i);
			else
				w.insertElementAt(error2,i);
	    }
	    
	    //acciones semánticas para el estado 7
	    w=semanticMatrix.get(7);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 1)
	    		w.insertElementAt(error2,i);
	    	else if(i == 12)
				w.insertElementAt(semanticAction08,i);
			else if( i == 14)
				w.insertElementAt(error1,i);
			else if(i == 20)
				w.insertElementAt(error2,i);
			else
				w.insertElementAt(semanticAction02,i);
	    }
	    	
		    //acciones semánticas para el estado 8
		    w=semanticMatrix.get(8);

		    for(int i = 0; i < CHAR_SETS; i++) {
		    	if(i == 4 || i == 19)
		    		w.insertElementAt(semanticAction02,i);
				else
					w.insertElementAt(semanticAction07,i);
	    }
		    
		    //acciones semánticas para el estado 9
		    w=semanticMatrix.get(9);

		    for(int i = 0; i < CHAR_SETS; i++) {
		    	if(i == 10)
		    		w.insertElementAt(semanticAction02,i);
		    	else if(i == 14)
		    		w.insertElementAt(error1,i);
				else
					w.insertElementAt(error2,i);
	    }
		    
		    //acciones semánticas para el estado 10
		    w=semanticMatrix.get(10);

		    for(int i = 0; i < CHAR_SETS; i++) {
		    	if(i == 14)
		    		w.insertElementAt(error1,i);
		    	else if(i == 19)
		    		w.insertElementAt(semanticAction02,i);
				else
					w.insertElementAt(error2,i);
	    }
		    
	    //acciones semánticas para el estado 11
	    w=semanticMatrix.get(11);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 14)
	    		w.insertElementAt(error1,i);
	    	else if(i == 19)
	    		w.insertElementAt(semanticAction02,i);
			else
				w.insertElementAt(error2,i);
	    }
	    
	    //acciones semánticas para el estado 12
	    w=semanticMatrix.get(12);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 19)
	    		w.insertElementAt(semanticAction02,i);
			else
				w.insertElementAt(semanticAction07,i);
	    }
	    
	    //acciones semánticas para el estado 13
	    w=semanticMatrix.get(13);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 9)
	    		w.insertElementAt(semanticAction12,i);
			else
				w.insertElementAt(semanticAction13,i);
	    }
	    
	    //acciones semánticas para el estado 14
	    w=semanticMatrix.get(14);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 9)
	    		w.insertElementAt(semanticAction14,i);
			else
				w.insertElementAt(semanticAction15,i);
	    }
	    
	    //acciones semánticas para el estado 15
	    w=semanticMatrix.get(15);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 9)
	    		w.insertElementAt(semanticAction16,i);
			else
				w.insertElementAt(semanticAction17,i);
	    }
	    
	    //acciones semánticas para el estado 16
	    w=semanticMatrix.get(16);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 9)
	    		w.insertElementAt(semanticAction18,i);
	    	if(i == 14)
	    		w.insertElementAt(error1,i);
			else
				w.insertElementAt(error2,i);
	    }
	    
	    //acciones semánticas para el estado 17
	    w=semanticMatrix.get(17);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 14)
	    		w.insertElementAt(error1,i);
	    	if(i == 16)
	    		continue;
			else
				w.insertElementAt(error2,i);
	    }
	    
	    //acciones semánticas para el estado 18
	    w=semanticMatrix.get(18);

	    for(int i = 0; i < CHAR_SETS; i++) {
	    	if(i == 14)
	    		w.insertElementAt(error1,i);
	    	if(i == 15)
	    		w.insertElementAt(semanticAction06,i);
			else
				w.insertElementAt(error2,i);
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
			case '-':
				return 10;
			case '*':
			case '(':
			case ')':
			case '{':
			case '}':
			case ',':
			case ';':
				return 11;
			case '\'':
				return 12;
			case '.':			
				return 13;
			case '~':			
				return 14;
			case 'l':
				return 15;
			case 'u':
				return 16;
		}
		
		int charType = Character.getType(c);
        if (charType == Character.LOWERCASE_LETTER)
        	return 17;
        if (charType == Character.UPPERCASE_LETTER)
        	return 18;
		 if(Character.isDigit(c))
			 return 19;
			
		return 20;
	}
	
	public int getNextState(int currentState, char c) {
		System.out.println("getNextState: currentState:"+currentState+".char:"+c);
		return transitionMatrix.get(currentState).get(this.getId(c));
	}
	
	public SemanticAction getSemanticAction(int actualState, char c) {
		return semanticMatrix.get(actualState).get(getId(c));
	}
}
