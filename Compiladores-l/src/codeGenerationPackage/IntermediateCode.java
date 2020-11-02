package codeGenerationPackage;

import java.util.ArrayList;
import java.util.HashMap;

import lexicalAnalyzerPackage.LexicalAnalyzer;
import utilitiesPackage.ErrorReceiver;

public class IntermediateCode {

	private SymbolsTable st;
	private ArrayList<ProcedureData> procedureStack;
	private HashMap<String,ProcedureData> proceduresData;
	private ArrayList<String> realParametersTypes;
	private LexicalAnalyzer la;
	private TripletsManager tm;


	public IntermediateCode(SymbolsTable st, LexicalAnalyzer la, TripletsManager tm) {
		this.st = st;
		procedureStack = new ArrayList<>();
		proceduresData = new HashMap<>();
		realParametersTypes = new ArrayList<>();
		this.la = la;
		this.tm = tm;
	}

	//######### MANEJO DE VARIABLES #########

	public void variableDeclarationControl(String varId,String scope, String dataType) {
		String fullVarId = varId + scope;

		//obtengo (de existir) el nombre con el que la variable se declaro antes
		String idDeclaration = st.findClosestIdDeclaration(fullVarId);

		//obtengo el valor del shadowing en caso de que se este declarando dentro de un procedimiento
		boolean shadowing = false;
		if(!procedureStack.isEmpty())
			shadowing = procedureStack.get(procedureStack.size() -1).isShadowingActivated();

		if(idDeclaration == null){
			//si es la primera vez que se quiere declarar, se concreta la declaración
			setVarDeclaration(varId,scope,dataType);
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
			setVarDeclaration(varId,scope,dataType);
		}
	}
	public Symbol setVarDeclaration(String id, String scope, String dataType){
		//borro el símbolo que corresponde con el identificador de la variable
		st.removeSymbol(id);

		//creo un nuevo símbolo del tipo variable con el nombre que se corresponde con el scope actual
		String fullVarId = id + scope;
		Symbol vs = new Symbol(fullVarId,Symbol._IDENTIFIER_LEXEME,dataType,Symbol._VARIABLE_USE);

		//lo agrego a la tabla de símbolos
		st.addSymbol(fullVarId,vs);
		return vs;
	}
	public String variableAssignmentControl(String varId, String scope, Operand expression){
		String fullVarId = varId + scope;
		String varIdDeclaration = st.findClosestIdDeclaration(fullVarId);

		//se borra el símbolo xq se tiene que agregar uno nuevo con los datos del scope y el tipo
		st.removeSymbol(varId);
		Triplet t;

		//si no se encuentran una declaración se tira error
		if(varIdDeclaration == null){
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,
					"se utiliza una variable antes de declararla");
			t = tm.createTriplet("=", new Operand(Operand.ST_POINTER,varId + ":undefined"),expression);
		} else {

			//si se encuentra la declaración, se debe controlar los tipos
			String varType = st.getSymbol(varIdDeclaration).getDataType();
			String expressionType = expression.getDataType();
			if(!varType.equals(expressionType)){
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,
						"tipos incopatbiles, el tipo de la variable es " + varType
								+ " y se le asigno una expresión de tipo " + expressionType);

				//creamos un triplet que indique el error
				t = tm.createTriplet("ERROR");
			}
			else{
				//se muestra el nombre de la variable con el scope en dondé se declaro, no en donde se encontró
				//para hacerlo mas legible
				t = tm.createTriplet("=", new Operand(Operand.ST_POINTER,varIdDeclaration),expression);

				//se incrementa la cantidad de referencias que tiene la variable
				st.getSymbol(varIdDeclaration).addReference();
			}


		}
		//retornamos el numero de triplet para que se pueda asignar al $$ de la sentencia ejecutable
		return t.getId();
	}
	public Operand operationTypesControl(String operator, Operand op1, Operand op2){
		Triplet t;
		if(op1.getDataType().equals(op2.getDataType())){
			System.out.println("tipo 1: " + op1.getDataType() + " Tipo 2: " + op2.getDataType());
			t = tm.createTriplet(operator,op1, op2);
			return new Operand(Operand.TRIPLET_POINTER,t.getId(), op1.getDataType());
		}
		else{
			System.out.println("[Linea " + la.getCurrentLine() + "] Incopatibilidad de tipos: "
					+ op1.getDataType() + operator + op2.getDataType() );
			t = tm.createTriplet("ERROR");
			return new Operand(Operand.TRIPLET_POINTER,t.getId(), "ERROR");
		}
	}


	//######### MANEJO DE PROCEDIMIENTOS #########

	/*
	 *	se preservan los datos de los procedimientos mas que nada
	 *  para controlar los tipos y cantidad de parametros
	 */
	public void saveProcedureData(ProcedureData pd){
		this.proceduresData.put(pd.getFullProcId(),pd);
	}
	public ProcedureData getProcedureData(String fullProcId){
		return this.proceduresData.get(fullProcId);
	}


	/*
	 *	se maneja un stack de procedimientos para controlar
	 *  los anidamientos que se permiten con los NA's
	 */
	public void addProcedureDataToStack(ProcedureData newProcedureData){
		int remainingNests;

		//para cada procedimiento apilado
		for(ProcedureData pd: procedureStack){

			//le resto 1 a la cantidad de anidamientos restantes
			remainingNests = pd.subtractNA();

			//si la cantidad de anidamientos restantes es negativa signifca que ya supero su cantidad permitida
			if(remainingNests < 0)
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),
						ErrorReceiver.SEMANTICO, "No se puede declarar el procedimiento " +
								newProcedureData.getFullProcId() + " porque se supera la cantidad de anidamientos permitidos del procedimiento " + pd.getFullProcId());
		}

		// lo agrego a la pila
		procedureStack.add(newProcedureData);
	}
	public void removeLastProcedureFromStack(){
		procedureStack.remove(procedureStack.size()-1);
	}
	public ProcedureData getProcedureDataFromStack(){
		return procedureStack.get(procedureStack.size()-1);
	}
	public ProcedureData unstackLastProcedure(){

		//desapilo el último procedimiento agregado
		ProcedureData pRemoved = procedureStack.remove(procedureStack.size() - 1);

		//le sumo 1 a todos los procedimientos apilados
		for(ProcedureData pd: procedureStack)
			pd.addNA();

		return pRemoved;
	}


	//controles en la declaracion de procedimiento
	public void realParameterControl(String parameterId, String scope) {
		String fullId = parameterId + scope;

		//obtengo el id de la delaración de la variable del ambito más cercano
		String idDeclaration = st.findClosestIdDeclaration(fullId);

		//si no existe una variable declarada con ese nombre tiramos error
		if (idDeclaration == null)
			ErrorReceiver.displayError(ErrorReceiver.ERROR, la.getCurrentLine(), ErrorReceiver.SEMANTICO,
					"No existe una variable al alcanze con este nombre");

			//si el identificador corresponde a un procedimiento tiramos error
		else if (st.getSymbol(idDeclaration).getUse().equals(Symbol._PROCEDURE_USE))
			ErrorReceiver.displayError(ErrorReceiver.ERROR, la.getCurrentLine(), ErrorReceiver.SEMANTICO,
					"No se puede pasar un procedimiento por parametro");

			//sino significa que el parametro se corresponde con una variable o parametro de otro procedimiento
		else {

			//borro el símbolo extra que se creo con este identificador
			st.removeSymbol(parameterId);

			//obtengo el símbolo que se corresponde con el id de la variable que se pasa por parametro
			Symbol sp = st.getSymbol(idDeclaration);

			//le incremento las referencias
			sp.addReference();

			//agrego el tipo de este identificador a la lista de parametros reales así se puede controlar
			//cuando se terminen de leer los parametros
			realParametersTypes.add(sp.getDataType());
		}
	}
	public void cleanRealParameters(){
		this.realParametersTypes = new ArrayList<>();
	}
	public void procedureHeaderControl(String procId, String scope){
		//en este procedure data se van a ir guardando los datos del procedimiento
		//a medida que se los encuentre
		ProcedureData pd;

		//obtengo el nombre con el que se pudo haber declarado antes el procedimiento
		String idDeclaration = st.findClosestIdDeclaration(procId + scope);

		// verificamos si se declaro en este ambito
		if(idDeclaration != null && idDeclaration.equals(procId + scope)){
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,
					" el procedimiento \"" + idDeclaration + " ya ha sido declarado en este ambito " + "\".");

			//se le da un nombre con error al procedureData para detectar el error más adelante
			pd = new ProcedureData(procId + ":ERROR");
		}
		//si es la primera vez que se declara
		else{
			//se crea un terceto para indicar donde comienza la declaración del procedimineto
			tm.createTriplet("PDB",new Operand(Operand.ST_POINTER,procId + scope));

			//se le da el nombre completo del procedimiento al procedureData
			pd = new ProcedureData(procId + scope);
		}

		//se lo debe apilar para controlar en el resto del procedimiento
		addProcedureDataToStack(pd);
	}
	public void procedureDeclarationControl(String procId, String scope){

		//desapilo el último procedimiento y actualizo el numero de anidamientos de los apilados
		ProcedureData pd = unstackLastProcedure();

		//si el procedimiento no fue declarado en este scope concretamos la declaración
		if(pd.getFullProcId() != procId + scope)
			setProcDeclaration(procId, pd);
	}
	public Symbol setProcDeclaration(String procId,ProcedureData pd){
		//borro el símbolo que corresponde con el identificador del procedimiento
		st.removeSymbol(procId);

		//agrego el simbolo del procedimiento a la tabla de símbolos
		Symbol ps = new Symbol(pd.getFullProcId(),Symbol._IDENTIFIER_LEXEME,"-",Symbol._PROCEDURE_USE);
		ps.setShadowing(pd.isShadowingActivated());
		ps.setNA(pd.getNA());
		st.addSymbol(pd.getFullProcId(),ps);

		return ps;
	}


	//contorles en la invocación de procedimiento
	public void procedureCall(String id,String scope) {

		//cuando se escribe el id del procedimiento nuevo se crea un nuevo símbolo en la tabla para ese id
		//se borra xq se supone que se esta llamando a un procedimiento que existe cuyo id también registra el scope
		st.removeSymbol(id);

		//obtenemos el nombre con el scope donde realmente se declaro el procedimiento si es que se declaro
		String idDeclaration = st.findClosestIdDeclaration(id + scope);

		//si hay un identificador declarado al alcance con el mismo id
		if(idDeclaration != null){
			Symbol is = st.getSymbol(idDeclaration);
			if(is.getUse().equals(Symbol._PROCEDURE_USE)){

				if(areParametersCallCorrect(idDeclaration,this.realParametersTypes)){
					//obtenemos los datos del procedimiento
					ProcedureData pd = proceduresData.get(idDeclaration);

					//guardamos un terceto asociado a la llamada del procedimiento
					tm.createTriplet("PC",new Operand(Operand.ST_POINTER,idDeclaration));

					//le sumamos 1 a la referencia porque se uso de nuevo el identificador
					is.addReference();
				}

			}
			else
				//hay una variable al alcance con el mismo nombre pero no es un procedimiento
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,id + "no es un procedimiento");
		}
		else
			//no hay nada declarado con ese identificador al alcance
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"no existe un procedimiento " + id + " declarado al alcance");
	}
	public boolean areParametersCallCorrect(String procFullId, ArrayList<String> realParametersType){
		ProcedureData pd = proceduresData.get(procFullId);
		int realParametersAmount = realParametersType.size();
		if(pd.getParametersAmount() != realParametersAmount){
			System.out.println("HIJO DE PUTA ESTA CANTIDAD DE PARAMETROS NO ES LA CORRECTA");
			return false;
		}
		else {
			if (realParametersAmount-- > 0  && !realParametersType.get(0).equals(pd.getFirstFormalParameterType())) {
				System.out.println("ERROR EL PRIMER PARAMETRO DEBE SER UN " + pd.getFirstFormalParameterType() + " y es " + realParametersType.get(0));
				return false;
			}
			if(realParametersAmount-- > 0 && !realParametersType.get(1).equals(pd.getSecondFormalParameterType())){
				System.out.println("ERROR EL SEGUNDO PARAMETRO DEBE SER UN " + pd.getSecondFormalParameterType() + " y es " + realParametersType.get(1));
				return false;
			}
			if(realParametersAmount > 0 && !realParametersType.get(2).equals(pd.getThirdFormalParameterType())){
				System.out.println("ERROR EL TERCER PARAMETRO DEBE SER UN " + pd.getThirdFormalParameterType() + " y es " + realParametersType.get(2));
				return false;
			}
		}
		return true;
	}
}
