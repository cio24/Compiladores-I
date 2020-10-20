package codeGenerationPackage;

public class Triplet {
	public static int TRIPLET_COUNTER = 0;
	
	private String id;

	private Operator operator;
	private Operand operand1;
	private Operand operand2;
	
	private String type;
	

	public Triplet(Operator opt) {
		operator = opt;
		operand1 = new Operand(Operand.NULL_OPERATOR);
		operand2 = new Operand(Operand.NULL_OPERATOR);
		this.id = String.valueOf(++TRIPLET_COUNTER);
	}

	public Triplet(Operator opt, Operand op1) {
		this(opt);
		if (op1 != null)
			this.operand1 = op1;
	}

	public Triplet(Operator opt, Operand op1, Operand op2) {
		this(opt,op1);
		if (op2 != null)
			this.operand2 = op2;
	}

	public Triplet(Operator opt, Operand op1, Operand op2, String type) {
		this(opt,op1,op2);
		this.type = type;
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
