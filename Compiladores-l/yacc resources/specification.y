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
%}


%token IF THEN ELSE END_IF OUT FUNC RETURN 
ULONGINT DOUBLE LOOP UNTIL ID CONSTANT CSTRING LESSEQUAL GREATEQUAL EQUAL NEQUAL
PROC NA SHADOWING TRUE FALSE UP DOWN

%start program
%%

program :  /* EMPTY */		{showMessage( "[Linea " + la.getCurrentLine() + "] WARNING sintactico: Programa vacio!");}
		|  sentences  		{showMessage( "[Linea " + la.getCurrentLine() + "] Programa completo.");}
		|  error			{showMessage( "[Linea " + la.getCurrentLine() + "] ERROR sintactico: no se encontraron sentencias validas.");}
	;
		

sentences  :  sentence ';' 				//{showMessage( "[Linea " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentences sentence ';' 	//{showMessage( "[Linea " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentence error			{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' ausente al final de la sentencia.");} /*testeado*/
		   |  error	';'					{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");} /*testeado*/
;


sentence  :  declaration	
		  |  executable		
;

declaration  : type  variable_list	{showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de variable/s.");}
			 | procedure			{showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de procedimiento.");}
			 | variable_list 		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + $1.sval + "\".");}  /*testeado*/
			 | type 				{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
;

variable_list  :  ID  
			   |  ID  ','  variable_list
;

type  :  ULONGINT	
	  |  DOUBLE
;

true_false : TRUE
           | FALSE
;

procedure  :  PROC  ID  '('  parameter_list  ')'  na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}
		   |  PROC  ID  '('   ')'                 na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}	
		   |  PROC  ID  '('  parameter_list  ')'  na_shad_definition           {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}	
		   |  PROC  ID  '('    ')'                na_shad_definition           {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}	
		   |  PROC      '('  parameter_list  ')'  na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}		
		   |  PROC      '('   ')'                 na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}		
	       |  PROC  ID  '(' error  ')'            na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se definio mal la lista de parametros.");}	
;

na_shad_definition : NA  '='  CONSTANT  SHADOWING  '='  true_false 
				   | '='  CONSTANT  SHADOWING  '='  true_false {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra NA");}
				   | NA CONSTANT  SHADOWING  '='  true_false {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del NA.");}
				   | NA '=' SHADOWING  '='  true_false {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el valor del NA.");}
				   | NA  '='  CONSTANT '='  true_false {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra SHADOWING.");}
				   | NA  '='  CONSTANT  SHADOWING true_false {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del SHADOWING.");}
;


proc_body : '{' sentences  '}' 
		  | '{' error  '}' {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: cuerpo del procedimiento mal definido.");}
		  | '{' '}'
;

parameter_list  :  parameter  {showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
				|  parameter  ','  parameter  {showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
				|  parameter  ','  parameter  ','  parameter  {showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
				|  parameter  ','  parameter  ','  parameter  ',' parameter_list	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
;

parameter  :  type  ID
		   |  type 		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
		   |  ID 		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
;

id_list  :  ID								{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID						{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID  ','  ID	        {showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID  ','  ID  ',' id_list {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
;

procedure_call :  ID  '('  id_list  ')'
		       |  ID  '('  ')' 			
;

executable  :  ID  '='  expression		{showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");
										Operand op1 = new Operand(Operand.ST_POINTER,$1.sval); 
      									Operand op2 = (Operand) $3.obj; 
      									Operator opt = new Operator("=");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									$$.obj = new Operand(Operand.TRIPLET,t.getId());}
			|  ID  '='  error			{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
			|  error '='  expression	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
			|  ID  EQUAL  expression 	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
			|  if_clause  				{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
			|  loop_clause	    		{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
			|  procedure_call			{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
			|  out_clause				{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'
            |  '>'
 			|  '<'  '<'					{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
            |  '>'  '>'			        {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
            |  LESSEQUAL  '='           {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
            |  GREATEQUAL  '='          {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
            |  NEQUAL  '='              {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
            |  '<'  '>'                 {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
;

condition  :  expression  comparator  expression 	{showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");}
		   |  expression '=' expression 			{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence  ';'
			   | '{' '}'
;			   


if_clause  :  IF  if_condition  cpo_then  ELSE  cpo_else  END_IF  {
                                                                  int unstacked = ic.topOfStack();
																  ic.popFromStack();
							 	                                  Triplet trip = ic.getTriplet(unstacked);
								                                  trip.modifyFirstOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+1)));
                                                                  }
 		   
 		   |  IF  if_condition  cpo_then END_IF                   {
                                                                  int unstacked = ic.topOfStack();
																  ic.popFromStack();
							 	                                  Triplet trip = ic.getTriplet(unstacked);
								                                  trip.modifyFirstOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+1)));
 		                                                          }     
 		   /*
 		   |  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block error 	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
 		   |  IF  '('  condition  ')'  sentence_block  error 						{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
 		   |  IF  '('  error  ')'  sentence_block  ELSE  sentence_block END_IF 		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
 		   |  IF  '('  error  ')'  sentence_block  END_IF 							{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
		   |  IF  '('  condition  ')'  error  ELSE  sentence_block END_IF 			{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
		   |  IF  '('  condition  ')'  sentence_block  ELSE  error END_IF 			{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula ELSE_IF.");}
 		   |  IF  '('  condition  ')'  error  END_IF 								{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
           |  IF  condition  sentence_block  ELSE  sentence_block END_IF			{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
 		   |  IF  condition  sentence_block  END_IF                                {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
 */
;

if_condition  :   '('  condition  ')'  {
									Operand op1 = (Operand) $2.obj;
      									Operand op2 = new Operand(Operand.TOBEDEFINED,"-1");
      									Operator opt = new Operator("BF");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									ic.pushToStack(t.getNumId());
      									$$.obj = new Operand(Operand.TRIPLET,t.getId());
										}
;

cpo_then  :  sentence_block  {
								int unstacked = ic.topOfStack(); //we get the id of the triplet on the top of the stack
								ic.popFromStack(); //we remove the id triplet from the top of the stack
								Triplet trip = ic.getTriplet(unstacked); //then we get the triplet so we can write in the second operand
								trip.modifySecondOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+2))); //the adress of the jump
								Operand op1 = new Operand(Operand.TOBEDEFINED,"-1");
								Operand op2 = null;
								Operator opt = new Operator("BI");
								Triplet t = new Triplet (opt,op1,op2);
								ic.addTriplet(t);
								ic.pushToStack(t.getNumId());
								}
;

cpo_else  :  sentence_block
;

loop_clause  :  loop_begin  sentence_block  UNTIL '('  condition  ')'  {       int unstacked = ic.topOfStack(); //we get the id of the triplet that represent the adress of the tag that we need to jump
                                                                          ic.popFromStack(); //we remove the id triplet from the top of the stack
                                                                          Operand op1 = (Operand) $2.obj; //we get the triplet asociate to the condition
                                                                          Operand op2 = new Operand(Operand.TRIPLET,ic.getTriplet(unstacked).getId()); //this will contain the jump adress
                                                                          Operator opt = new Operator("BF"); //the operation of the tiplet is the branch not equal
                                                                          Triplet t = new Triplet(opt,op1,op2);
                                                                          ic.addTriplet(t); //then we save the triplet in order to retrieve it later for the generation of the code
                                                                          $$.obj = new Operand(Operand.TRIPLET,t.getId()); //finally we associate an operand created with the tiplet to the loop_condition
                                                                          }
			 |  LOOP  sentence_block  error							{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
			 |  LOOP  sentence_block  UNTIL  '('  error  ')'		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la clausula UNTIL debe incluir una condicion entre parentesis");}
			 |  LOOP  error  UNTIL  '('  condition  ')'				{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
             |  LOOP  sentence_block  UNTIL  condition		        {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
;

loop_begin : LOOP 	     {
                        				       Operator opt = new Operator("TAG TO JUMP");
                        				       Triplet t = new Triplet(opt); //this triplet is used as a tag to jump
                        				       ic.pushToStack(t.getNumId()); //we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition
                        				       ic.addTriplet(t); //then we save the triplet in order to retrieve it later for the generation of the code
                        				       $$.obj = new Operand(Operand.TRIPLET,t.getId()); //finally we associate an operand created with the tiplet to the loop_begin
                        				     }


/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'        	 { Operand op = new Operand(Operand.TOBEDEFINED,$3.sval);
						   Operator opt = new Operator("OUT");
						   Triplet t = new Triplet(opt,op);
						   ic.addTriplet(t);

 						 }
			|  OUT  '('  error  ')'  {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT solo acepta cadenas de caracteres.");}
			|  OUT  CSTRING    	 {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term 	{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
										Operand op1 = (Operand) $1.obj; 
      									Operand op2 = (Operand) $3.obj; 
      									Operator opt = new Operator("+");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									$$.obj = new Operand(Operand.TRIPLET,t.getId());}
			|  expression  '-'  term 	{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
										Operand op1 = (Operand) $1.obj; 
      									Operand op2 = (Operand) $3.obj; 
      									Operator opt = new Operator("-");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									$$.obj = new Operand(Operand.TRIPLET,t.getId());}
			|  term 				 	{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
										 $$.obj = $1.obj;}
			|  expression  '+'  error 	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
			|  expression  '-'  error 	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
			|  error  '+'  term 		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
			|  error  '-'  term 		{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
            |  expression  '+'  '+'  term 	{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
;

term  :  term  '*'  factor {showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
							Operand op1 = (Operand) $1.obj; 
      						Operand op2 = (Operand) $3.obj; 
      						Operator opt = new Operator("*");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						$$.obj = new Operand(Operand.TRIPLET,t.getId());}
      |  term  '/'  factor {showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      						Operand op1 = (Operand) $1.obj; 
      						Operand op2 = (Operand) $3.obj; 
      						Operator opt = new Operator("/");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						$$.obj = new Operand(Operand.TRIPLET,t.getId());}
	  |  factor            {showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 $$.obj = $1.obj;}
	  |  term '*' error    {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
  	  |  term '/' error    {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
	  |  error '*' factor    {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
  	  |  error '/' factor    {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
	  |  term  '*'  '*'  factor {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
      |  term  '/'  '/'  factor {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
;

factor  :  ID           {  $$.obj = new Operand(Operand.ST_POINTER,$1.sval); }
	    |  CONSTANT     {  $$.obj = new Operand(Operand.ST_POINTER,$1.sval); }
	    |  '-' CONSTANT {
							 // Manejo la entrada positiva de esta constante		    				
		    				 Symbol positivo = la.getSymbolsTable().getSymbol($2.sval);
		    				 if (positivo.getType()==Symbol._ULONGINT)
		    				 	showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: una constante del tipo entero largo sin signo no puede ser negativa");
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

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
	ic = new IntermediateCode();
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