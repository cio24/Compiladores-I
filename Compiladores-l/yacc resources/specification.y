%{
package lexicalAnalyzerPackage;

import codeGenerationPackage.*;
import usefulClassesPackage.Constants;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Math;
import java.math.BigDecimal;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import usefulClassesPackage.ErrorReceiver;
%}


%token IF THEN ELSE END_IF OUT FUNC RETURN 
ULONGINT DOUBLE LOOP UNTIL ID CONSTANT CSTRING LESSEQUAL GREATEQUAL EQUAL NEQUAL
PROC NA SHADOWING TRUE FALSE UP DOWN

%start program
%%

program :  /* EMPTY */		{ErrorReceiver.displayError(ErrorReceiver.WARNING,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"Programa vacio");}
		|  sentences  		{showMessage( "[Linea " + la.getCurrentLine() + "] Programa completo.");}
		|  error			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"No se encontraron sentencias validas");}
	;
		

sentences  :  sentence ';' 				//{showMessage( "[Linea " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentences sentence ';' 	//{showMessage( "[Linea " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentence error			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"';' ausente al final de la sentencia.");} /*testeado*/
		   |  error	';'					{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");} /*testeado*/
;


sentence  :  declaration	
		  |  executable		
;

declaration  : type  variable_list	{
										showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de variable/s.");
										Symbol symbol;
										for (String v : variableDeclarationIdentifiers){
											symbol = la.getSymbolsTable().getSymbol(v+scope);
											if(symbol != null)	
												ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO," doble declaración de la variable \"" + symbol.getLexeme() + "\".");
											else{
												symbol = la.getSymbolsTable().getSymbol(v);
												symbol.setType($1.sval);
												la.getSymbolsTable().setScope(v,scope);					
											}
										}
										//Resetear la lista de identificadores siendo identificados.
										variableDeclarationIdentifiers.clear();
									} 
			 | procedure			
			 | variable_list 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"no hay tipo para el identificador\"" + $1.sval + "\".");}  /*testeado*/
			 | type 				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un identificador y no se encontro");}
;

variable_list  :  ID  {
  				        //Resetear la lista de identificadores siendo identificados
  				        variableDeclarationIdentifiers.clear();
	 			        //añadir a la lista de identificadores actualmente siendo identificados el identificador
	 			        variableDeclarationIdentifiers.add($1.sval);
	 			      }
			   |  ID  ','  variable_list 
			          {
	 			        //añadir a la lista de identificadores actualmente siendo identificados el identificador identificado
	 			        variableDeclarationIdentifiers.add($1.sval);
	 			      }
;

type  :  ULONGINT  { $$.sval=Symbol._ULONGINT; }
	  |  DOUBLE	   { $$.sval=Symbol._DOUBLE; }
;

true_false : TRUE
           | FALSE
;

/*
procedure  :  PROC  ID  '('  parameter_list  ')'  na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}
		   |  PROC  ID  '('   ')'                 na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}	
		   |  PROC  ID  '('  parameter_list  ')'  na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   |  PROC  ID  '('    ')'                na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   |  PROC      '('  parameter_list  ')'  na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el identificador del procedimiento");}		
		   |  PROC      '('   ')'                 na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el identificador del procedimiento");}		
	       |  PROC  ID  '(' error  ')'            na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se definio mal la lista de parametros");}	
;
*/
procedure  :  procedure_header  '('  parameter_list  ')'  na_shad_definition proc_body {
							showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");
							scope = la.getSymbolsTable().removeScope(scope);
 							}
		   |  procedure_header  '('   ')'                 na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");
		   										scope = la.getSymbolsTable().removeScope(scope);
		   										}
		   |  procedure_header  '('  parameter_list  ')'  na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   |  procedure_header  '('    ')'                na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}		
	       |  procedure_header  '(' error  ')'            na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se definio mal la lista de parametros");}	
;

procedure_header  :  PROC  ID  {
                               	//setear en el atabla de simbolos el tipo del identificador en procedure
                               	scope += ":"+$2.sval;
                               }
             	|  PROC 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el identificador del procedimiento");}
;

na_shad_definition : NA  '='  CONSTANT  SHADOWING  '='  true_false 
				   | '='  CONSTANT  SHADOWING  '='  true_false {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra NA");}
				   | NA CONSTANT  SHADOWING  '='  true_false {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del NA");}
				   | NA '=' SHADOWING  '='  true_false {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el valor de NA");}
				   | NA  '='  CONSTANT '='  true_false {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra SHADOWING");}
				   | NA  '='  CONSTANT  SHADOWING true_false {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del SHADOWING");}
;


proc_body : '{' sentences  '}' 
		  | '{' error  '}' {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"cuerpo del procedimiento mal definido");}
		  | '{' '}'
;

parameter_list  :  parameter  														{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
				|  parameter  ','  parameter  										{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
				|  parameter  ','  parameter  ','  parameter  						{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
				|  parameter  ','  parameter  ','  parameter  ',' parameter_list	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir un maximo de 3 parametros");}
;

parameter  :  type  ID
		   |  type 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta identificador en declaracion de parametro");}
		   |  ID 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el tipo en declaracion de parametro");}
;

id_list  :  ID									{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID							{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID  ','  ID	        	{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID  ','  ID  ',' id_list 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir una maximo de 3 parametros");}
;

procedure_call :  ID  '('  id_list  ')' {showMessage("[Linea " + la.getCurrentLine() + "] Llamada a procedimiento.");
      									Triplet t = createTriplet("FC",new Operand(Operand.ST_POINTER,$1.sval),new Operand(Operand.TO_BE_DEFINED,"-1"));}
		       |  ID  '('  ')' 			{showMessage("[Linea " + la.getCurrentLine() + "] Llamada a procedimiento.");
      									Triplet t = createTriplet("FC",new Operand(Operand.ST_POINTER,$1.sval),new Operand(Operand.TO_BE_DEFINED,"-1"));}
;

executable  :  ID  '='  expression		{showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");
  									  	boolean correctlyDeclared = linkToDeclaration($1.sval);
  									  	showMessage("El escope en la declaracion es: " + scope);
  									  	String name = $1.sval + scope;
  									  	Triplet t;
										if(!correctlyDeclared){
											ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se utiliza una variable antes de declararla");
      											t = createEmptyTriplet();
      										} else {
      											//el nombre que se muestra en la tabla de símbolos es el correspondiente con el scope donde
      											//esta realmente definida la variable, no con el scope en donde se encontró.
      											name = la.getSymbolsTable().findSTReference(name);
      											t = createTriplet("=",new Operand(Operand.ST_POINTER,name),$3.obj);
										}
										$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
										}
			|  ID  '='  error			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una expresion del lado derecho");}
			|  error '='  expression	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una identificador del lado izquierdo");}
			|  ID  EQUAL  expression 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, ¿quisiste decir '=' ?");}
			|  if_clause  				{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
			|  loop_clause	    		{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
			|  procedure_call			{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
			|  out_clause				{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'	{$$.obj = new Operator("<");}
            |  '>'
 	    	|  '<'  '<'					{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token < duplicado");}
            |  '>'  '>'			        {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token > duplicado");}
            |  LESSEQUAL  '='           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '<='?");}
            |  GREATEQUAL  '='          {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '>='?");}
            |  NEQUAL  '='              {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
            |  '<'  '>'                 {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
;

condition  :  expression  comparator  expression 	{
													showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");
													Triplet t = createTriplet((Operator)$2.obj, $1.obj,$3.obj);
													$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
													}
		   |  expression '=' expression 			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparacion invalida. ¿Quisiste decir '=='?");}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence  ';'
			   | '{' '}'
;			   


if_clause  :  IF  if_condition   then_body  ELSE  else_body  END_IF 	{ updateFirstOperandFromStack(1); }
 		   |  IF  if_condition   then_body END_IF                   	{ updateFirstOperandFromStack(1); }     
 		   |  IF  if_condition   then_body  ELSE  else_body error 		{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
 		   |  IF  if_condition   then_body  error 						{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
 		   |  IF  if_condition   error  ELSE  else_body END_IF 			{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
		   |  IF  if_condition   then_body  ELSE  error END_IF 			{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula ELSE_IF"); }
 		   |  IF  if_condition   error  END_IF 							{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
 
;

if_condition  :   '('  condition  ')'  {Triplet t = createTriplet("BF",$2.obj,new Operand(Operand.TO_BE_DEFINED,"-1"));
      									ic.pushToStack(t.getNumId());
      									$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
			 |    '('  error  ')'		{
			 							Triplet t = createEmptyTriplet();
										ic.pushToStack(t.getNumId());
      									$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
      									ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"luego de la palabra reservada IF se espera una condicion entre parentesis");}
		     |    condition             {
			 							Triplet t = createEmptyTriplet();
										ic.pushToStack(t.getNumId());
      									$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
      									ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'."); }
	    	 |    condition  ')'        {	    	 
			 							Triplet t = createEmptyTriplet();
										ic.pushToStack(t.getNumId());
      									$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
      									ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'."); } 
;

then_body  :  sentence_block  	{ 
								Triplet t = createBITriplet(new Operand(Operand.TO_BE_DEFINED,"-1"));
							    ic.pushToStack(t.getNumId());			
							    }
;

else_body  :  sentence_block
;

loop_clause  :  loop_begin  sentence_block  UNTIL loop_condition
			 |  loop_begin  sentence_block  error							{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la clausula UNTIL en la sentencia LOOP");}
			 |  loop_begin  error  UNTIL  '('  condition  ')'				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir un bloque de sentencias");}
;

loop_condition : '('  condition  ')'{       
							   		Triplet t = createBFTriplet($2.obj);
						       		$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId()); //finally we associate an operand created with the tiplet to the loop_condition
							        }
				| '('  error  ')'	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la clausula UNTIL debe incluir una condicion entre parentesis"); }
				| condition 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir una condicion encerrada por '(' ')'"); }
;

loop_begin : LOOP 	     {ic.pushToStack(ic.currentTripletIndex() + 1); /*we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition*/}
;


/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'	{ 
										Operand op = new Operand(Operand.TO_BE_DEFINED,$3.sval);
									   	Operator opt = new Operator("OUT");
									   	Triplet t = new Triplet(opt,op);
									   	ic.addTriplet(t);
			 						 	}
			|  OUT  '('  error  ')' 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT solo acepta cadenas de caracteres"); }
			|  OUT  CSTRING    	 	   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'"); }
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term 		{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
      										Triplet t = createTriplet("+",$1.obj,$3.obj);
      										$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
			|  expression  '-'  term 		{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
	      									Triplet t = createTriplet("-",$1.obj,$3.obj);
	      									$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
			|  term 				 		{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
											 $$.obj = $1.obj;}
			|  expression  '+'  error 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la suma debe contener un termino valido");}
			|  expression  '-'  error 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la resta debe contener un termino valido");}
			|  error  '+'  term 			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la suma debe contener una expresion valida");}
			|  error  '-'  term 			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la resta debe contener una expresion valida");}
            |  expression  '+'  '+'  term 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '+' sobrante");}
;

term  :  term  '*'  factor 		{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
      							Triplet t = createTriplet("*",$1.obj,$3.obj);
      							$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
      |  term  '/'  factor 		{showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      							Triplet t = createTriplet("/",$1.obj, $3.obj);
      							$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
	  |  factor            		{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 	$$.obj = $1.obj;}
	  |  term '*' error    		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
  	  |  term '/' error    		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la division debe llevar una constante o un identificador");}
	  |  error '*' factor   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
  	  |  error '/' factor   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la division debe llevar un termino o un factor");}
	  |  term  '*'  '*'  factor {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '*' sobrante");}
      |  term  '/'  '/'  factor {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '/' sobrante");}
;

factor  :  ID           {   boolean correctlyDeclared = linkToDeclaration($1.sval);	
							if(!correctlyDeclared){
							    ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,"se utiliza una variable antes de declararla");
								$$.obj = new Operand(Operand.ST_POINTER);	
							} else {
								$$.obj = new Operand(Operand.ST_POINTER, $1.sval+scope);										
							}				
						}
	    |  CONSTANT     {  $$.obj = new Operand(Operand.ST_POINTER,$1.sval); }
	    |  '-' CONSTANT {
						 // Manejo la entrada positiva de esta constante		    				
	    				 Symbol positivo = la.getSymbolsTable().getSymbol($2.sval);
	    				 if (positivo.getType()==Symbol._ULONGINT)
	    				 	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"una constante del tipo entero largo sin signo no puede ser negativa");
	    				 else{
		    				 if(positivo.subtractReference() == 0){ // Remove reference and if it reaches 0, remove SyboleTable entry
		    				 	la.getSymbolsTable().removeSymbol(positivo.getLexeme());
		    				 }
		    				 	    				 
		    				 // Creo nueva entrada o actualizo la existente con una referencia
		    				 Symbol negativo = la.getSymbolsTable().getSymbol("-"+$2.sval);
		    				 if (negativo != null){
		    				 	negativo.addReference();  // Ya existe la entrada
		    				 }else{
		    				 	String lexema = "-"+positivo.getLexeme();
		    				 	Symbol nuevoNegativo = new Symbol(lexema,positivo.getType());
		    				 	la.getSymbolsTable().addSymbol(lexema,nuevoNegativo);
		    				 }
		    				 $2.sval = "-"+$2.sval;
		    				 
		    				 $$.obj = new Operand(Operand.ST_POINTER,$2.sval);
	    				 }		    				 		
	    			 	}
;



/*-------> Gramatica de expresiones <-------*/
%%

public LexicalAnalyzer la;
public IntermediateCode ic;
private String scope;

public Vector<String> variableDeclarationIdentifiers; //Para completar el tipo de variables declaradas

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
	ic = new IntermediateCode();
	
	variableDeclarationIdentifiers=new Vector<String>();
	variableDeclarationIdentifiers.clear();
	
	scope = ":main";
	
}

public void parse(){
	yyparse();
}

public void yyerror(String errorMessage){
	if(errorMessage.equals("syntax error")){
		//System.out.println("[Linea " + la.getCurrentLine()+ "] " + s + ".");
	}

}

int yylex(){
	yylval = new ParserVal();
	yychar = la.yylex(yylval);
	return yychar;
}

public void showMessage(String message) {
	System.out.println(message);
}

public Triplet createTriplet(String optString,Object obj1, Object obj2){
	Operator opt = new Operator(optString);
	return createTriplet(opt,obj1,obj2);
}

public Triplet createTriplet(Operator optObj,Object obj1, Object obj2){
	Operator opt = (Operator) optObj;
	Operand op1 = (Operand) obj1;
	Operand op2 = (Operand) obj2;
	Triplet t = new Triplet(opt,op1,op2);
	ic.addTriplet(t);
	return t;
}

public Triplet createBFTriplet(Object obj1){
	int unstacked = ic.topOfStack(); //we get the id of the triplet that represent the adress of the tag that we need to jump
    ic.popFromStack(); //we remove the id triplet from the top of the stack
    Operand op1 = (Operand) obj1; //we get the triplet asociate to the condition
    Operand op2 = new Operand(Operand.TRIPLET_POINTER,String.valueOf(unstacked)); //this will contain the jump adress
    Operator opt = new Operator("BF"); //the operation of the tiplet is the branch not equal
    Triplet t = new Triplet(opt,op1,op2);
    ic.addTriplet(t); //then we save the triplet in order to retrieve it later for the generation of the code
    return t;
}

public Triplet createBITriplet(Object obj1){
	updateSecondOperandFromStack(2);
	Operand op1 = (Operand) obj1;
	Operator opt = new Operator("BI");
	Triplet t = new Triplet (opt,op1);
	ic.addTriplet(t);
	return t;
}

public void updateSecondOperandFromStack(int amount){
	int unstacked = ic.topOfStack(); //we get the id of the triplet on the top of the stack
	ic.popFromStack(); //we remove the id triplet from the top of the stack
	Triplet trip = ic.getTriplet(unstacked); //then we get the triplet so we can write in the second operand
	trip.modifySecondOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(ic.currentTripletIndex()+amount))); //the adress of the jump
}

public void updateFirstOperandFromStack(int amount){
	int unstacked = ic.topOfStack(); //we get the id of the triplet on the top of the stack
	ic.popFromStack(); //we remove the id triplet from the top of the stack
	Triplet trip = ic.getTriplet(unstacked); //then we get the triplet so we can write in the second operand
	trip.modifyFirstOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(ic.currentTripletIndex()+amount))); //the adress of the jump
}

public Triplet createEmptyTriplet(){
	Triplet t = new Triplet(new Operator("ERROR"));
	ic.addTriplet(t);
	return t;
}

//true si esta declarada en el scope actual o en uno que lo contiene
public boolean linkToDeclaration(String idName){

	SymbolsTable st = la.getSymbolsTable();
	String name = idName + scope; // Nombre entero

	//te devuelve el nombre que tiene una referencia en la tabla de simbolos, puede ser exactamente el mismo
	//si se definio en el scope actual o un nombre más corto si se definio en un scope que contiene al actual
	//no te puede dar nunca null, ya que si o si esta en la tabla de símbolos
	name = st.findSTReference(name);

	Symbol s = la.getSymbolsTable().getSymbol(idName);
	//si llega a cero, significa que la única referencia que tiene el id en la tabla de simbolos era de esta
	//sentencia ejecutable, por lo tanto no estaba declarada
	if(s.subtractReference() == 0) // Remove reference and if it reaches 0, remove SymbolTable entry
		la.getSymbolsTable().removeSymbol(idName);
	
	return name != null && name.contains(":");
}

public void showErrorMessage(String message) {
	System.out.println(message);
}




