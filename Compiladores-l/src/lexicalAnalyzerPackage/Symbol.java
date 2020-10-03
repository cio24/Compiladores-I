package lexicalAnalyzerPackage;

public class Symbol {
	public final static String _DEFAULT_TYPE = "Default type";
	public final static String _ID = "ID";
	public final static String _DOUBLE = "Double";
	public final static String _ULONGINT = "Long int unsigned";
	public final static String _STRING = "String";

	private String type = _DEFAULT_TYPE;
	private String lexeme = "";
	private String codeLine = "00";
	private int ref = 0;
	
	public Symbol(String lexemme, String codeLine, String type) {
		this.lexeme = lexemme;
		this.codeLine = codeLine;
		this.type = type;
		this.ref = 1;
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

	public String getCodeLine() {
		return this.codeLine;
	}

	public String getType() {
		return this.type;
	}

	public int getRef() {
		return this.ref;
	}
	
	public int addRef() {
		this.ref ++;
		return this.ref;
	}

	public int removeRef() {
		this.ref--;
		return this.ref;
	}
	
	@Override
	public String toString() {
		return "\n\tTipo: \"" + this.type + "\" | Referencias: " + this.ref; 
	}
}