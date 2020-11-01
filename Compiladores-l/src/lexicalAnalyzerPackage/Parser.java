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
//#line 31 "Parser.java"




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
    7,    7,    7,    7,    9,    9,   11,   11,   11,   11,
   11,   11,   12,   12,   12,   10,   10,   10,   10,   13,
   13,   13,   14,   14,   14,   14,   15,   15,    4,    4,
    4,    4,    4,    4,    4,    4,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   21,   21,
   22,   22,   22,   17,   17,   17,   17,   17,   17,   17,
   23,   23,   23,   23,   24,   25,   18,   18,   18,   27,
   27,   27,   26,   19,   19,   19,   16,   16,   16,   16,
   16,   16,   16,   16,   28,   28,   28,   28,   28,   28,
   28,   28,   28,   29,   29,   29,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    6,
    5,    5,    4,    6,    2,    1,    6,    5,    5,    5,
    5,    5,    3,    3,    2,    1,    3,    5,    7,    2,
    1,    1,    1,    3,    5,    7,    4,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    2,    3,    3,
    3,    2,    2,    6,    4,    6,    4,    6,    6,    4,
    3,    3,    1,    2,    1,    1,    4,    3,    6,    3,
    3,    1,    1,    4,    4,    2,    3,    3,    1,    3,
    3,    3,    3,    4,    3,    3,    1,    3,    3,    3,
    3,    4,    4,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,   93,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,    0,   55,   53,   54,
   56,    0,    7,    0,    0,  114,  115,    0,    0,    0,
    0,    0,    0,  107,   96,    0,    0,    0,    0,    0,
   25,    0,    0,    6,    4,    0,   10,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  116,
    0,    0,   57,    0,    0,    0,    0,    0,    0,    0,
   84,    0,   85,    0,    0,    0,    0,    0,    0,   15,
    0,   48,    0,    0,    0,    5,    0,   42,    0,    0,
    0,    0,    0,    0,   73,    0,   72,   88,    0,    0,
    0,    0,  110,  111,   82,   81,   65,   66,   67,    0,
   63,   68,   64,    0,    0,    0,    0,    0,    0,    0,
   80,   77,    0,   75,  108,    0,  105,  109,    0,  106,
   95,   94,    0,   47,    0,    0,    0,    0,   40,    0,
    0,    0,   71,    0,   92,   87,    0,   86,    0,    0,
    0,  112,  113,    0,    0,    0,    0,    0,    0,   21,
    0,    0,    0,    0,    0,   78,   79,   76,   74,    0,
   24,    0,    0,    0,    0,    0,   35,    0,   20,    0,
   89,   91,   90,    0,    0,    0,    0,    0,    0,   34,
   33,    0,    0,   18,   19,   29,    0,   32,   31,   30,
   28,    0,   46,   27,   39,
};
final static short yydgoto[] = {                          9,
   10,   51,   12,   13,   14,   15,   16,  196,   17,   91,
  138,  160,   92,   83,   18,   30,   19,   20,   21,   70,
   31,  148,   32,   74,  149,   22,  146,   33,   34,
};
final static short yysindex[] = {                       -79,
   18,  -28,  -35,    0,    0,    0,  -34, -236,    0,  357,
  -51,    0,    0, -221,    0,    0,  -11,    0,    0,    0,
    0,  226,    0,   60,   33,    0,    0,   82, -212,  157,
   97,  248,   12,    0,    0, -196,   60, -221,  -25,  104,
    0,   84,   96,    0,    0,  117,    0,  156,  -60,  264,
  110, -242,   48,  126,  126,   11,   11,   99,  102,    0,
  129,  134,    0,  137,   60,   56,  139,   38,  176,   60,
    0,   28,    0,  -94,   16,  166,  158,  162,   48,    0,
  160,    0,  165,   33,   48,    0,  242,    0,  -54,   41,
  276,  279,  284,   18,    0,  316,    0,    0,   -6,   77,
   12,   12,    0,    0,    0,    0,    0,    0,    0,   48,
    0,    0,    0,   77,  126,   12,   77,   12,   48,  281,
    0,    0,  294,    0,    0,   11,    0,    0,   11,    0,
    0,    0,   59,    0,  -54,  -12,   62,  209,    0,  -54,
   37,   60,    0,  181,    0,    0,   12,    0,   76,  -52,
  -77,    0,    0,  295,  209,   68, -239,   90,  331,    0,
  209,  302,  312,  455,  313,    0,    0,    0,    0,   93,
    0,  314,  -16,  315,  319,  -26,    0,  344,    0,   37,
    0,    0,    0,  339, -111,  119, -111, -111, -111,    0,
    0,  346,  125,    0,    0,    0, -111,    0,    0,    0,
    0,   37,    0,    0,    0,
};
final static short yyrindex[] = {                       402,
  405,    0,    0,    0,    0,    0,  -46,  369,    0,  413,
    0,    0,    0,  -44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  310,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -46,    0,    0,    0,    0,
    0,    0,   10,    0,    0,    0,    0,    0,    0,    0,
  184,  199,    0,  203,    0,  206,  220,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,    0,
  378,    0,    0,   36,   39,    0,    0,    0,    0,  140,
    0,  382,    0,    0,    0,    0,    0,    0,    0,    0,
  -19,    3,    0,    0,    0,    0,    0,    0,    0,  135,
    0,    0,    0,   25,    0,   47,   69,   91,  150,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   54,    0,    0,
    0,    0,    0,    0,    0,    0,  113,    0,    0,    0,
    0,    0,    0,  385,    0,    0,    0,    0,    0,    0,
   58,  386,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  395,    0,    0,    0,    0,    0,    0,
    0,  397,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -39,  358,    0,    0,  -45,   17,    0,  -86,    0,  237,
   24,  -40, -113,  249,    0,  123,    0,    0,    0,    0,
   -5,  101,    0,    0,  320,    0,    0,  -18,   -4,
};
final static int YYTABLESIZE=632;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         99,
   24,   99,   90,   99,   36,   39,  137,   45,   24,   38,
   96,   28,   14,   98,   13,   82,   29,   99,   99,   99,
   99,  102,   59,  102,   99,  102,   40,  162,   48,  173,
   47,   41,   23,  144,   24,  101,  102,  174,   29,  102,
  102,  102,  102,  103,  187,  103,   46,  103,  157,  116,
  118,  103,  104,   75,   80,   29,   60,  126,   76,   77,
   29,  103,  103,  103,  103,  100,  192,  100,   51,  100,
  127,  130,   52,   78,   56,   54,   23,   55,   24,   57,
  115,   99,   29,  100,  100,  100,  100,   97,   24,   97,
   68,   97,   69,  145,   50,   90,  147,   49,  190,  198,
  199,  200,  201,  102,   29,   97,   97,   97,   97,  101,
  204,  101,   23,  101,  171,  111,   22,  112,   56,  178,
  179,  152,   52,   57,  153,  103,   29,  101,  101,  101,
  101,   98,   73,   98,   90,   98,  163,   71,  165,  105,
   56,   54,  106,   55,   24,   57,   53,  100,   29,   98,
   98,   98,   98,  104,   86,  104,   90,  104,  155,   79,
   38,  122,   85,  161,  123,  124,  194,  195,   97,   97,
   29,  104,  104,  104,  104,   70,    1,    2,  168,  197,
   41,    3,  169,   41,    4,    5,    6,  110,    7,  107,
   69,  101,  119,   70,  108,    8,   89,  109,  131,   68,
  113,   69,  132,  133,   44,  134,   93,  167,   69,   14,
   29,   13,  129,   98,   99,   99,   66,   65,   67,   99,
   29,  136,   99,   99,   99,   29,   99,   25,   59,   99,
   99,   99,   99,   99,   35,  104,  102,  102,   37,   26,
   27,  102,   81,   60,  102,  102,  102,   58,  102,   25,
   61,  102,  102,  102,  102,  102,  156,   70,  103,  103,
  186,   26,   27,  103,   62,   51,  103,  103,  103,   52,
  103,  125,   69,  103,  103,  103,  103,  103,   26,   27,
  100,  100,  135,   26,   27,  100,  120,  121,  100,  100,
  100,   50,  100,  114,   49,  100,  100,  100,  100,  100,
    4,    5,   97,   97,   88,   26,   27,   97,  139,   23,
   97,   97,   97,   22,   97,   25,  140,   97,   97,   97,
   97,   97,  141,  142,  101,  101,  154,   26,   27,  101,
  158,  159,  101,  101,  101,  166,  101,   58,  170,  101,
  101,  101,  101,  101,  172,  180,   98,   98,   50,   26,
   27,   98,  181,  183,   98,   98,   98,   11,   98,   84,
  184,   98,   98,   98,   98,   98,  175,   43,  104,  104,
   50,   26,   27,  104,  185,  188,  104,  104,  104,  189,
  104,  100,  193,  104,  104,  104,  104,  104,   95,  202,
   70,   70,   81,   26,   27,   70,  194,  195,   70,   70,
   70,    1,   70,   50,    3,   69,   69,   11,   26,   70,
   69,   87,    2,   69,   69,   69,   50,   69,   43,    4,
    5,  128,   36,   88,   69,   44,   37,   61,   62,   63,
   64,  117,   83,   26,   27,   45,  164,   38,  205,   59,
  143,  203,  151,   26,   27,    0,    0,    0,   26,   27,
    0,   59,   59,   43,   60,  177,    0,    0,   58,    0,
    0,   61,    0,    0,    0,    0,   60,   60,  191,    0,
   58,   58,    0,   61,   61,   62,    0,    0,    0,    0,
    0,   49,    2,    0,    0,    0,    3,   62,   62,    4,
    5,    6,    0,    7,    0,  182,   56,   54,    0,   55,
    8,   57,    0,   72,    2,    0,    0,    0,    3,    0,
    0,    4,    5,    6,    0,    7,   11,    0,    0,   94,
    2,    0,    8,    0,    3,    0,    0,    4,    5,    6,
    0,    7,    0,    0,    0,   43,   42,    2,    8,    0,
    0,    3,    0,    0,    4,    5,    6,    0,    7,  150,
    2,    0,    0,    0,    3,    8,    0,    4,    5,    6,
    0,    7,    0,    0,    0,   83,   83,    0,    8,    0,
   83,   42,    2,   83,   83,   83,    3,   83,    0,    4,
    5,    6,    0,    7,   83,    0,  176,    2,    0,    0,
    8,    3,    0,    0,    4,    5,    6,    0,    7,   42,
    2,    0,    0,    0,    3,    8,    0,    4,    5,    6,
    0,    7,   42,    2,    0,    0,    0,    3,    8,    0,
    4,    5,    6,    0,    7,    0,    0,    0,    0,    0,
    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   61,   43,   48,   45,   40,   40,   61,   59,   61,   44,
   50,   40,   59,  256,   59,   41,   45,   59,   60,   61,
   62,   41,   28,   43,  267,   45,   61,  141,   40,  269,
   14,  268,   59,   40,   61,   54,   55,  277,   45,   59,
   60,   61,   62,   41,   61,   43,  268,   45,   61,   68,
   69,   56,   57,   42,   38,   45,  269,   42,   47,  256,
   45,   59,   60,   61,   62,   41,  180,   43,   59,   45,
   75,   76,   59,  270,   42,   43,   59,   45,   61,   47,
   43,  123,   45,   59,   60,   61,   62,   41,   61,   43,
   43,   45,   45,   99,   59,  141,  115,   59,  125,  186,
  187,  188,  189,  123,   45,   59,   60,   61,   62,   41,
  197,   43,   59,   45,  155,   60,   59,   62,   42,  159,
  161,  126,   22,   47,  129,  123,   45,   59,   60,   61,
   62,   41,   32,   43,  180,   45,  142,   41,  144,   41,
   42,   43,   41,   45,   61,   47,   24,  123,   45,   59,
   60,   61,   62,   41,   59,   43,  202,   45,  135,   37,
   44,  256,   40,  140,  259,  260,  278,  279,   59,  123,
   45,   59,   60,   61,   62,   41,  256,  257,  256,   61,
   41,  261,  260,   44,  264,  265,  266,   65,  268,   61,
   41,  123,   70,   59,   61,  275,   41,   61,   41,   43,
   62,   45,   41,   44,  256,   41,  267,  260,   59,  256,
   45,  256,   47,  123,  256,  257,   60,   61,   62,  261,
   45,  276,  264,  265,  266,   45,  268,  256,   45,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  268,
  269,  261,  268,   45,  264,  265,  266,   45,  268,  256,
   45,  271,  272,  273,  274,  275,  269,  123,  256,  257,
  277,  268,  269,  261,   45,  256,  264,  265,  266,  256,
  268,  256,  123,  271,  272,  273,  274,  275,  268,  269,
  256,  257,   41,  268,  269,  261,  259,  260,  264,  265,
  266,  256,  268,  256,  256,  271,  272,  273,  274,  275,
  264,  265,  256,  257,  268,  268,  269,  261,  268,  256,
  264,  265,  266,  256,  268,  256,   41,  271,  272,  273,
  274,  275,   44,   40,  256,  257,  268,  268,  269,  261,
  269,  123,  264,  265,  266,  260,  268,  256,   44,  271,
  272,  273,  274,  275,  277,   44,  256,  257,  123,  268,
  269,  261,   41,   41,  264,  265,  266,    0,  268,  256,
  268,  271,  272,  273,  274,  275,  277,   10,  256,  257,
  123,  268,  269,  261,   61,   61,  264,  265,  266,   61,
  268,  256,   44,  271,  272,  273,  274,  275,  125,   44,
  256,  257,  268,  268,  269,  261,  278,  279,  264,  265,
  266,    0,  268,  123,    0,  256,  257,   50,   40,  275,
  261,  256,    0,  264,  265,  266,  123,  268,   41,  264,
  265,  256,   41,  268,  275,   41,   41,  271,  272,  273,
  274,  256,  123,  268,  269,   41,  256,   41,  202,  256,
  125,  193,  123,  268,  269,   -1,   -1,   -1,  268,  269,
   -1,  268,  269,   96,  256,  125,   -1,   -1,  256,   -1,
   -1,  256,   -1,   -1,   -1,   -1,  268,  269,  125,   -1,
  268,  269,   -1,  268,  269,  256,   -1,   -1,   -1,   -1,
   -1,  256,  257,   -1,   -1,   -1,  261,  268,  269,  264,
  265,  266,   -1,  268,   -1,   41,   42,   43,   -1,   45,
  275,   47,   -1,  256,  257,   -1,   -1,   -1,  261,   -1,
   -1,  264,  265,  266,   -1,  268,  159,   -1,   -1,  256,
  257,   -1,  275,   -1,  261,   -1,   -1,  264,  265,  266,
   -1,  268,   -1,   -1,   -1,  178,  256,  257,  275,   -1,
   -1,  261,   -1,   -1,  264,  265,  266,   -1,  268,  256,
  257,   -1,   -1,   -1,  261,  275,   -1,  264,  265,  266,
   -1,  268,   -1,   -1,   -1,  256,  257,   -1,  275,   -1,
  261,  256,  257,  264,  265,  266,  261,  268,   -1,  264,
  265,  266,   -1,  268,  275,   -1,  256,  257,   -1,   -1,
  275,  261,   -1,   -1,  264,  265,  266,   -1,  268,  256,
  257,   -1,   -1,   -1,  261,  275,   -1,  264,  265,  266,
   -1,  268,  256,  257,   -1,   -1,   -1,  261,  275,   -1,
  264,  265,  266,   -1,  268,   -1,   -1,   -1,   -1,   -1,
   -1,  275,
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
"procedure : procedure_header '(' parameter_list ')' na_shad_definition proc_body",
"procedure : procedure_header '(' ')' na_shad_definition proc_body",
"procedure : procedure_header '(' parameter_list ')' na_shad_definition",
"procedure : procedure_header '(' ')' na_shad_definition",
"procedure : procedure_header '(' error ')' na_shad_definition proc_body",
"procedure_header : PROC ID",
"procedure_header : PROC",
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
"sentence_block : sentence ';'",
"sentence_block : '{' '}'",
"if_clause : IF if_condition then_body ELSE else_body END_IF",
"if_clause : IF if_condition then_body END_IF",
"if_clause : IF if_condition then_body ELSE else_body error",
"if_clause : IF if_condition then_body error",
"if_clause : IF if_condition error ELSE else_body END_IF",
"if_clause : IF if_condition then_body ELSE error END_IF",
"if_clause : IF if_condition error END_IF",
"if_condition : '(' condition ')'",
"if_condition : '(' error ')'",
"if_condition : condition",
"if_condition : condition ')'",
"then_body : sentence_block",
"else_body : sentence_block",
"loop_clause : loop_begin sentence_block UNTIL loop_condition",
"loop_clause : loop_begin sentence_block error",
"loop_clause : loop_begin error UNTIL '(' condition ')'",
"loop_condition : '(' condition ')'",
"loop_condition : '(' error ')'",
"loop_condition : condition",
"loop_begin : LOOP",
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

//#line 775 "specification.y"

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




//#line 572 "Parser.java"
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
//#line 27 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.WARNING,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"Programa vacio");}
break;
case 2:
//#line 31 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] Programa completo.");}
break;
case 3:
//#line 35 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"No se encontraron sentencias validas");}
break;
case 6:
//#line 49 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"';' ausente al final de la sentencia.");}
break;
case 7:
//#line 53 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");}
break;
case 10:
//#line 63 "specification.y"
{
				 showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de variable/s.");
				 Symbol symbol;
				 for (String v : variableDeclarationIdentifiers){
					/* Agregar a la tabla de simbolos la varialbe con el scope, salvo que ya este declarada entonces tira error*/
					ic.variableDeclarationControl(v,scope,val_peek(1).sval);
				 }
				 /*Resetear la lista de identificadores siendo identificados.*/
				 variableDeclarationIdentifiers.clear();
			 }
break;
case 12:
//#line 78 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 13:
//#line 82 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un identificador y no se encontro");}
break;
case 14:
//#line 86 "specification.y"
{
	 			        /*añadir a la lista de identificadores actualmente siendo identificados el identificador*/
	 			        variableDeclarationIdentifiers.add(val_peek(0).sval);
	 			      }
break;
case 15:
//#line 93 "specification.y"
{
	 			        /*añadir a la lista de identificadores actualmente siendo identificados el identificador identificado*/
	 			        variableDeclarationIdentifiers.add(val_peek(2).sval);
	 			      }
break;
case 16:
//#line 101 "specification.y"
{ yyval.sval=Symbol._ULONGINT_TYPE; }
break;
case 17:
//#line 105 "specification.y"
{ yyval.sval=Symbol._DOUBLE_TYPE; }
break;
case 18:
//#line 110 "specification.y"
{yyval.sval = "TRUE";}
break;
case 19:
//#line 114 "specification.y"
{yyval.sval = "FALSE";}
break;
case 20:
//#line 119 "specification.y"
{
				showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");

				ProcedureData pd = ic.getProcedureDataFromStack();
				String fullProcId = pd.getFullProcId();
				String procId = fullProcId.substring(0,fullProcId.indexOf(":"));

				/*restauramos el scope ahora que se termino de detectar el procedimiento*/
				scope = st.removeScope(scope);
				if(!fullProcId.contains("ERROR")){

					/*se actualiza la pila de los procedimientos y si este no se declaro se declara*/
					ic.procedureDeclarationControl(procId, scope);

					/*se guarda para que cuando se invoque se puedan controlar los parametros*/
					ic.saveProcedureData(pd);
					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}
				else{
					/*se quita del stack el procedimiento*/
					ic.removeLastProcedureFromStack();

					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}
			}
break;
case 21:
//#line 147 "specification.y"
{
				showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");

				ProcedureData pd = ic.getProcedureDataFromStack();
				String fullProcId = pd.getFullProcId();
				String procId = fullProcId.substring(0,fullProcId.indexOf(":"));

				/*restauramos el scope ahora que se termino de detectar el procedimiento*/
				scope = st.removeScope(scope);
				if(!fullProcId.equals(procId + ":ERROR")){

					/*se actualiza la pila de los procedimientos y si este no se declaro se declara*/
					ic.procedureDeclarationControl(procId, scope);

					/*se guarda para que cuando se invoque se puedan controlar los parametros*/
					ic.saveProcedureData(pd);

					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}
				else{
					/*se quita del stack el procedimiento*/
					ic.removeLastProcedureFromStack();

					tm.createTriplet("PDE",new Operand(Operand.ST_POINTER,procId + scope));
				}

			}
break;
case 22:
//#line 177 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}
break;
case 23:
//#line 181 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el cuerpo del procedimiento");}
break;
case 24:
//#line 185 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se definio mal la lista de parametros");}
break;
case 25:
//#line 190 "specification.y"
{
	/*
	si el procedimiento se definio en este scope tira error y preserva información
	del error para que se pueda detectar en otras etapadas de la declaración
	Si no tiene error agrega el nombre del identificador en una estructura que se
	apila para que a medida que se encuentran los demás datos se puedan ir guardando
	*/
	ic.procedureHeaderControl(val_peek(0).sval,scope);

	/*expando el scope*/
	scope += ":"+val_peek(0).sval;
}
break;
case 26:
//#line 205 "specification.y"
{
	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,
		"falta definir el identificador del procedimiento");

	/*para poder controlar el error más adelante generamos un procedureData error*/
	ic.addProcedureDataToStack(new ProcedureData("ERROR"));
}
break;
case 27:
//#line 216 "specification.y"
{
						ProcedureData pd = ic.getProcedureDataFromStack();
						if(!pd.getFullProcId().equals("ERROR")){

							/*guardo los nuevos datos del procedimiento*/
							pd.setNA(Integer.valueOf(val_peek(3).sval));

							boolean shadowing;
							if(val_peek(0).sval.equals("TRUE"))
								pd.setShadowing(true);
							else
								pd.setShadowing(false);
						}
	                		}
break;
case 28:
//#line 233 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra NA");}
break;
case 29:
//#line 237 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del NA");}
break;
case 30:
//#line 241 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta definir el valor de NA");}
break;
case 31:
//#line 245 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la palabra SHADOWING");}
break;
case 32:
//#line 249 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el '=' del SHADOWING");}
break;
case 34:
//#line 257 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"cuerpo del procedimiento mal definido");}
break;
case 36:
//#line 264 "specification.y"
{
					showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");

					/*obtengo los datos del procedimiento que se esta declarando*/
					ProcedureData pd = ic.getProcedureDataFromStack();

					if(!pd.getFullProcId().equals("ERROR")){
						/*le agrego el tipo del primer parametro*/
						pd.setFirstFormalParameterType(val_peek(0).sval);
					}
				}
break;
case 37:
//#line 278 "specification.y"
{
					showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");
					/*obtengo los datos del procedimiento que se esta declarando*/
					ProcedureData pd = ic.getProcedureDataFromStack();

					if(!pd.getFullProcId().equals("ERROR")){
						/*le agrego el tipo del primer parametro*/
						pd.setFirstFormalParameterType(val_peek(2).sval);
						pd.setSecondFormalParameterType(val_peek(0).sval);
					}
				}
break;
case 38:
//#line 292 "specification.y"
{
					showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");
					/*obtengo los datos del procedimiento que se esta declarando*/
					ProcedureData pd = ic.getProcedureDataFromStack();

					if(!pd.getFullProcId().equals("ERROR")){
						/*le agrego el tipo del primer parametro*/
						pd.setFirstFormalParameterType(val_peek(4).sval);
						pd.setSecondFormalParameterType(val_peek(2).sval);
						pd.setThirdFormalParameterType(val_peek(0).sval);
					}
				}
break;
case 39:
//#line 307 "specification.y"
{
					ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir un maximo de 3 parametros");
				}
break;
case 40:
//#line 313 "specification.y"
{
				/*borro de la tabla de símbolos el identificador del parametro*/
				st.removeSymbol(val_peek(0).sval);

				/*agrego el identificador pero con los datos del parametro y el scope*/
				String fullId = val_peek(0).sval + scope;
				Symbol sp = new Symbol(fullId, Symbol._IDENTIFIER_LEXEME, val_peek(1).sval, Symbol._PARAMETER_USE, Symbol._COPY_VALUE_SEMANTIC);
				st.addSymbol(fullId, sp);

				/*guardo solo el tipo del parametro para poder usarlo para controlar más tarde cuando se invoque al procedimiento*/
				yyval.sval = val_peek(1).sval;
			}
break;
case 41:
//#line 328 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta identificador en declaracion de parametro");}
break;
case 42:
//#line 332 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta el tipo en declaracion de parametro");}
break;
case 43:
//#line 336 "specification.y"
{
			showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			/*chequea que el parametro haga referencia a una variable*/
			/*si es correcto seguarda en un arreglo para más adelante controlar si el tipo se corresponde*/
			ic.realParameterControl(val_peek(0).sval,scope);
		}
break;
case 44:
//#line 345 "specification.y"
{
		 	showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			ic.realParameterControl(val_peek(2).sval,scope);
			ic.realParameterControl(val_peek(0).sval,scope);
		}
break;
case 45:
//#line 353 "specification.y"
{
		 	showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			ic.realParameterControl(val_peek(4).sval,scope);
			ic.realParameterControl(val_peek(2).sval,scope);
			ic.realParameterControl(val_peek(0).sval,scope);
		}
break;
case 46:
//#line 362 "specification.y"
{
		 	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"un procedimiento puede recibir una maximo de 3 parametros");
		 	showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");
			ic.realParameterControl(val_peek(6).sval,scope);
			ic.realParameterControl(val_peek(4).sval,scope);
			ic.realParameterControl(val_peek(2).sval,scope);
		}
break;
case 47:
//#line 373 "specification.y"
{
					ic.procedureCall(val_peek(3).sval,scope);

					/*vaciamos la lista para cuando se vuelva a llamar a otro procedimiento*/
					ic.cleanRealParameters();
				}
break;
case 48:
//#line 382 "specification.y"
{
					ic.procedureCall(val_peek(2).sval,scope);

					/*vaciamos la lista para cuando se vuelva a llamar a otro procedimiento*/
					ic.cleanRealParameters();
				}
break;
case 49:
//#line 392 "specification.y"
{
					showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");
					String fullVarId = val_peek(2).sval + scope;
					String varIdDeclaration = st.findClosestIdDeclaration(fullVarId);
					/*se borra el símbolo xq se tiene que agregar uno nuevo con los datos del scope y el tipo*/
					st.removeSymbol(val_peek(2).sval);
					Triplet t;
					if(varIdDeclaration == null){
						ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se utiliza una variable antes de declararla");
						t = tm.createTriplet("=", new Operand(Operand.ST_POINTER,val_peek(2).sval + ":undefined"),(Operand) val_peek(0).obj);
					} else {
						/*se muestra el nombre de la variable con el scope en dondé se declaro, no en donde se encontró*/
						/*para hacerlo mas legible*/
						t = tm.createTriplet("=", new Operand(Operand.ST_POINTER,varIdDeclaration),(Operand) val_peek(0).obj);

						/*se incrementa la cantidad de referencias que tiene la variable*/
						st.getSymbol(varIdDeclaration).addReference();
					}
					yyval.obj = t.getId();
				}
break;
case 50:
//#line 415 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una expresion del lado derecho");}
break;
case 51:
//#line 419 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, se espera una identificador del lado izquierdo");}
break;
case 52:
//#line 423 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"asignacion erronea, ¿quisiste decir '=' ?");}
break;
case 53:
//#line 427 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 54:
//#line 431 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 55:
//#line 435 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 56:
//#line 439 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 57:
//#line 444 "specification.y"
{yyval.obj = val_peek(0).sval;}
break;
case 58:
//#line 448 "specification.y"
{yyval.obj = val_peek(0).sval;}
break;
case 59:
//#line 452 "specification.y"
{yyval.obj = val_peek(0).sval;}
break;
case 60:
//#line 456 "specification.y"
{yyval.obj = val_peek(0).sval;}
break;
case 61:
//#line 460 "specification.y"
{yyval.obj = "<";}
break;
case 62:
//#line 464 "specification.y"
{yyval.obj = ">";}
break;
case 63:
//#line 468 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token < duplicado");}
break;
case 64:
//#line 472 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"token > duplicado");}
break;
case 65:
//#line 476 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '<='?");}
break;
case 66:
//#line 480 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '>='?");}
break;
case 67:
//#line 484 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
break;
case 68:
//#line 488 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparador erroneo. ¿Quisiste decir '!='?");}
break;
case 69:
//#line 491 "specification.y"
{
													showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");
													Triplet t = tm.createTriplet((String) val_peek(1).obj, (Operand) val_peek(2).obj, (Operand) val_peek(0).obj);
													yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
													}
break;
case 70:
//#line 497 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"comparacion invalida. ¿Quisiste decir '=='?");}
break;
case 74:
//#line 511 "specification.y"
{ tm.updateOperandFromStack(1,1);
 			  tm.popFromStack();
			  tm.popFromStack();
 			  
 			}
break;
case 75:
//#line 519 "specification.y"
{
			  	tm.popFromStack(); /*desapilo el último terceto xq era el del BI*/
			  	tm.removeLastTriplet(); /*lo saco de la lista de tercetos*/
			  	/*actualizo el terceto del BF xq se realiza suponiendo que va a haber un BI*/
				tm.updateOperandFromStack(0,2);
				tm.popFromStack();
           }
break;
case 76:
//#line 529 "specification.y"
{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
break;
case 77:
//#line 533 "specification.y"
{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta palabra reservada END_INF al final de la sentencia IF"); }
break;
case 78:
//#line 537 "specification.y"
{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
break;
case 79:
//#line 541 "specification.y"
{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula ELSE_IF"); }
break;
case 80:
//#line 545 "specification.y"
{ ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"se esperaba un bloque de sentencias dentro de la clausula IF"); }
break;
case 81:
//#line 551 "specification.y"
{
				Triplet t = tm.createTriplet("BF",(Operand) val_peek(1).obj,new Operand(Operand.TO_BE_DEFINED));
				tm.pushToStack(Integer.valueOf(t.getId()));
				/*$$.obj = t.getId();  //Once again i ask for ur forgiveness my lord*/
			}
break;
case 82:
//#line 559 "specification.y"
{
				Triplet t = tm.createEmptyTriplet();
				tm.pushToStack(Integer.valueOf(t.getId()));
				yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
				ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"luego de la palabra reservada IF se espera una condicion entre parentesis");
			}
break;
case 83:
//#line 568 "specification.y"
{
			Triplet t = tm.createEmptyTriplet();
			tm.pushToStack(Integer.valueOf(t.getId()));
			yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'.");
			
			 }
break;
case 84:
//#line 578 "specification.y"
{	    	 
			Triplet t = tm.createEmptyTriplet();
			tm.pushToStack(Integer.valueOf(t.getId()));
			yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
			ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"La clausula IF requiere una condicion encerrada en '(' ')'."); 
			}
break;
case 85:
//#line 586 "specification.y"
{ 
								/* actualizo el triplete del salto del if al cuerpo de then*/
								tm.updateOperandFromStack(2,2);
								/*tm.popFromStack();*/
								/* Creo el triplete de salto incondicional para que salte el else*/
								Triplet t = tm.createBITriplet(new Operand(Operand.TO_BE_DEFINED));
								tm.pushToStack(Integer.valueOf(t.getId()));
							    }
break;
case 88:
//#line 603 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"falta la clausula UNTIL en la sentencia LOOP");}
break;
case 89:
//#line 607 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 90:
//#line 612 "specification.y"
{       
				Triplet t = tm.createBTriplet(val_peek(1).obj,"BT"); /*Desapilar la direccion de salto del comienzo del loop.*/
				/*$$.obj = new Operand(Operand.TRIPLET_POINTER,t.getId()); //finally we associate an operand created with the tiplet to the loop_condition //Comentado porque no sabemos para que lo queremos.*/
				}
break;
case 91:
//#line 619 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la clausula UNTIL debe incluir una condicion entre parentesis"); }
break;
case 92:
//#line 623 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia LOOP debe incluir una condicion encerrada por '(' ')'"); }
break;
case 93:
//#line 627 "specification.y"
{tm.pushToStack(tm.getCurrentTripletIndex() + 1); /*we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition*/}
break;
case 94:
//#line 635 "specification.y"
{
									   	Triplet t = tm.createTriplet("OUT",new Operand(Operand.ST_POINTER,(String) val_peek(1).sval));
			 						 	}
break;
case 95:
//#line 638 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT solo acepta cadenas de caracteres"); }
break;
case 96:
//#line 639 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'"); }
break;
case 97:
//#line 648 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
      		Triplet t = tm.createTriplet("+",(Operand) val_peek(2).obj, (Operand) val_peek(0).obj);
      	    yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
      		}
break;
case 98:
//#line 655 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
	      	Triplet t = tm.createTriplet("-",(Operand) val_peek(2).obj, (Operand) val_peek(0).obj);
	      	yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
	      	}
break;
case 99:
//#line 662 "specification.y"
{
			showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
			yyval.obj = val_peek(0).obj;
			}
break;
case 100:
//#line 669 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la suma debe contener un termino valido");}
break;
case 101:
//#line 673 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la resta debe contener un termino valido");}
break;
case 102:
//#line 677 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la suma debe contener una expresion valida");}
break;
case 103:
//#line 681 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la resta debe contener una expresion valida");}
break;
case 104:
//#line 685 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '+' sobrante");}
break;
case 105:
//#line 690 "specification.y"
{
		showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
		Triplet t = tm.createTriplet("*",(Operand) val_peek(2).obj, (Operand) val_peek(0).obj);
		yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
		}
break;
case 106:
//#line 698 "specification.y"
{
      showMessage("[Linea " + la.getCurrentLine() + "] Division.");
	  Triplet t = tm.createTriplet("/",(Operand) val_peek(2).obj, (Operand) val_peek(0).obj);
	  yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
	  }
break;
case 107:
//#line 706 "specification.y"
{
	  	showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	   yyval.obj = val_peek(0).obj;
	  }
break;
case 108:
//#line 711 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 109:
//#line 713 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 110:
//#line 715 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 111:
//#line 717 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 112:
//#line 719 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '*' sobrante");}
break;
case 113:
//#line 721 "specification.y"
{ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"operador '/' sobrante");}
break;
case 114:
//#line 726 "specification.y"
{
			String fullId = val_peek(0).sval + scope;
			st.removeSymbol(val_peek(0).sval); 										/*NO ENTIENDO ESTO PLEASE*/
			String realName = st.findClosestIdDeclaration(fullId);
			if(realName == null){
			    ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SEMANTICO,"se utiliza una variable antes de declararla");
				yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval + ":undefined");
			} else {
				yyval.obj = new Operand(Operand.ST_POINTER,realName);
			}				
		}
break;
case 115:
//#line 740 "specification.y"
{  			 
	    yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval);
        }
break;
case 116:
//#line 746 "specification.y"
{
			 /* Manejo la entrada positiva de esta constante		    				*/
			 Symbol positivo = st.getSymbol(val_peek(0).sval);
			 if (positivo.getDataType()==Symbol._ULONGINT_TYPE)
			 	ErrorReceiver.displayError(ErrorReceiver.ERROR,la.getCurrentLine(),ErrorReceiver.SINTACTICO,"una constante del tipo entero largo sin signo no puede ser negativa");
			 else{
			 	 st.removeSymbol(positivo.getLexeme());
				 		    				 	    				 
				 /* Creo nueva entrada o actualizo la existente con una referencia*/
				 Symbol negativo = st.getSymbol("-"+val_peek(0).sval);
				 if (negativo != null){
				 	negativo.addReference();  /* Ya existe la entrada*/
				 }else{
				 	String lexema = "-"+positivo.getLexeme();
				 	Symbol nuevoNegativo = new Symbol(lexema,positivo.getLexemeType(),positivo.getDataType());
				 	st.addSymbol(lexema,nuevoNegativo);
				 }
				 val_peek(0).sval = "-"+val_peek(0).sval;
				 
				 yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval);
			 }	
		 }
break;
//#line 1424 "Parser.java"
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
