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
	}

	public Symbol getSymbol(String name) {
		return this.symbolTable.get(name);
	}

	public Set<String> getAll() {
		return this.symbolTable.keySet();
	}
	
}


