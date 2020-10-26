package semanticActionPackage;
import lexicalAnalyzerPackage.LexicalAnalyzer;
import usefulClassesPackage.ErrorReceiver;

public abstract class SemanticAction {
	protected LexicalAnalyzer lexicalAnalyzer;
	
	public SemanticAction(LexicalAnalyzer lexicalAnalyzer){
		this.lexicalAnalyzer = lexicalAnalyzer;
	}
	
	public abstract void execute();
}
