package codeGenerationPackage;

import java.util.ArrayList;
import java.util.Stack;

public class TripletsManager {
    private ArrayList<Triplet> triplets;
    private Stack<Integer> tripletsIdStack;

    public TripletsManager() {
        triplets = new ArrayList<>();
        tripletsIdStack = new Stack<>();
    }

    public void addTriplet(Triplet t) {
        triplets.add(t);
    }

    public Triplet getTriplet(int index) {
        return triplets.get(index-1);
    }

    public void pushToStack(Integer i) {
        tripletsIdStack.push(i);
        //System.out.println("Estado de la pila: " + tripletsIdStack);

    }

    public void popFromStack() {
        try {
            tripletsIdStack.pop();
        }
        catch(Exception e) {
            System.out.println("La pila ya está vacía.");
        }
        //System.out.println("Estado de la pila: " + tripletsIdStack);
    }

    public Integer topOfStack() {
        Integer i;
        try {
            i =  tripletsIdStack.peek();
        }
        catch(Exception e) {
            i = -1;
        }
        return i;
    }

    public void removeLastTriplet(){
        if(!triplets.isEmpty()){
            triplets.remove(triplets.size() -1);
            Triplet.TRIPLET_COUNTER--;
        }
    }

    public int getCurrentTripletIndex() {
        return Triplet.TRIPLET_COUNTER;
    }

    public void updateOperandFromStack(int offset, int operand){
        int unstacked = topOfStack(); //we get the id of the triplet on the top of the stack
        Triplet t = getTriplet(unstacked); //then we get the triplet so we can write in the operand
        if(operand == 1)
            t.setFirstOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(getCurrentTripletIndex()+offset))); //the adress of the jump
        else if (operand == 2)
            t.setSecondOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(getCurrentTripletIndex()+offset))); //the adress of the jump

    }

    public Triplet createEmptyTriplet(){
        Triplet t = new Triplet("ERROR");
        addTriplet(t);
        return t;
    }

    public Triplet createTriplet(String operator, Operand firstOperand, Operand secondOperand){
        Triplet t = new Triplet(operator, firstOperand, secondOperand);
        addTriplet(t);
        return t;
    }

    public Triplet createTriplet(String Operator, Operand firstOperand){
        Triplet t = new Triplet(Operator, firstOperand);
        addTriplet(t);
        return t;
    }

    public Triplet createTriplet(String Operator){
        Triplet t = new Triplet(Operator);
        addTriplet(t);
        return t;
    }

    //Crea un triplet de salto dirigido al comienzo de la sentencia loop
    public Triplet createBTriplet(Object obj1, String branchType){
        int unstacked = this.topOfStack(); //we get the id of the triplet that represent the adress of the tag that we need to jump
        this.popFromStack(); //we remove the id triplet from the top of the stack
        Operand op1 = (Operand) obj1; //we get the triplet asociate to the condition
        Operand op2 = new Operand(Operand.TRIPLET_POINTER,String.valueOf(unstacked)); //this will contain the jump adress
        String opt = branchType; //the operation of the tiplet is the branch not equal
        Triplet t = this.createTriplet(opt,op1,op2);
        return t;
    }

    //Actualiza un triplet de salto que se encuentra en el final de la rama THEN de un IF,dirigido al final de toda la sentencia IF.
    public Triplet createBITriplet(Object obj1){
        Operand op1 = (Operand) obj1;
        String opt = "BI";
        Triplet t = this.createTriplet(opt,op1);
        return t;
    }

    @Override
    public String toString() {
        String str = " \n\n ******* TRIPLETES ********** \n";
        for(Triplet t : triplets) {
            str += t.toString() + "\n";
        }
        return str;
    }

}
