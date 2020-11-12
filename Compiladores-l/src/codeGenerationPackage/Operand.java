package codeGenerationPackage;	

public  class Operand {		
	public static final String ST_POINTER = "ST_POINTER";	
	public static final String TRIPLET_POINTER = "TRIPLET";	
	public static final String TO_BE_DEFINED = "TOBEDEFINED";	
	public static final String NULL_OPERATOR = "NULL_OPERATOR";	

	private String operandType;
	private String ref;
	private String dataType;

	public Operand(String operandType, String ref, String dataType){
		this(operandType,ref);
		this.dataType = dataType;
	}

	public Operand(String operandType, String ref) {
		this(operandType);
		this.ref = ref;	
	}	

	public Operand(String operandType) {
		this.operandType = operandType;
		this.ref = "-";	
		this.dataType = "ERROR";
	}	

	public String getOperandType() {
		return operandType;
	}

	public String setOperandType(String operandType) {
		return this.operandType = operandType;
	}
	public String getDataType(){
		return this.dataType;
	}

	public String getRef() {	
		return ref;	
	}	
	
	// Te devuelve el string que usamos en assembler para representar ese operando
	public String getAssemblerReference(TripletsManager tm){
		String source = this.getRef();

		// Si es puntero a triplete [1] devolvemos el registro donde se guardo el resultado de ese triplete
		if(this.getOperandType().equals(Operand.TRIPLET_POINTER))
			source = tm.getTriplet(Integer.valueOf(this.getRef())).getResultLocation();
		// Si es una variable le agregamos el underscore
		else if(source.contains(":"))
			source =  "_" + source;
		// Si no es ninguna de las anteriores entonces es una constante y la devolvemos como esta
		
		return source;
	}

	public boolean isPointer() {
		return this.operandType.equals(Operand.TRIPLET_POINTER);
	}
	
	public boolean isVar() {
		return this.ref.contains(":") || this.ref.contains("@");
	}
	
	@Override	
	public String toString() {	
		if (operandType.equals(Operand.TRIPLET_POINTER))
			return "["+ref+"]";	
		return ref;	
	}
	
	public boolean isImmediate(SymbolsTable st) {
		if (operandType.equals(Operand.ST_POINTER))
		{
			Symbol s=st.getSymbol(ref);
			if (s.getLexemeType().equals(Symbol._CONSTANT_LEXEME))
				return true;
		}
		return false;
	}
}	

