%token IF THEN ELSE END_IF OUT FUNC RETURN 
ULONGINT DOUBLE LOOP UNTIL ID CONSTANT CSTRING LESSEQUAL GREATEQUAL EQUAL NEQUAL
PROC NA SHADOWING TRUE FALSE UP DOWN

%%
program : sentences
		;
		
sentences  :  sentence
		   |  sentence  ';'  sentences
;


sentence  :  declaration
		  |  executable
;

declaration  :  type  variable_list
			 |  procedure
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

executable  :  ID  '='  expression
			|  if_clause  
			|  loop_clause
			|  function_call
;

comparator  :  EQUAL
            |  NEQUAL
            |  LESSEQUAL 
            |  GREATEQUAL
            |  '<'
            |  '>'
;

condition  :  expression  comparator  expression 
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence
;			   


if_clause  :  IF  '('  condition  ')'  sentence_block  ELSE  sentence_block ENDIF
 		   |  IF  '('  condition  ')'  sentence_block  ENDIF
;

loop_clause  :  LOOP  sentence_block  UNTIL  '('  condition  ')'
;

/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term
			|  expression  '-'  term
			|  term
			|  '-'  expression 
;

term  :  term  '*'  factor
      |  term  '/'  factor	
	  |  factor
;

factor  :  ID 
	    |  CONSTANT
;

/*-------> Gramatica de expresiones <-------*/
%%
⠀⠀⠀⠀⠀