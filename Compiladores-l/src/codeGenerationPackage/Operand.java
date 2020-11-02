package codeGenerationPackage;	

public  class Operand {		
	public static final String ST_POINTER = "ST_POINTER";	
	public static final String TRIPLET_POINTER = "TRIPLET";	
	public static final String TO_BE_DEFINED = "TOBEDEFINED";	
	public static final String NULL_OPERATOR = "NULL_OPERATOR";	

	public String operandType;
	public String ref;
	private String dataType;

	public Operand(String operandType, String ref, String dataType){
		this(operandType,ref);
		this.dataType = dataType;
	}

	public Operand(String operandType, String ref) {
		this.operandType = operandType;
		this.ref = ref;	
	}	

	public Operand(String operandType) {
		this.ref = "-";	
	}	

	public String getOperandType() {
		return operandType;
	}

	public String getDataType(){
		return this.dataType;
	}

	public String getRef() {	
		return ref;	
	}	

	@Override	
	public String toString() {	
		if (operandType.equals(Operand.TRIPLET_POINTER))
			return "["+ref+"]";	
		return ref;	
	}	
}	

