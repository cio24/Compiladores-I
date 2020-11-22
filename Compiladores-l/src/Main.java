import java.io.IOException;

import assemblerPackage.AssemblerGenerator;
import lexicalAnalyzerPackage.Parser;
import testingPackage.Test;
import utilitiesPackage.ErrorReceiver;

public class Main {
		
	public static void main(String[] args) throws IOException {
		// Extraemos el path absoluto del archivo a compilar
		String path = args[0];
		
		// Creamos el parser
		Parser p = new Parser(path);

		// Parseamos el código, si hay errores se imprimen acá
		p.parse();
		
		// Mostramos tabla de símbolos
		p.la.getSymbolsTable().print();
		
		// Mostramos los tripletes, imprimiendo el tripletManager
		System.out.println(p.tm);
		
		// Avisar si la compilación fue exitosa
		if(!ErrorReceiver.hasErrors)
			System.out.println( "Compilacion exitosa");
		else
			System.out.println( "Compilacion fallida");

		// Generamos el path para el código compilado, que es similar al actual pero cambiando la extensión
		String outAssemblerFile = path.substring(0,path.indexOf(".")) + ".asm";
		// Creo el generador de assembler
		AssemblerGenerator ag= new AssemblerGenerator(p.la.getSymbolsTable(),p.tm, outAssemblerFile);
		// Genero el código
		ag.createAssembler();
		
		//Test test=new Test();

	}
}
