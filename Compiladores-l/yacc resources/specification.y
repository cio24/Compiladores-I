%{
package lexicalAnalyzerPackage;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
%}


%token IF THEN ELSE END_IF OUT FUNC RETURN 
ULONGINT DOUBLE LOOP UNTIL ID CONSTANT CSTRING LESSEQUAL GREATEQUAL EQUAL NEQUAL
PROC NA SHADOWING TRUE FALSE UP DOWN

%start program
%%
program : sentences  {showMessage("Pograma reducido,linea:"+la.getCurrentLine());}
		;
		
sentences  :  sentence ';' 
		   |  sentence  ';'  sentences
;


sentence  :  declaration	
		  |  executable		
;

declaration  :  type  variable_list		{showMessage("Declaracion de variables reducida,linea:"+la.getCurrentLine());}
			 |  procedure	{showMessage("Declaracion de procedimiento reducida,linea:"+la.getCurrentLine());}
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

procedure  :  PROC  ID  '('  parameter_list  ')'  NA  '='  CONSTANT  SHADOWING  '='  true_false  '{'  sentences  '}'
           |  PROC  ID  '('  ')'  '{'  sentences  '}'
;

parameter_list  :  parameter
				|  parameter  ','  parameter
				|  parameter  ','  parameter  ','  parameter
;

parameter  :  type  ID
;

id_list  :  ID
		 |  ID  ','  ID
		 |  ID  ','  ID  ','  ID
;

function_call  :  ID  '('  id_list  ')'		
		       |  ID  '('  ')'
;

executable  :  ID  '='  expression	{showMessage("Asignacion reducida,linea:"+la.getCurrentLine());}
			|  if_clause  		{showMessage("Control IF reducida,linea:"+la.getCurrentLine());}
			|  loop_clause	    {showMessage("Constrol LOOP reducida,linea:"+la.getCurrentLine());}
			|  function_call	{showMessage("Llamado a funcion reducida,linea:"+la.getCurrentLine());}
			|  out_clause	{showMessage("Salida por pantalla reducida,linea:"+la.getCurrentLine());}
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'
            |  '>'
;

condition  :  expression  comparator  expression 	{showMessage("Condicion reducida,linea:"+la.getCurrentLine());}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence
;			   


if_clause  :  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block END_IF
 		   |  IF  '('  condition  ')'  sentence_block  END_IF
;

loop_clause  :  LOOP  sentence_block  UNTIL  '('  condition  ')'
;

/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term	{showMessage("Expresion reducida,linea:"+la.getCurrentLine());}
			|  expression  '-'  term
			|  term
;

term  :  term  '*'  factor
      |  term  '/'  factor	
	  |  factor
;

factor  :  ID 
	    |  CONSTANT
	    |  '-' CONSTANT
;

/*-------> Gramatica de expresiones <-------*/
%%

LexicalAnalyzer la;

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
} 

public void yyerror(String s){
    System.out.println(s);

}

public int yylex(){
	return la.yylex();
}

public void showMessage(String mg) {
	System.out.println(mg);
}