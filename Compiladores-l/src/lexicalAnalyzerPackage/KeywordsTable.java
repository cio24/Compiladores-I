package lexicalAnalyzerPackage;

import java.util.Vector;

import utilitiesPackage.Constants;;

public class KeywordsTable {
	private Vector<String> tokensKeyWords;
	private Vector<Integer> tokensValues;
	
	public KeywordsTable() {
		tokensKeyWords = new Vector<String>();
		tokensValues = new Vector<Integer>();
		
		tokensKeyWords.add("IF");       tokensValues.add(Constants.IF);
		tokensKeyWords.add("THEN");     tokensValues.add(Constants.THEN);
		tokensKeyWords.add("ELSE");     tokensValues.add(Constants.ELSE);
		tokensKeyWords.add("END_IF");   tokensValues.add(Constants.END_IF);
		tokensKeyWords.add("OUT");      tokensValues.add(Constants.OUT);
		tokensKeyWords.add("FUNC");     tokensValues.add(Constants.FUNC);
		tokensKeyWords.add("RETURN");   tokensValues.add(Constants.RETURN);
		tokensKeyWords.add("ULONGINT"); tokensValues.add(Constants.ULONGINT);
		tokensKeyWords.add("DOUBLE");   tokensValues.add(Constants.DOUBLE);
		tokensKeyWords.add("LOOP");     tokensValues.add(Constants.LOOP);
		tokensKeyWords.add("UNTIL");    tokensValues.add(Constants.UNTIL);
		tokensKeyWords.add("TRUE");     tokensValues.add(Constants.TRUE);
		tokensKeyWords.add("FALSE");    tokensValues.add(Constants.FALSE);
		tokensKeyWords.add("NA");       tokensValues.add(Constants.NA);
		tokensKeyWords.add("SHADOWING");
		tokensValues.add(Constants.SHADOWING);
		tokensKeyWords.add("PROC");     tokensValues.add(Constants.PROC);
	}
	
	public int getTokenValue(String tokenKeyword) {
		int token=-1;
		int index= tokensKeyWords.indexOf(tokenKeyword);
		if (index!=-1)
			token= tokensValues.elementAt(index);
		return token;
	}
}
