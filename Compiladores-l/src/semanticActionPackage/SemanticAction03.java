package semanticActionPackage;

public class SemanticAction03 extends SemanticAction {

	@Override
	public void execute() {
		lan.returnLastCharacterRead();
		String lexem=lan.getCurrentLexem();
		if (lexem.length()<=20) 
			////Tenemos que poder acceder a la tabla de simbolos o como hacemos desde aca?
			
	}

}
