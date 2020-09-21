import java.io.IOException;
import java.util.Scanner;

import lexicalAnalyzerPackage.LexicalAnalyzer;

public class Main {
		
		public static void main(String[] args) throws IOException {
		
			LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("C:\\Users\\Cio\\Desktop\\hola.txt");
			
			char c='s';
			while(c=='s') {
				lexicalAnalyzer.getNextToken();
				Scanner sc = new Scanner(System.in);   
				c = sc.next().charAt(0);
			}
		
	}
}
