package semanticActionPackage;
import lexicalAnalyzerPackage.LexicalAnalyzer;

public abstract class SemanticAction {
	protected LexicalAnalyzer lan;
	public abstract void execute();
}
