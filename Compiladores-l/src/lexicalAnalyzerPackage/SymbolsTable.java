package lexicalAnalyzerPackage;

import java.util.HashMap;
import java.util.Set;

public class SymbolsTable {

	HashMap<String, Symbol> symbolTable;

	// Takes in a string returns a symbol
	public SymbolsTable() {
		this.symbolTable = new HashMap<>();
	}

	public void addSymbol(String name, Symbol symbol) {
		if (!this.symbolTable.containsKey(name))
			this.symbolTable.put(name, symbol);
		else {
			this.symbolTable.get(name).addRef();
		}
	}

	public Symbol getSymbol(String name) {
		return this.symbolTable.get(name);
	}

	public void removeSymbol(String name) {
		this.symbolTable.remove(name);
	}
	
	public Set<String> getAll() {
		return this.symbolTable.keySet();
	}
	
	public void print(){ 
		System.out.println("\n\n ****** TABLA DE SÍMBOLOS ******* ");
		for( HashMap.Entry<String, Symbol> entry : this.symbolTable.entrySet() ){
		    System.out.println( entry.getKey() + " => " + entry.getValue() );
		}
	}
}


