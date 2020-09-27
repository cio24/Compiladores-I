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

program : sentences  				{showMessage( "[Line " + la.getCurrentLine() + "] Program completed!");}
	;
		
sentences  :  sentence ';' 
		   |  sentence ';' sentences
		   |  error 				{showMessage("[Line " + la.getCurrentLine() + "] Syntax error: ';' expected but didn't found.");} /*testeado*/
;


sentence  :  declaration	
		  |  executable		
;

declaration  :  type  variable_list	{showMessage("[Line " + la.getCurrentLine() + "] Variable declaration found.");}
			 |  procedure			{showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}
			 | variable_list 		{showMessage("[Line " + la.getCurrentLine() + "] Syntax error: there's no type for the identifier \"" + ((Symbol)$1.obj).getLexeme()  + "\".");}  /*testeado*/
			 | type 				{showMessage("[Line " + la.getCurrentLine() + "] Syntax error: identifier expected but didn't found.");}
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

procedure  :  PROC  ID  '('  parameter_list  ')'  NA  '='  CONSTANT  SHADOWING  '='  true_false  '{'  sentences  '}' {showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}	
           |  PROC  ID  '('  ')'  '{'  sentences  '}' 																 {showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}	
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

procedure_call :  ID  '('  id_list  ')' {showMessage("[Line " + la.getCurrentLine() + "] Procedure call found.");}	
		       |  ID  '('  ')' 			{showMessage("[Line " + la.getCurrentLine() + "] Procedure call found.");}	
;

executable  :  ID  '='  expression	{showMessage("[Line " + la.getCurrentLine() + "] Assignment found.");}
			|  if_clause  			{showMessage("[Line " + la.getCurrentLine() + "] If clause found.");}
			|  loop_clause	    	{showMessage("[Line " + la.getCurrentLine() + "] Loop clause found.");}
			|  procedure_call		{showMessage("[Line " + la.getCurrentLine() + "] Function clause found.");}
			|  out_clause			{showMessage("[Line " + la.getCurrentLine() + "] Out clause found.");}
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'
            |  '>'
;

condition  :  expression  comparator  expression 	{showMessage("[Line " + la.getCurrentLine() + "] Condition found.");}
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

expression  :  expression  '+'  term {showMessage("[Line " + la.getCurrentLine() + "] Addition expression found.");}
			|  expression  '-'  term {showMessage("[Line " + la.getCurrentLine() + "] Subtract expression found.");}
			|  term 				 {showMessage("[Line " + la.getCurrentLine() + "] Term found.");}
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

public void parse(){
	yyparse();
}

public void yyerror(String s){
	if(s.equals("syntax error"))
		System.out.println("[Line " + la.getCurrentLine()+ "] " + s + ".");

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