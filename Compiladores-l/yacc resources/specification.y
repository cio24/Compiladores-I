%{
package lexicalAnalyzerPackage;

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

program :  /* EMPTY */		{showMessage( "[Line " + la.getCurrentLine() + "] WARNING sintactico: Programa vacio!");}
		|  sentences  		{showMessage( "[Line " + la.getCurrentLine() + "] Programa completo.");}
		|  error			{showMessage( "[Line " + la.getCurrentLine() + "] ERROR sintactico: no se encontraron sentencias validas.");}
	;
		
sentences  :  sentence ';' 				//{showMessage( "[Line " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentences sentence ';' 	//{showMessage( "[Line " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentence error			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: ';' ausente al final de la sentencia.");} /*testeado*/
;


sentence  :  declaration	
		  |  executable		
;

declaration  : type  variable_list	{showMessage("[Line " + la.getCurrentLine() + "] Declaracion de variable.");}
			 | procedure			{showMessage("[Line " + la.getCurrentLine() + "] Declaracion PROC.");}
			 | variable_list 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + $1.sval + "\".");}  /*testeado*/
			 | type 				{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
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

sentences_proc : sentence ';',
			   | sentence ';' sentences_proc 

procedure  :  PROC  ID  '('  parameter_list  ')'  NA  '='  CONSTANT  SHADOWING  '='  true_false  '{'  sentences_proc  '}' {showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}	
           |  PROC  ID  '('  ')'  NA  '='  CONSTANT  SHADOWING  '='  true_false   '{'  sentences_proc  '}' 			     {showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}	
           | PROC  ID  '('  parameter_list  ')'  NA  '=' '-' CONSTANT  SHADOWING  '='  true_false  '{'  sentences_proc  '}' {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}	
           | PROC  ID  '('    ')'  NA  '=' '-' CONSTANT  SHADOWING  '='  true_false  '{'  sentences_proc  '}' {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}	
;

parameter_list  :  parameter
				|  parameter  ','  parameter
				|  parameter  ','  parameter  ','  parameter
				|  parameter  ','  parameter  ','  parameter  ',' parameter	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
;

parameter  :  type  ID
		   |  type 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
		   |  ID 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
;

id_list  :  ID
		 |  ID  ','  ID
		 |  ID  ','  ID  ','  ID	
		 |  ID  ','  ID  ','  ID  ',' ID	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
		 |  error 							{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
;

procedure_call :  ID  '('  id_list  ')'
		       |  ID  '('  ')' 			
;

executable  :  ID  '='  expression		{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
			|  ID  '='  error			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
			|  error  '='  expression	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
			|  ID  EQUAL  expression 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
			|  if_clause  				{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
			|  loop_clause	    		{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
			|  procedure_call			{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
			|  out_clause				{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'
            |  '>'
;

condition  :  expression  comparator  expression 	{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
		   |  expression '=' expression 			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence
			   | '{' '}'
;			   


if_clause  :  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block END_IF
 		   |  IF  '('  condition  ')'  sentence_block  END_IF
 		   |  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block error 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
 		   |  IF  '('  condition  ')'  sentence_block  error 						{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
 		   |  IF  '('  error  ')'  sentence_block  ELSE  sentence_block END_IF 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
 		   |  IF  '('  error  ')'  sentence_block  END_IF 							{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
		   |  IF  '('  condition  ')'  error  ELSE  sentence_block END_IF 			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
		   |  IF  '('  condition  ')'  sentence_block  ELSE  error END_IF 			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
 		   |  IF  '('  condition  ')'  error  END_IF 								{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
;

loop_clause  :  LOOP  sentence_block  UNTIL  '('  condition  ')'
			 |  LOOP  sentence_block  error							{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
			 |  LOOP  sentence_block  UNTIL  '('  error  ')'		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
			 |  LOOP  error  UNTIL  '('  condition  ')'				{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
;

/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'
			|  OUT  '('  error  ')'  {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term 	{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
			|  expression  '-'  term 	{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
			|  term 				 	{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
			|  expression  '+'  error 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
			|  expression  '-'  error 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
			|  error  '+'  term 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
			|  error  '-'  term 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
;

term  :  term  '*'  factor {showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
      |  term  '/'  factor {showMessage("[Line " + la.getCurrentLine() + "] Division.");}
	  |  factor            {showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
	  |  term '*' error    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
  	  |  term '/' error    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
	  |  error '*' factor    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
  	  |  error '/' factor    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
	  
;

factor  :  ID 
	    |  CONSTANT
	    |  '-' CONSTANT {
							// Manejo la entrada positiva de esta constante		    				
		    				 Symbol positivo = la.symbolsTable.getSymbol($2.sval);
		    				 if(positivo.removeRef() == 0){ // Remove reference and if it reaches 0, remove SyboleTable entry
		    				 	la.symbolsTable.removeSymbol(positivo.getLexeme());
		    				 }
		    				 
		    				 // TODO: QUE HACER CON - 4_ul ??????
		    				 
		    				 // Creo nueva entrada o actualizo la existente con una referencia
		    				 Symbol negativo = la.symbolsTable.getSymbol("-"+$2.sval);
		    				 if (negativo != null){
		    				 	negativo.addRef();  // Ya existe la entrada
		    				 }else{
		    				 	String lexema = "-"+positivo.getLexeme();
		    				 	Symbol nuevoNegativo = new Symbol(lexema,la.getCurrentLine(),positivo.getType());
		    				 	la.symbolsTable.addSymbol(lexema,nuevoNegativo);
		    				 }
	    				 	$2.sval = "-"+$2.sval;
	    				 		
	    				 }
;



/*-------> Gramatica de expresiones <-------*/
%%

public LexicalAnalyzer la;

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
}

public void parse(){
	yyparse();
}

public void yyerror(String s){
	if(s.equals("syntax error")){
		//System.out.println("[Line " + la.getCurrentLine()+ "] " + s + ".");
	}

}

int yylex(){
	yyval = new ParserVal();
	AtomicReference<ParserVal> ref = new AtomicReference<>();
	yychar = la.yylex(ref);
	yylval = ref.get(); // get next token
	return yychar;
}

public void showMessage(String mg) {
	System.out.println(mg);
}