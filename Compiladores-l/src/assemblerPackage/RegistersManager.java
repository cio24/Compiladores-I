package assemblerPackage;

import java.util.ArrayList;

public class RegistersManager {
    private ArrayList<Register> registers;

    public RegistersManager(){
        registers = new ArrayList<>();
        registers.add(new Register('A'));
        registers.add(new Register('B'));
        registers.add(new Register('C'));
        registers.add(new Register('D'));
    }

    public String getEntireRegister(){
        for(Register r: registers){
            if(r.isFree())
                return r.getEntire();
        }
        return "NO HAY REGISTROS ENTEROS LIBRES";
    }

    public String getHalfRegister(){
        for(Register r: registers){
            if(r.isFree())
                return r.getHalf();
        }
        return "NO HAY MEDIOS REGISTROS LIBRES";
    }

    public String getQuarterRegister(){
        for(Register r: registers){
            if(r.isLowerHalfFree())
                return r.getLowerHalf();
            if(r.isHigherHalfFree())
                return r.getHigherHalf();
        }
        return "NO HAY CUARTOS DE REGISTROS LIBRES";
    }


}
