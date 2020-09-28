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

import usefulClassesPackage.Constants;
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
    7,    7,    7,    7,   10,   10,   10,   10,   11,   11,
   11,   12,   12,   12,   12,   12,   13,   13,    4,    4,
    4,    4,    4,    4,    4,    4,   18,   18,   18,   18,
   18,   18,   19,   19,   20,   20,   20,   15,   15,   15,
   15,   15,   15,   15,   15,   15,   16,   16,   16,   16,
   17,   17,   14,   14,   14,   14,   14,   14,   14,   21,
   21,   21,   21,   21,   21,   21,   22,   22,   22,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    1,    1,    2,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    2,    3,
   14,   13,   15,   14,    1,    3,    5,    7,    2,    1,
    1,    1,    3,    5,    7,    1,    4,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    3,    1,    2,    8,    6,    8,
    6,    8,    6,    8,    8,    6,    6,    3,    6,    6,
    4,    4,    3,    3,    1,    3,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   15,   16,    0,    0,    0,    0,    0,
    0,    7,    8,    0,   11,   10,   45,   43,   44,   46,
    0,    0,    0,    0,    0,   56,    0,    0,    0,    0,
    0,    0,    0,    0,    6,    4,    0,    9,    0,   87,
   88,    0,    0,    0,   82,    0,    0,    0,    0,    0,
    0,   57,    0,   68,    0,    0,   14,   36,    0,   38,
    0,    0,    0,    0,    5,    0,    0,    0,    0,   89,
    0,    0,    0,    0,    0,   49,   50,   47,   48,    0,
   51,   52,    0,    0,   72,   71,    0,   55,    0,    0,
   37,   31,    0,    0,    0,    0,    0,    0,    0,   85,
   86,    0,    0,    0,    0,   83,   80,   84,   81,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   29,
    0,    0,    0,   63,    0,   66,   61,    0,   59,   70,
   69,   67,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   62,   64,   65,   60,   58,
    0,    0,    0,    0,    0,    0,   35,    0,    0,    0,
    0,    0,   17,   18,    0,    0,    0,    0,   28,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   22,
    0,    0,    0,   20,   24,   21,    0,   23,
};
final static short yydgoto[] = {                          9,
   10,   26,   12,   13,   14,   15,   16,  165,  175,   95,
   96,   61,   17,   47,   18,   19,   20,   83,   48,   27,
   44,   45,
};
final static short yysindex[] = {                      -126,
  -44,    7,   17,    0,    0,  -98,  -39, -196,    0,  136,
  -36,    0,    0, -172,    0,    0,    0,    0,    0,    0,
   31,   49, -186,  -55,  142,    0, -222,   31, -172,  -31,
   53,   46,  -44,   48,    0,    0,   84,    0,  399,    0,
    0, -152,   22,   68,    0,  386,   42,   81,   95,  103,
  129,    0,  157,    0,  135,   22,    0,    0,  102,    0,
  137,  399,   22,  -17,    0,   64,   64,   10,   10,    0,
   71,   73,   75,   78,  -85,    0,    0,    0,    0,   31,
    0,    0,   31,  115,    0,    0,   31,    0,   80, -149,
    0,    0,  -94,  -84,  144,  143,   79,   68,   68,    0,
    0,   79,   68,   79,   68,    0,    0,    0,    0,  -86,
   22,   22,  -46, -182,  147,  393,  148,  150,  130,    0,
  -78, -131,  -85,    0,  -85,    0,    0,  121,    0,    0,
    0,    0,  -61,  -33,  149,  173,  -38,  -37,  -54,  -96,
  177,  -48,  -26,  -13, -131,    0,    0,    0,    0,    0,
  -23,  191,  -24,   -3,  -15,  216,    0, -117,  214,  222,
    8, -131,    0,    0,  161, -117, -117,  230,    0,  136,
  175,  183, -117,  237,  182,  136,  136,  187,  136,    0,
  203,  205,  136,    0,    0,    0,  210,    0,
};
final static short yyrindex[] = {                       337,
  338,    0,    0,    0,    0,    0,  -56,    0,    0,  345,
    0,    0,    0,  -51,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -56,    0,    0,    0,
    0,    0,    9,  -41,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   94,    0,    0,  310,    0,
    0,   96,  106,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -5,    0,  316,    0,  -32,  -10,    0,
    0,   -1,   21,   30,   52,    0,    0,    0,    0,    0,
  317,  318,    0,    0,    0,    0,    0,  319,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  323,    0,    0,    0,    0,
  326,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  327,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  244,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  349,   16,    0,    0,  -21,   34,    0,  -67,  -29,    0,
 -108,    0,    0,   25,    0,    0,    0,    0,   56,    4,
   85,  128,
};
final static int YYTABLESIZE=446;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
   30,   75,   13,   75,   29,   21,   21,   12,   79,   60,
   79,  143,   79,  136,   21,   11,   21,   75,   75,   75,
   75,   31,   36,   93,   25,   34,   79,   79,   79,   79,
   78,  155,   78,   54,   78,   30,  156,   25,   30,   77,
   11,   77,   94,   77,   55,   43,   22,   38,   78,   78,
   78,   78,   56,  169,   42,   63,   23,   77,   77,   77,
   77,   74,   57,   74,   72,   74,   71,   41,   34,   49,
   76,   32,   76,  127,   76,   42,  128,  129,  110,   74,
   74,   74,   74,   50,   72,   64,   71,  114,   76,   76,
   76,   76,   73,   42,   73,   37,   73,   42,  171,  172,
   94,   81,   80,   82,  111,  178,   65,  112,   42,   73,
   73,   73,   73,   73,   74,   42,   70,   42,  118,   42,
   68,   84,   42,   94,   42,   69,  137,   29,  138,    1,
    2,  140,    4,    5,    3,   85,   92,    4,    5,    6,
   94,    7,  115,   86,  117,   90,  181,  182,    8,  184,
   98,   99,   42,  187,   40,  103,  105,   24,    2,  149,
  163,  164,    3,  150,   39,    4,    5,    6,   87,    7,
   33,    2,  123,  124,   89,    3,    8,   91,    4,    5,
    6,  119,    7,  120,  121,  174,  122,  130,  132,    8,
  134,  174,  174,  133,  174,  100,  101,  135,  174,   13,
  107,  109,   13,   13,   12,  148,  141,   12,   12,  144,
   13,   51,  125,  126,   75,   12,  145,   75,   75,   35,
  151,  146,  147,   79,   58,   75,   79,   79,  152,   75,
   75,   75,   75,   28,   79,  142,   59,   25,   79,   79,
   79,   79,  153,   25,  157,   78,    4,    5,   78,   78,
   92,  158,  159,  161,   77,  154,   78,   77,   77,  162,
   78,   78,   78,   78,   41,   77,   52,   41,   41,   77,
   77,   77,   77,  160,  166,   41,   74,   40,   41,   74,
   74,   88,  167,  170,  168,   76,   39,   74,   76,   76,
  173,   74,   74,   74,   74,  179,   76,  176,   40,   41,
   76,   76,   76,   76,   46,  177,  180,   73,   62,  183,
   73,   73,   76,   77,   78,   79,   40,   41,   73,   97,
   40,   41,   73,   73,   73,   73,  102,  185,  104,  186,
  106,   40,   41,  108,  188,  116,    1,    3,   40,   41,
   40,   41,   40,   41,    2,   40,   41,   40,   41,   42,
   32,   40,   42,   42,   40,   40,   25,   54,   53,   33,
   42,   39,   40,   26,   39,   39,   34,   27,   19,    0,
  113,    2,   39,   53,    0,    3,  139,    2,    4,    5,
    6,    3,    7,    0,    4,    5,    6,    0,    7,    8,
    0,   33,    2,    0,    0,    8,    3,   33,    2,    4,
    5,    6,    3,    7,    0,    4,    5,    6,    0,    7,
    8,    0,   33,    2,    0,    0,    8,    3,    0,    0,
    4,    5,    6,    0,    7,    0,   75,   68,   67,    0,
   66,    8,   69,  131,   68,   67,    0,   66,    0,   69,
   68,   67,    0,   66,    0,   69,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   59,   45,   44,   61,   61,   59,   41,   41,
   43,   45,   45,  122,   61,    0,   61,   59,   60,   61,
   62,   61,   59,   41,  123,   10,   59,   60,   61,   62,
   41,   45,   43,  256,   45,   41,  145,  123,   44,   41,
   25,   43,   64,   45,  267,   21,   40,   14,   59,   60,
   61,   62,   28,  162,   45,   31,   40,   59,   60,   61,
   62,   41,   29,   43,   43,   45,   45,   59,   53,  256,
   41,  268,   43,  256,   45,   45,  259,  260,   75,   59,
   60,   61,   62,  270,   43,   40,   45,   84,   59,   60,
   61,   62,   41,   45,   43,  268,   45,   45,  166,  167,
  122,   60,   61,   62,   80,  173,   59,   83,   45,   42,
   59,   60,   61,   62,   47,   45,  269,   45,  268,   45,
   42,   41,   45,  145,   45,   47,  123,   44,  125,  256,
  257,  128,  264,  265,  261,   41,  268,  264,  265,  266,
  162,  268,   87,   41,   89,   44,  176,  177,  275,  179,
   66,   67,   59,  183,   59,   71,   72,  256,  257,  256,
  278,  279,  261,  260,   59,  264,  265,  266,   40,  268,
  256,  257,  259,  260,   40,  261,  275,   41,  264,  265,
  266,  276,  268,  268,   41,  170,   44,   41,   41,  275,
   61,  176,  177,   44,  179,   68,   69,  276,  183,  256,
   73,   74,  259,  260,  256,  260,  268,  259,  260,   61,
  267,  267,  259,  260,  256,  267,   44,  259,  260,  256,
   44,  260,  260,  256,  256,  267,  259,  260,  277,  271,
  272,  273,  274,  273,  267,  269,  268,  123,  271,  272,
  273,  274,  269,  123,  268,  256,  264,  265,  259,  260,
  268,   61,  277,  269,  256,  269,  267,  259,  260,   44,
  271,  272,  273,  274,  256,  267,  125,  259,  260,  271,
  272,  273,  274,  277,   61,  267,  256,  268,  269,  259,
  260,  125,   61,  123,  277,  256,  256,  267,  259,  260,
   61,  271,  272,  273,  274,   59,  267,  123,  268,  269,
  271,  272,  273,  274,  256,  123,  125,  256,  256,  123,
  259,  260,  271,  272,  273,  274,  268,  269,  267,  256,
  268,  269,  271,  272,  273,  274,  256,  125,  256,  125,
  256,  268,  269,  256,  125,  256,    0,    0,  268,  269,
  268,  269,  268,  269,    0,  268,  269,  268,  269,  256,
   41,  256,  259,  260,  259,  260,   41,   41,   41,   41,
  267,  256,  267,   41,  259,  260,   41,   41,  125,   -1,
  256,  257,  267,   25,   -1,  261,  256,  257,  264,  265,
  266,  261,  268,   -1,  264,  265,  266,   -1,  268,  275,
   -1,  256,  257,   -1,   -1,  275,  261,  256,  257,  264,
  265,  266,  261,  268,   -1,  264,  265,  266,   -1,  268,
  275,   -1,  256,  257,   -1,   -1,  275,  261,   -1,   -1,
  264,  265,  266,   -1,  268,   -1,   41,   42,   43,   -1,
   45,  275,   47,   41,   42,   43,   -1,   45,   -1,   47,
   42,   43,   -1,   45,   -1,   47,
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
"sentences : sentences sentence ';'",
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
"procedure : PROC ID '(' parameter_list ')' NA '=' '-' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"procedure : PROC ID '(' ')' NA '=' '-' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
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
"executable : ID EQUAL expression",
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
"condition : expression '=' expression",
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

//#line 193 "specification.y"

public LexicalAnalyzer la;

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
//#line 467 "Parser.java"
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
{showMessage( "[Line " + la.getCurrentLine() + "] WARNING sintactico: Programa vacio!");}
break;
case 2:
//#line 22 "specification.y"
{showMessage( "[Line " + la.getCurrentLine() + "] Programa completo.");}
break;
case 3:
//#line 23 "specification.y"
{showMessage( "[Line " + la.getCurrentLine() + "] ERROR sintactico: no se encontraron sentencias validas.");}
break;
case 6:
//#line 28 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: ';' ausente al final de la sentencia.");}
break;
case 9:
//#line 36 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaracion de variable.");}
break;
case 10:
//#line 37 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaracion PROC.");}
break;
case 11:
//#line 38 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 12:
//#line 39 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
break;
case 21:
//#line 57 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 22:
//#line 58 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 23:
//#line 59 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 24:
//#line 60 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 28:
//#line 66 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
break;
case 30:
//#line 70 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 31:
//#line 71 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 35:
//#line 77 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 36:
//#line 78 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 39:
//#line 85 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
break;
case 40:
//#line 86 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 41:
//#line 87 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 42:
//#line 88 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 43:
//#line 89 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 44:
//#line 90 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 45:
//#line 91 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 46:
//#line 92 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 53:
//#line 103 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
break;
case 54:
//#line 104 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 60:
//#line 118 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 61:
//#line 119 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 62:
//#line 120 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 63:
//#line 121 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 64:
//#line 122 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 65:
//#line 123 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 66:
//#line 124 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 68:
//#line 128 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 69:
//#line 129 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
break;
case 70:
//#line 130 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 72:
//#line 138 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 73:
//#line 145 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 74:
//#line 146 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 75:
//#line 147 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
break;
case 76:
//#line 148 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 77:
//#line 149 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 78:
//#line 150 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 79:
//#line 151 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 80:
//#line 154 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 81:
//#line 155 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Division.");}
break;
case 82:
//#line 156 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 83:
//#line 157 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 84:
//#line 158 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 85:
//#line 159 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 86:
//#line 160 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 89:
//#line 166 "specification.y"
{
							/* Manejo la entrada positiva de esta constante		    				*/
		    				 Symbol positivo = la.symbolsTable.getSymbol(val_peek(0).sval);
		    				 if(positivo.removeRef() == 0){ /* Remove reference and if it reaches 0, remove SyboleTable entry*/
		    				 	la.symbolsTable.removeSymbol(positivo.getLexeme());
		    				 }
		    				 
		    				 /* TODO: QUE HACER CON - 4_ul ??????*/
		    				 
		    				 /* Creo nueva entrada o actualizo la existente con una referencia*/
		    				 Symbol negativo = la.symbolsTable.getSymbol("-"+val_peek(0).sval);
		    				 if (negativo != null){
		    				 	negativo.addRef();  /* Ya existe la entrada*/
		    				 }else{
		    				 	String lexema = "-"+positivo.getLexeme();
		    				 	Symbol nuevoNegativo = new Symbol(lexema,la.getCurrentLine(),positivo.getType());
		    				 	la.symbolsTable.addSymbol(lexema,nuevoNegativo);
		    				 }
	    				 	val_peek(0).sval = "-"+val_peek(0).sval;
	    				 		
	    				 }
break;
//#line 848 "Parser.java"
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
