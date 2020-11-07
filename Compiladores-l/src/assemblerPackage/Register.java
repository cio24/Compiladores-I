package assemblerPackage;

public class Register {

    private char name;
    private boolean HFree;
    private boolean LFree;


    public Register(char name){
        this.name = name;
    }

    public String getEntire(){
        return "E" + name + "X";
    }

    public String getHalf(){
        return name + "X";
    }

    public String getHigherHalf(){
        return name + "H";
    }

    public String getLowerHalf(){
        return name + "L";
    }

    public void setFree(boolean isFree){
        HFree = isFree;
        LFree = isFree;
    }

    public void setLowerHalfFree(boolean isFree){
        LFree = isFree;
    }

    public void setHigherHalfFree(boolean isFree){
        HFree = isFree;
    }

    public boolean isFree(){
        return !HFree && !LFree;
    }

    public boolean isLowerHalfFree(){
        return !LFree;
    }

    public boolean isHigherHalfFree(){
        return !HFree;
    }
}
