package lexicalAnalyzerPackage;

public class Symbol {
	public final static String _DEFAULT_TYPE = "Default type";
	public final static String _ID = "ID";
	public final static String _DOUBLE = "Double";
	public final static String _ULONGINT = "Long int unsigned";
	public final static String _STRING = "String";

	private String type;
	private String lexeme = "";
	private int referenceCount = 0;
	private boolean declared = false;
	
	
	public Symbol(String lexeme, String type) {
		this.lexeme = lexeme;
		this.type = type;
		this.referenceCount = 1;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLexeme() {
		return this.lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public String getType() {
		return this.type;
	}

	public int getReferenceCount() {
		return this.referenceCount;
	}
	
	public int addReference() {
		this.referenceCount++;
		return this.referenceCount;
	}

	public int subtractReference() {
		this.referenceCount--;
		return this.referenceCount;
	}
	
	public boolean declare() {
		if(this.declared)
			return true;
		
		this.declared = true;
		return false;
	}
	
	@Override
	public String toString() {
		return "\n\tTipo: \"" + this.type + "\" | Referencias: " + this.referenceCount;
	}
}