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

import usefullClassesPackage.Constants;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Math;
import java.math.BigDecimal;
import java.io.*;
import java.util.StringTokenizer;
//#line 27 "Parser.java"




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
    0,    0,    0,    1,    1,    1,    2,    2,    3,    3,
    3,    3,    6,    6,    5,    5,    8,    8,    9,    9,
    7,    7,   10,   10,   10,   10,   11,   11,   11,   12,
   12,   12,   12,   12,   13,   13,    4,    4,    4,    4,
    4,    4,    4,   18,   18,   18,   18,   18,   18,   19,
   20,   20,   20,   15,   15,   15,   15,   15,   15,   15,
   15,   15,   16,   16,   16,   16,   17,   17,   14,   14,
   14,   14,   14,   14,   14,   21,   21,   21,   21,   21,
   21,   21,   22,   22,   22,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    1,    1,    2,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    2,    3,
   14,   13,    1,    3,    5,    7,    2,    1,    1,    1,
    3,    5,    7,    1,    4,    3,    3,    3,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    3,    1,    2,    8,    6,    8,    6,    8,    6,    8,
    8,    6,    6,    3,    6,    6,    4,    4,    3,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    3,    3,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   15,   16,    0,    0,    0,    0,    2,
    0,    7,    8,    0,   11,   10,   42,   40,   41,   43,
    0,    0,    0,    0,    0,   52,    0,    0,    0,    0,
    0,    6,    0,    0,    9,    0,   83,   84,    0,    0,
    0,   78,    0,    0,    0,    0,    0,    0,    0,   53,
    0,   64,    0,   14,   34,    0,   36,    0,    0,    0,
    0,    5,    0,    0,    0,    0,   85,    0,    0,    0,
    0,    0,   46,   47,   44,   45,   48,   49,    0,    0,
   68,   67,    0,   51,    0,    0,   35,   29,    0,    0,
    0,    0,    0,    0,    0,   81,   82,    0,    0,    0,
    0,   79,   76,   80,   77,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    0,    0,    0,   59,    0,
   62,   57,    0,   55,   66,   65,   63,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   58,
   60,   61,   56,   54,    0,    0,    0,    0,   33,    0,
    0,    0,   17,   18,    0,    0,   26,    0,    0,    0,
    0,    0,    0,   22,    0,   20,   21,
};
final static short yydgoto[] = {                          9,
   10,   26,   12,   13,   14,   15,   16,  155,  161,   91,
   92,   58,   17,   44,   18,   19,   20,   79,   45,   27,
   41,   42,
};
final static short yysindex[] = {                      -132,
  -51,   14,   50,    0,    0, -106,  -20, -222,    0,    0,
  -43,    0,    0, -172,    0,    0,    0,    0,    0,    0,
  -31,   -2, -221,  -55,  126,    0, -231, -172,  -34,   27,
   69,    0,  -78,   63,    0,  121,    0,    0, -155,   10,
   21,    0,  355,   37,   90,   96,   97,  101,  -51,    0,
   23,    0,  109,    0,    0,  102,    0,  112,  121,   10,
  -19,    0,   47,   47,    6,    6,    0,   56,   58,   65,
   67,  -84,    0,    0,    0,    0,    0,    0,  -31,  100,
    0,    0,  -31,    0,   72, -114,    0,    0, -120, -111,
  120,  123,   28,   21,   21,    0,    0,   28,   21,   28,
   21,    0,    0,    0,    0, -176,   10,  -53, -133,  124,
  362,  130,  132,  128,    0,  -91, -170,  -84,    0,  -84,
    0,    0,  106,    0,    0,    0,    0,  -76,  -75,  134,
  149,  -64,  -61,  -46, -182,  161,  -69,  -59, -170,    0,
    0,    0,    0,    0,  -52,  156,  -57,  177,    0, -160,
  175, -170,    0,    0,  127, -160,    0,  -78,  129,  184,
  133,  -78,  -78,    0,  138,    0,    0,
};
final static short yyrindex[] = {                       265,
  272,    0,    0,    0,    0,    0,  -58,    0,    0,    0,
    0,    0,    0,  -56,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    5,  -58,    0,    0,    0,    0,    0,   83,
  -41,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  237,    0,    0,   88,   93,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0,  238,    0,  -32,  -12,    0,    0,   -3,   17,   26,
   46,    0,    0,    0,    0,    0,  239,    0,    0,    0,
    0,    0,  240,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  246,    0,    0,    0,    0,  251,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  253,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  176,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    1,   12,    0,    0,  -17,   38,    0,  148,  -42,    0,
  -24,    0,    0,    2,    0,    0,    0,    0,   19,   -7,
   76,  314,
};
final static int YYTABLESIZE=409;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
   13,   71,   12,   71,    4,   21,   57,   21,   74,   21,
   74,   11,   74,   39,   21,   33,   25,   71,   71,   29,
   71,   89,   40,   28,   52,   51,   74,   74,   75,   74,
   75,   60,   75,   62,   46,   53,   11,   72,   25,   72,
   30,   72,   39,   90,   11,   31,   75,   75,   47,   75,
   39,   35,   68,   22,   69,   72,   72,   69,   72,   69,
   28,   69,   70,   28,  106,   54,   73,   71,   73,   65,
   73,   39,  109,  143,   66,   69,   69,  144,   69,   68,
  107,   69,  118,  119,   73,   73,   70,   73,   70,   23,
   70,   39,  131,    4,    5,   34,   77,   88,   78,   90,
   39,  110,   39,  112,   70,   70,   28,   70,   61,   39,
  132,   39,  133,   67,  148,  135,   39,  153,  154,  165,
  166,   90,  122,    1,    2,  123,  124,  157,    3,    4,
   80,    4,    5,    6,   90,    7,   81,   82,   94,   95,
   83,   39,    8,   99,  101,   86,   38,   84,   85,   24,
    2,   37,   87,  113,    3,  114,  115,    4,    5,    6,
  116,    7,   65,   63,  125,   64,  117,   66,    8,  160,
  127,   49,    2,  160,  160,  128,    3,   49,    2,    4,
    5,    6,    3,    7,  130,    4,    5,    6,  129,    7,
    8,  136,  139,  137,  138,  140,    8,   13,  141,   12,
   13,   13,   12,   12,  145,  120,  121,  146,   13,  147,
   12,   48,   32,  142,   71,  149,  150,   71,   71,  151,
  152,   55,   25,   74,   36,   71,   74,   74,   25,   71,
   71,   71,   71,   56,   74,  156,   37,   38,   74,   74,
   74,   74,  163,   75,    4,    5,   75,   75,   88,  158,
   50,  162,   72,   43,   75,   72,   72,  164,   75,   75,
   75,   75,  167,   72,    1,   37,   38,   72,   72,   72,
   72,    3,   69,   37,   38,   69,   69,   30,   23,   50,
   31,   73,   59,   69,   73,   73,   24,   69,   69,   69,
   69,   32,   73,   25,   37,   38,   73,   73,   73,   73,
   19,   70,   93,  159,   70,   70,    0,   73,   74,   75,
   76,   98,   70,  100,   37,   38,   70,   70,   70,   70,
  102,    0,  104,   37,   38,   37,   38,  111,    0,    0,
    0,    0,   37,   38,   37,   38,    0,    0,   39,   37,
   38,   39,   39,   38,    0,    0,   38,   38,   37,   39,
    0,   37,   37,    0,   38,  108,    2,    0,    0,   37,
    3,  134,    2,    4,    5,    6,    3,    7,    0,    4,
    5,    6,    0,    7,    8,    0,    0,    0,   96,   97,
    8,   49,    2,  103,  105,    0,    3,    0,    0,    4,
    5,    6,    0,    7,    0,   72,   65,   63,    0,   64,
    8,   66,  126,   65,   63,    0,   64,    0,   66,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   59,   43,   59,   45,    0,   61,   41,   61,   41,   61,
   43,    0,   45,   45,   61,   59,  123,   59,   60,   40,
   62,   41,   21,   44,  256,   25,   59,   60,   41,   62,
   43,   30,   45,   33,  256,  267,   25,   41,  123,   43,
   61,   45,   45,   61,   33,  268,   59,   60,  270,   62,
   45,   14,   43,   40,   45,   59,   60,   41,   62,   43,
   41,   45,   42,   44,   72,   28,   41,   47,   43,   42,
   45,   45,   80,  256,   47,   59,   60,  260,   62,   43,
   79,   45,  259,  260,   59,   60,   41,   62,   43,   40,
   45,   45,  117,  264,  265,  268,   60,  268,   62,  117,
   45,   83,   45,   85,   59,   60,   44,   62,   40,   45,
  118,   45,  120,  269,  139,  123,   45,  278,  279,  162,
  163,  139,  256,  256,  257,  259,  260,  152,  261,  125,
   41,  264,  265,  266,  152,  268,   41,   41,   63,   64,
   40,   59,  275,   68,   69,   44,   59,  125,   40,  256,
  257,   59,   41,  268,  261,  276,  268,  264,  265,  266,
   41,  268,   42,   43,   41,   45,   44,   47,  275,  158,
   41,  256,  257,  162,  163,   44,  261,  256,  257,  264,
  265,  266,  261,  268,  276,  264,  265,  266,   61,  268,
  275,  268,   44,  269,   61,  260,  275,  256,  260,  256,
  259,  260,  259,  260,   44,  259,  260,  277,  267,  269,
  267,  267,  256,  260,  256,  268,   61,  259,  260,  277,
   44,  256,  123,  256,  256,  267,  259,  260,  123,  271,
  272,  273,  274,  268,  267,   61,  268,  269,  271,  272,
  273,  274,   59,  256,  264,  265,  259,  260,  268,  123,
  125,  123,  256,  256,  267,  259,  260,  125,  271,  272,
  273,  274,  125,  267,    0,  268,  269,  271,  272,  273,
  274,    0,  256,  268,  269,  259,  260,   41,   41,   41,
   41,  256,  256,  267,  259,  260,   41,  271,  272,  273,
  274,   41,  267,   41,  268,  269,  271,  272,  273,  274,
  125,  256,  256,  156,  259,  260,   -1,  271,  272,  273,
  274,  256,  267,  256,  268,  269,  271,  272,  273,  274,
  256,   -1,  256,  268,  269,  268,  269,  256,   -1,   -1,
   -1,   -1,  268,  269,  268,  269,   -1,   -1,  256,  268,
  269,  259,  260,  256,   -1,   -1,  259,  260,  256,  267,
   -1,  259,  260,   -1,  267,  256,  257,   -1,   -1,  267,
  261,  256,  257,  264,  265,  266,  261,  268,   -1,  264,
  265,  266,   -1,  268,  275,   -1,   -1,   -1,   65,   66,
  275,  256,  257,   70,   71,   -1,  261,   -1,   -1,  264,
  265,  266,   -1,  268,   -1,   41,   42,   43,   -1,   45,
  275,   47,   41,   42,   43,   -1,   45,   -1,   47,
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
"program :",
"program : sentences",
"program : error",
"sentences : sentence ';'",
"sentences : sentence ';' sentences",
"sentences : sentence error",
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
"sentences_proc : sentence ';'",
"sentences_proc : sentence ';' sentences_proc",
"procedure : PROC ID '(' parameter_list ')' NA '=' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"procedure : PROC ID '(' ')' NA '=' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"parameter_list : parameter",
"parameter_list : parameter ',' parameter",
"parameter_list : parameter ',' parameter ',' parameter",
"parameter_list : parameter ',' parameter ',' parameter ',' parameter",
"parameter : type ID",
"parameter : type",
"parameter : ID",
"id_list : ID",
"id_list : ID ',' ID",
"id_list : ID ',' ID ',' ID",
"id_list : ID ',' ID ',' ID ',' ID",
"id_list : error",
"procedure_call : ID '(' id_list ')'",
"procedure_call : ID '(' ')'",
"executable : ID '=' expression",
"executable : ID '=' error",
"executable : error '=' expression",
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
"sentence_block : '{' '}'",
"if_clause : IF '(' condition ')' sentence_block ELSE sentence_block END_IF",
"if_clause : IF '(' condition ')' sentence_block END_IF",
"if_clause : IF '(' condition ')' sentence_block ELSE sentence_block error",
"if_clause : IF '(' condition ')' sentence_block error",
"if_clause : IF '(' error ')' sentence_block ELSE sentence_block END_IF",
"if_clause : IF '(' error ')' sentence_block END_IF",
"if_clause : IF '(' condition ')' error ELSE sentence_block END_IF",
"if_clause : IF '(' condition ')' sentence_block ELSE error END_IF",
"if_clause : IF '(' condition ')' error END_IF",
"loop_clause : LOOP sentence_block UNTIL '(' condition ')'",
"loop_clause : LOOP sentence_block error",
"loop_clause : LOOP sentence_block UNTIL '(' error ')'",
"loop_clause : LOOP error UNTIL '(' condition ')'",
"out_clause : OUT '(' CSTRING ')'",
"out_clause : OUT '(' error ')'",
"expression : expression '+' term",
"expression : expression '-' term",
"expression : term",
"expression : expression '+' error",
"expression : expression '-' error",
"expression : error '+' term",
"expression : error '-' term",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"term : term '*' error",
"term : term '/' error",
"term : error '*' factor",
"term : error '/' factor",
"factor : ID",
"factor : CONSTANT",
"factor : '-' CONSTANT",
};

//#line 180 "specification.y"

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
//#line 449 "Parser.java"
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
//#line 21 "specification.y"
{showMessage( "[Line " + la.getCurrentLine() + "] WARNING sintáctico: Programa vacío!");}
break;
case 2:
//#line 22 "specification.y"
{showMessage( "[Line " + la.getCurrentLine() + "] Programa completo.");}
break;
case 3:
//#line 23 "specification.y"
{showMessage( "[Line " + la.getCurrentLine() + "] ERROR sintáctico: no se encontraron sentencias válidas.");}
break;
case 6:
//#line 28 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: ';' ausente al final de la sentencia.");}
break;
case 9:
//#line 36 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaración de variable.");}
break;
case 10:
//#line 37 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaración PROC.");}
break;
case 11:
//#line 38 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 12:
//#line 39 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un identificador y no se encontró.");}
break;
case 21:
//#line 57 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}
break;
case 22:
//#line 58 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedure declaration found.");}
break;
case 26:
//#line 64 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: un procedimiento puede recibir un máximo de trés parametros.");}
break;
case 28:
//#line 68 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta identificador en declaración de parámetro.");}
break;
case 29:
//#line 69 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta tipo en declaración de parámetro.");}
break;
case 33:
//#line 75 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: un procedimiento puede recibir un máximo de trés parametros.");}
break;
case 34:
//#line 76 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 37:
//#line 83 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignación.");}
break;
case 38:
//#line 84 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: asignación errónea. Se espera una expresión del lado derecho.");}
break;
case 39:
//#line 85 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: asignación errónea. Se espera un identificador del lado izquierdo.");}
break;
case 40:
//#line 86 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 41:
//#line 87 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 42:
//#line 88 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocación PROC.");}
break;
case 43:
//#line 89 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 50:
//#line 100 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condición.");}
break;
case 56:
//#line 114 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 57:
//#line 115 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 58:
//#line 116 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: luego de la palabra reservada IF se espera una condición entre paréntesis.");}
break;
case 59:
//#line 117 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: luego de la palabra reservada IF se espera una condición entre paréntesis.");}
break;
case 60:
//#line 118 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 61:
//#line 119 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 62:
//#line 120 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 64:
//#line 124 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: falta la cláusula UNTIL en la sentencia LOOP");}
break;
case 65:
//#line 125 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la cláusula UNTIL debe incluir una condición entre paréntesis");}
break;
case 66:
//#line 126 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 68:
//#line 134 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 69:
//#line 141 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 70:
//#line 142 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 71:
//#line 143 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Término.");}
break;
case 72:
//#line 144 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la suma debe contener un término válido.");}
break;
case 73:
//#line 145 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la resta debe contener un término válido.");}
break;
case 74:
//#line 146 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la suma debe contener una expresión válida.");}
break;
case 75:
//#line 147 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la resta debe contener una expresión válida.");}
break;
case 76:
//#line 150 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicación.");}
break;
case 77:
//#line 151 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] División.");}
break;
case 78:
//#line 152 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 79:
//#line 153 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la múltiplicación debe llevar una constante o un identificador");}
break;
case 80:
//#line 154 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado derecho de la división debe llevar una constante o un identificador");}
break;
case 81:
//#line 155 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la múltiplicación debe llevar una término o un factor");}
break;
case 82:
//#line 156 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: el lado izquierdo de la división debe llevar un término o un factor");}
break;
case 85:
//#line 162 "specification.y"
{
	    				 Symbol symbol = la.symbolsTable.getSymbol(val_peek(0).sval);
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
break;
//#line 805 "Parser.java"
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
