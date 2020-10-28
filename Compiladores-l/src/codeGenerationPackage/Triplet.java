package codeGenerationPackage;

public class Triplet {
	public static int TRIPLET_COUNTER = 0;
	
	private String getId;
	private String operator;
	private String firstOperand;
	private String secondOperand;
	
	private String type;
	

	public Triplet(String operator) {
		this.getId = String.valueOf(++TRIPLET_COUNTER);
		this.operator = operator;
		this.firstOperand = "-";
		this.secondOperand = "-";
	}

	public Triplet(String operator, String firstOperand) {
		this.getId = String.valueOf(++TRIPLET_COUNTER);
		this.operator = operator;
		this.firstOperand = firstOperand;
		this.secondOperand = "-";
	}

	public Triplet(String operator, String firstOperand, String secondOperand) {
		this.getId = String.valueOf(++TRIPLET_COUNTER);
		this.operator = operator;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
	}
	
	public String getId() {
		return this.getId;
	}
	
	@Override
	public String toString() {
		return this.getId + ": (" + this.operator + ", " + this.firstOperand + ", " + this.secondOperand + ")";
	}
	
	public void setFirstOperand(String firstOperand) {
		this.firstOperand=firstOperand;
	}
	
	public void setSecondOperand(String secondOperand) {
		this.secondOperand=secondOperand;
	}
}
