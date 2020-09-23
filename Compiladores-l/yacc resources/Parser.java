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
    0,    1,    1,    2,    2,    3,    3,    6,    6,    5,
    5,    8,    8,    7,    7,    9,    9,    9,   10,   11,
   11,   11,   12,   12,    4,    4,    4,    4,   16,   16,
   16,   16,   16,   16,   17,   18,   18,   14,   14,   15,
   19,   13,   13,   13,   13,   20,   20,   20,   21,   21,
};
final static short yylen[] = {                            2,
    1,    1,    3,    1,    1,    2,    1,    1,    3,    1,
    1,    1,    1,   14,    7,    1,    3,    5,    2,    1,
    3,    5,    4,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    1,    8,    6,    6,
    4,    3,    3,    1,    2,    3,    3,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   10,   11,    0,    0,    0,    0,    1,    0,    4,
    5,    0,    7,   28,   26,   27,    0,    0,   37,    0,
    0,    0,    0,    0,    0,    6,   49,   50,    0,    0,
    0,    0,   48,    0,    0,    0,   24,    0,    0,    0,
    3,    0,    0,   31,   32,   29,   30,   33,   34,    0,
    0,    0,    0,    0,    0,   36,    0,    0,   23,    0,
    0,    0,    0,    9,    0,    0,    0,    0,   46,   47,
    0,    0,    0,   19,    0,    0,    0,   39,   40,    0,
    0,    0,    0,    0,   22,   15,    0,    0,   38,    0,
   18,    0,    0,   12,   13,    0,    0,    0,   14,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   26,   13,   96,   62,   63,
   38,   14,   30,   15,   16,   52,   31,   20,    0,   32,
   33,
};
final static short yysindex[] = {                      -171,
  -27,    0,    0, -119,  -28, -245,    0,    0,  -32,    0,
    0, -223,    0,    0,    0,    0,  -43, -171,    0, -224,
  -41,  -43,   12, -171,   14,    0,    0,    0,  -43,  -40,
   13,  -26,    0,  -69,   22,   23,    0,   27,  -19,  -35,
    0, -223,  -19,    0,    0,    0,    0,    0,    0, -237,
 -237,  -43, -119, -237, -237,    0,  -43, -194,    0,  -47,
 -191,   38,   40,    0,  -26,  -26,  -19, -225,    0,    0,
   41,   47, -171,    0, -193, -228, -119,    0,    0, -183,
  -25,   35,   58, -157,    0,    0, -164, -228,    0, -170,
    0,   45, -238,    0,    0,  -15, -171,  -16,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    7,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   19,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,   69,    0,    0,   28,    0,
    0,    0,   39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   70,    0,   10,   30,   71,    0,    0,    0,
    0,   72,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   73,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   -9,    4,    0,    0,  -29,   74,    0,    0,    0,  -59,
    0,    0,   -4,    0,    0,    0,   60,  -39,    0,   -1,
   11,
};
final static int YYTABLESIZE=313;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   44,   29,   50,   18,   51,   60,    2,   19,   34,   42,
   61,   21,   17,   68,   41,   54,   83,   39,    8,   48,
   55,   49,   23,   50,   43,   51,   24,   25,   91,   43,
   27,   28,   22,   77,   78,    2,    3,   84,   45,   94,
   95,   44,   35,   44,   25,   44,   61,   67,   65,   66,
   42,   40,   42,   53,   42,   56,   19,   42,   61,   44,
   44,   57,   44,   81,   69,   70,   58,   59,   42,   42,
   43,   42,   43,   72,   43,   73,   74,    8,   75,   45,
   19,   79,   82,   76,   85,    1,   25,   98,   43,   43,
   80,   43,    2,    3,    4,   87,    5,   45,   45,   86,
   45,   88,   89,    6,   90,   93,   92,   97,   99,   20,
   16,   35,   21,   17,    0,   64,   71,    0,    0,    0,
    0,    0,    0,    0,    0,   44,    0,    0,    0,    0,
    0,    2,    0,    0,   42,    0,    0,    1,    0,    0,
    0,    0,    0,    8,    2,    3,    4,    0,    5,    0,
    0,    0,   25,    0,   43,    6,    0,    0,    0,    0,
    0,    0,    0,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,   28,   36,    0,    2,    3,
   44,   45,   46,   47,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   44,
   44,    0,    0,    0,    0,    0,    0,   44,   42,   42,
    0,   44,   44,   44,   44,    0,   42,    8,    8,    0,
   42,   42,   42,   42,    0,    8,   25,   25,   43,   43,
    0,    0,    0,    0,   25,    0,   43,   45,   45,    0,
   43,   43,   43,   43,    0,   45,    0,    0,    0,   45,
   45,   45,   45,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   45,   43,  123,   45,   41,    0,    4,   18,    0,
   40,   40,   40,   53,   24,   42,   76,   22,    0,   60,
   47,   62,  268,   43,   29,   45,   59,    0,   88,    0,
  268,  269,   61,  259,  260,  264,  265,   77,    0,  278,
  279,   41,  267,   43,  268,   45,   76,   52,   50,   51,
   41,   40,   43,   41,   45,  125,   53,   44,   88,   59,
   60,   40,   62,   73,   54,   55,   44,   41,   59,   60,
   41,   62,   43,  268,   45,  123,  268,   59,   41,   41,
   77,   41,  276,   44,  268,  257,   59,   97,   59,   60,
   44,   62,  264,  265,  266,   61,  268,   59,   60,  125,
   62,   44,  260,  275,  269,   61,  277,  123,  125,   41,
   41,   41,   41,   41,   -1,   42,   57,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,
   -1,  125,   -1,   -1,  125,   -1,   -1,  257,   -1,   -1,
   -1,   -1,   -1,  125,  264,  265,  266,   -1,  268,   -1,
   -1,   -1,  125,   -1,  125,  275,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  268,  269,  268,   -1,  264,  265,
  271,  272,  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  259,
  260,   -1,   -1,   -1,   -1,   -1,   -1,  267,  259,  260,
   -1,  271,  272,  273,  274,   -1,  267,  259,  260,   -1,
  271,  272,  273,  274,   -1,  267,  259,  260,  259,  260,
   -1,   -1,   -1,   -1,  267,   -1,  267,  259,  260,   -1,
  271,  272,  273,  274,   -1,  267,   -1,   -1,   -1,  271,
  272,  273,  274,
};
}
final static short YYFINAL=7;
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
"sentences : sentence",
"sentences : sentence ';' sentences",
"sentence : declaration",
"sentence : executable",
"declaration : type variable_list",
"declaration : procedure",
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
"function_call : ID '(' id_list ')'",
"function_call : ID '(' ')'",
"executable : ID '=' expression",
"executable : if_clause",
"executable : loop_clause",
"executable : function_call",
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
"expression : '-' expression",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : CONSTANT",
};

//#line 115 "C:\Users\Thomas\git\Compiladores-I\Compiladores-l\src\specification.y"
⠀⠀⠀⠀⠀
//#line 328 "Parser.java"
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
