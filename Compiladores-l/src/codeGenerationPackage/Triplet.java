package codeGenerationPackage;

public class Triplet {
	public static int TRIPLET_COUNTER = 0;
	
	private String id;
	private String operator;
	private Operand firstOperand;
	private Operand secondOperand;
	
	private String resultLocation; //Para generacion de assembler

	public Triplet(String operator) {
		this.id = String.valueOf(++TRIPLET_COUNTER);
		this.operator = operator;
		this.firstOperand = new Operand(Operand.NULL_OPERATOR,"-");
		this.secondOperand = new Operand(Operand.NULL_OPERATOR,"-");
	}

	public Triplet(String operator, Operand firstOperand) {
		this(operator);
		this.firstOperand = firstOperand;
	}

	public Triplet(String operator, Operand firstOperand, Operand secondOperand) {
		this(operator,firstOperand);
		this.secondOperand = secondOperand;
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.id + ": (" + this.operator + ", " + this.firstOperand + ", " + this.secondOperand + ")";
	}
	
	public void setFirstOperand(Operand firstOperand) {
		this.firstOperand=firstOperand;
	}
	
	public void setSecondOperand(Operand secondOperand) {
		this.secondOperand=secondOperand;
	}
	
	public boolean isCommutativeOperation() {
		if (operator.equals("+") || operator.equals("*"))
			return true;
		else return false;
	}

	public String getResultLocation() {
		return resultLocation;
	}

	public void setResultLocation(String resultLocation) {
		this.resultLocation = resultLocation;
	}
}
