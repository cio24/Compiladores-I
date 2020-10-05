import java.io.IOException;

import lexicalAnalyzerPackage.Parser;

public class Main {
		
	public static void main(String[] args) throws IOException {
		String path = args[0];
		Parser p = new Parser(path);
		p.parse();
		p.la.symbolsTable.print();
	}
}
