%{
package lexicalAnalyzerPackage;
import codeGenerationPackage.*;
import symbolPackage.*;
import usefulClassesPackage.Constants;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Math;
import java.math.BigDecimal;
import java.util.ArrayList;
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

program :  /* EMPTY */		

		{ErrorReceiver.displayError(ErrorReceiver.WARNING,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"Programa vacio");}
		
		|  sentences  		
		
		{showMessage( "[Linea " + la.getCurrentLine() + "] Programa completo.");}
		
		|  error			
		
		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"No se encontraron sentencias validas");}
;
		

sentences  :  sentence ';' 		
		
           //{showMessage( "[Linea " + la.getCurrentLine() + "] Sentencia.");}
           
		   |  sentences sentence ';' 	
		   
		   //{showMessage( "[Linea " + la.getCurrentLine() + "] Sentencia.");}
		   
		   |  sentence error			
		   
		   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"';' ausente al final de la sentencia.");} /*testeado*/
		   
		   |  error	';'					
		   
		   {showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");} /*testeado*/
;


sentence  :  declaration	
		  |  executable		
;

declaration  : type  variable_list

			 {
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
			 	
			 | variable_list 		
			 
			 {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"no hay tipo para el identificador\"" + $1.sval + "\".");}  
			 
			 | type 				
			 
			 {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un identificador y no se encontro");}
;

variable_list  :  ID
				    {
	 			        //añadir a la lista de identificadores actualmente siendo identificados el identificador
	 			        variableDeclarationIdentifiers.add($1.sval);
	 			      }
	 			      
			   |  ID  ','  variable_list 
			   
			          {
	 			        //añadir a la lista de identificadores actualmente siendo identificados el identificador identificado
	 			        variableDeclarationIdentifiers.add($1.sval);
	 			      }
;

type  :  ULONGINT  

      { $$.sval=Symbol._ULONGINT_TYPE; }
      
	  |  DOUBLE	   
	  
	  { $$.sval=Symbol._DOUBLE_TYPE; }
;

true_false : TRUE 

           {$$.sval = "TRUE";}
           
           | FALSE 
           
           {$$.sval = "FALSE";}
;

procedure  :  procedure_header  '('  parameter_list  ')'  na_shad_definition proc_body

			{
				showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");

				ProcedureData pd = ic.getProcedureDataFromStack();
				String fullProcId = pd.getFullProcId();
				String procId = fullProcId.substring(0,fullProcId.indexOf(":"));

				//restauramos el scope ahora que se termino de detectar el procedimiento
				scope = st.removeScope(scope);
				if(!fullProcId.contains("ERROR")){

					//se actualiza la pila de los procedimientos y si este no se declaro se declara
					ic.procedureDeclarationControl(procId, scope);

					//se guarda para que cuando se invoque se puedan controlar los parametros
					ic.saveProcedureData(pd);
					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}
				else{
					//se quita del stack el procedimiento
					ic.removeLastProcedureFromStack();

					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}
			}

	  |  procedure_header  '('   ')'      na_shad_definition proc_body
	  
			{
				showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");

				ProcedureData pd = ic.getProcedureDataFromStack();
				String fullProcId = pd.getFullProcId();
				String procId = fullProcId.substring(0,fullProcId.indexOf(":"));

				//restauramos el scope ahora que se termino de detectar el procedimiento
				scope = st.removeScope(scope);
				if(!fullProcId.equals(procId + ":ERROR")){

					//se actualiza la pila de los procedimientos y si este no se declaro se declara
					ic.procedureDeclarationControl(procId, scope);

					//se guarda para que cuando se invoque se puedan controlar los parametros
					ic.saveProcedureData(pd);

					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}
				else{
					//se quita del stack el procedimiento
					ic.removeLastProcedureFromStack();

					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}

			}
			
		   |  procedure_header  '('  parameter_list  ')'  na_shad_definition   
		           
		   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   
		   |  procedure_header  '('    ')'                na_shad_definition    
		          
		   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}	
		   	
	       |  procedure_header  '(' error  ')'            na_shad_definition proc_body 
	       
	       {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se definio mal la lista de parametros");}	
	       
;

procedure_header  :  PROC  ID  
{
	/*
	si el procedimiento se definio en este scope tira error y preserva información
	del error para que se pueda detectar en otras etapadas de la declaración
	Si no tiene error agrega el nombre del identificador en una estructura que se
	apila para que a medida que se encuentran los demás datos se puedan ir guardando
	*/
	ic.procedureHeaderControl($2.sval,scope);

	//expando el scope
	scope += ":"+$2.sval;
}
                
             	  |  PROC
             	   
{
	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,
		"falta definir el identificador del procedimiento");

	//para poder controlar el error más adelante generamos un procedureData error
	ic.addProcedureDataToStack(new ProcedureData("ERROR"));
}
;

na_shad_definition : NA  '='  CONSTANT  SHADOWING  '='  true_false 

					{
						ProcedureData pd = ic.getProcedureDataFromStack();
						if(!pd.getFullProcId().contains("ERROR")){

							//guardo los nuevos datos del procedimiento
							pd.setNA(Integer.valueOf($3.sval));

							boolean shadowing;
							if($6.sval.equals("TRUE"))
								pd.setShadowing(true);
							else
								pd.setShadowing(false);
						}
	                		}
	                
				   | '='  CONSTANT  SHADOWING  '='  true_false 		
				   
				   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra NA");}
				   
				   | NA CONSTANT  SHADOWING  '='  true_false 		
				   
				   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del NA");}
				   
				   | NA '=' SHADOWING  '='  true_false 				
				   
				   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el valor de NA");}
			   	   
			   	   | NA  '='  CONSTANT '='  true_false				
			   	   
			   	   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra SHADOWING");}
				  
				   | NA  '='  CONSTANT  SHADOWING true_false 		
				   
				   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del SHADOWING");}
;


proc_body : '{' sentences '}'

		  | '{' error  '}' 
		  
		  {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"cuerpo del procedimiento mal definido");}
		  
		  | '{' '}'
;

parameter_list  :  parameter  	
													
				{
					showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");

					//obtengo los datos del procedimiento que se esta declarando
					ProcedureData pd = ic.getProcedureDataFromStack();

					if(!pd.getFullProcId().equals("ERROR")){
						//le agrego el tipo del primer parametro
						pd.setFirstFormalParameterType($1.sval);
					}
				}
				
				|  parameter  ','  parameter  	
													
				{
					showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");
					//obtengo los datos del procedimiento que se esta declarando
					ProcedureData pd = ic.getProcedureDataFromStack();

					if(!pd.getFullProcId().equals("ERROR")){
						//le agrego el tipo del primer parametro
						pd.setFirstFormalParameterType($1.sval);
						pd.setSecondFormalParameterType($3.sval);
					}
				}
				
				|  parameter  ','  parameter  ','  parameter  		
								
				{
					showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");
					//obtengo los datos del procedimiento que se esta declarando
					ProcedureData pd = ic.getProcedureDataFromStack();

					if(!pd.getFullProcId().equals("ERROR")){
						//le agrego el tipo del primer parametro
						pd.setFirstFormalParameterType($1.sval);
						pd.setSecondFormalParameterType($3.sval);
						pd.setThirdFormalParameterType($5.sval);
					}
				}
				
				|  parameter  ','  parameter  ','  parameter  ',' parameter_list	
				
				{
					ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir un maximo de 3 parametros");
				}
;

parameter  :  type  ID
			{
				//borro de la tabla de símbolos el identificador del parametro
				st.removeSymbol($2.sval);

				//agrego el identificador pero con los datos del parametro y el scope
				String fullId = $2.sval + scope;
				Symbol sp = new Symbol(fullId, Symbol._IDENTIFIER_LEXEME, $1.sval, Symbol._PARAMETER_USE, Symbol._COPY_VALUE_SEMANTIC);
				st.addSymbol(fullId, sp);

				//guardo solo el tipo del parametro para poder usarlo para controlar más tarde cuando se invoque al procedimiento
				$$.sval = $1.sval;
			}

		   |  type

		   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta identificador en declaracion de parametro");}
		   
		   |  ID 		
		   
		   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el tipo en declaracion de parametro");}
;

id_list  :  ID		
		{
			showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			//chequea que el parametro haga referencia a una variable
			//si es correcto seguarda en un arreglo para más adelante controlar si el tipo se corresponde
			ic.realParameterControl($1.sval,scope);
		}
			
		 |  ID  ','  ID	
		 						
		 {
		 	showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			ic.realParameterControl($1.sval,scope);
			ic.realParameterControl($3.sval,scope);
		}
		 
		 |  ID  ','  ID  ','  ID	  
		       	
		 {
		 	showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			ic.realParameterControl($1.sval,scope);
			ic.realParameterControl($3.sval,scope);
			ic.realParameterControl($5.sval,scope);
		}
		 
		 |  ID  ','  ID  ','  ID  ',' id_list 	
		 
		 {
		 	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir una maximo de 3 parametros");
		 	showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			ic.realParameterControl($1.sval,scope);
			ic.realParameterControl($3.sval,scope);
			ic.realParameterControl($5.sval,scope);
		}
;

procedure_call :  ID  '('  id_list  ')'

   				{
					ic.procedureCall($1.sval,scope);

					//vaciamos la lista para cuando se vuelva a llamar a otro procedimiento
					ic.cleanRealParameters();
				}

		       |  ID  '('  ')'

		       		{
					ic.procedureCall($1.sval,scope);

					//vaciamos la lista para cuando se vuelva a llamar a otro procedimiento
					ic.cleanRealParameters();
				}
;

executable  :  ID  '='  expression
		
				{
					/*
					además de controlar que se use una variable que se claro, se chequea si
					los tipos son compatibles y se tira error de ser necesario
					*/
					$$.obj = ic.variableAssignmentControl($1.sval,scope,(Operand) $3.obj);
				}
						
			|  ID  '='  error		
				
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una expresion del lado derecho");}
			
			|  error '='  expression	
			
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una identificador del lado izquierdo");}
			
			|  ID  EQUAL  expression 	
			
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, ¿quisiste decir '=' ?");}
			
			|  if_clause  				
			
			{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
			
			|  loop_clause	    		
			
			{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
			
			|  procedure_call			
			
			{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
			
			|  out_clause				
			
			{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
;

comparator  :  EQUAL		
			
			{$$.obj = $1.sval;}
           
            |  NEQUAL		
           
            {$$.obj = $1.sval;}
           
            |  LESSEQUAL 	
          
            {$$.obj = $1.sval;}
           
            |  GREATEQUAL 	
          
            {$$.obj = $1.sval;}
          
            |  '<'		
          
            {$$.obj = "<";}
           
            |  '>' 		
          
            {$$.obj = ">";}
 	    
 	    	|  '<'  '<'					
 	    
 	    	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token < duplicado");}
         
            |  '>'  '>'			        
         
            {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token > duplicado");}
         
            |  LESSEQUAL  '='           
         
            {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '<='?");}
         
            |  GREATEQUAL  '='          
         
            {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '>='?");}
         
            |  NEQUAL  '='              
         
            {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
       
            |  '<'  '>'                 
         
            {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
;

condition  :  expression  comparator  expression
{
													showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");
													Triplet t = tm.createTriplet((String) $2.obj, (Operand) $1.obj, (Operand) $3.obj);
													$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
													}
		   |  expression '=' expression 			
		   {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparacion invalida. ¿Quisiste decir '=='?");}
;

/*-------> Gramatica de control<-------*/


sentence_block  :  '{'  sentences  '}'
			   |  sentence  ';'
			   | '{' '}'
;			   


if_clause  :  IF  if_condition   then_body  ELSE  else_body  END_IF 
	
			{ tm.updateOperandFromStack(1,1);
 			  tm.popFromStack();
			  tm.popFromStack();
 			  
 			}
 			
 		   |  IF  if_condition then_body END_IF      
 		                	
 		   {
			  	tm.popFromStack(); //desapilo el último terceto xq era el del BI
			  	tm.removeLastTriplet(); //lo saco de la lista de tercetos
			  	//actualizo el terceto del BF xq se realiza suponiendo que va a haber un BI
				tm.updateOperandFromStack(0,2);
				tm.popFromStack();
           }
           
 		   |  IF  if_condition   then_body  ELSE  else_body error 		
 		   
 		   { ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
 		   
 		   |  IF  if_condition   then_body  error 						
 		   
 		   { ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
 		   
 		   |  IF  if_condition   error  ELSE  else_body END_IF 			
 		   
 		   { ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
		   
		   |  IF  if_condition   then_body  ELSE  error END_IF 			
		   
		   { ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula ELSE_IF"); }
 		   
 		   |  IF  if_condition   error  END_IF 							
 		   
 		   { ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
 
;

if_condition  :   '('  condition  ')'  

			{
				Triplet t = tm.createTriplet("BF",(Operand) $2.obj,new Operand(Operand.TO_BE_DEFINED));
				tm.pushToStack(Integer.valueOf(t.getId()));
				//$$.obj = t.getId();  //Once again i ask for ur forgiveness my lord
			}
			
			 |    '('  error  ')'		
			 
			{
				Triplet t = tm.createEmptyTriplet();
				tm.pushToStack(Integer.valueOf(t.getId()));
				$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"luego de la palabra reservada IF se espera una condicion entre parentesis");
			}
			
		     |    condition    
		              
		    {
			Triplet t = tm.createEmptyTriplet();
			tm.pushToStack(Integer.valueOf(t.getId()));
			$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'.");
			
			 }
			 
	    	 |    condition  ')'        
	    	 
	    	 {	    	 
			Triplet t = tm.createEmptyTriplet();
			tm.pushToStack(Integer.valueOf(t.getId()));
			$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'."); 
			} 
;

then_body  :  sentence_block  	{ 
								// actualizo el triplete del salto del if al cuerpo de then
								tm.updateOperandFromStack(2,2);
								//tm.popFromStack();
								// Creo el triplete de salto incondicional para que salte el else
								Triplet t = tm.createBITriplet(new Operand(Operand.TO_BE_DEFINED));
								tm.pushToStack(Integer.valueOf(t.getId()));
							    }
;

else_body  :  sentence_block
;

loop_clause  :  loop_begin  sentence_block  UNTIL loop_condition
			 
			 |  loop_begin  sentence_block  error							
			 
			 {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la clausula UNTIL en la sentencia LOOP");}
			 
			 |  loop_begin  error  UNTIL  '('  condition  ')'				
			 
			 {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir un bloque de sentencias");}
;

loop_condition : '('  condition  ')'

				{       
				Triplet t = tm.createBTriplet($2.obj,"BT"); //Desapilar la direccion de salto del comienzo del loop.
				//$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId()); //finally we associate an operand created with the tiplet to the loop_condition //Comentado porque no sabemos para que lo queremos.
				}
				
				| '('  error  ')'	
				
				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la clausula UNTIL debe incluir una condicion entre parentesis"); }
				
				| condition 		
				
				{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir una condicion encerrada por '(' ')'"); }
;

loop_begin : LOOP 	     
				{tm.pushToStack(tm.getCurrentTripletIndex() + 1); /*we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition*/}
;


/*-------> Gramatica de control<-------*/

/*-------> Gramatica de salida<-------*/

out_clause  :  OUT  '('  CSTRING  ')'
{
									   	Triplet t = tm.createTriplet("OUT",new Operand(Operand.ST_POINTER,(String) $3.sval));
			 						 	}
			|  OUT  '('  error  ')' 	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT solo acepta cadenas de caracteres"); }
			|  OUT  CSTRING    	 	   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'"); }
;

/*-------> Gramatica de salida<-------*/
       
/*-------> Gramatica de expresiones <-------*/

expression  :  expression  '+'  term 
		
		{

			showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
			/*
			controla que los operandos sean del mismo tipo en caso de no serlo muestra un mensaje de error
			en cualquier caso crea un triplet y retorna su id para que se vaya pasando hacía arriba
			sabe dios después como voy a controlar eso xD
			*/
			$$.obj = ic.operationTypesControl("+", (Operand) $1.obj,(Operand) $3.obj);
		}
      		
			|  expression  '-'  term 		
			
		{
			showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
			/*
			controla que los operandos sean del mismo tipo en caso de no serlo muestra un mensaje de error
			en cualquier caso crea un triplet y retorna su id para que se vaya pasando hacía arriba
			sabe dios después como voy a controlar eso xD
			*/
			$$.obj = ic.operationTypesControl("-", (Operand) $1.obj,(Operand) $3.obj);
	      	}
	      	
			|  term 
			
			{
			showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
			$$.obj = $1.obj;
			}
			
			|  expression  '+'  error 		
			
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la suma debe contener un termino valido");}
			
			|  expression  '-'  error 		
			
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la resta debe contener un termino valido");}
			
			|  error  '+'  term 			
			
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la suma debe contener una expresion valida");}
			
			|  error  '-'  term 			
			
			{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la resta debe contener una expresion valida");}
            
            |  expression  '+'  '+'  term 	
            
            {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '+' sobrante");}
;

term  :  term  '*'  factor 	
	
{
		showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
		/*
		controla que los operandos sean del mismo tipo en caso de no serlo muestra un mensaje de error
		en cualquier caso crea un triplet y retorna su id para que se vaya pasando hacía arriba
		sabe dios después como voy a controlar eso xD
		*/
		$$.obj = ic.operationTypesControl("*", (Operand) $1.obj,(Operand) $3.obj);
	}
		
      |  term  '/'  factor 		
      
      {
		showMessage("[Linea " + la.getCurrentLine() + "] Division.");
		/*
		controla que los operandos sean del mismo tipo en caso de no serlo muestra un mensaje de error
		en cualquier caso crea un triplet y retorna su id para que se vaya pasando hacía arriba
		sabe dios después como voy a controlar eso xD
		*/
		$$.obj = ic.operationTypesControl("/", (Operand) $1.obj,(Operand) $3.obj);
	  }
	  
	  |  factor            		
	  
	  {
	  	showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	   	$$.obj = $1.obj;
	  }
	  			
	  |  term '*' error    		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
  	 
  	  |  term '/' error    		{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la division debe llevar una constante o un identificador");}
	 
	  |  error '*' factor   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
  	 
  	  |  error '/' factor   	{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la division debe llevar un termino o un factor");}
	 
	  |  term  '*'  '*'  factor {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '*' sobrante");}
    
      |  term  '/'  '/'  factor {ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '/' sobrante");}
;

factor  :  ID
        			
			{
			String fullId = $1.sval + scope;
			st.removeSymbol($1.sval);
			String realName = st.findClosestIdDeclaration(fullId);
			String dataType = st.getSymbol(realName).getDataType();
			if(realName == null){
			    ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,"se utiliza una variable antes de declararla");
				$$.obj = new Operand(Operand.ST_POINTER,$1.sval + ":undefined",dataType);
			} else {

				$$.obj = new Operand(Operand.ST_POINTER,realName,dataType);
			}				
		}
		
	    |  CONSTANT     
	    
		{
			Operand op1;
			if($1.sval.contains("."))
				op1 = new Operand(Operand.ST_POINTER,$1.sval,Symbol._DOUBLE_TYPE);
			else
				op1 = new Operand(Operand.ST_POINTER,$1.sval,Symbol._ULONGINT_TYPE);
			$$.obj = op1;

		}
	    
	    |  '-' CONSTANT 
	    
	    {
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
				 
				 $$.obj = new Operand(Operand.ST_POINTER,$2.sval,Symbol._ULONGINT_TYPE);
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
public TripletsManager tm;
private int lastParametersFound;

public Vector<String> variableDeclarationIdentifiers; //Para completar el tipo de variables declaradas

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
	st = la.getSymbolsTable();
	tm = new TripletsManager();
	ic = new IntermediateCode(st,la,tm);

	variableDeclarationIdentifiers=new Vector<String>();
	variableDeclarationIdentifiers.clear();
	lastParametersFound = -1;
	
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




