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
    7,    7,    7,    7,   11,   11,   11,   11,   11,   11,
   12,   12,   12,   10,   10,   10,   10,   13,   13,   13,
   14,   14,   14,   14,   14,   15,   15,    4,    4,    4,
    4,    4,    4,    4,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   21,   21,   22,   22,
   22,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   18,   18,   18,   18,   18,   19,   19,   19,
   16,   16,   16,   16,   16,   16,   16,   16,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   24,   24,   24,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    1,    1,    2,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    2,    3,
    7,    6,    6,    7,    6,    5,    5,    5,    5,    5,
    3,    3,    2,    1,    3,    5,    7,    2,    1,    1,
    1,    3,    5,    7,    1,    4,    3,    3,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    2,    2,    2,    3,    3,    3,    2,
    2,    8,    6,    8,    6,    8,    6,    8,    8,    6,
    6,    4,    6,    3,    6,    6,    4,    4,    4,    2,
    3,    3,    1,    3,    3,    3,    3,    4,    3,    3,
    1,    3,    3,    3,    3,    4,    4,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   15,   16,    0,    0,    0,    0,    0,
    0,    7,    8,    0,   11,   10,   53,   51,   52,   54,
    6,    0,    0,  108,  109,    0,    0,    0,    0,    0,
  101,   90,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    0,    9,    0,    0,    0,
    0,    0,    0,    0,  110,    0,    0,   55,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   71,    0,   70,   84,    0,    0,   14,   45,
    0,   47,    0,    0,    0,    5,    0,    0,    0,  104,
  105,    0,    0,   63,   64,   65,    0,   61,   66,   62,
    0,    0,    0,    0,    0,    0,    0,   82,  102,    0,
   99,  103,    0,  100,   89,   88,    0,   69,    0,   87,
    0,   46,    0,   40,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  106,  107,    0,    0,    0,    0,    0,
    0,    0,    0,   38,    0,    0,    0,   77,    0,   80,
   75,    0,   73,   81,   86,   85,   83,    0,    0,    0,
    0,    0,    0,   23,    0,    0,    0,    0,    0,    0,
    0,   24,    0,    0,    0,    0,    0,   33,    0,    0,
   21,    0,   76,   78,   79,   74,   72,    0,    0,    0,
    0,    0,    0,   32,    0,   31,    0,   44,   17,   18,
   27,    0,   30,   29,   28,   26,   20,    0,   25,   37,
};
final static short yydgoto[] = {                          9,
   10,   36,   12,   13,   14,   15,   16,  201,  180,  127,
  143,  164,  128,   83,   17,   28,   18,   19,   20,   65,
   29,   37,   30,   31,
};
final static short yysindex[] = {                       313,
   36,  -28,  -35,    0,    0,  222,  -34, -223,    0,  319,
   22,    0,    0, -199,    0,    0,    0,    0,    0,    0,
    0,   82,   74,    0,    0,  104, -178,  157,  235,  -11,
    0,    0, -240,  -60,  264,   54, -207,   82, -199,   38,
   82,  103,   84,   88,    0,  115,    0,   75,  126,  126,
   11,   11,   99,  128,    0,  118,  121,    0,  122,   82,
   73,  125,   60,  176,   82,  -98,   16,  166,  147,  149,
  153,   36,    0,  292,    0,    0,   -6,   75,    0,    0,
  151,    0,  155,   75,  156,    0,   10,  -11,  -11,    0,
    0,  235,  248,    0,    0,    0,   75,    0,    0,    0,
   10,  126,  -11,   10,  -11,   75,  235,    0,    0,   11,
    0,    0,   11,    0,    0,    0,   82,    0,  181,    0,
  -70,    0,  158,    0,  -54,  -65,  160,  161,  -96,    6,
 -227,  -11,  -56,    0,    0,  165,  499,  171,  226,  -54,
   -8,   14,  164,    0,  -54,  -79,  235,    0,  235,    0,
    0,  270,    0,    0,    0,    0,    0,   20,  164,   18,
 -218,   24,  298,    0,  164,  258,   32,   45,  -52, -180,
  263,    0,  249,  -33,  253,  256,  -53,    0,  250,  199,
    0,  -79,    0,    0,    0,    0,    0, -233, -154,  119,
 -154, -154, -154,    0,  319,    0,  279,    0,    0,    0,
    0, -154,    0,    0,    0,    0,    0,  -79,    0,    0,
};
final static short yyrindex[] = {                       327,
  331,    0,    0,    0,    0,    0,  273,    0,    0,  336,
    0,    0,    0,  280,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  273,    0,  287,    0,    0,
    0,    0,    0,    0,    0,  184,  198,    0,  203,    0,
  206,  212,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  290,    0,    0,
  312,    0,    0,  295,    0,    0,    0,  -19,    3,    0,
    0,    0,    0,    0,    0,    0,  135,    0,    0,    0,
   25,    0,   47,   69,   91,  150,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   58,    0,  320,    0,    0,
    0,  113,    0,    0,    0,    0,    0,    0,  326,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  309,  334,    0,    0,    0,    0,
  335,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  255,    0,  342,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  355,   15,    0,    0,  -71,   -1,    0,  -25,  207,  196,
  -67,  -82, -135,  217,    0,   33,    0,    0,    0,    0,
  -23,    8,  392,   71,
};
final static int YYTABLESIZE=594;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         93,
   22,   93,   54,   93,   33,   40,  142,   22,   22,   39,
  166,   26,   47,  126,   11,   69,   27,   93,   93,   93,
   93,   96,   80,   96,   44,   96,   41,  191,  151,   70,
   67,  152,  153,  119,   81,   68,   66,   79,   27,   96,
   96,   96,   96,   97,   42,   97,  197,   97,   76,   11,
  174,   51,  161,  120,   48,   27,   52,  110,  175,   77,
   27,   97,   97,   97,   97,   94,   22,   94,   46,   94,
   78,  194,  159,   84,  126,  186,  172,  165,   82,  187,
   45,   93,  181,   94,   94,   94,   94,   91,   44,   91,
   55,   91,   97,  136,   21,  138,   22,  106,   39,  129,
  131,   39,  102,   96,   27,   91,   91,   91,   91,   95,
  126,   95,   75,   95,  133,   51,   49,   63,   50,   64,
   52,   90,   91,  199,  200,   97,   27,   95,   95,   95,
   95,   92,   98,   92,   99,   92,  126,  111,  114,   92,
   51,   49,   85,   50,   22,   52,   86,   94,   27,   92,
   92,   92,   92,   98,  167,   98,  168,   98,   39,  170,
  107,  108,  147,  148,  203,  204,  205,  206,   93,   91,
   27,   98,   98,   98,   98,   68,  209,  179,   94,  202,
  134,   95,   96,  135,    4,    5,  100,  115,  124,  116,
   67,   95,  117,   68,  121,  122,  125,  139,  140,   63,
  145,   64,  144,  154,  146,  155,   71,  185,   67,  179,
   27,  157,  113,   92,   93,   93,   61,   60,   62,   93,
   27,  141,   93,   93,   93,   27,   93,   23,   57,   93,
   93,   93,   93,   93,   32,   98,   96,   96,   38,   24,
   25,   96,   58,  190,   96,   96,   96,   56,   96,   23,
   59,   96,   96,   96,   96,   96,   60,   68,   97,   97,
  160,   24,   25,   97,  149,  150,   97,   97,   97,  158,
   97,  109,   67,   97,   97,   97,   97,   97,   24,   25,
   94,   94,  162,   24,   25,   94,  163,  171,   94,   94,
   94,  183,   94,   80,  173,   94,   94,   94,   94,   94,
  176,  182,   91,   91,  184,   81,  188,   91,  195,  189,
   91,   91,   91,  192,   91,  101,  193,   91,   91,   91,
   91,   91,  208,  196,   95,   95,    1,   24,   25,   95,
    3,   13,   95,   95,   95,    2,   95,   23,   12,   95,
   95,   95,   95,   95,   35,   49,   92,   92,   50,   24,
   25,   92,   41,   48,   92,   92,   92,   35,   92,   53,
   34,   92,   92,   92,   92,   92,   42,   22,   98,   98,
   35,   24,   25,   98,   35,   43,   98,   98,   98,   19,
   98,   87,   36,   98,   98,   98,   98,   98,   73,   74,
   68,   68,   35,   24,   25,   68,  199,  200,   68,   68,
   68,  207,   68,  210,  198,   67,   67,    0,    0,   68,
   67,  123,    0,   67,   67,   67,  118,   67,    0,    4,
    5,  112,  178,  124,   67,    0,    0,   56,   57,   58,
   59,  104,    0,   24,   25,    0,  137,    0,    0,   57,
   88,   89,    0,   24,   25,    0,    0,    0,   24,   25,
    0,   57,   57,   58,  103,  105,    0,    0,   56,    0,
    0,   59,    0,    0,    0,   58,   58,   60,    0,    0,
   56,   56,    0,   59,   59,    0,    0,   34,    2,   60,
   60,    0,    3,    0,    0,    4,    5,    6,    0,    7,
   43,    2,    0,  132,    0,    3,    8,    0,    4,    5,
    6,    0,    7,  130,    2,    0,    0,    0,    3,    8,
    0,    4,    5,    6,    0,    7,    0,    0,    0,   72,
    2,    0,    8,    0,    3,  169,    2,    4,    5,    6,
    3,    7,    0,    4,    5,    6,    0,    7,    8,  156,
   51,   49,    0,   50,    8,   52,    0,   43,    2,    0,
    0,    0,    3,  177,    2,    4,    5,    6,    3,    7,
    0,    4,    5,    6,    0,    7,    8,    0,    1,    2,
    0,    0,    8,    3,   43,    2,    4,    5,    6,    3,
    7,    0,    4,    5,    6,    0,    7,    8,    0,    0,
    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   61,   43,   26,   45,   40,   40,   61,   61,   61,   44,
  146,   40,   14,   85,    0,  256,   45,   59,   60,   61,
   62,   41,  256,   43,   10,   45,   61,   61,  256,  270,
   42,  259,  260,   40,  268,   47,   29,   39,   45,   59,
   60,   61,   62,   41,  268,   43,  182,   45,  256,   35,
  269,   42,   61,   77,   22,   45,   47,   42,  277,  267,
   45,   59,   60,   61,   62,   41,   61,   43,  268,   45,
   38,  125,  140,   41,  146,  256,  159,  145,   41,  260,
   59,  123,  165,   59,   60,   61,   62,   41,   74,   43,
  269,   45,   60,  117,   59,  119,   61,   65,   41,   92,
   93,   44,   43,  123,   45,   59,   60,   61,   62,   41,
  182,   43,   59,   45,  107,   42,   43,   43,   45,   45,
   47,   51,   52,  278,  279,  123,   45,   59,   60,   61,
   62,   41,   60,   43,   62,   45,  208,   67,   68,   41,
   42,   43,   40,   45,   61,   47,   59,  123,   45,   59,
   60,   61,   62,   41,  147,   43,  149,   45,   44,  152,
  259,  260,  259,  260,  190,  191,  192,  193,   41,  123,
   45,   59,   60,   61,   62,   41,  202,  163,   61,   61,
  110,   61,   61,  113,  264,  265,   62,   41,  268,   41,
   41,  123,   40,   59,   44,   41,   41,  268,   41,   43,
   41,   45,  268,  260,   44,   41,  267,  260,   59,  195,
   45,   41,   47,  123,  256,  257,   60,   61,   62,  261,
   45,  276,  264,  265,  266,   45,  268,  256,   45,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  268,
  269,  261,   45,  277,  264,  265,  266,   45,  268,  256,
   45,  271,  272,  273,  274,  275,   45,  123,  256,  257,
  269,  268,  269,  261,  259,  260,  264,  265,  266,   44,
  268,  256,  123,  271,  272,  273,  274,  275,  268,  269,
  256,  257,  269,  268,  269,  261,  123,  268,  264,  265,
  266,  260,  268,  256,  277,  271,  272,  273,  274,  275,
  277,   44,  256,  257,  260,  268,   44,  261,   59,   61,
  264,  265,  266,   61,  268,  256,   61,  271,  272,  273,
  274,  275,   44,  125,  256,  257,    0,  268,  269,  261,
    0,   59,  264,  265,  266,    0,  268,  256,   59,  271,
  272,  273,  274,  275,  123,   59,  256,  257,   59,  268,
  269,  261,   41,   59,  264,  265,  266,  123,  268,  256,
   41,  271,  272,  273,  274,  275,   41,   59,  256,  257,
  123,  268,  269,  261,   41,   41,  264,  265,  266,  125,
  268,  256,   41,  271,  272,  273,  274,  275,  125,   35,
  256,  257,  123,  268,  269,  261,  278,  279,  264,  265,
  266,  195,  268,  208,  188,  256,  257,   -1,   -1,  275,
  261,  256,   -1,  264,  265,  266,  125,  268,   -1,  264,
  265,  256,  125,  268,  275,   -1,   -1,  271,  272,  273,
  274,  256,   -1,  268,  269,   -1,  256,   -1,   -1,  256,
   49,   50,   -1,  268,  269,   -1,   -1,   -1,  268,  269,
   -1,  268,  269,  256,   63,   64,   -1,   -1,  256,   -1,
   -1,  256,   -1,   -1,   -1,  268,  269,  256,   -1,   -1,
  268,  269,   -1,  268,  269,   -1,   -1,  256,  257,  268,
  269,   -1,  261,   -1,   -1,  264,  265,  266,   -1,  268,
  256,  257,   -1,  102,   -1,  261,  275,   -1,  264,  265,
  266,   -1,  268,  256,  257,   -1,   -1,   -1,  261,  275,
   -1,  264,  265,  266,   -1,  268,   -1,   -1,   -1,  256,
  257,   -1,  275,   -1,  261,  256,  257,  264,  265,  266,
  261,  268,   -1,  264,  265,  266,   -1,  268,  275,   41,
   42,   43,   -1,   45,  275,   47,   -1,  256,  257,   -1,
   -1,   -1,  261,  256,  257,  264,  265,  266,  261,  268,
   -1,  264,  265,  266,   -1,  268,  275,   -1,  256,  257,
   -1,   -1,  275,  261,  256,  257,  264,  265,  266,  261,
  268,   -1,  264,  265,  266,   -1,  268,  275,   -1,   -1,
   -1,   -1,   -1,  275,
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
"sentences : error ';'",
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
"procedure : PROC ID '(' parameter_list ')' na_shad_definition proc_body",
"procedure : PROC ID '(' parameter_list ')' na_shad_definition",
"procedure : PROC ID '(' ')' na_shad_definition proc_body",
"procedure : PROC ID '(' error ')' na_shad_definition proc_body",
"na_shad_definition : NA '=' CONSTANT SHADOWING '=' true_false",
"na_shad_definition : '=' CONSTANT SHADOWING '=' true_false",
"na_shad_definition : NA CONSTANT SHADOWING '=' true_false",
"na_shad_definition : NA '=' SHADOWING '=' true_false",
"na_shad_definition : NA '=' CONSTANT '=' true_false",
"na_shad_definition : NA '=' CONSTANT SHADOWING true_false",
"proc_body : '{' sentences_proc '}'",
"proc_body : '{' error '}'",
"proc_body : '{' '}'",
"parameter_list : parameter",
"parameter_list : parameter ',' parameter",
"parameter_list : parameter ',' parameter ',' parameter",
"parameter_list : parameter ',' parameter ',' parameter ',' parameter_list",
"parameter : type ID",
"parameter : type",
"parameter : ID",
"id_list : ID",
"id_list : ID ',' ID",
"id_list : ID ',' ID ',' ID",
"id_list : ID ',' ID ',' ID ',' id_list",
"id_list : error",
"procedure_call : ID '(' id_list ')'",
"procedure_call : ID '(' ')'",
"executable : ID '=' expression",
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
"comparator : '<' '<'",
"comparator : '>' '>'",
"comparator : LESSEQUAL '='",
"comparator : GREATEQUAL '='",
"comparator : NEQUAL '='",
"comparator : '<' '>'",
"condition : expression comparator expression",
"condition : expression '=' expression",
"sentence_block : '{' sentences '}'",
"sentence_block : sentence ';'",
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
"if_clause : IF condition sentence_block ELSE sentence_block END_IF",
"if_clause : IF condition sentence_block END_IF",
"loop_clause : LOOP sentence_block UNTIL '(' condition ')'",
"loop_clause : LOOP sentence_block error",
"loop_clause : LOOP sentence_block UNTIL '(' error ')'",
"loop_clause : LOOP error UNTIL '(' condition ')'",
"loop_clause : LOOP sentence_block UNTIL condition",
"out_clause : OUT '(' CSTRING ')'",
"out_clause : OUT '(' error ')'",
"out_clause : OUT CSTRING",
"expression : expression '+' term",
"expression : expression '-' term",
"expression : term",
"expression : expression '+' error",
"expression : expression '-' error",
"expression : error '+' term",
"expression : error '-' term",
"expression : expression '+' '+' term",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"term : term '*' error",
"term : term '/' error",
"term : error '*' factor",
"term : error '/' factor",
"term : term '*' '*' factor",
"term : term '/' '/' factor",
"factor : ID",
"factor : CONSTANT",
"factor : '-' CONSTANT",
};

//#line 224 "specification.y"

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
	yylval = new ParserVal();
	AtomicReference<ParserVal> ref = new AtomicReference<>();
	yychar = la.yylex(ref,yylval);
	//yylval = ref.get(); // get next token
	//yylval = la.yylval;
	return yychar;
}

public void showMessage(String mg) {
	System.out.println(mg);
}
//#line 529 "Parser.java"
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
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mala, maldita.");}
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
//#line 58 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 22:
//#line 59 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR: sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 23:
//#line 60 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 24:
//#line 61 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se definio mal la lista de parametros.");}
break;
case 26:
//#line 65 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra NA");}
break;
case 27:
//#line 66 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del NA.");}
break;
case 28:
//#line 67 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el valor del NA.");}
break;
case 29:
//#line 68 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra SHADOWING.");}
break;
case 30:
//#line 69 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del SHADOWING.");}
break;
case 32:
//#line 73 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: cuerpo del procedimiento mal definido.");}
break;
case 37:
//#line 80 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
break;
case 39:
//#line 84 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 40:
//#line 85 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 42:
//#line 89 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 44:
//#line 91 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 45:
//#line 92 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 48:
//#line 99 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
break;
case 49:
//#line 101 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 50:
//#line 102 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 51:
//#line 103 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 52:
//#line 104 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 53:
//#line 105 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 54:
//#line 106 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 61:
//#line 115 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 62:
//#line 116 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 63:
//#line 117 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 64:
//#line 118 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 65:
//#line 119 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 66:
//#line 120 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 67:
//#line 123 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
break;
case 68:
//#line 124 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 74:
//#line 138 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 75:
//#line 139 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 76:
//#line 140 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 77:
//#line 141 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 78:
//#line 142 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 79:
//#line 143 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 80:
//#line 144 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 81:
//#line 145 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 82:
//#line 146 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 84:
//#line 150 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 85:
//#line 151 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
break;
case 86:
//#line 152 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 87:
//#line 153 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 89:
//#line 161 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 90:
//#line 162 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 91:
//#line 169 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 92:
//#line 170 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 93:
//#line 171 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
break;
case 94:
//#line 172 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 95:
//#line 173 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 96:
//#line 174 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 97:
//#line 175 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 98:
//#line 176 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 99:
//#line 179 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 100:
//#line 180 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Division.");}
break;
case 101:
//#line 181 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 102:
//#line 182 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 103:
//#line 183 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 104:
//#line 184 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 105:
//#line 185 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 106:
//#line 186 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 107:
//#line 187 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 110:
//#line 193 "specification.y"
{
							/* Manejo la entrada positiva de esta constante		    				*/
		    				 Symbol positivo = la.symbolsTable.getSymbol(val_peek(0).sval);
		    				 if (positivo.getType()==Symbol._ULONGINT)
		    				 	showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: una constante del tipo entero largo sin signo no puede ser negativa");
		    				 else{
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
	    				 		
	    				 }
break;
//#line 990 "Parser.java"
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
