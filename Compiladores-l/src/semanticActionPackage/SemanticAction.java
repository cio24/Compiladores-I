package semanticActionPackage;
import lexicalAnalyzerPackage.LexicalAnalyzer;

public abstract class SemanticAction {
	protected LexicalAnalyzer lexicalAnalyzer;
	
	public SemanticAction(LexicalAnalyzer lexicalAnalyzer){
		this.lexicalAnalyzer = lexicalAnalyzer;
	}
	
	public abstract void execute();
}
