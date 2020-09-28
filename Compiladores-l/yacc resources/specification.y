%{
package lexicalAnalyzerPackage;

import usefullClassesPackage.Constants;
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

program :  /* EMPTY */		{showMessage( "[Line " + la.getCurrentLine() + "] WARNING sintáctico: Programa vacío!");}
		|  sentences  		{showMessage( "[Line " + la.getCurrentLine() + "] Programa completo.");}
		|  error			{showMessage( "[Line " + la.getCurrentLine() + "] ERROR sintáctico: no se encontraron sentencias válidas.");}
	;
		
sentences  :  sentence ';' 				//{showMessage( "[Line " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentence ';' sentences	//{showMessage( "[Line " + la.getCurrentLine() + "] Sentencia.");}
		   |  sentence error			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: ';' ausente al final de la sentencia.");} /*testeado*/
;


sentence  :  declaration	
		  |  executable		
;

declaration  : type  variable_list	{showMessage("[Line " + la.getCurrentLine() + "] Declaración de variable.");}
			 | procedure			{showMessage("[Line " + la.getCurrentLine() + "] Declaración PROC.");}
			 | variable_list 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: no hay tipo para el identificador\"" + $1.sval + "\".");}  /*testeado*/
			 | type 				{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un identificador y no se encontró.");}
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

procedure  :  PROC  ID  '('  parameter_list  ')'  NA  '='  CONSTANT  SHADOWING  '='  true_false  '{'  sentences_proc  '}' {showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}	
           |  PROC  ID  '('  ')'  NA  '='  CONSTANT  SHADOWING  '='  true_false   '{'  sentences_proc  '}' 			     {showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}	
;

parameter_list  :  parameter
				|  parameter  ','  parameter
				|  parameter  ','  parameter  ','  parameter
				|  parameter  ','  parameter  ','  parameter  ',' parameter	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: un procedimiento puede recibir un máximo de trés parametros.");}
;

parameter  :  type  ID
		   |  type 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta identificador en declaración de parámetro.");}
		   |  ID 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta tipo en declaración de parámetro.");}
;

id_list  :  ID
		 |  ID  ','  ID
		 |  ID  ','  ID  ','  ID	
		 |  ID  ','  ID  ','  ID  ',' ID	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: un procedimiento puede recibir un máximo de trés parametros.");}
		 |  error 							{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
;

procedure_call :  ID  '('  id_list  ')'
		       |  ID  '('  ')' 			
;

executable  :  ID  '='  expression		{showMessage("[Line " + la.getCurrentLine() + "] Asignación.");}
			|  ID  '='  error			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: asignación errónea. Se espera una expresión del lado derecho.");}
			|  error  '='  expression	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: asignación errónea. Se espera un identificador del lado izquierdo.");}
			|  if_clause  				{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
			|  loop_clause	    		{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
			|  procedure_call			{showMessage("[Line " + la.getCurrentLine() + "] Invocación PROC.");}
			|  out_clause				{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'
            |  '>'
;

condition  :  expression  comparator  expression 	{showMessage("[Line " + la.getCurrentLine() + "] Condición.");}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence
			   | '{' '}'
;			   


if_clause  :  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block END_IF
 		   |  IF  '('  condition  ')'  sentence_block  END_IF
 		   |  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block error 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta palabra reservada END_INF al final de la sentencia IF");}
 		   |  IF  '('  condition  ')'  sentence_block  error 						{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta palabra reservada END_INF al final de la sentencia IF");}
 		   |  IF  '('  error  ')'  sentence_block  ELSE  sentence_block END_IF 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: luego de la palabra reservada IF se espera una condición entre paréntesis.");}
 		   |  IF  '('  error  ')'  sentence_block  END_IF 							{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: luego de la palabra reservada IF se espera una condición entre paréntesis.");}
		   |  IF  '('  condition  ')'  error  ELSE  sentence_block END_IF 			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
		   |  IF  '('  condition  ')'  sentence_block  ELSE  error END_IF 			{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
 		   |  IF  '('  condition  ')'  error  END_IF 								{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
;

loop_clause  :  LOOP  sentence_block  UNTIL  '('  condition  ')'
			 |  LOOP  sentence_block  error							{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta la cláusula UNTIL en la sentencia LOOP");}
			 |  LOOP  sentence_block  UNTIL  '('  error  ')'		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la cláusula UNTIL debe incluir una condición entre paréntesis");}
			 |  LOOP  error  UNTIL  '('  condition  ')'				{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia LOOP debe incluir un bloque de sentencias");}
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
			|  term 				 	{showMessage("[Line " + la.getCurrentLine() + "] Término.");}
			|  expression  '+'  error 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la suma debe contener un término válido.");}
			|  expression  '-'  error 	{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la resta debe contener un término válido.");}
			|  error  '+'  term 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la suma debe contener una expresión válida.");}
			|  error  '-'  term 		{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la resta debe contener una expresión válida.");}
;

term  :  term  '*'  factor {showMessage("[Line " + la.getCurrentLine() + "] Multiplicación.");}
      |  term  '/'  factor {showMessage("[Line " + la.getCurrentLine() + "] División.");}
	  |  factor            {showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
	  |  term '*' error    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la múltiplicación debe llevar una constante o un identificador");}
  	  |  term '/' error    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la división debe llevar una constante o un identificador");}
	  |  error '*' factor    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la múltiplicación debe llevar una término o un factor");}
  	  |  error '/' factor    {showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la división debe llevar un término o un factor");}
	  
;

factor  :  ID 
	    |  CONSTANT
	    |  '-' CONSTANT {
	    				 Symbol symbol = la.symbolsTable.getSymbol($2.sval);
	    				 if(symbol.getType().equals(Symbol._DOUBLE)){
							String lexeme = symbol.getLexeme();
							String snumber = lexeme.replace('d','e');
							BigDecimal number = new BigDecimal(snumber);
							BigDecimal min = new BigDecimal("2.2250738585072014d-308");
							BigDecimal max = new BigDecimal("1.7976931348623157d+308");
							if (!(number.compareTo(max) < 0  && min.compareTo(number)< 0))
								showMessage("[Line " + la.getCurrentLine() + "] DOBLE negativo fuera de rango.");
						}
	    				}
;



/*-------> Gramatica de expresiones <-------*/
%%

LexicalAnalyzer la;

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