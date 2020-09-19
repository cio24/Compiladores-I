package semanticActionPackage;

public class Error2 extends SemanticAction{

	public void execute() {
		System.out.println("Error Lexico en linea "+lan.getCurrentLine()+": Caracter inesperado");
	}

}
