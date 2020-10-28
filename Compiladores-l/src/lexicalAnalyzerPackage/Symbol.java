package lexicalAnalyzerPackage;

public class Symbol {

	//Lexeme types
	public final static String _DEFAULT_TYPE = "Default type";
	public final static String _ID_LEXEME = "Identifier";
	public final static String _DOUBLE_CONSTANT = "DOUBLE Constant";
	public final static String _ULONGINT_CONSTANT = "ULONGINT Constant";
	public final static String _STRING_CONSTANT = "STRING Constant";

	//data types
	public final static String _DOUBLE_TYPE = "Double";
	public final static String _ULONGINT_TYPE = "Unsigned Long Int";
	public final static String _STRING_TYPE = "String";

	//uses
	public final static String _VARIABLE = "Variable";
	public final static String _PROCEDURE = "Procedure";
	public final static String _PARAMETER = "Parameter";

	//pass by types
	public final static String _COPY_VALUE = "Copy Value";

	private String lexemeType;
	private String dataType;
	private String lexeme = "";
	private int referenceCount = 0;
	private String use;
	private String passByType = _COPY_VALUE;

	public Symbol(String lexeme, String lexemeType) {
		this.lexeme = lexeme;
		this.lexemeType = lexemeType;

		switch(lexemeType){
			case _DOUBLE_CONSTANT:
				this.dataType = _DOUBLE_TYPE;
				break;
			case _ULONGINT_CONSTANT:
				this.dataType = _ULONGINT_TYPE;
				break;
			case _STRING_CONSTANT:
				this.dataType = _STRING_TYPE;
				break;
			default:
				this.dataType = "Undefined";
		}
		this.referenceCount = 1;
		this.use = "Undefined";
	}

	public Symbol(String lexeme, String lexemeType, String dataType) {
		this.lexeme = lexeme;
		this.lexemeType = lexemeType;
		this.dataType = dataType;
		this.referenceCount = 1;
		this.use = "Undefined";
	}

	public Symbol(String lexeme, String lexemeType, String dataType, String use) {
		this.lexeme = lexeme;
		this.lexemeType = lexemeType;
		this.dataType = dataType;
		this.use = use;
		this.referenceCount = 1;
	}

	public void setLexemeType(String type) {
		this.lexemeType = type;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	public String getLexeme() {
		return this.lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public String getLexemeType() {
		return this.lexemeType;
	}

	public String getDataType() {
		return this.dataType;
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

	public void setUse(String use){
		this.use = use;
	}

	public String getUse(){
		return this.use;
	}
	
	@Override
	public String toString() {
		String use;
		if(this.use.equals("Undefined"))
			use = "";
		else
			use = " | Use: " + this.use;
		String passByType = "";
		if(this.use.equals(_PARAMETER))
			passByType = " | Pass by: " + _COPY_VALUE;
		return "\n\tLexeme: \"" + this.lexemeType + "\"" + use + passByType + " | Type: " + this.dataType + " | References: " + this.referenceCount;
	}
}