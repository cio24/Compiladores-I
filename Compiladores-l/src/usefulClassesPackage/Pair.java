package usefulClassesPackage;

public class Pair {
	Object el1,el2;
	public Pair(Object el1,Object el2) {
		this.el1=el1;
		this.el2=el2;
	}
	public Object firstElement() {
		return el1;
	}
	public Object secondElement() {
		return el2;
	}
}
