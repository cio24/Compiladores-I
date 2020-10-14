package codeGenerationPackage;

public  class Operand {	
	public static final String ST_POINTER = "ST_POINTER";
	public static final String TRIPLET = "TRIPLET";
	
	public String type;
	public String ref;
	
	public Operand(String type, String ref) {
		this.type = type;
		this.ref = ref;
	}
	
	public String getType() {
		return type;
	} 
	
	public String getRef() {
		return ref;
	}

	@Override
	public String toString() {
		if (type == Operand.TRIPLET) {
			return "["+ref+"]";
		}else {
			return ref;
		}
	}
}
