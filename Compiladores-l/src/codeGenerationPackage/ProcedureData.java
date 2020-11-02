package codeGenerationPackage;


public class ProcedureData {
    private boolean shadowingActivated;
    private int NA;
    private String firstFormalParameterType;
    private String secondFormalParameterType;
    private String thirdFormalParameterType;
    private int parametersAmount;
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


    public int addNA(){
        return ++this.NA;
    }
    public int subtractNA(){
        return --this.NA;
    }
    public boolean isShadowingActivated() {
        return shadowingActivated;
    }

}
