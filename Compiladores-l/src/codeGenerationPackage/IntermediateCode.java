package codeGenerationPackage;

import java.util.ArrayList;

public class IntermediateCode {
	
	private ArrayList<Triplet> triplets;
	
	public IntermediateCode() {
		triplets = new ArrayList<Triplet>();
	}
	
	public void addTriplet(Triplet t) {
		triplets.add(t);
	}
	
	@Override
	public String toString() {
		String str = "\n ******* TRIPLETES ********** \n";
		for(Triplet t : triplets) {
			str += t.toString() + "\n";
		}
		return str;
	}
}
