package codeGenerationPackage;

public class Triplet {
	public static int TRIPLET_COUNTER = 0;
	
	private String id;

	private Operator operator;
	private Operand operand1;
	private Operand operand2;
	
	private String type;
	
	public Triplet(Operator opt, Operand op1, Operand op2) {
		operator = opt;
		operand1 = op1;
		operand2 = op2;
		this.id = String.valueOf(++TRIPLET_COUNTER);
	}

	public Triplet(Operator opt, Operand op1, Operand op2, String type) {
		this.operator = opt;
		this.operand1 = op1;
		this.operand2 = op2;
		this.type = type;
		this.id = String.valueOf(++TRIPLET_COUNTER);
	}
	
	public String getId() {
		return id;
	}
	
	public Integer getNumId() {
		return Integer.parseInt(id);
	}
	
	@Override
	public String toString() {
		return id + ": (" + operator + ", " + operand1 + ", " + operand2 + ")";
	}
	
	public void modifyFirstOperand(Operand op) {
		operand1=op;
	}
	
	public void modifySecondOperand(Operand op) {
		operand2=op;
	}
}
