package assemblerPackage;

import codeGenerationPackage.Symbol;
import codeGenerationPackage.SymbolsTable;
import utilitiesPackage.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class RegistersManager {
    private ArrayList<Register> registers;
    private Hashtable<String,String> savedRegisters;

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

    public Register getRegister(String registerName){
        //watch out!
        return registers.get(0);
    }

    public void saveValue(Register r) throws IOException {

        //si no hay un registro libre, creamos una variable auxiliar y la guardamos en la st
        String aux = getEntireRegister();
        if(aux == null){
            aux = "@aux" + (++AssemblerGenerator.variablesAuxCounter);
            AssemblerGenerator.st.addSymbol(aux,new Symbol(aux,Symbol._IDENTIFIER_LEXEME,
                    Symbol._ULONGINT_TYPE,Symbol._VARIABLE_USE));
        }

        //generamos el codigo para guardar los datos del registo r
        AssemblerGenerator.code.write("MOV " + aux + "," + r.getEntire());
        AssemblerGenerator.code.newLine();

        //guardamos una referencia de la guardada para poder restaurar
        savedRegisters.put(r.getEntire(),aux);
    }

    public void restore(Register r) throws IOException {
        //recuperamos el nombre de la variable o registro auxiliar que usamos para guardar el valor de r
        String aux = savedRegisters.get(r.getEntire());
        if(aux != null){
            //generamos el codigo para guardar los datos del registo r
            AssemblerGenerator.code.write("MOV " + r.getEntire() + "," + aux);
            AssemblerGenerator.code.newLine();

            //eliminamos del hash la guardada
            savedRegisters.remove(r);
        }
    }
}
