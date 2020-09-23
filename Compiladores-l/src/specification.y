

%token IF THEN ELSE END_IF OUT FUNC RETURN 
ULONGINT DOUBLE LOOP UNTIL ID CONSTANT CSTRING LESSEQUAL GREATEQUAL EQUAL NEQUAL
PROC NA SHADOWING TRUE FALSE UP DOWN

%%
program : sentences
		;
		
sentences : sentence
		  | sentence ';' sentences
		  ;

sentence  : declaration
	      ;

declaration : PROC
            ;
%%