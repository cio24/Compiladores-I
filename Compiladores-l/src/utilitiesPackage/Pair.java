package utilitiesPackage;

public class Pair {
	Object firstElement, secondElement;
	public Pair(Object firstElement, Object secondElement) {
		this.firstElement = firstElement;
		this.secondElement =secondElement;
	}
	public Object firstElement() {
		return firstElement;
	}
	public Object secondElement() {
		return secondElement;
	}

	public void setFirstElement(Object firstElement){
		this.firstElement = firstElement;
	}

	public void setSecondElement(Object secondElement){
		this.secondElement = secondElement;
	}
}
