package codeGenerationPackage;

import java.util.ArrayList;
import java.util.Stack;

public class IntermediateCode {
	
	private ArrayList<Triplet> triplets;
	
	private Stack<Integer> stk;
	
	public IntermediateCode() {
		triplets = new ArrayList<Triplet>();
		stk = new Stack<Integer>();
	}
	
	public void addTriplet(Triplet t) {
		triplets.add(t);
	}
	
	public Triplet getTriplet(int index) {
		return triplets.get(index-1);
	}
	
	@Override
	public String toString() {
		String str = "\n ******* TRIPLETES ********** \n";
		for(Triplet t : triplets) {
			str += t.toString() + "\n";
		}
		return str;
	}
	
	public void pushToStack(Integer i) {
		stk.push(i);
		System.out.println("Estado de la pila: "+stk); 

	}
	
	public void popFromStack() {
		stk.pop();
		System.out.println("Estado de la pila: "+stk); 
	}
	
	public Integer topOfStack() {
		return stk.peek();
	}
	
	public int currentTripletIndex() {
		return Triplet.TRIPLET_COUNTER;
	}
}
