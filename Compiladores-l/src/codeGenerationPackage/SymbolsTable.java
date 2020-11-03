package codeGenerationPackage;

import java.util.HashMap;
import java.util.Set;

public class SymbolsTable {

	HashMap<String,Symbol> symbolsTable;
	private String scope;

	// Takes in a string returns a symbol
	public SymbolsTable() {
		this.symbolsTable = new HashMap<>();
		this.scope = ":main";
	}

	public void addSymbol(String name, Symbol symbol) {
		if (!this.symbolsTable.containsKey(name))
			this.symbolsTable.put(name, symbol);
		else {
			this.symbolsTable.get(name).addReference();
		}
	}

	public Symbol getSymbol(String name) {
		return this.symbolsTable.get(name);
	}

	public void removeSymbol(String name) {
		Symbol s = getSymbol(name);

		if(s.subtractReference() <= 0) // Remove reference and if it reaches 0, remove SymbolTable entry
			this.symbolsTable.remove(name);
	}
	
	public void setScope(String name, String scope) {
		Symbol s = getSymbol(name);
		this.removeSymbol(name);
		String newName = name+scope;
		s.setLexeme(newName);
		this.addSymbol(newName, s);		
	}

	public String removeScope(String name) { //le va quitando la última parte del scope al nombre
		if(!name.contains(":")) //si llega a un nombre sin ":" significa que se llego al string vacio?
			return null;
		return name.substring(0,name.lastIndexOf(":"));
	}

	//te devuelve el nombre que corresponde al lugar en donde fue declarado el id, de haber sido declarado
	public String findClosestIdDeclaration(String fullId) {
		while(fullId != null && this.getSymbol(fullId) == null)//mientras no encuentre el simbolo para este nombre
			fullId = removeScope(fullId); //le saco la última parte del scope
		
		if(fullId != null && fullId.contains(":"))
			return fullId;
		return null;
	}

	public Set<String> getAll() {
		return this.symbolsTable.keySet();
	}
	
	public void print(){ 
		System.out.println("\n\n ****** TABLA DE SIMBOLOS ******* \n");
        System.out.format("%40s | %20s | %12s | %15s | %10s | %10s | %40s","Lexema","Tipo del lexema","Referencias","Uso","Tipo dato","Semantica", "Info procedimiento");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for( HashMap.Entry<String, Symbol> entry : this.symbolsTable.entrySet() ){
		    entry.getValue().printRow(entry.getKey()); 
		}
	}
}

