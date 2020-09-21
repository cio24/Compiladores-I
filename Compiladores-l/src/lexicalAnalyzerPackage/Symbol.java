package lexicalAnalyzerPackage;

public class Symbol {
	public final static String _DEFAULT_TYPE = "Default type";
	public final static String _ID = "ID";
	public final static String _DOUBLE = "Double";
	public final static String _CONSTANT = "Constant";
	public final static String _STRING = "String";

	private String type = _DEFAULT_TYPE;
	private String lexemme = "";
	private int codeLine = -1;
	
	public Symbol(String lexemme) {
		this.lexemme = lexemme;
	}

	public Symbol(String lexemme, int codeLine) {
		this.lexemme = lexemme;
		this.codeLine = codeLine;
	}	

	public Symbol(String lexemme, String type) {
		this.lexemme = lexemme;
		this.type = type;
	}
	
	public Symbol(String lexemme, int codeLine, String type) {
		this.lexemme = lexemme;
		this.codeLine = codeLine;
		this.type = type;
	}
		
	@Override
	public String toString() {
		return "Symbol";
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLexemme() {
		return this.lexemme;
	}

	public int getCodeLine() {
		return this.codeLine;
	}

	public String getType() {
		return this.type;
	}

}