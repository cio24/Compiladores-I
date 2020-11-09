package assemblerPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import codeGenerationPackage.*;

public class AssemblerGenerator {
	
	//public static final String BASE_PATH = "/home/chequeado/Documentos/Facultad/Compiladores/Compiladores-I/Compiladores-l/src/testingPackage/";
	//public static final String BASE_PATH = "C:\\Users\\Thomas\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	public static final String BASE_PATH = "C:\\Users\\Cio\\git\\Compiladores-I\\Compiladores-l\\src\\testingPackage\\";
	

	public static final String FILENAME = "codigo1.txt";
	private static final String ADD = "ADD";
	private static final String SUB = "SUB";
	private static final String MUL = "MUL";
	private static final String DIV = "DIV";

	public static int variablesAuxCounter;
	private RegistersManager rm;
	
	public static BufferedWriter code;

	
	public static SymbolsTable st;
	public static TripletsManager tm;
	//String outfilepath;
	
	public AssemblerGenerator(SymbolsTable st,TripletsManager tm /*,String outfilepath*/) {
		this.st=st;
		this.tm=tm;
		variablesAuxCounter = 0;
		rm = new RegistersManager();
		//this.outfilepath = outfilepath;
	}
	
	public void createAssemblerFile() throws IOException {
		if (/*!ErrorReceiver.hasErrors*/true)
		{
				File assembler = new File(BASE_PATH+FILENAME/*outfilepath*/); ///No vale la pena hacer una clase para esto
				FileOutputStream fos = new FileOutputStream(assembler);
				code = new BufferedWriter(new OutputStreamWriter(fos));
			 
				generateAssembler();
			 
				code.close();

		}
	}
	
	public void generateAssembler() throws IOException {
		generateHeader(); // Genera la primer parte del archivo con todas las librerias y sintaxis requerida
		generateCodeSection(); // Genera la seccion donde se vuelca el codigo
		generateDataSection(); // Genera la seccion donde se vuelca toda la informaci�n de la tabla de simbolos
	}
	
	public void writeMov(String dest,String src) throws IOException {
		code.write("MOV "+dest+","+src);
		code.newLine();
	}
	
	public void generateDataSection() throws IOException {
		
	};
	
	public void generateCodeSection() throws IOException {
		for(Triplet t: tm.triplets)
			if(t.getOperator().equals("="))
				assignmentOnInt(t);
			else
				arithmeticOpOnInt(t);
	};
	
	public void generateHeader() throws IOException {

		code.write(".386");
		code.newLine();
		code.write(".model flat, stdcall");
		code.newLine();
		code.write("option casemap :none");
		code.newLine();
		code.write("include \\masm32\\include\\windows.inc");
		code.newLine();
		code.write("include \\masm32\\include\\kernel32.inc");
		code.newLine();
		code.write("include \\masm32\\include\\user32.inc");
		code.newLine();
		code.write("includelib \\masm32\\lib\\kernel32.lib");
		code.newLine();
		code.write("includelib \\masm32\\lib\\user32.lib");
		code.newLine();
		code.newLine();
		
	}

	public void mulInteger(Triplet t) throws IOException {
		
		//los souce pueden ser un string con una variable o un string con una constante
		//la variable puede ser una que guarde un resultado de un triplet
		String source1 = getSource(t.getFirstOperand());
		String source2 = getSource(t.getSecondOperand());

		//antes de esto se tiene que ver si esta libre y sino, liberarlo y guardar lo que tenia
		//en una variable auxiliar
		Register registerA = rm.getRegister("A");
		Register registerD = rm.getRegister("D");

		if(!registerA.isFree())
			rm.saveValue(registerA);

		if(!registerD.isFree())
			rm.saveValue(registerD);

		//codigo assembler xD
		code.write("MOV " + registerA.getEntire() + "," + source1);
		code.newLine();
		code.write("MUL " + registerA.getEntire() + "," + source2);
		code.newLine();

		//restauramos los registros de haberse guardado
		rm.restore(registerA);
		rm.restore(registerD);

		//guardar el resultado de la operaci�n en el triplet
		t.setResultLocation(registerA.getEntire());

	}
	
	
	public void mulInteger2(Triplet t) throws IOException {

		
		//los source pueden ser un string con una variable o un string con una constante
		//la variable puede ser una que guarde un resultado de un triplet
		String source1 = getSource(t.getFirstOperand());
		String source2 = getSource(t.getSecondOperand());

		//antes de esto se tiene que ver si esta libre y sino, liberarlo y guardar lo que tenia
		//en una variable auxiliar
		
		Register registerA = rm.getRegister("A");
		Register registerD = rm.getRegister("D");
		
		Register resultRegister; //Para guardar donde quedar� el resultado
		
		boolean saveA = true; //suponemos que hay que salvar los registros A y D
		boolean saveD = true;
		boolean source1IsA = false; //suponemos que ninguno de los dos operandos es A
		boolean source2IsA = false; 
		
		//Determinamos si A esta libre, y si ademas es algun operando (y necesitamos saber cual). Si se cumpliera alguna de estas condiciones
		//Podemos evitar salvar este registro
		
		if (registerA.isFree())
			saveA = false;
		if (source1.equals(registerA.getEntire())) {
			saveA = false;
			source1IsA = true;
		}
		if(source2.equals(registerA.getEntire())) {
			saveA = false;
			source2IsA = true;
		}
		
		//Determinamos si D esta libre, y si ademas es algun operando . Si se cumpliera alguna de estas condiciones
		//Podemos evitar salvar este registro
		
		if (registerD.isFree() || source1.equals(registerD.getEntire()) || source2.equals(registerD.getEntire()))
			saveD = false;

		if(saveA)
			rm.saveValue(registerA);

		if(saveD)
			rm.saveValue(registerD);
		
		if (source1IsA) {
			code.write("MUL " + source1 + "," + source2);
			code.newLine();
			if (saveD)  //se restaura D si es necesario, A nunca ser� necesario restaurarlo ya que era uno de los operandos
				rm.restore(registerD);
			else //esto se ejecuta solo si el registro estaba libre antes de la multiplicacion o bien era algun operando,por lo que debe estar libre
				registerD.setFree(true);
			//si source2 es un registro, se libera.
			if(rm.getRegister(source2)!=null) 
				rm.getRegister(source2).setFree(true);
			resultRegister=rm.getRegister("A");
		}
		
		else if (source2IsA) {
			code.write("MUL" + " " + source2 + "," + source1);
			code.newLine();
			if (saveD)  //se restaura D si es necesario, A nunca ser� necesario restaurarlo ya que era uno de los operandos
				rm.restore(registerD);
			else //esto se ejecuta solo si el registro estaba libre antes de la multiplicacion o bien era algun operando,por lo que debe estar libre
				registerD.setFree(true);
			//si source1 es un registro, se libera.
			if(rm.getRegister(source1)!=null) 
				rm.getRegister(source1).setFree(true);
			resultRegister=rm.getRegister("A");
		}
		
		else { //este codigo se ejecuta si ninguno de los dos operandos son el registro A, el tratamiento es el mismo tanto si
			//los operandos estan en registros o si estan en variables, ya que lo que necesitamos hacer en ambos casos es 
			//pasar el source1 al registro A (recordando que este ya fue salvado si necesitaba serlo)
			code.write( "MOV " + registerA.getEntire() + ", " + source1);
			code.newLine();
			code.write( "MUL" + " " + registerA.getEntire() + ", " + source2);
			code.newLine();
			
			//si source2 es un registro, se libera.
			if(rm.getRegister(source2)!=null) 
				rm.getRegister(source2).setFree(true);
			
			if (saveA) //significa que en A habia algo, por lo que hay que restaurarlo guardando en otro lugar nuestra multiplicacion actual
				       // como source2 podria no ser un registro, no sabemos si se puede usar para dicho fin.
			{
				Register reg = rm.getEntireRegisterFree();
			}
			
			///falta terminar 
		}

		

		//guardar el resultado de la operaci�n en el triplet
		t.setResultLocation(resultRegister.getEntire());

	}

	public String getSource(Operand op){
		String source = op.getRef();

		if(op.getOperandType().equals(Operand.TRIPLET_POINTER))
			source = getTripletAssociated(op).getResultLocation();
		else if(source.contains(":"))
			source =  "_" + source;

		return source;
	}

	public Triplet getTripletAssociated(Operand op){
		return tm.getTriplet(Integer.valueOf(op.getRef()));
	}

	public void arithmeticOpOnInt(Triplet t) throws IOException {

		//los operandos pueden ser un string con una variable, un string con una constante
		//o un resultado de un triplet que puede ser un registro
		String op1 = getSource(t.getFirstOperand());
		String op2 = getSource(t.getSecondOperand());

		String opt = getAssemblerOpt(t.getOperator());

		//si ning�n operando es un registro o solo el segundo es un registro y la operaci�n NO es conmutativa
		if(!rm.isAReg(op1) && !rm.isAReg(op2) || !rm.isAReg(op1) && rm.isAReg(op2) && !isCommutative(opt)){

			//si la operaci�n es una multiplicaci�n debemos controlar el tema de los registros
			if(opt.equals(MUL)){
				return;
			}

			//obtenemos un registro para pasarle el valor del operando uno
			Register reg = rm.getEntireRegisterFree();

			//generamos el assembler
			code.write( "MOV " + reg.getEntire() + ", " + op1);
			code.newLine();
			code.write( opt + " " + reg.getEntire() + ", " + op2);
			code.newLine();

			//si el operador dos era un registro lo liberamos
			if(rm.isAReg(op2))
				rm.getRegister(op2).setFree(true);

			//finalmente guardamos el resultado en el terceto
			t.setResultLocation(reg.getEntire());
		}
		else{
			//si entra ac� es xq un operando es un registro y el otro no
			// si el registro es el segundo operando, la operacion es conmutativa

			//si la operaci�n es conmutativa intercambiamos los operandos
			if(!rm.isAReg(op1)){
				String aux = op2;
				op2 = op1;
				op1 = aux;
			}

			//si la operaci�n es una multiplicaci�n debemos controlar el tema de los registros
			if(opt.equals(MUL)){
				return;
				/*
				Register regA = rm.getRegister("A");
				Register regD = rm.getRegister("D");

				//si el primer operando no es el A, debemos hacer que sea el A si o si!
				if(!op1.equals(regA.getEntire())) {

					//si el registro A esta ocupado lo liberamos y guardamos su valor en una variable auxiliar
					if (!regA.isFree())
						rm.saveValue(regA);



					code.write("MOV " + regA.getEntire() + "," + op1);

				 */
				}

			//finalmente generalmos el assembler la operaci�n
			code.write( opt + " " + op1 + ", " + op2);
			code.newLine();

			//finalmente guardamos el resultado en el terceto
			t.setResultLocation(op1);
		}

	}
	
	
	public void arithmeticOpOnInt2(Triplet t) throws IOException {

		//los operandos pueden ser un string con una variable, un string con una constante
		//o un resultado de un triplet que puede ser un registro
		String op1 = getSource(t.getFirstOperand());
		String op2 = getSource(t.getSecondOperand());

		String opt = getAssemblerOpt(t.getOperator());

		//Si el primer operando es un registro, el segundo podria ser un registro, una variable o una constante
		if(rm.isAReg(op1)){
			if (opt.equals("+") || opt.equals("-"))
			{
				//generamos el assembler
				code.write( opt + " " + op1 + ", " + op2);
				code.newLine();
					
					if (rm.isAReg(op2)) //Si el segundo operando es un registro se puede liberar
						rm.getRegister(op2).setFree(true);
					
				t.setResultLocation(op1);
				
				return;
			}
			if (opt.equals("*"))
			{
				
			}
			if (opt.equals("/"))
			{
				
			}
		}
		
		if (!rm.isAReg(op1) && rm.isAReg(op2) && isCommutative(opt))
		{
			if (opt.equals("+"))
			{
				//generamos el assembler
				code.write( opt + " " + op2 + ", " + op1);
				code.newLine();
					
				t.setResultLocation(op2);
				
				return;
			}
			if (opt.equals("*"))
			{
				
			}
		}
		if (!rm.isAReg(op1) && rm.isAReg(op2) && !isCommutative(opt))
		{
			if (opt.equals("-"))
			{
				Register reg = rm.getEntireRegisterFree();

				//generamos el assembler
				code.write( "MOV " + reg.getEntire() + ", " + op1);
				code.newLine();
				code.write( opt + " " + reg.getEntire() + ", " + op2);
				code.newLine();

				rm.getRegister(op2).setFree(true);

				//finalmente guardamos el resultado en el terceto
				t.setResultLocation(reg.getEntire());
				
				return;
			}
			if (opt.equals("/"))
			{
				
			}
		}
		if (!rm.isAReg(op1) && !rm.isAReg(op2))
		{
			if (opt.equals("+") || opt.equals("-"))
			{
				Register reg = rm.getEntireRegisterFree();

				//generamos el assembler
				code.write( "MOV " + reg.getEntire() + ", " + op1);
				code.newLine();
				code.write( opt + " " + reg.getEntire() + ", " + op2);
				code.newLine();

				//finalmente guardamos el resultado en el terceto
				t.setResultLocation(reg.getEntire());
				
				return;
			}
			if (opt.equals("/"))
			{
				
			}
			if (opt.equals("*"))
			{
				
			}
		}
	}

	public void assignmentOnInt(Triplet t) throws IOException {
		String op1 = getSource(t.getFirstOperand());
		String op2 = getSource(t.getSecondOperand());

		//si el segundo operando es una variable, lo tenemos que pasar a un registro
		//xq no podemos hacer un MOV entre dos variables
		if(op2.contains(":") || op2.contains("@")){
			Register r = rm.getEntireRegisterFree();
			AssemblerGenerator.code.write("MOV " + r.getEntire() + ", " + op2);
			code.newLine();
			op2 = r.getEntire();
		}

		//finalmente hacemos la asignaci�n
		AssemblerGenerator.code.write("MOV " + op1 + ", " + op2);
		code.newLine();

		//guardamos el nombre de la variable donde se guardo el resultado
		t.setResultLocation(op1);
	}

	private boolean isCommutative(String opt) {
		return opt.equals("+") || opt.equals("*");
	}

	private String getAssemblerOpt(String opt) {
		switch(opt){
			case "+": return ADD;
			case "-": return SUB;
			case "*": return MUL;
			case "/": return DIV;
		}
		return "unknownOperator";
	}

}
