//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "specification.y"
package lexicalAnalyzerPackage;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
//#line 25 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short ELSE=259;
public final static short END_IF=260;
public final static short OUT=261;
public final static short FUNC=262;
public final static short RETURN=263;
public final static short ULONGINT=264;
public final static short DOUBLE=265;
public final static short LOOP=266;
public final static short UNTIL=267;
public final static short ID=268;
public final static short CONSTANT=269;
public final static short CSTRING=270;
public final static short LESSEQUAL=271;
public final static short GREATEQUAL=272;
public final static short EQUAL=273;
public final static short NEQUAL=274;
public final static short PROC=275;
public final static short NA=276;
public final static short SHADOWING=277;
public final static short TRUE=278;
public final static short FALSE=279;
public final static short UP=280;
public final static short DOWN=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    2,    2,    3,    3,    3,    3,
    6,    6,    5,    5,    8,    8,    7,    7,    9,    9,
    9,   10,   11,   11,   11,   12,   12,    4,    4,    4,
    4,    4,   17,   17,   17,   17,   17,   17,   18,   19,
   19,   14,   14,   15,   16,   13,   13,   13,   20,   20,
   20,   21,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    3,    1,    1,    1,    2,    1,    1,    1,
    1,    3,    1,    1,    1,    1,   14,    7,    1,    3,
    5,    2,    1,    3,    5,    4,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    8,    6,    6,    4,    3,    3,    1,    3,    3,
    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    4,    0,    0,   13,   14,    0,    0,    0,    0,    1,
    0,    5,    6,    0,    9,    8,   31,   29,   30,   32,
    0,    0,    0,   41,    0,    0,    0,    0,    0,    0,
    0,    7,   52,   53,    0,    0,    0,    0,   51,    0,
    0,    0,   12,    0,   27,    0,    0,    0,    3,   54,
   35,   36,   33,   34,   37,   38,    0,    0,    0,    0,
    0,    0,   45,   40,    0,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,   49,   50,    0,    0,    0,
   22,    0,    0,    0,   43,   44,    0,    0,    0,    0,
    0,   25,   18,    0,    0,   42,    0,   21,    0,    0,
   15,   16,    0,    0,    0,   17,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   14,   15,   16,  103,   70,   71,
   46,   17,   36,   18,   19,   20,   59,   37,   25,   38,
   39,
};
final static short yysindex[] = {                      -203,
    0,   -3,    1,    0,    0, -117,   -5, -225,    0,    0,
  -13,    0,    0, -216,    0,    0,    0,    0,    0,    0,
  -44, -215, -203,    0, -189, -216,  -34,  -44,   24, -203,
   35,    0,    0,    0, -188,  -28,   39,   -2,    0,   41,
  -42,   44,    0,   42,    0,   46,   -1,  -27,    0,    0,
    0,    0,    0,    0,    0,    0,  -44,  -44,  -44, -117,
  -44,  -44,    0,    0,  -44, -183,    0,  -31, -180,   48,
   47,   -2,   -2,   -1, -200,    0,    0,   52,   50, -203,
    0, -181, -197, -117,    0,    0, -172,  -25,   37,   55,
 -159,    0,    0, -167, -197,    0, -174,    0,   43, -208,
    0,    0,  -18, -203,  -19,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  -56,    0,    0,    0,
    0,    0,    0,  -54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   10,
  -56,    0,    0,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,   66,    0,    0,  -51,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   67,  -32,  -12,   68,    0,    0,    0,    0,   69,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   70,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   -7,    6,    0,    0,  -26,   12,    0,    0,    0,  -59,
    0,    0,   -8,    0,    0,    0,    0,   49,  -35,   17,
   15,
};
final static int YYTABLESIZE=262;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   35,   48,   11,   48,   10,   23,   45,   28,   46,    2,
   46,   24,   46,   68,   57,   41,   58,   48,   48,   47,
   48,   69,   49,   90,   75,   32,   46,   46,   47,   46,
   47,   55,   47,   56,   27,   98,   21,   43,   26,   61,
   22,   57,   29,   58,   62,   30,   47,   47,   91,   47,
   74,   31,    1,    2,   40,   28,   69,    3,   84,   85,
    4,    5,    6,   48,    7,   24,    4,    5,   69,  101,
  102,    8,   88,   72,   73,   76,   77,   42,   26,   60,
   50,   63,   64,   65,   79,   66,   67,   81,   82,   24,
   83,   80,   86,   87,   89,   92,  105,   94,   95,   93,
   96,   97,   99,  100,  104,  106,   23,   19,   39,   24,
   20,    0,    0,   78,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    2,    0,    0,    0,    0,    2,
    0,    0,    0,    3,    0,    0,    4,    5,    6,    0,
    7,    0,    0,    0,    0,    0,    0,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   11,   11,   10,   10,    0,   28,   28,    0,
   11,    0,   10,    0,    0,   28,    0,   48,   48,    0,
    0,    0,    0,   33,   34,   48,   46,   46,    0,   48,
   48,   48,   48,   44,   46,    0,    4,    5,   46,   46,
   46,   46,   51,   52,   53,   54,   47,   47,    0,    0,
    0,    0,    0,    0,   47,    0,    0,    0,   47,   47,
   47,   47,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   45,   43,   59,   45,   59,  123,   41,   59,   41,    0,
   43,    6,   45,   41,   43,   23,   45,   59,   60,   28,
   62,   48,   30,   83,   60,   14,   59,   60,   41,   62,
   43,   60,   45,   62,   40,   95,   40,   26,   44,   42,
   40,   43,  268,   45,   47,   59,   59,   60,   84,   62,
   59,  268,  256,  257,  270,   61,   83,  261,  259,  260,
  264,  265,  266,   40,  268,   60,  264,  265,   95,  278,
  279,  275,   80,   57,   58,   61,   62,  267,   44,   41,
  269,   41,  125,   40,  268,   44,   41,  268,   41,   84,
   44,  123,   41,   44,  276,  268,  104,   61,   44,  125,
  260,  269,  277,   61,  123,  125,   41,   41,   41,   41,
   41,   -1,   -1,   65,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,  257,
   -1,   -1,   -1,  261,   -1,   -1,  264,  265,  266,   -1,
  268,   -1,   -1,   -1,   -1,   -1,   -1,  275,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  259,  260,  259,  260,   -1,  259,  260,   -1,
  267,   -1,  267,   -1,   -1,  267,   -1,  259,  260,   -1,
   -1,   -1,   -1,  268,  269,  267,  259,  260,   -1,  271,
  272,  273,  274,  268,  267,   -1,  264,  265,  271,  272,
  273,  274,  271,  272,  273,  274,  259,  260,   -1,   -1,
   -1,   -1,   -1,   -1,  267,   -1,   -1,   -1,  271,  272,
  273,  274,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","THEN","ELSE","END_IF","OUT","FUNC",
"RETURN","ULONGINT","DOUBLE","LOOP","UNTIL","ID","CONSTANT","CSTRING",
"LESSEQUAL","GREATEQUAL","EQUAL","NEQUAL","PROC","NA","SHADOWING","TRUE",
"FALSE","UP","DOWN",
};
final static String yyrule[] = {
"$accept : program",
"program : sentences",
"sentences : sentence ';'",
"sentences : sentence ';' sentences",
"sentences : error",
"sentence : declaration",
"sentence : executable",
"declaration : type variable_list",
"declaration : procedure",
"declaration : variable_list",
"declaration : type",
"variable_list : ID",
"variable_list : ID ',' variable_list",
"type : ULONGINT",
"type : DOUBLE",
"true_false : TRUE",
"true_false : FALSE",
"procedure : PROC ID '(' parameter_list ')' NA '=' CONSTANT SHADOWING '=' true_false '{' sentences '}'",
"procedure : PROC ID '(' ')' '{' sentences '}'",
"parameter_list : parameter",
"parameter_list : parameter ',' parameter",
"parameter_list : parameter ',' parameter ',' parameter",
"parameter : type ID",
"id_list : ID",
"id_list : ID ',' ID",
"id_list : ID ',' ID ',' ID",
"procedure_call : ID '(' id_list ')'",
"procedure_call : ID '(' ')'",
"executable : ID '=' expression",
"executable : if_clause",
"executable : loop_clause",
"executable : procedure_call",
"executable : out_clause",
"comparator : EQUAL",
"comparator : NEQUAL",
"comparator : LESSEQUAL",
"comparator : GREATEQUAL",
"comparator : '<'",
"comparator : '>'",
"condition : expression comparator expression",
"sentence_block : '{' sentences '}'",
"sentence_block : sentence",
"if_clause : IF '(' condition ')' sentence_block ELSE sentence_block END_IF",
"if_clause : IF '(' condition ')' sentence_block END_IF",
"loop_clause : LOOP sentence_block UNTIL '(' condition ')'",
"out_clause : OUT '(' CSTRING ')'",
"expression : expression '+' term",
"expression : expression '-' term",
"expression : term",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : CONSTANT",
"factor : '-' CONSTANT",
};

//#line 134 "specification.y"

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
//#line 363 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 19 "specification.y"
{showMessage( "[Line " + la.getCurrentLine() + "] Program completed!");}
break;
case 4:
//#line 24 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Syntax error: ';' expected but didn't found.");}
break;
case 7:
//#line 32 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Variable declaration found.");}
break;
case 8:
//#line 33 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}
break;
case 9:
//#line 34 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Syntax error: there's no type for the identifier \"" + ((Symbol)val_peek(0).obj).getLexeme()  + "\".");}
break;
case 10:
//#line 35 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Syntax error: identifier expected but didn't found.");}
break;
case 17:
//#line 50 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}
break;
case 18:
//#line 51 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}
break;
case 26:
//#line 67 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure call found.");}
break;
case 27:
//#line 68 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure call found.");}
break;
case 28:
//#line 71 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Assignment found.");}
break;
case 29:
//#line 72 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] If clause found.");}
break;
case 30:
//#line 73 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Loop clause found.");}
break;
case 31:
//#line 74 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Function clause found.");}
break;
case 32:
//#line 75 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Out clause found.");}
break;
case 39:
//#line 86 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condition found.");}
break;
case 46:
//#line 115 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Addition expression found.");}
break;
case 47:
//#line 116 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Subtract expression found.");}
break;
case 48:
//#line 117 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Term found.");}
break;
//#line 588 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
