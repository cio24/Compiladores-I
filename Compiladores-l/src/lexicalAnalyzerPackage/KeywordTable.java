package lexicalAnalyzerPackage;

import java.util.Vector;
import usefulClassesPackage.Constants;;

public class KeywordTable {
	private Vector<String> kws;
	private Vector<Integer> kwstoken;
	
	public KeywordTable() {
		kws=new Vector<String>();
		kwstoken= new Vector<Integer>();
		
		kws.add("IF");       kwstoken.add(Constants.IF);
		kws.add("THEN");     kwstoken.add(Constants.THEN);
		kws.add("ELSE");     kwstoken.add(Constants.ELSE);
		kws.add("END_IF");   kwstoken.add(Constants.END_IF);
		kws.add("OUT");      kwstoken.add(Constants.OUT);
		kws.add("FUNC");     kwstoken.add(Constants.FUNC);
		kws.add("RETURN");   kwstoken.add(Constants.RETURN);
		kws.add("ULONGINT"); kwstoken.add(Constants.ULONGINT);
		kws.add("DOUBLE");   kwstoken.add(Constants.DOUBLE);
		kws.add("LOOP");     kwstoken.add(Constants.LOOP);
		kws.add("UNTIL");    kwstoken.add(Constants.UNTIL);
	}
	
	public int getKeywordToken(String kw) {
		int token=-1;
		int index=kws.indexOf(kw);
		if (index!=-1)
			token=kwstoken.elementAt(index);
		return token;
	}
}
