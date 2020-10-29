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
											// Agregar a la tabla de simbolos la varialbe con el scope, salvo que ya este declarada entonces tira error
											ic.variableDeclarationControl(v,scope,$1.sval);
										}
										//Resetear la lista de identificadores siendo identificados.
										variableDeclarationIdentifiers.clear();
									} 
			 | procedure			
			 | variable_list 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"no hay tipo para el identificador\"" + $1.sval + "\".");}  
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

type  :  ULONGINT  { $$.sval=Symbol._ULONGINT_TYPE; }
	  |  DOUBLE	   { $$.sval=Symbol._DOUBLE_TYPE; }
;

true_false : TRUE {$$.sval = "TRUE";}
           | FALSE {$$.sval = "FALSE";}
;

/*
procedure  :  PROC  ID  '('  parameter_list  ')'  na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");
										st.getSymbol($2.sval).
										}
		   |  PROC  ID  '('   ')'                 na_shad_definition proc_body {showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}	
		   |  PROC  ID  '('  parameter_list  ')'  na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   |  PROC  ID  '('    ')'                na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   |  PROC      '('  parameter_list  ')'  na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el identificador del procedimiento");}		
		   |  PROC      '('   ')'                 na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el identificador del procedimiento");}		
	       |  PROC  ID  '(' error  ')'            na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se definio mal la lista de parametros");}	
;
*/
procedure  :  procedure_header  '('  parameter_list  ')'  na_shad_definition proc_body
											{
												showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");

												//restauramos el scope ahora que se termino de detectar el procedimiento
												scope = st.removeScope(scope);

												String procedureId = $1.sval;

												//se actualiza la pila de los procedimientos y si este no se declaro se declara
													ic.procedureDeclarationControl(procedureId, scope);

											}

	  |  procedure_header  '('   ')'      na_shad_definition proc_body
		   									{
												showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");

												//restauramos el scope ahora que se termino de detectar el procedimiento
												scope = st.removeScope(scope);

												String procedureId = $1.sval;

												//se actualiza la pila de los procedimientos y si este no se declaro se declara
												ic.procedureDeclarationControl(procedureId, scope);
 											}
		   |  procedure_header  '('  parameter_list  ')'  na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   |  procedure_header  '('    ')'                na_shad_definition           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}		
	       |  procedure_header  '(' error  ')'            na_shad_definition proc_body {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se definio mal la lista de parametros");}	
;

procedure_header  :  PROC  ID  {
				//guardo el nombre del procedimiento en el procedure_header
				$$.sval = $2.sval;

				//guardo el nombre completo del procedimiento xq lo necesito para hacer cosas
				 //en la parte del NA de abajo en donde NO se puede acceder al procedure_header!
				lastIdentifierFound = $2.sval + scope;

				//actualizo el scope
				scope += ":"+$2.sval;
                               }
             	   |  PROC 	   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el identificador del procedimiento");}
;

na_shad_definition : NA  '='  CONSTANT  SHADOWING  '='  true_false {
									String fullProcedureId = lastIdentifierFound;
									String idNameDeclaration = st.findClosestIdDeclaration(fullProcedureId);
									if(idNameDeclaration == fullProcedureId)
										ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO ,"El procedimiento " + fullProcedureId + "ya se encuentra declarado en el mismo scope");
									else{ //si es la primera vez que se declara el procedimiento es se declaro en otro ambito al alcance

										int NA = Integer.valueOf($3.sval);
										if(NA < 1 || NA > 4){
											ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO ,"El valor que indica los niveles de anidamientos debe estar entre 1 y 4, se define por defecto 4");
											NA = 4;
										}

										boolean shadowing;

										if($6.sval.equals("TRUE"))
											shadowing = true;
										else
											shadowing = false;
										//lo agrego al stack y tira error si supera el anidamiento de un procedimiento padre
										ic.addProcedureToStack(fullProcedureId,NA,shadowing);
									}
								   }
				   | '='  CONSTANT  SHADOWING  '='  true_false 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra NA");}
				   | NA CONSTANT  SHADOWING  '='  true_false 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del NA");}
				   | NA '=' SHADOWING  '='  true_false 				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el valor de NA");}
			   	   | NA  '='  CONSTANT '='  true_false				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra SHADOWING");}
				   | NA  '='  CONSTANT  SHADOWING true_false 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del SHADOWING");}
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

id_list  :  ID		{
				//ESTE CODIGO DEBE METERSE EN UN METODO PARA PODER USARLO EN EL RESTO DE LOS CASOS DONDE HAY MÁS ID'S

				showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
				String id = $1.sval;
				String fullId = id + scope;

				//obtengo la delaración de la variable del ambito más cercano
				String procIdDeclaration = st.findClosestIdDeclaration(fullId);
				if(procIdDeclaration == null) //no existe una variable declarada con ese nombre
					ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,"No existe una variable al alcanze con este nombre");
				else if(st.getSymbol(procIdDeclaration).getUse().equals(Symbol._PROCEDURE)) //el nombre corresponde a un procedimiento
					ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,"No se puede pasar un procedimiento por parametro");
				else{
					//obtengo el nombre del procedimiento que se esta invocando
					String procedureId = lastIdentifierFound;

					//obtengo el símbolo correspondiente al procedimiento
					Symbol procSymbol = st.getSymbol(procIdDeclaration);

					//creo un símbolo neuvo para la variable que se crea por el pasaje copia-valor de los parametros
					Symbol parameter = new Symbol(id + ":" + procIdDeclaration, Symbol._ID_LEXEME, procSymbol.getDataType(),Symbol._PARAMETER);

					//lo agrego a la tabla de símbolos
					st.addSymbol(id,parameter);
				}
			}
		 |  ID  ','  ID							{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID  ','  ID	        	{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
		 |  ID  ','  ID  ','  ID  ',' id_list 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir una maximo de 3 parametros");}
;

procedure_call :  ID  '('  id_list  ')' {
											ic.procedureCall($1.sval,scope,la.getCurrentLine());
											//lo tengo que guardar xq sirven para la creación de las variables de los parametros
											//estas necesitan tener el mismo scope que una variable de dentro del procedimiento
											lastIdentifierFound = $1.sval;
										}
		       |  ID  '('  ')' {
									ic.procedureCall($1.sval,scope,la.getCurrentLine());
								}
;

executable  :  ID  '='  expression		{
							showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");
							String fullVarId = $1.sval + scope;
							String varIdDeclaration = st.findClosestIdDeclaration(fullVarId);
							st.removeSymbol($1.sval); //SIGO SIN ENTENDER ESTO!! YA LO ENTENDI XD
							Triplet t;
							if(varIdDeclaration == null){
								ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se utiliza una variable antes de declararla");
								t = ic.createTriplet("=", $1.sval + ":undefined",(String) $3.obj);
							} else {
								//se muestra el nombre de la variable con el scope en dondé se declaro, no en donde se encontró
								//para hacerlo mas legible
								t = ic.createTriplet("=", varIdDeclaration,(String) $3.obj);

								//se incrementa la cantidad de referencias que tiene la variable
								st.getSymbol(varIdDeclaration).addReference();
							}
							$$.obj = t.getId();
						}
			|  ID  '='  error			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una expresion del lado derecho");}
			|  error '='  expression	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una identificador del lado izquierdo");}
			|  ID  EQUAL  expression 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, ¿quisiste decir '=' ?");}
			|  if_clause  				{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
			|  loop_clause	    		{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
			|  procedure_call			{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
			|  out_clause				{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
;

comparator  :  EQUAL		{$$.obj = $1.sval;}
            |  NEQUAL		{$$.obj = $1.sval;}
            |  LESSEQUAL 	{$$.obj = $1.sval;}
            |  GREATEQUAL 	{$$.obj = $1.sval;}
            |  '<'		{$$.obj = "<";}
            |  '>' 		{$$.obj = ">";}
 	    	|  '<'  '<'					{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token < duplicado");}
            |  '>'  '>'			        {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token > duplicado");}
            |  LESSEQUAL  '='           {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '<='?");}
            |  GREATEQUAL  '='          {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '>='?");}
            |  NEQUAL  '='              {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
            |  '<'  '>'                 {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
;

condition  :  expression  comparator  expression 	{
													showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");
													Triplet t = ic.createTriplet((String) $2.obj, (String) $1.obj, (String) $3.obj);
													$$.obj = t.getId();
													}
		   |  expression '=' expression 			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparacion invalida. ¿Quisiste decir '=='?");}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence  ';'
			   | '{' '}'
;			   


if_clause  :  IF  if_condition   then_body  ELSE  else_body  END_IF 	{ ic.updateFirstOperandFromStack(1);
 									  ic.popFromStack();
 									}
 		   |  IF  if_condition   then_body END_IF                   	{
										  ic.popFromStack(); //desapilo el último terceto xq era el del BI
 		    								  ic.removeLastTriplet(); //lo saco de la lista de tercetos
 		    								  //actualizo el terceto del BF xq se realiza suponiendo que va a haber un BI
										ic.updateSecondOperandFromStack(0);
										ic.popFromStack();

 		    								}
 		   |  IF  if_condition   then_body  ELSE  else_body error 		{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
 		   |  IF  if_condition   then_body  error 						{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
 		   |  IF  if_condition   error  ELSE  else_body END_IF 			{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
		   |  IF  if_condition   then_body  ELSE  error END_IF 			{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula ELSE_IF"); }
 		   |  IF  if_condition   error  END_IF 							{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
 
;

if_condition  :   '('  condition  ')'  {Triplet t = ic.createTriplet("BF",(String) $2.obj);
      									ic.pushToStack(Integer.valueOf(t.getId()));
      									$$.obj = t.getId();}
			 |    '('  error  ')'		{
			 							Triplet t = ic.createEmptyTriplet();
										ic.pushToStack(Integer.valueOf(t.getId()));
      									$$.obj = t.getId();
      									ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"luego de la palabra reservada IF se espera una condicion entre parentesis");}
		     |    condition             {
			 							Triplet t = ic.createEmptyTriplet();
										ic.pushToStack(Integer.valueOf(t.getId()));
      									$$.obj = t.getId();
      									ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'."); }
	    	 |    condition  ')'        {	    	 
			 							Triplet t = ic.createEmptyTriplet();
										ic.pushToStack(Integer.valueOf(t.getId()));
      									$$.obj = t.getId();
      									ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'."); } 
;

then_body  :  sentence_block  	{ 
								Triplet t = ic.createTriplet("BI");
								//va 1, xq el terceto que correponde con el 0 es el de BI y queremos saltar a uno después con el BF
								ic.updateSecondOperandFromStack(1);
							    	ic.pushToStack(Integer.valueOf(t.getId()));
							    }
;

else_body  :  sentence_block
;

loop_clause  :  loop_begin  sentence_block  UNTIL loop_condition
			 |  loop_begin  sentence_block  error							{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la clausula UNTIL en la sentencia LOOP");}
			 |  loop_begin  error  UNTIL  '('  condition  ')'				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir un bloque de sentencias");}
;

loop_condition : '('  condition  ')'{       
							   		Triplet t = ic.createTriplet("BF",(String) $2.obj);
						       		$$.obj = t.getId(); //finally we associate an operand created with the tiplet to the loop_condition
							        }
				| '('  error  ')'	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la clausula UNTIL debe incluir una condicion entre parentesis"); }
				| condition 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir una condicion encerrada por '(' ')'"); }
;

loop_begin : LOOP 	     {ic.pushToStack(ic.currentTripletIndex() + 1); /*we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition*/}
;


/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'	{
									   	Triplet t = new Triplet("OUT",(String) $3.sval);
									   	ic.addTriplet(t);
			 						 	}
			|  OUT  '('  error  ')' 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT solo acepta cadenas de caracteres"); }
			|  OUT  CSTRING    	 	   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'"); }
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term 		{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
      										Triplet t = ic.createTriplet("+",(String) $1.obj, (String) $3.obj);
      										$$.obj = t.getId();}
			|  expression  '-'  term 		{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
	      									Triplet t = ic.createTriplet("-",(String) $1.obj, (String) $3.obj);
	      									$$.obj = t.getId();}
			|  term 				 		{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
											 $$.obj = $1.obj;}
			|  expression  '+'  error 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la suma debe contener un termino valido");}
			|  expression  '-'  error 		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la resta debe contener un termino valido");}
			|  error  '+'  term 			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la suma debe contener una expresion valida");}
			|  error  '-'  term 			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la resta debe contener una expresion valida");}
            |  expression  '+'  '+'  term 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '+' sobrante");}
;

term  :  term  '*'  factor 		{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
      							Triplet t = ic.createTriplet("*",(String) $1.obj, (String) $3.obj);
      							$$.obj = t.getId();}
      |  term  '/'  factor 		{showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      							Triplet t = ic.createTriplet("/",(String) $1.obj, (String) $3.obj);
      							$$.obj = t.getId();}
	  |  factor            		{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 	$$.obj = $1.obj;}
	  |  term '*' error    		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
  	  |  term '/' error    		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la division debe llevar una constante o un identificador");}
	  |  error '*' factor   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
  	  |  error '/' factor   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la division debe llevar un termino o un factor");}
	  |  term  '*'  '*'  factor {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '*' sobrante");}
      |  term  '/'  '/'  factor {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '/' sobrante");}
;

factor  :  ID           			{
							String fullId = $1.sval + scope;
							st.removeSymbol($1.sval); //NO ENTIENDO ESTO PLEASE
							if(st.findClosestIdDeclaration(fullId) == null){
							    ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,"se utiliza una variable antes de declararla");
								$$.obj = $1.sval + ":undefined";
							} else {
								$$.obj = $1.sval+scope;
							}				
						}
	    |  CONSTANT     {  $$.obj = $1.sval; }
	    |  '-' CONSTANT {
						 // Manejo la entrada positiva de esta constante		    				
	    				 Symbol positivo = st.getSymbol($2.sval);
	    				 if (positivo.getDataType()==Symbol._ULONGINT_TYPE)
	    				 	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"una constante del tipo entero largo sin signo no puede ser negativa");
	    				 else{
	    				 	 st.removeSymbol(positivo.getLexeme());
		    				 		    				 	    				 
		    				 // Creo nueva entrada o actualizo la existente con una referencia
		    				 Symbol negativo = st.getSymbol("-"+$2.sval);
		    				 if (negativo != null){
		    				 	negativo.addReference();  // Ya existe la entrada
		    				 }else{
		    				 	String lexema = "-"+positivo.getLexeme();
		    				 	Symbol nuevoNegativo = new Symbol(lexema,positivo.getLexemeType(),positivo.getDataType());
		    				 	st.addSymbol(lexema,nuevoNegativo);
		    				 }
		    				 $2.sval = "-"+$2.sval;
		    				 
		    				 $$.obj = $2.sval;
	    				 }		    				 		
	    			 	}
;



/*-------> Gramatica de expresiones <-------*/
%%

public LexicalAnalyzer la;
public IntermediateCode ic;
private String lastIdentifierFound;
private String scope;
private SymbolsTable st;

public Vector<String> variableDeclarationIdentifiers; //Para completar el tipo de variables declaradas

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
	st = la.getSymbolsTable();
	ic = new IntermediateCode(st,la);

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

public void showErrorMessage(String message) {
	System.out.println(message);
}




