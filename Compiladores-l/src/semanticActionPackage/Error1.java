package semanticActionPackage;

public class Error1 extends SemanticAction {

	public void execute() {
		lan.setToken(-99); 
		System.out.println("Error Lexico en linea "+lan.getCurrentLine()+": Fin de archivo inesperado");
	}

}
