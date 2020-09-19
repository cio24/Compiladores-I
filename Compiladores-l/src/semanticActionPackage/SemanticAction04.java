package semanticActionPackage;

public class SemanticAction04 extends SemanticAction {

	@Override
	public void execute() {
		lan.returnLastCharacterRead();
		lan.setToken((int)'/');
	}

}
