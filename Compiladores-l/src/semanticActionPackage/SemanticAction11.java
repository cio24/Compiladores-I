package semanticActionPackage;

public class SemanticAction11 extends SemanticAction{

	@Override
	public void execute() {
		lan.setToken((int)lan.getLastCharacterRead());
	}
	
}
