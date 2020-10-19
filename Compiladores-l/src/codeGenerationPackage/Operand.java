package codeGenerationPackage;

public  class Operand {	
	public static final String ST_POINTER = "ST_POINTER";
	public static final String TRIPLET_POINTER = "TRIPLET";
	public static final String TO_BE_DEFINED = "TOBEDEFINED";
	public static final String NULL_OPERATOR = "NULL_OPERATOR";
	
	public String type;
	public String ref;
	
	public Operand(String type, String ref) {
		this.type = type;
		this.ref = ref;
	}

	public Operand(String type) {
		this.ref = "-";
	}
	
	public String getType() {
		return type;
	} 
	
	public String getRef() {
		return ref;
	}

	@Override
	public String toString() {
		if (type == Operand.TRIPLET_POINTER)
			return "["+ref+"]";
		return ref;
	}
}
