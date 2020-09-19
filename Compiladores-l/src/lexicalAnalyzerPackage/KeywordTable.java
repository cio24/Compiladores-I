package lexicalAnalyzerPackage;

import java.util.Vector;
import usefulClassesPackage.Pair;

public class KeywordTable {
	private Vector<String> kws;
	private Vector<Integer> kwstoken;
	
	public KeywordTable() {
		kws=new Vector<String>();
		kwstoken= new Vector<Integer>();
		
		kws.add("IF");       kwstoken.add(901);
		kws.add("THEN");     kwstoken.add(902);
		kws.add("ELSE");     kwstoken.add(903);
		kws.add("END_IF");   kwstoken.add(904);
		kws.add("OUT");      kwstoken.add(905);
		kws.add("FUNC");     kwstoken.add(906);
		kws.add("RETURN");   kwstoken.add(907);
		kws.add("ULONGINT"); kwstoken.add(908);
		kws.add("DOUBLE");   kwstoken.add(909);
		kws.add("LOOP");     kwstoken.add(910);
		kws.add("UNTIL");    kwstoken.add(911);
	}
	
	public int getKeywordToken(String kw) {
		int token=-1;
		int index=kws.indexOf(kw);
		if (index!=-1)
			token=kwstoken.elementAt(index);
		return token;
	}
}
