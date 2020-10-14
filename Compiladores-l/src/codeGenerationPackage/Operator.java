package codeGenerationPackage;

public class Operator {

	public String type;
	
	public Operator(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	} 
	
	@Override
	public String toString() {
		return type;
	}
	
}
