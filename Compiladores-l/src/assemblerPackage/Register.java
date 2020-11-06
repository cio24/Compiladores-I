package assemblerPackage;

public class Register {

    private char name;
    private boolean H;
    private boolean L;


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

    public void setFree(){
        H = false;
        L = false;
    }

    public void setLowerHalfFree(){
        L = false;
    }

    public void setHigherHalfFree(){
        L = false;
    }

    public boolean isFree(){
        return !H && !L;
    }

    public boolean isLowerHalfFree(){
        return !L;
    }

    public boolean isHigherHalfFree(){
        return !H;
    }
}
