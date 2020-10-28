package codeGenerationPackage;

import java.util.ArrayList;
import java.util.Stack;

import lexicalAnalyzerPackage.Symbol;
import lexicalAnalyzerPackage.SymbolsTable;
import usefulClassesPackage.ErrorReceiver;

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
		try {
		  stk.pop();
		}
		catch(Exception e) {
			System.out.println("La pila ya está vacía."); 
		}
		System.out.println("Estado de la pila: "+stk); 
	}
	
	public Integer topOfStack() { 
		Integer i;
		try {
			  i =  stk.peek();
		}
		catch(Exception e) {
		  i = -1;
		}
		return i;
	}

	public void removeLastTriplet(){
		if(!triplets.isEmpty())
			triplets.remove(triplets.size() -1);
	}

	public int currentTripletIndex() {
		return Triplet.TRIPLET_COUNTER;
	}
	
	public void variableDeclaration(String v,String scope, String type, SymbolsTable st, String currentLine) {		
		// Ver si la variable ya esta declarada
		Symbol symbol = st.getSymbol(v+scope);
		if(symbol != null){
			// Si ya se declaro tirar error y eliminar de la tabla de simbolos 
			ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SEMANTICO," doble declaración de la variable \"" + symbol.getLexeme() + "\".");
			symbol = st.getSymbol(v);
			if(symbol.subtractReference() == 0) // Remove reference and if it reaches 0, remove SymbolTable entry
				st.removeSymbol(v);
		}
		else{
			symbol = st.getSymbol(v);
			//	Setear tipo
			symbol.setDataType(type);
			// Sino setear el uso como variable
			symbol.setUse(Symbol._VARIABLE);
			// Setear scope correctamente (cambia id en tabla de simbolos)
			st.setScope(v,scope);					
		}
	}
	
	public void procedureDeclaration(String v,String scope, SymbolsTable st, String currentLine) {		
		// Ver si la variable ya esta declarada
		Symbol symbol = st.getSymbol(v+scope);
		if(symbol != null){
			// Si ya se declaro tirar error y eliminar de la tabla de simbolos 
			ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SEMANTICO," doble declaración de procedimiento \"" + symbol.getLexeme() + "\".");
			symbol = st.getSymbol(v);
			if(symbol.subtractReference() == 0) // Remove reference and if it reaches 0, remove SymbolTable entry
				st.removeSymbol(v);
		}
		else{
			symbol = st.getSymbol(v);
			// Sino setear el uso como variable
			symbol.setUse(Symbol._PROCEDURE);
			// Setear scope correctamente (cambia id en tabla de simbolos)
			st.setScope(v,scope);					
		}
	}
	
	public void procedureCall(String name,String scope, SymbolsTable st, String currentLine) {
		boolean isDeclared = isDeclared(name,scope,st);
		st.removeSymbol(name);
		if(isDeclared){ //hay un identificador declarado al alcance con el mismo nombre
			Symbol s = st.getSymbol(st.findSTReference(name + scope)); //obtengo el simbolo correspondiente al identificador
			if(s.getUse().equals(Symbol._PROCEDURE)){ //si el uso es un procedimiento
				Triplet t = createTriplet("PC",st.findSTReference(name + scope));
			}
			else //hay una variable al alcance con el mismo nombre pero no es un procedimiento
				ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SINTACTICO,name + "no es un procedimiento");
		}else  //no hay nada declarado con ese identificador al alcance
			ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SINTACTICO,"no existe un procedimiento " + name + " declarado al alcance");		
	}
	
	public void updateSecondOperandFromStack(int amount){
		int unstacked = topOfStack(); //we get the id of the triplet on the top of the stack
		//ic.popFromStack(); //we remove the id triplet from the top of the stack
		Triplet trip = getTriplet(unstacked); //then we get the triplet so we can write in the second operand
		trip.setSecondOperand("[" + String.valueOf(currentTripletIndex()+amount) + "]"); //the adress of the jump
	}
	
	public void updateFirstOperandFromStack(int amount){
		int unstacked = topOfStack(); //we get the id of the triplet on the top of the stack
		//ic.popFromStack(); //we remove the id triplet from the top of the stack
		Triplet trip = getTriplet(unstacked); //then we get the triplet so we can write in the second operand
		trip.setFirstOperand("[" + String.valueOf(currentTripletIndex()+amount) + "]"); //the adress of the jump
	}
	
	public Triplet createEmptyTriplet(){
		Triplet t = new Triplet("ERROR");
		addTriplet(t);
		return t;
	}
	
	public Triplet createTriplet(String operator, String firstOperand, String secondOperand){
		Triplet t = new Triplet(operator, firstOperand, secondOperand);
		//showMessage("NUEVO TRIPLET CREADO CON EL OPERANDO: " + operator + " --> " + t.toString());
		addTriplet(t);
		return t;
	}
	
	public Triplet createTriplet(String Operator, String firstOperand){
		Triplet t = new Triplet(Operator, firstOperand);
		addTriplet(t);
		return t;
	}
	
	public Triplet createTriplet(String Operator){
		Triplet t = new Triplet(Operator);
		addTriplet(t);
		return t;
	}
	
	//true si esta declarada en el scope actual o en uno que lo contiene
	public boolean isDeclared(String name, String scope, SymbolsTable st){
		String fullName = name + scope; // Nombre entero
		
		fullName = st.findSTReference(fullName);
		
		return fullName != null && fullName.contains(":");
	}

}
