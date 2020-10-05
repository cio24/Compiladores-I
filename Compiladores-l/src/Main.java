import java.io.IOException;

import lexicalAnalyzerPackage.Parser;

public class Main {
		
	public static void main(String[] args) throws IOException {
		String path = args[0];
		String filename = "program4.txt";
		System.out.println(path);
		Parser p = new Parser(path + filename);
		p.parse();
		p.la.getSymbolsTable().print();
	}
}
