package codeGenerationPackage;

import java.util.ArrayList;
import java.util.Stack;
import lexicalAnalyzerPackage.LexicalAnalyzer;
import lexicalAnalyzerPackage.Symbol;
import lexicalAnalyzerPackage.SymbolsTable;
import usefulClassesPackage.ErrorReceiver;
import usefulClassesPackage.ProcedureData;

public class IntermediateCode {
	
	private ArrayList<Triplet> triplets;
	private Stack<Integer> stk;
	private SymbolsTable st;
	private ArrayList<ProcedureData> procedureStack;
	private LexicalAnalyzer la;

	public IntermediateCode(SymbolsTable st, LexicalAnalyzer la) {
		triplets = new ArrayList<Triplet>();
		stk = new Stack<Integer>();
		this.st = st;
		procedureStack = new ArrayList<>();
		this.la = la;
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

	public void setDeclaration(String idName, String scope, String type, String use){
		Symbol s = st.getSymbol(idName);
		s.setDataType(type);
		s.setUse(use);

		//se le agrega el scope a la key del símbolo que posee la key idName
		st.setScope(idName,scope);
	}

	public void setDeclaration(String idName, String scope, String use){
		setDeclaration(idName,scope,"None",use);
	}
	
	public void variableDeclaration(String v,String scope, String type, String currentLine) {
		// Ver si la variable ya esta declarada en este scope
		Symbol symbol = st.getSymbol(v+scope);
		if(symbol != null){
			// Si ya se declaro tirar error y eliminar de la tabla de simbolos 
			ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SEMANTICO," esta variable ya ha sido declarada \"" + symbol.getLexeme() + "\".");
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
	
	public void procedureCall(String idName,String scope, String currentLine) {
		st.removeSymbol(idName); //no entiendo xq se hace esto

		String IdNameDeclaration = st.findClosestIdDeclaration(idName + scope);
		if(IdNameDeclaration != null){ //hay un identificador declarado al alcance con el mismo nombre
			Symbol s = st.getSymbol(IdNameDeclaration);
			if(s.getUse().equals(Symbol._PROCEDURE)) {
				Triplet t = createTriplet("PC",new Operand(Operand.ST_POINTER,IdNameDeclaration)); //que hacemos con este terceto??
			}
			else //hay una variable al alcance con el mismo nombre pero no es un procedimiento
				ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SINTACTICO,idName + "no es un procedimiento");
		}
		else  //no hay nada declarado con ese identificador al alcance
			ErrorReceiver.displayError(ErrorReceiver.ERROR,currentLine,ErrorReceiver.SINTACTICO,"no existe un procedimiento " + idName + " declarado al alcance");
	}
	
	public void updateSecondOperandFromStack(int offset){
		int unstacked = topOfStack(); //we get the id of the triplet on the top of the stack
		//ic.popFromStack(); //we remove the id triplet from the top of the stack
		Triplet trip = getTriplet(unstacked); //then we get the triplet so we can write in the second operand
		trip.setSecondOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(currentTripletIndex()+offset))); //the adress of the jump
	}
	
	public void updateFirstOperandFromStack(int offset){
		int unstacked = topOfStack(); //we get the id of the triplet on the top of the stack
		//ic.popFromStack(); //we remove the id triplet from the top of the stack
		Triplet trip = getTriplet(unstacked); //then we get the triplet so we can write in the second operand
		trip.setFirstOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(currentTripletIndex()+offset))); //the adress of the jump
	}
	
	public Triplet createEmptyTriplet(){
		Triplet t = new Triplet("ERROR");
		addTriplet(t);
		return t;
	}
	
	public Triplet createTriplet(String operator, Operand firstOperand, Operand secondOperand){
		Triplet t = new Triplet(operator, firstOperand, secondOperand);
		//showMessage("NUEVO TRIPLET CREADO CON EL OPERANDO: " + operator + " --> " + t.toString());
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

	public void addProcedureToStack(String fullProcedureId,int NA, boolean shadowing){
		int remainingNests;

		//para cada procedimiento apilado
		for(ProcedureData p: procedureStack){

			//le resto 1 a la cantidad de anidamientos restantes
			remainingNests = p.subtractNA();

			//si la cantidad de anidamientos restantes es negativa signifca que ya supero su cantidad permitida
			if(remainingNests < 0)
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),
					ErrorReceiver.SEMANTICO, "No se puede declarar el procedimiento " +
					fullProcedureId + " porque supera su cantidad de anidamientos permitidos");
		}

		//agrego el nuevo procedimiento a la pila
		procedureStack.add(new ProcedureData(fullProcedureId,NA,shadowing));
	}

	public void unstackLastProcedure(){

		//desapilo el último procedimiento agregado
		procedureStack.remove(procedureStack.size() - 1);

		//le sumo 1 a todos los procedimientos apilados
		for(ProcedureData p: procedureStack)
			p.sumNA();
	}

	public void procedureDeclarationControl(String procedureId, String scope){
		String fullProcedureId = procedureId + scope;
		String idDeclaration = st.findClosestIdDeclaration(fullProcedureId);

		//si el procedimiento no fue declarado en este scope concretamos la declaración
		if(idDeclaration != fullProcedureId)
			setDeclaration(procedureId, scope, Symbol._PROCEDURE);

		//desapilo el último procedimiento y actualizo el numero de anidamientos de los apilados
		unstackLastProcedure();
	}

	public void variableDeclarationControl(String varId,String scope, String dataType) {
		String fullVarId = varId + scope;

		//obtengo (de existir) el nombre con el que la variable se declaro antes
		String idDeclaration = st.findClosestIdDeclaration(fullVarId);

		//obtengo el valor del shadowing en caso de que se este declarando dentro de un procedimiento
		boolean shadowing = false;
		if(!procedureStack.isEmpty())
			shadowing = procedureStack.get(procedureStack.size() -1).isShadowing();

		if(idDeclaration == null){
			//si es la primera vez que se quiere declarar, se concreta la declaración
			setDeclaration(varId,scope,dataType,Symbol._VARIABLE);
		}
		else if(idDeclaration.equals(fullVarId)){
			//si ya existe una declaración de esta variable en el mismo ambito tiramos error
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO," esta variable ya ha sido declarada \"" + varId + "\".");

			//acomodamos la tabla de simbolos
			Symbol s = st.getSymbol(varId);
			if(s.subtractReference() <= 0) // Remove reference and if it reaches 0, remove SymbolTable entry
				st.removeSymbol(varId);
		}
		else{
			//si el shadowing es true y ya existe una declaración en otro ambito al alcance tiramos error
			if(shadowing)
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO," la variable \"" + varId + scope + "\" viola el shadowing");
			//aunque no se pueda declarar por shadowing la declaramos igual
			//asi se pueden detectar errores en relación a esta variable
			setDeclaration(varId,scope,dataType,Symbol._VARIABLE);
		}
	}
	
	//Crea un triplet de salto dirigido al comienzo de la sentencia loop
	public Triplet createBFTriplet(Object obj1){	
		int unstacked = this.topOfStack(); //we get the id of the triplet that represent the adress of the tag that we need to jump	
	    this.popFromStack(); //we remove the id triplet from the top of the stack	
	    Operand op1 = (Operand) obj1; //we get the triplet asociate to the condition	
	    Operand op2 = new Operand(Operand.TRIPLET_POINTER,String.valueOf(unstacked)); //this will contain the jump adress	
	    String opt = "BF"; //the operation of the tiplet is the branch not equal	
	    Triplet t = this.createTriplet(opt,op1,op2);	
	    return t;	
	}	

	//Actualiza un triplet de salto que se encuentra en el final de la rama THEN de un IF,dirigido al final de toda la sentencia IF.
	public Triplet createBITriplet(Object obj1){	
		updateSecondOperandFromStack(1);	
		Operand op1 = (Operand) obj1;	
		String opt = "BI";	
		Triplet t = this.createTriplet(opt,op1);	
		return t;	
	}
	
	
}
