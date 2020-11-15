package assemblerPackage;

import codeGenerationPackage.Symbol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class RegistersManager {
    private ArrayList<Register> registers;
    private Hashtable<String,String> savedRegisters;

    public RegistersManager(){
        registers = new ArrayList<>();
        registers.add(new Register('B'));
        registers.add(new Register('C'));
        registers.add(new Register('D'));
        registers.add(new Register('A'));
    }

    public Register getEntireRegisterFree(){
        for(Register r: registers)
            if(r.isFree())
                return r;
        System.out.println("NO HAY REGISTROS LIBRES");
        return null;
    }

    public Register getHalfRegisterFree(){
        return getEntireRegisterFree();
    }

    public Register getQuarterRegisterFree(){
        for(Register r: registers)
            if(r.isLowerHalfFree() || r.isHigherHalfFree())
                return r;
        System.out.println("NO HAY CUARTOS DE REGISTROS LIBRES");
        return null;
    }

    public Register getRegister(String regName){
        for(Register r: registers)
            if(r.getEntire().equals(regName))
                return r;
        return null;
    }

    public void saveValue(Register r) throws IOException {

        Register regAux = getEntireRegisterFree();
        String aux;

        //si no hay un registro libre creamos una variable auxiliar
        if(regAux == null){
            aux = "@aux" + (++AssemblerGenerator.variablesAuxCounter);
            AssemblerGenerator.st.addSymbol(aux,new Symbol(aux,Symbol._IDENTIFIER_LEXEME,
                    Symbol._ULONGINT_TYPE,Symbol._VARIABLE_USE));
        }
        else{
            //si hay un registro libre, usamos ese y lo seteamos como ocupado
            aux = regAux.getEntire();
            regAux.setFree(false);
        }

        //generamos el codigo para guardar los datos del registo r
        AssemblerGenerator.actualCode.add("MOV " + aux + "," + r.getEntire());

        //guardamos una referencia del registro guardado para poder restaurarlo
        savedRegisters.put(r.getEntire(),aux);
    }

    public void restore(Register r) throws IOException {

        //recuperamos el nombre de la variable o registro auxiliar que usamos para guardar el valor de r
        String aux = savedRegisters.get(r.getEntire());

        if(aux != null){
            //generamos el codigo para restaurar el registro r
            AssemblerGenerator.actualCode.add("MOV " + r.getEntire() + "," + aux);

            //eliminamos del hash el valor guardado de r
            savedRegisters.remove(r.getEntire());
        }
        else
            System.out.println("NO EXISTE UN VALOR GUARDADO DEL REGISTRO " + r.getEntire());
    }

    public boolean isAReg(String regName){
        for(Register r: registers){
            if(r.getEntire().equals(regName) || r.getHalf().equals(regName)
                    || r.getHigherHalf().equals(regName) || r.getLowerHalf().equals(regName))
                return true;
        }
        return false;
    }
}
