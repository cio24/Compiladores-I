package codeGenerationPackage;

public class Symbol {

    //Lexeme types
    public final static String _IDENTIFIER_LEXEME = "Identifier";
    public final static String _CONSTANT_LEXEME = "Constant";

    //data types
    public final static String _DOUBLE_TYPE = "DOUBLE";
    public final static String _ULONGINT_TYPE = "ULONGINT";
    public final static String _STRING_TYPE = "STRING";
    public final static String _NONE_TYPE = "NONE";
    public final static String _UNDEFINED_TYPE = "UNDEFINED";

    //uses
    public final static String _VARIABLE_USE = "Variable";
    public final static String _PROCEDURE_USE = "Procedure";
    public final static String _PARAMETER_USE = "Parameter";
    public final static String _UNDEFINED_USE = "Undefined";

    //parameter semantic
    public static String _COPY_VALUE_SEMANTIC = "Copy-Value";


    private String lexeme;
    private String lexemeType;
    private int referenceCount;
    private String dataType;
    private String use;
    private String parameterSemantic;
    private ProcedureData procedureData;

    //MILLONES DE CONSTRUCTORES LAY AHEAD

    public Symbol(String lexeme, String lexemeType) {
        this(lexeme,lexemeType,"-");
    }

    public Symbol(String lexeme, String lexemeType, String dataType) {
        this(lexeme,lexemeType,dataType,"-");
    }

    public Symbol(String lexeme, String lexemeType, String dataType, String use) {
        this(lexeme,lexemeType,dataType,use,"-");
    }

    public Symbol(String lexeme, String lexemeType, String dataType, String use, String parameterSemantic) {
        this.lexeme = lexeme;
        this.lexemeType = lexemeType;
        this.referenceCount = 1;
        this.dataType = dataType;
        this.use = use;
        this.parameterSemantic = parameterSemantic;
    }

    // MILLONES DE SETTERS & GETTERS UNTIL THE END OF TIMES
    public String getLexeme(){
        return this.lexeme;
    }
    public void setLexeme(String lexeme){
        this.lexeme = lexeme;
    }
    public String getLexemeType() {
        return lexemeType;
    }
    public void setLexemeType(String lexemeType) {
        this.lexemeType = lexemeType;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getUse() {
        return use;
    }
    public void setUse(String use) {
        this.use = use;
    }
    public int addReference() {
        return ++this.referenceCount;
    }
    public int subtractReference() {
        return --this.referenceCount;
    }

    public ProcedureData getProcedureData() {
    	return this.procedureData;
    }
    
    public void setProcedureData(ProcedureData pd) {
    	this.procedureData = pd;
    }
    
    // OVERRIDE PRINT PARA TABLA DE SIMBOLOS
    
    @Override
    public String toString() {
        String dataType;
        if(this.dataType.equals("-"))
            dataType = "";
        else
            dataType = " | Type: " + this.dataType;

        String use;
        if(this.use.equals("-"))
            use = "";
        else
            use = " | Use: " + this.use;

        String pd = "";
        if(this.procedureData != null)
        	pd = this.procedureData.toString();
        
        String semantic;
        if(this.parameterSemantic.equals("-"))
            semantic = "";
        else
            semantic = " | Semantic: " + this.parameterSemantic;

       
        return " Lexeme: \"" + this.lexemeType + "\"" + " | References: " + this.referenceCount
                + use + dataType + semantic + pd + "\n";
    }
    
    public void printRow(String lexeme)
    {        
        String pd = "";
        if(this.procedureData != null)
        	pd = this.procedureData.toString();
            
        System.out.format("%40s | %20s | %12s | %15s | %10s | %10s | %40s",
        		 lexeme, this.lexemeType, this.referenceCount, this.use,  this.dataType, this.parameterSemantic, pd);
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    	
    }
}
