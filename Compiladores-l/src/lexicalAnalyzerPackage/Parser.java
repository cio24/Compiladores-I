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






//#line 1 "specification.y"

package lexicalAnalyzerPackage;

import usefulClassesPackage.Constants;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Math;
import java.math.BigDecimal;
import java.io.*;
import java.util.StringTokenizer;
//#line 28 "Parser.java"




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
    0,    0,    0,    1,    1,    1,    1,    2,    2,    3,
    3,    3,    3,    6,    6,    5,    5,    8,    8,    7,
    7,    7,    7,    7,    7,    7,   10,   10,   10,   10,
   10,   10,   11,   11,   11,    9,    9,    9,    9,   12,
   12,   12,   13,   13,   13,   13,   13,   14,   14,    4,
    4,    4,    4,    4,    4,    4,    4,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   20,
   20,   21,   21,   21,   16,   16,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   17,   17,   17,   17,   17,
   18,   18,   18,   15,   15,   15,   15,   15,   15,   15,
   15,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   23,   23,   23,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    7,
    6,    6,    5,    6,    5,    7,    6,    5,    5,    5,
    5,    5,    3,    3,    2,    1,    3,    5,    7,    2,
    1,    1,    1,    3,    5,    7,    1,    4,    3,    3,
    3,    3,    3,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    2,    2,    2,    2,    2,    3,
    3,    3,    1,    2,    8,    6,    8,    6,    8,    6,
    8,    8,    6,    6,    4,    6,    3,    6,    6,    4,
    4,    4,    2,    3,    3,    1,    3,    3,    3,    3,
    4,    3,    3,    1,    3,    3,    3,    3,    4,    4,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   56,   54,   55,   57,
    7,    0,    0,  111,  112,    0,    0,    0,    0,    0,
  104,   93,    0,    0,    0,   73,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    6,    4,    0,   10,    0,
    0,    0,    0,    0,    0,    0,  113,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   74,    0,   87,    0,    0,   15,
   47,    0,   49,    0,    0,    0,    0,   42,    0,    0,
    0,    0,    5,    0,    0,    0,  107,  108,    0,    0,
   66,   67,   68,    0,   64,   69,   65,    0,    0,    0,
    0,    0,    0,    0,   85,  105,    0,  102,  106,    0,
  103,   92,   91,    0,   72,    0,   90,    0,   48,    0,
    0,    0,    0,    0,    0,   40,    0,    0,    0,    0,
    0,    0,    0,  109,  110,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,    0,    0,    0,
   80,    0,   83,   78,    0,   76,   84,   89,   88,   86,
    0,    0,   21,    0,    0,    0,    0,    0,    0,   35,
    0,   24,    0,    0,    0,    0,    0,    0,   26,   20,
    0,    0,    0,    0,    0,   34,   33,    0,   79,   81,
   82,   77,   75,    0,   18,   19,   29,    0,   32,   31,
   30,   28,    0,   46,   27,   39,
};
final static short yydgoto[] = {                          9,
   10,   36,   12,   13,   14,   15,   16,  207,   91,  135,
  157,   92,   84,   17,   28,   18,   19,   20,   67,   29,
   37,   30,   31,
};
final static short yysindex[] = {                       476,
   44,  137,  -35,    0,    0,  393,  -34,  -39,    0,  482,
   -5,    0,    0, -260,    0,    0,    0,    0,    0,    0,
    0,  168,   80,    0,    0,  183, -255,  140,  399,   30,
    0,    0, -221,  -46,  357,    0, -220,  168, -260,  165,
  198,  -17,   15,  -16,   22,    0,    0,   54,    0,  126,
  205,  205,   48,   48,  459,   92,    0,   79,   86,    0,
   94,  168,  118,   29,  166,  212,  168, -209,  162,  378,
  127,  138,  119,   44,    0,  440,    0,  148,  126,    0,
    0,  142,    0,  143,   80,  126,  164,    0,  -54,  -87,
  146,  145,    0,   52,   30,   30,    0,    0,  399,  414,
    0,    0,    0,  126,    0,    0,    0,   52,  205,   30,
   52,   30,  126,  399,    0,    0,   48,    0,    0,   48,
    0,    0,    0,  168,    0,  216,    0,  -78,    0,  150,
  -54,  155,  -52,  -72,   75,    0,  -54, -190, -199,   50,
 -143,   30,  -61,    0,    0,  167,  493,  171,  159,  -54,
   75,  -54,  -11, -198,   -4,  455,    0,   75,  244,  399,
    0,  399,    0,    0,  434,    0,    0,    0,    0,    0,
   27,   75,    0,   75,  241,  -33,  262,  263,  -30,    0,
  461,    0, -190,   45,   72,  -50, -111,  302,    0,    0,
 -160,   60, -160, -160, -160,    0,    0,  305,    0,    0,
    0,    0,    0, -201,    0,    0,    0, -160,    0,    0,
    0,    0, -190,    0,    0,    0,
};
final static short yyrindex[] = {                       353,
  354,    0,    0,    0,    0,    0,  272,    0,    0,  361,
    0,    0,    0,  308,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  272,    0,  317,
    0,    0,    0,    0,    0,    0,    0,  220,  227,    0,
  242,    0,  249,  256,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  323,    0,
    0,  319,    0,    0,  338,  348,    0,    0,    0,  123,
    0,  327,    0,    0,  -19,    3,    0,    0,    0,    0,
    0,    0,    0,  135,    0,    0,    0,   25,    0,   47,
   69,   91,  286,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  113,    0,    0,    0,    0,    0,    0,  330,    0,
  360,    0,    0,    0,    0,    0,    0,    0,  334,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  381,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  342,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  349,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -32,  443,    0,    0,  -18,   20,    0,  -51,  -75,  307,
  -12, -125,  185,    0,   35,    0,    0,    0,    0,   11,
    1,  -13,  -37,
};
final static int YYTABLESIZE=757;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         96,
   43,   96,   76,   96,   33,   40,  134,   48,  154,   39,
   22,  132,  159,   57,   22,   97,   98,   96,   96,   96,
   96,   99,   87,   99,   90,   99,   41,  193,   21,   68,
   22,  118,  121,   49,   71,   77,   56,   95,   96,   99,
   99,   99,   99,  100,   22,  100,   78,  100,   72,  114,
  115,  110,  112,   47,   81,   89,   50,  198,   80,  160,
  161,  100,  100,  100,  100,   97,   82,   97,   90,   97,
  176,   69,   79,    4,    5,   86,   70,   88,  177,  144,
   93,   96,  145,   97,   97,   97,   97,   94,  127,   94,
  107,   94,   27,   53,  196,  142,  104,   39,   54,  139,
  141,  113,   21,   99,   22,   94,   94,   94,   94,   98,
   22,   98,  164,   98,  143,  165,  166,  205,  206,   90,
  208,   53,   51,  181,   52,  100,   54,   98,   98,   98,
   98,   95,  100,   95,  146,   95,  148,  216,  173,  101,
  209,  210,  211,  212,  202,  182,  102,   97,  203,   95,
   95,   95,   95,  101,  103,  101,  215,  101,  124,  189,
  184,  190,  185,   41,   90,  187,   41,  122,   65,   94,
   66,  101,  101,  101,  101,   71,   26,  105,  123,  106,
  136,   27,   65,  129,   66,  128,  137,  126,  138,  149,
  150,   98,   27,   71,   90,  152,  155,  156,  167,   63,
   62,   64,  171,  117,  131,   83,   27,  168,  109,  201,
   27,  170,   27,   95,   96,   96,  153,   96,   96,   96,
   73,  133,   96,   96,   96,   96,   96,   27,   42,   96,
   96,   96,   96,   96,   32,  101,   99,   99,   38,   99,
   99,   99,   27,  192,   99,   99,   99,   99,   99,   27,
   46,   99,   99,   99,   99,   99,   27,   71,  100,  100,
   27,  100,  100,  100,   60,  175,  100,  100,  100,  100,
  100,   61,  178,  100,  100,  100,  100,  100,    4,    5,
   97,   97,   88,   97,   97,   97,   59,  183,   97,   97,
   97,   97,   97,   62,  188,   97,   97,   97,   97,   97,
   63,  191,   94,   94,  199,   94,   94,   94,  162,  163,
   94,   94,   94,   94,   94,   24,   25,   94,   94,   94,
   94,   94,  194,  195,   98,   98,   70,   98,   98,   98,
   14,  200,   98,   98,   98,   98,   98,  205,  206,   98,
   98,   98,   98,   98,   70,  204,   95,   95,  213,   95,
   95,   95,    1,    3,   95,   95,   95,   95,   95,   43,
    2,   95,   95,   95,   95,   95,   13,   36,  101,  101,
   44,  101,  101,  101,   37,   52,  101,  101,  101,  101,
  101,   53,   45,  101,  101,  101,  101,  101,  214,   38,
   71,   71,   23,   71,   71,   71,   51,    0,   71,   71,
   71,   71,   71,   23,   24,   25,   50,    0,   70,   71,
   58,   59,   60,   61,    0,   24,   25,  116,   23,  130,
   81,  108,   27,   23,  120,    0,    0,    4,    5,   24,
   25,   88,   82,   24,   25,   24,   25,  151,   55,   22,
    0,    0,   11,  158,    0,    0,    0,    0,    0,    0,
   24,   25,   45,   85,    0,    0,  172,    0,  174,    0,
   94,    0,    0,    0,    0,   24,   25,  111,    0,    0,
    0,  147,   24,   25,    0,   60,    0,   11,    0,   24,
   25,   75,   61,   24,   25,    0,    0,   60,   60,    0,
    0,    0,    0,    0,   61,   61,    0,   59,    0,   99,
   53,   51,    0,   52,   62,   54,    0,    0,    0,   59,
   59,   63,    0,    0,    0,   35,   62,   62,   45,    0,
    0,   35,    0,   63,   63,    0,    0,   14,    0,    0,
   14,   14,    0,  169,   53,   51,   35,   52,   14,   54,
    0,   70,   70,    0,   70,   70,   70,    0,    0,   70,
   70,   70,   70,   70,    0,    0,   35,    0,    0,    0,
   70,    0,    0,   13,  125,    0,   13,   13,    0,    0,
    0,    0,   52,    0,   13,   52,   52,    0,   53,  180,
    0,   53,   53,   52,    0,  197,    0,    0,    0,   53,
    0,    0,    0,   51,    0,    0,   51,   51,   11,    0,
    0,    0,    0,   50,   51,    0,   50,   50,    0,    0,
    0,    0,   74,    2,   50,   23,    0,    3,   23,   23,
    4,    5,    6,   45,    7,    0,   23,    0,    0,    0,
    0,    8,    0,  119,    0,    0,   22,    0,    0,   22,
   22,    0,    0,    0,    0,   24,   25,   22,   34,    2,
    0,    0,    0,    3,   44,    2,    4,    5,    6,    3,
    7,    0,    4,    5,    6,    0,    7,    8,    0,  140,
    2,    0,    0,    8,    3,    0,    0,    4,    5,    6,
    0,    7,    0,    0,    0,    0,    0,    0,    8,  186,
    2,    0,    0,    0,    3,   44,    2,    4,    5,    6,
    3,    7,    0,    4,    5,    6,    0,    7,    8,    0,
  179,    2,    0,    0,    8,    3,   44,    2,    4,    5,
    6,    3,    7,    0,    4,    5,    6,    0,    7,    8,
    0,    1,    2,    0,    0,    8,    3,   44,    2,    4,
    5,    6,    3,    7,    0,    4,    5,    6,    0,    7,
    8,    0,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   35,   45,   40,   40,   61,  268,   61,   44,
   61,   87,  138,  269,   61,   53,   54,   59,   60,   61,
   62,   41,   40,   43,   43,   45,   61,   61,   59,   29,
   61,   69,   70,   14,  256,  256,   26,   51,   52,   59,
   60,   61,   62,   41,   61,   43,  267,   45,  270,  259,
  260,   65,   66,   59,  256,   41,   22,  183,   39,  259,
  260,   59,   60,   61,   62,   41,  268,   43,   87,   45,
  269,   42,   38,  264,  265,   41,   47,  268,  277,  117,
   59,  123,  120,   59,   60,   61,   62,   41,   78,   43,
   62,   45,   45,   42,  125,  109,   62,   44,   47,   99,
  100,   67,   59,  123,   61,   59,   60,   61,   62,   41,
   61,   43,  256,   45,  114,  259,  260,  278,  279,  138,
   61,   42,   43,  156,   45,  123,   47,   59,   60,   61,
   62,   41,   41,   43,  124,   45,  126,  213,  151,   61,
  192,  193,  194,  195,  256,  158,   61,  123,  260,   59,
   60,   61,   62,   41,   61,   43,  208,   45,   40,  172,
  160,  174,  162,   41,  183,  165,   44,   41,   43,  123,
   45,   59,   60,   61,   62,   41,   40,   60,   41,   62,
  268,   45,   43,   41,   45,   44,   41,   40,   44,  268,
   41,  123,   45,   59,  213,   41,  269,  123,  260,   60,
   61,   62,   44,   42,   41,   41,   45,   41,   43,  260,
   45,   41,   45,  123,  256,  257,  269,  259,  260,  261,
  267,  276,  264,  265,  266,  267,  268,   45,  268,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  259,
  260,  261,   45,  277,  264,  265,  266,  267,  268,   45,
  256,  271,  272,  273,  274,  275,   45,  123,  256,  257,
   45,  259,  260,  261,   45,  277,  264,  265,  266,  267,
  268,   45,  277,  271,  272,  273,  274,  275,  264,  265,
  256,  257,  268,  259,  260,  261,   45,   44,  264,  265,
  266,  267,  268,   45,  268,  271,  272,  273,  274,  275,
   45,   61,  256,  257,  260,  259,  260,  261,  259,  260,
  264,  265,  266,  267,  268,  268,  269,  271,  272,  273,
  274,  275,   61,   61,  256,  257,   41,  259,  260,  261,
   59,  260,  264,  265,  266,  267,  268,  278,  279,  271,
  272,  273,  274,  275,   59,   44,  256,  257,   44,  259,
  260,  261,    0,    0,  264,  265,  266,  267,  268,   41,
    0,  271,  272,  273,  274,  275,   59,   41,  256,  257,
   41,  259,  260,  261,   41,   59,  264,  265,  266,  267,
  268,   59,   41,  271,  272,  273,  274,  275,  204,   41,
  256,  257,  256,  259,  260,  261,   59,   -1,  264,  265,
  266,  267,  268,  256,  268,  269,   59,   -1,  123,  275,
  271,  272,  273,  274,   -1,  268,  269,  256,   59,  256,
  256,  256,   45,  256,   47,   -1,   -1,  264,  265,  268,
  269,  268,  268,  268,  269,  268,  269,  131,  256,   59,
   -1,   -1,    0,  137,   -1,   -1,   -1,   -1,   -1,   -1,
  268,  269,   10,  256,   -1,   -1,  150,   -1,  152,   -1,
  256,   -1,   -1,   -1,   -1,  268,  269,  256,   -1,   -1,
   -1,  256,  268,  269,   -1,  256,   -1,   35,   -1,  268,
  269,  125,  256,  268,  269,   -1,   -1,  268,  269,   -1,
   -1,   -1,   -1,   -1,  268,  269,   -1,  256,   -1,   41,
   42,   43,   -1,   45,  256,   47,   -1,   -1,   -1,  268,
  269,  256,   -1,   -1,   -1,  123,  268,  269,   76,   -1,
   -1,  123,   -1,  268,  269,   -1,   -1,  256,   -1,   -1,
  259,  260,   -1,   41,   42,   43,  123,   45,  267,   47,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,  264,
  265,  266,  267,  268,   -1,   -1,  123,   -1,   -1,   -1,
  275,   -1,   -1,  256,  125,   -1,  259,  260,   -1,   -1,
   -1,   -1,  256,   -1,  267,  259,  260,   -1,  256,  125,
   -1,  259,  260,  267,   -1,  125,   -1,   -1,   -1,  267,
   -1,   -1,   -1,  256,   -1,   -1,  259,  260,  156,   -1,
   -1,   -1,   -1,  256,  267,   -1,  259,  260,   -1,   -1,
   -1,   -1,  256,  257,  267,  256,   -1,  261,  259,  260,
  264,  265,  266,  181,  268,   -1,  267,   -1,   -1,   -1,
   -1,  275,   -1,  256,   -1,   -1,  256,   -1,   -1,  259,
  260,   -1,   -1,   -1,   -1,  268,  269,  267,  256,  257,
   -1,   -1,   -1,  261,  256,  257,  264,  265,  266,  261,
  268,   -1,  264,  265,  266,   -1,  268,  275,   -1,  256,
  257,   -1,   -1,  275,  261,   -1,   -1,  264,  265,  266,
   -1,  268,   -1,   -1,   -1,   -1,   -1,   -1,  275,  256,
  257,   -1,   -1,   -1,  261,  256,  257,  264,  265,  266,
  261,  268,   -1,  264,  265,  266,   -1,  268,  275,   -1,
  256,  257,   -1,   -1,  275,  261,  256,  257,  264,  265,
  266,  261,  268,   -1,  264,  265,  266,   -1,  268,  275,
   -1,  256,  257,   -1,   -1,  275,  261,  256,  257,  264,
  265,  266,  261,  268,   -1,  264,  265,  266,   -1,  268,
  275,   -1,   -1,   -1,   -1,   -1,  275,
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
"procedure : PROC ID '(' parameter_list ')' na_shad_definition proc_body",
"procedure : PROC ID '(' ')' na_shad_definition proc_body",
"procedure : PROC ID '(' parameter_list ')' na_shad_definition",
"procedure : PROC ID '(' ')' na_shad_definition",
"procedure : PROC '(' parameter_list ')' na_shad_definition proc_body",
"procedure : PROC '(' ')' na_shad_definition proc_body",
"procedure : PROC ID '(' error ')' na_shad_definition proc_body",
"na_shad_definition : NA '=' CONSTANT SHADOWING '=' true_false",
"na_shad_definition : '=' CONSTANT SHADOWING '=' true_false",
"na_shad_definition : NA CONSTANT SHADOWING '=' true_false",
"na_shad_definition : NA '=' SHADOWING '=' true_false",
"na_shad_definition : NA '=' CONSTANT '=' true_false",
"na_shad_definition : NA '=' CONSTANT SHADOWING true_false",
"proc_body : '{' sentences '}'",
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
"comparator : '<' '<'",
"comparator : '>' '>'",
"comparator : LESSEQUAL '='",
"comparator : GREATEQUAL '='",
"comparator : NEQUAL '='",
"comparator : '<' '>'",
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

//#line 220 "specification.y"


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
//#line 571 "Parser.java"
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
case 7:
//#line 29 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");}
break;
case 10:
//#line 37 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaracion de variable.");}
break;
case 11:
//#line 38 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaracion PROC.");}
break;
case 12:
//#line 39 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 13:
//#line 40 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
break;
case 20:
//#line 55 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 21:
//#line 56 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 22:
//#line 57 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 23:
//#line 58 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 24:
//#line 59 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}
break;
case 25:
//#line 60 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}
break;
case 26:
//#line 61 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se definio mal la lista de parametros.");}
break;
case 28:
//#line 65 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra NA");}
break;
case 29:
//#line 66 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del NA.");}
break;
case 30:
//#line 67 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el valor del NA.");}
break;
case 31:
//#line 68 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra SHADOWING.");}
break;
case 32:
//#line 69 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del SHADOWING.");}
break;
case 34:
//#line 74 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: cuerpo del procedimiento mal definido.");}
break;
case 39:
//#line 81 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
break;
case 41:
//#line 85 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 42:
//#line 86 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 44:
//#line 90 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 46:
//#line 92 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 47:
//#line 93 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 50:
//#line 100 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
break;
case 51:
//#line 101 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 52:
//#line 102 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 53:
//#line 103 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 54:
//#line 104 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 55:
//#line 105 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 56:
//#line 106 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 57:
//#line 107 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 64:
//#line 116 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 65:
//#line 117 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 66:
//#line 118 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 67:
//#line 119 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 68:
//#line 120 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 69:
//#line 121 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 70:
//#line 124 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
break;
case 71:
//#line 125 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 77:
//#line 139 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 78:
//#line 140 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 79:
//#line 141 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 80:
//#line 142 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 81:
//#line 143 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 82:
//#line 144 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 83:
//#line 145 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 84:
//#line 146 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 85:
//#line 147 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 87:
//#line 151 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 88:
//#line 152 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
break;
case 89:
//#line 153 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 90:
//#line 154 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 92:
//#line 162 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 93:
//#line 163 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 94:
//#line 170 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 95:
//#line 171 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 96:
//#line 172 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
break;
case 97:
//#line 173 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 98:
//#line 174 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 99:
//#line 175 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 100:
//#line 176 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 101:
//#line 177 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 102:
//#line 180 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 103:
//#line 181 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Division.");}
break;
case 104:
//#line 182 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 105:
//#line 183 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 106:
//#line 184 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 107:
//#line 185 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 108:
//#line 186 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 109:
//#line 187 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 110:
//#line 188 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 113:
//#line 194 "specification.y"
{
							/* Manejo la entrada positiva de esta constante		    				*/
		    				 Symbol positivo = la.symbolsTable.getSymbol(val_peek(0).sval);
		    				 if(positivo.removeRef() == 0){ /* Remove reference and if it reaches 0, remove SyboleTable entry*/
		    				 	la.symbolsTable.removeSymbol(positivo.getLexeme());
		    				 }
		    				 
		    				 /* TODO: QUE HACER CON - 4_ul ??????*/
		    				 
		    				 /* Creo nueva entrada o actualizo la existente con una referencia*/
		    				 Symbol negativo = la.symbolsTable.getSymbol("-"+val_peek(0).sval);
		    				 if (negativo != null){
		    				 	negativo.addRef();  /* Ya existe la entrada*/
		    				 }else{
		    				 	String lexema = "-"+positivo.getLexeme();
		    				 	Symbol nuevoNegativo = new Symbol(lexema,la.getCurrentLine(),positivo.getType());
		    				 	la.symbolsTable.addSymbol(lexema,nuevoNegativo);
		    				 }
	    				 	val_peek(0).sval = "-"+val_peek(0).sval;
	    				 		
	    				 }
break;
//#line 1048 "Parser.java"
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
