package utilitiesPackage;

public class ErrorReceiver {
	public static boolean hasErrors = false;
	
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARNING";
	
	public static final String LEXICO = "Lexico";
	public static final String SINTACTICO = "Sintactico";
	public static final String SEMANTICO = "Semantico";
	
	public static void displayError(String mode,String line,String type,String additionalInfo) {
		System.out.println("----> [Línea " + line  + "] "+ mode +" "+type +": "+additionalInfo);
		if (mode==ERROR) 
			hasErrors=true;		
	}
	
	public void displayError(String mode,String line,String type) {
		ErrorReceiver.displayError(mode, line, type,"");
	}
}
