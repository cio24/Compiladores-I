package codeGenerationPackage;

import java.util.ArrayList;

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

    /*
    public ProcedureData(String fullProcId, boolean shadowingActivated, int NA) {
        this(fullProcId,shadowingActivated,NA);
        this.shadowingActivated = shadowingActivated;
        this.NA = NA;
        this.parametersAmount = 0;
        this.fullProcId = fullProcId;
    }

    public ProcedureData(String fullProcId, String firstFormalParameterType, boolean shadowingActivated, int NA) {
        this(fullProcId,shadowingActivated,NA);
        this.firstFormalParameterType = firstFormalParameterType;
        this.parametersAmount++;
    }

    public ProcedureData(String fullProcId, String firstFormalParameterType, String secondFormalParameterType, boolean shadowingActivated, int NA) {
        this(fullProcId,firstFormalParameterType,shadowingActivated,NA);
        this.secondFormalParameterType = secondFormalParameterType;
        this.parametersAmount++;
    }

    public ProcedureData(String fullProcId, String firstFormalParameterType, String secondFormalParameterType, String thirdFormalParameterType, boolean shadowingActivated, int NA) {
        this(fullProcId,firstFormalParameterType,secondFormalParameterType,shadowingActivated,NA);
        this.thirdFormalParameterType = thirdFormalParameterType;
        this.parametersAmount++;
    }

     */

    public String getFirstFormalParameterType() {
        return firstFormalParameterType;
    }
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
    public String getSecondFormalParameterType() {
        return secondFormalParameterType;
    }
    public String getThirdFormalParameterType() {
        return thirdFormalParameterType;
    }
    public int getParametersAmount(){
        return this.parametersAmount;
    }

    public String getFullProcId(){
        return this.fullProcId;
    }

    public void setNA(int NA){
        this.NA = NA;
    }
    public int addNA(){
        return ++this.NA;
    }
    public int subtractNA(){
        return --this.NA;
    }
    public void setShadowing(boolean shadowing){
        this.shadowingActivated = shadowing;
    }
    public boolean isShadowingActivated() {
        return shadowingActivated;
    }
    public void setShadowingActivated(boolean shadowingActivated) {
        this.shadowingActivated = shadowingActivated;
    }
}
