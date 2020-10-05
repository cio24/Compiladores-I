package lexicalAnalyzerPackage;

import java.util.HashMap;
import java.util.Set;

public class SymbolsTable {

	HashMap<String, Symbol> symbolsTable;

	// Takes in a string returns a symbol
	public SymbolsTable() {
		this.symbolsTable = new HashMap<>();
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
		this.symbolsTable.remove(name);
	}
	
	public Set<String> getAll() {
		return this.symbolsTable.keySet();
	}
	
	public void print(){ 
		System.out.println("\n\n ****** TABLA DE SIMBOLOS ******* ");
		for( HashMap.Entry<String, Symbol> entry : this.symbolsTable.entrySet() ){
		    System.out.println( entry.getKey() + " => " + entry.getValue() );
		}
	}
}


