package codeGenerationPackage;


public class ProcedureData {
    private boolean shadowingActivated;
    private int NA = -1;
    private String firstFormalParameterType;
    private Symbol firstParameterSymbol;
    private String secondFormalParameterType;
    private Symbol secondParameterSymbol;
    private String thirdFormalParameterType;
    private Symbol thirdParameterSymbol;
    private int parametersAmount = 0;
    private String fullProcId;

    public ProcedureData(String fullProcId){
        this.fullProcId = fullProcId;
    }

    //GETTERS
    public String getFirstFormalParameterType() {
        return firstFormalParameterType;
    }
    public String getSecondFormalParameterType() {
        return secondFormalParameterType;
    }
    public String getThirdFormalParameterType() {
        return thirdFormalParameterType;
    }
    public Symbol getFirstParameterSymbol() {
    	return this.firstParameterSymbol;
    }  
    public Symbol getSecondParameterSymbol() {
    	return this.secondParameterSymbol;
    }  
    public Symbol getThirdParameterSymbol() {
    	return this.thirdParameterSymbol;
    }    
    public int getParametersAmount(){
        return this.parametersAmount;
    }
    public int getNA(){
        return this.NA;
    }
    public String getFullProcId(){
        return this.fullProcId;
    }

    //SETTERS
    public void setFirstFormalParameterType(String firstFormalParameterType) {
        this.firstFormalParameterType = firstFormalParameterType;
        this.parametersAmount++;
    }
    public void setSecondFormalParameterType(String secondFormalParameterType) {
        this.secondFormalParameterType = secondFormalParameterType;
        this.parametersAmount++;
    }
    public void setThirdFormalParameterType(String thirdFormalParameterType) {
        this.thirdFormalParameterType = thirdFormalParameterType;
        this.parametersAmount++;
    }
    public void setNA(int NA){
        this.NA = NA;
    }
    public void setShadowing(boolean shadowing){
        this.shadowingActivated = shadowing;
    }  
    public void setFirstParameterSymbol(Symbol s) {
    	this.firstParameterSymbol = s;
    }
    public void setSecondParameterSymbol(Symbol s) {
    	this.secondParameterSymbol = s;
    }
    public void setThirdParameterSymbol(Symbol s) {
    	this.thirdParameterSymbol = s;
    }

    public int addNA(){
        return ++this.NA;
    }
    public int subtractNA(){
        return --this.NA;
    }
    public boolean isShadowingActivated() {
        return shadowingActivated;
    }
    
    @Override
    public String toString() {
    	String shadowing = " Shadowing: " + String.valueOf(this.shadowingActivated);        
        String NA = " | NA: " + this.NA;
        
        String parameters = " | Parametros: ";
        if(this.parametersAmount == 0)
        	parameters = " | Sin parametros";
        if(this.parametersAmount >= 1)
        	parameters += this.firstFormalParameterType;
        if(this.parametersAmount >= 2)
        	parameters += ", " + this.secondFormalParameterType;
        if(this.parametersAmount >= 3)
        	parameters += ", " + this.thirdFormalParameterType;
        
        
        return shadowing + NA + parameters;
    }

}
