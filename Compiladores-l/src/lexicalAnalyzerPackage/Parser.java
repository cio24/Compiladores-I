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
   12,   12,   13,   13,   13,   13,   14,   14,    4,    4,
    4,    4,    4,    4,    4,    4,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   20,   20,
   21,   21,   21,   16,   16,   16,   16,   16,   16,   16,
   22,   22,   22,   22,   23,   24,   17,   17,   17,   26,
   26,   26,   25,   18,   18,   18,   15,   15,   15,   15,
   15,   15,   15,   15,   27,   27,   27,   27,   27,   27,
   27,   27,   27,   28,   28,   28,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    7,
    6,    6,    5,    6,    5,    7,    6,    5,    5,    5,
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
    0,    8,    9,    0,   12,   11,   55,   53,   54,   56,
    0,    7,    0,    0,  114,  115,    0,    0,    0,    0,
    0,    0,  107,   96,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    6,    4,    0,   10,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  116,    0,
    0,   57,    0,    0,    0,    0,    0,    0,    0,   84,
    0,   85,    0,    0,    0,    0,    0,    0,   15,    0,
   48,    0,    0,    0,    0,   42,    0,    0,    0,    0,
    5,    0,    0,   73,    0,   72,   88,    0,    0,    0,
    0,  110,  111,   82,   81,   65,   66,   67,    0,   63,
   68,   64,    0,    0,    0,    0,    0,    0,    0,   80,
   77,    0,   75,  108,    0,  105,  109,    0,  106,   95,
   94,    0,   47,    0,    0,    0,    0,    0,    0,   40,
    0,    0,    0,   71,    0,   92,   87,    0,   86,    0,
    0,    0,  112,  113,    0,    0,    0,    0,    0,    0,
    0,    0,   25,    0,    0,    0,    0,    0,   78,   79,
   76,   74,    0,    0,   21,    0,    0,    0,    0,    0,
    0,   35,    0,   24,    0,   89,   91,   90,    0,   26,
   20,    0,    0,    0,    0,    0,   34,   33,    0,    0,
   18,   19,   29,    0,   32,   31,   30,   28,    0,   46,
   27,   39,
};
final static short yydgoto[] = {                          9,
   10,   50,   12,   13,   14,   15,   16,  203,   89,  139,
  163,   90,   82,   17,   29,   18,   19,   20,   69,   30,
  149,   31,   73,  150,   21,  147,   32,   33,
};
final static short yysindex[] = {                       359,
   -4,  -28,  -35,    0,    0,    0,  -34,  -39,    0,  365,
  -46,    0,    0, -239,    0,    0,    0,    0,    0,    0,
  235,    0,   60,  122,    0,    0,   82, -192,  157,   55,
  252,   52,    0,    0, -223,   60, -239,  -25,  104,   53,
   15,   62,   56,    0,    0,   95,    0,   -1,  288,   96,
 -187,   73,  126,  126,   33,   33,  100,  118,    0,  101,
  116,    0,  128,   60,  106,  131,   38,  176,   60,    0,
  -53,    0,  -72,   16,  166,  154,  158,   73,    0,  159,
    0,  160,  122,   73,  156,    0,  -54,  -64,  164,  243,
    0,  265,   -4,    0,  321,    0,    0,   -6,  136,   52,
   52,    0,    0,    0,    0,    0,    0,    0,   73,    0,
    0,    0,  136,  126,   52,  136,   52,   73,  267,    0,
    0,  282,    0,    0,   33,    0,    0,   33,    0,    0,
    0,   41,    0,  283,  -54,  286,  -12,   67,  200,    0,
  -54, -189,   60,    0,  181,    0,    0,   52,    0,   71,
  -52, -123,    0,    0,  301,  -54,  200,  -54,   72, -210,
   77,  338,    0,  200,  302,  312,  437,  320,    0,    0,
    0,    0,   99,  200,    0,  200,  307,  -16,  310,  315,
  -36,    0,  344,    0, -189,    0,    0,    0,  336,    0,
    0,  -97,  119,  -97,  -97,  -97,    0,    0,  345,  125,
    0,    0,    0,  -97,    0,    0,    0,    0, -189,    0,
    0,    0,
};
final static short yyrindex[] = {                       402,
  404,    0,    0,    0,    0,    0,  -44,    0,    0,  408,
    0,    0,    0,   32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  304,
    0,  -41,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,   36,    0,    0,    0,    0,    0,    0,    0,  199,
  203,    0,  206,    0,  220,  225,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   39,    0,  376,
    0,    0,   54,   58,    0,    0,    0,   -9,    0,  382,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -19,
    3,    0,    0,    0,    0,    0,    0,    0,  135,    0,
    0,    0,   25,    0,   47,   69,   91,  150,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  113,    0,    0,
    0,    0,    0,    0,  385,    0,   61,    0,    0,    0,
    0,    0,    0,    0,  392,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   76,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  395,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  397,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  -38,  409,    0,    0,  298,   17,    0,  -93,  -71,    5,
   22, -112,  239,    0,  121,    0,    0,    0,    0,  -24,
    7,    0,    0,  319,    0,    0,  -17,   -3,
};
final static int YYTABLESIZE=640;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         99,
   41,   99,   58,   99,   35,   38,  138,   23,   23,   37,
   95,   27,   45,  136,   14,   81,   28,   99,   99,   99,
   99,  102,   22,  102,   23,  102,   39,   51,   46,  165,
   47,   41,   76,  145,   41,  100,  101,   72,   28,  102,
  102,  102,  102,  103,  194,  103,   77,  103,  160,  115,
  117,  102,  103,   79,   22,   87,   23,  125,  178,   23,
   28,  103,  103,  103,  103,  100,  179,  100,   97,  100,
  126,  129,  199,  146,    4,    5,   59,   28,   86,   98,
  114,   99,   28,  100,  100,  100,  100,   97,  197,   97,
   13,   97,   85,   74,   51,   70,  148,   52,   75,  205,
  206,  207,  208,  102,   28,   97,   97,   97,   97,  101,
  211,  101,   50,  101,   91,   67,   49,   68,  166,   23,
  168,  153,   23,  183,  154,  103,   28,  101,  101,  101,
  101,   98,  171,   98,   22,   98,  172,  212,   37,  157,
  104,   55,   53,   52,   54,  164,   56,  100,   28,   98,
   98,   98,   98,  104,   96,  104,   78,  104,  105,   84,
  174,  106,  176,   55,   53,  110,   54,  111,   56,   97,
   28,  104,  104,  104,  104,   70,  107,   55,  175,  204,
  201,  202,   56,  121,  109,  184,  122,  123,  108,  118,
   69,  101,  112,   70,  130,  190,  135,  191,  131,   67,
  133,   68,  132,  140,  141,  119,  120,  170,   69,   44,
   28,   14,  128,   98,   99,   99,   65,   64,   66,   99,
   28,  137,   99,   99,   99,   28,   99,   24,   40,   99,
   99,   99,   99,   99,   34,  104,  102,  102,   36,   25,
   26,  102,   80,   59,  102,  102,  102,   60,  102,   24,
   58,  102,  102,  102,  102,  102,  159,   70,  103,  103,
  193,   25,   26,  103,   61,   92,  103,  103,  103,   62,
  103,  124,   69,  103,  103,  103,  103,  103,    4,    5,
  100,  100,   86,   25,   26,  100,  142,   13,  100,  100,
  100,   51,  100,  113,   52,  100,  100,  100,  100,  100,
   25,   26,   97,   97,  143,   25,   26,   97,  155,   50,
   97,   97,   97,   49,   97,   24,   23,   97,   97,   97,
   97,   97,  162,  156,  101,  101,  158,   25,   26,  101,
  169,   22,  101,  101,  101,  161,  101,   57,   88,  101,
  101,  101,  101,  101,  173,  185,   98,   98,  177,   25,
   26,   98,  186,  180,   98,   98,   98,   49,   98,   83,
  188,   98,   98,   98,   98,   98,  189,  192,  104,  104,
  195,   25,   26,  104,   49,  196,  104,  104,  104,  200,
  104,   99,   88,  104,  104,  104,  104,  104,  209,   49,
   70,   70,   80,   25,   26,   70,  201,  202,   70,   70,
   70,    1,   70,    3,   49,   69,   69,    2,   11,   70,
   69,  134,   94,   69,   69,   69,   43,   69,   43,    4,
    5,  127,   36,   86,   69,   44,   83,   60,   61,   62,
   63,  116,   37,   25,   26,   45,  167,   38,  210,   88,
  152,    0,    0,   25,   26,  144,    0,    0,   25,   26,
    0,    0,    0,    0,   59,    0,    0,   11,   60,    0,
    0,   58,  182,    0,    0,    0,   59,   59,  198,    0,
   60,   60,    0,   58,   58,   61,    0,  187,   55,   53,
   62,   54,   88,   56,    0,    0,    0,   61,   61,    0,
   48,    2,   62,   62,    0,    3,    0,    0,    4,    5,
    6,    0,    7,   43,    0,    0,   88,   71,    2,    8,
    0,    0,    3,    0,    0,    4,    5,    6,    0,    7,
    0,    0,   42,    2,    0,    0,    8,    3,    0,    0,
    4,    5,    6,    0,    7,    0,    0,  151,    2,    0,
    0,    8,    3,   93,    2,    4,    5,    6,    3,    7,
    0,    4,    5,    6,    0,    7,    8,    0,    0,   83,
   83,    0,    8,    0,   83,    0,    0,   83,   83,   83,
   11,   83,    0,    0,    0,    0,   42,    2,   83,    0,
    0,    3,    0,    0,    4,    5,    6,    0,    7,    0,
    0,   43,    0,  181,    2,    8,    0,    0,    3,   42,
    2,    4,    5,    6,    3,    7,    0,    4,    5,    6,
    0,    7,    8,    0,    1,    2,    0,    0,    8,    3,
   42,    2,    4,    5,    6,    3,    7,    0,    4,    5,
    6,    0,    7,    8,    0,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   27,   45,   40,   40,   61,   61,   61,   44,
   49,   40,   59,   85,   59,   41,   45,   59,   60,   61,
   62,   41,   59,   43,   61,   45,   61,   21,  268,  142,
   14,   41,  256,   40,   44,   53,   54,   31,   45,   59,
   60,   61,   62,   41,   61,   43,  270,   45,   61,   67,
   68,   55,   56,   37,   59,   41,   61,   42,  269,   61,
   45,   59,   60,   61,   62,   41,  277,   43,  256,   45,
   74,   75,  185,   98,  264,  265,  269,   45,  268,  267,
   43,  123,   45,   59,   60,   61,   62,   41,  125,   43,
   59,   45,   40,   42,   59,   41,  114,   59,   47,  193,
  194,  195,  196,  123,   45,   59,   60,   61,   62,   41,
  204,   43,   59,   45,   59,   43,   59,   45,  143,   59,
  145,  125,   61,  162,  128,  123,   45,   59,   60,   61,
   62,   41,  256,   43,   59,   45,  260,  209,   44,  135,
   41,   42,   43,   23,   45,  141,   47,  123,   45,   59,
   60,   61,   62,   41,   59,   43,   36,   45,   41,   39,
  156,   61,  158,   42,   43,   60,   45,   62,   47,  123,
   45,   59,   60,   61,   62,   41,   61,   42,  157,   61,
  278,  279,   47,  256,   64,  164,  259,  260,   61,   69,
   41,  123,   62,   59,   41,  174,   41,  176,   41,   43,
   41,   45,   44,  268,   41,  259,  260,  260,   59,  256,
   45,  256,   47,  123,  256,  257,   60,   61,   62,  261,
   45,  276,  264,  265,  266,   45,  268,  256,  268,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  268,
  269,  261,  268,   45,  264,  265,  266,   45,  268,  256,
   45,  271,  272,  273,  274,  275,  269,  123,  256,  257,
  277,  268,  269,  261,   45,  267,  264,  265,  266,   45,
  268,  256,  123,  271,  272,  273,  274,  275,  264,  265,
  256,  257,  268,  268,  269,  261,   44,  256,  264,  265,
  266,  256,  268,  256,  256,  271,  272,  273,  274,  275,
  268,  269,  256,  257,   40,  268,  269,  261,  268,  256,
  264,  265,  266,  256,  268,  256,  256,  271,  272,  273,
  274,  275,  123,   41,  256,  257,   41,  268,  269,  261,
  260,  256,  264,  265,  266,  269,  268,  256,   41,  271,
  272,  273,  274,  275,   44,   44,  256,  257,  277,  268,
  269,  261,   41,  277,  264,  265,  266,  123,  268,  256,
   41,  271,  272,  273,  274,  275,  268,   61,  256,  257,
   61,  268,  269,  261,  123,   61,  264,  265,  266,   44,
  268,  256,   85,  271,  272,  273,  274,  275,   44,  123,
  256,  257,  268,  268,  269,  261,  278,  279,  264,  265,
  266,    0,  268,    0,  123,  256,  257,    0,    0,  275,
  261,  256,  125,  264,  265,  266,   41,  268,   10,  264,
  265,  256,   41,  268,  275,   41,  123,  271,  272,  273,
  274,  256,   41,  268,  269,   41,  256,   41,  200,  142,
  122,   -1,   -1,  268,  269,  125,   -1,   -1,  268,  269,
   -1,   -1,   -1,   -1,  256,   -1,   -1,   49,  256,   -1,
   -1,  256,  125,   -1,   -1,   -1,  268,  269,  125,   -1,
  268,  269,   -1,  268,  269,  256,   -1,   41,   42,   43,
  256,   45,  185,   47,   -1,   -1,   -1,  268,  269,   -1,
  256,  257,  268,  269,   -1,  261,   -1,   -1,  264,  265,
  266,   -1,  268,   95,   -1,   -1,  209,  256,  257,  275,
   -1,   -1,  261,   -1,   -1,  264,  265,  266,   -1,  268,
   -1,   -1,  256,  257,   -1,   -1,  275,  261,   -1,   -1,
  264,  265,  266,   -1,  268,   -1,   -1,  256,  257,   -1,
   -1,  275,  261,  256,  257,  264,  265,  266,  261,  268,
   -1,  264,  265,  266,   -1,  268,  275,   -1,   -1,  256,
  257,   -1,  275,   -1,  261,   -1,   -1,  264,  265,  266,
  162,  268,   -1,   -1,   -1,   -1,  256,  257,  275,   -1,
   -1,  261,   -1,   -1,  264,  265,  266,   -1,  268,   -1,
   -1,  183,   -1,  256,  257,  275,   -1,   -1,  261,  256,
  257,  264,  265,  266,  261,  268,   -1,  264,  265,  266,
   -1,  268,  275,   -1,  256,  257,   -1,   -1,  275,  261,
  256,  257,  264,  265,  266,  261,  268,   -1,  264,  265,
  266,   -1,  268,  275,   -1,   -1,   -1,   -1,   -1,  275,
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

//#line 283 "specification.y"

public LexicalAnalyzer la;
public IntermediateCode ic;

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
	ic = new IntermediateCode();
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

public Triplet createTriplet(String optString,Object obj1, Object obj2){
	Operator opt = new Operator(optString);
	return createTriplet(opt,obj1,obj2);
}

public Triplet createTriplet(Operator optObj,Object obj1, Object obj2){
	Operator opt = (Operator) optObj;
	Operand op1 = (Operand) obj1;
	Operand op2 = (Operand) obj2;
	Triplet t = new Triplet(opt,op1,op2);
	ic.addTriplet(t);
	return t;
}

public Triplet createBFTriplet(Object obj1){
	int unstacked = ic.topOfStack(); //we get the id of the triplet that represent the adress of the tag that we need to jump
    ic.popFromStack(); //we remove the id triplet from the top of the stack
    Operand op1 = (Operand) obj1; //we get the triplet asociate to the condition
    Operand op2 = new Operand(Operand.TRIPLET_POINTER,String.valueOf(unstacked)); //this will contain the jump adress
    Operator opt = new Operator("BF"); //the operation of the tiplet is the branch not equal
    Triplet t = new Triplet(opt,op1,op2);
    ic.addTriplet(t); //then we save the triplet in order to retrieve it later for the generation of the code
    return t;
}

public Triplet createBITriplet(Object obj1){
	updateSecondOperandFromStack(2);
	Operand op1 = (Operand) obj1;
	Operator opt = new Operator("BI");
	Triplet t = new Triplet (opt,op1);
	ic.addTriplet(t);
	return t;
}

public void updateSecondOperandFromStack(int amount){
	int unstacked = ic.topOfStack(); //we get the id of the triplet on the top of the stack
	ic.popFromStack(); //we remove the id triplet from the top of the stack
	Triplet trip = ic.getTriplet(unstacked); //then we get the triplet so we can write in the second operand
	trip.modifySecondOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(ic.currentTripletIndex()+amount))); //the adress of the jump
}

public void updateFirstOperandFromStack(int amount){
	int unstacked = ic.topOfStack(); //we get the id of the triplet on the top of the stack
	ic.popFromStack(); //we remove the id triplet from the top of the stack
	Triplet trip = ic.getTriplet(unstacked); //then we get the triplet so we can write in the second operand
	trip.modifyFirstOperand(new Operand(Operand.TRIPLET_POINTER,String.valueOf(ic.currentTripletIndex()+amount))); //the adress of the jump
}

public Triplet createEmptyTriplet(){
	Triplet t = new Triplet(new Operator("ERROR"));
	ic.addTriplet(t);
	return t;
}
//#line 602 "Parser.java"
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
//#line 22 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] WARNING sintactico: Programa vacio!");}
break;
case 2:
//#line 23 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] Programa completo.");}
break;
case 3:
//#line 24 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] ERROR sintactico: no se encontraron sentencias validas.");}
break;
case 6:
//#line 30 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' ausente al final de la sentencia.");}
break;
case 7:
//#line 31 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");}
break;
case 10:
//#line 39 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de variable/s.");}
break;
case 11:
//#line 40 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de procedimiento.");}
break;
case 12:
//#line 41 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 13:
//#line 42 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
break;
case 20:
//#line 57 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 21:
//#line 58 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 22:
//#line 59 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 23:
//#line 60 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 24:
//#line 61 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}
break;
case 25:
//#line 62 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}
break;
case 26:
//#line 63 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se definio mal la lista de parametros.");}
break;
case 28:
//#line 67 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra NA");}
break;
case 29:
//#line 68 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del NA.");}
break;
case 30:
//#line 69 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el valor del NA.");}
break;
case 31:
//#line 70 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra SHADOWING.");}
break;
case 32:
//#line 71 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del SHADOWING.");}
break;
case 34:
//#line 76 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: cuerpo del procedimiento mal definido.");}
break;
case 36:
//#line 80 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 37:
//#line 81 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 38:
//#line 82 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 39:
//#line 83 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 41:
//#line 87 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 42:
//#line 88 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 43:
//#line 91 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
break;
case 44:
//#line 92 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
break;
case 45:
//#line 93 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
break;
case 46:
//#line 94 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 47:
//#line 97 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Llamada a procedimiento.");
      									Triplet t = createTriplet("FC",new Operand(Operand.ST_POINTER,val_peek(3).sval),new Operand(Operand.TO_BE_DEFINED,"-1"));}
break;
case 48:
//#line 99 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Llamada a procedimiento.");
      									Triplet t = createTriplet("FC",new Operand(Operand.ST_POINTER,val_peek(2).sval),new Operand(Operand.TO_BE_DEFINED,"-1"));}
break;
case 49:
//#line 103 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");
      									Triplet t = createTriplet("=",new Operand(Operand.ST_POINTER,val_peek(2).sval),val_peek(0).obj);
										yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
break;
case 50:
//#line 106 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 51:
//#line 107 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 52:
//#line 108 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 53:
//#line 109 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 54:
//#line 110 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 55:
//#line 111 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 56:
//#line 112 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 61:
//#line 119 "specification.y"
{yyval.obj = new Operator("<");}
break;
case 63:
//#line 121 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 64:
//#line 122 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 65:
//#line 123 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 66:
//#line 124 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 67:
//#line 125 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 68:
//#line 126 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 69:
//#line 129 "specification.y"
{
													showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");
													Triplet t = createTriplet((Operator)val_peek(1).obj, val_peek(2).obj,val_peek(0).obj);
													yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
													}
break;
case 70:
//#line 134 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 74:
//#line 146 "specification.y"
{ updateFirstOperandFromStack(1); }
break;
case 75:
//#line 147 "specification.y"
{ updateFirstOperandFromStack(1); }
break;
case 76:
//#line 148 "specification.y"
{ showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 77:
//#line 149 "specification.y"
{ showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 78:
//#line 150 "specification.y"
{ showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
break;
case 79:
//#line 151 "specification.y"
{ showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula ELSE_IF.");}
break;
case 80:
//#line 152 "specification.y"
{ showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
break;
case 81:
//#line 156 "specification.y"
{Triplet t = createTriplet("BF",val_peek(1).obj,new Operand(Operand.TO_BE_DEFINED,"-1"));
      									ic.pushToStack(t.getNumId());
      									yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
break;
case 82:
//#line 159 "specification.y"
{
			 							Triplet t = createEmptyTriplet();
										ic.pushToStack(t.getNumId());
      									yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
      									showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
break;
case 83:
//#line 164 "specification.y"
{
			 							Triplet t = createEmptyTriplet();
										ic.pushToStack(t.getNumId());
      									yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
      									showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 84:
//#line 169 "specification.y"
{	    	 
			 							Triplet t = createEmptyTriplet();
										ic.pushToStack(t.getNumId());
      									yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());
	    	 							showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 85:
//#line 176 "specification.y"
{ 
								Triplet t = createBITriplet(new Operand(Operand.TO_BE_DEFINED,"-1"));
							    ic.pushToStack(t.getNumId());			
							    }
break;
case 88:
//#line 186 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 89:
//#line 187 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 90:
//#line 190 "specification.y"
{       
							   		Triplet t = createBFTriplet(val_peek(1).obj);
						       		yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId()); /*finally we associate an operand created with the tiplet to the loop_condition*/
							        }
break;
case 91:
//#line 194 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la clausula UNTIL debe incluir una condicion entre parentesis");}
break;
case 92:
//#line 195 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 93:
//#line 198 "specification.y"
{ic.pushToStack(ic.currentTripletIndex() + 1); /*we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition*/}
break;
case 94:
//#line 206 "specification.y"
{ 
										Operand op = new Operand(Operand.TO_BE_DEFINED,val_peek(1).sval);
									   	Operator opt = new Operator("OUT");
									   	Triplet t = new Triplet(opt,op);
									   	ic.addTriplet(t);
			 						 	}
break;
case 95:
//#line 212 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 96:
//#line 213 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 97:
//#line 220 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
      										Triplet t = createTriplet("+",val_peek(2).obj,val_peek(0).obj);
      										yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
break;
case 98:
//#line 223 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
	      									Triplet t = createTriplet("-",val_peek(2).obj,val_peek(0).obj);
	      									yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
break;
case 99:
//#line 226 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
											 yyval.obj = val_peek(0).obj;}
break;
case 100:
//#line 228 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 101:
//#line 229 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 102:
//#line 230 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 103:
//#line 231 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 104:
//#line 232 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 105:
//#line 235 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
      							Triplet t = createTriplet("*",val_peek(2).obj,val_peek(0).obj);
      							yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
break;
case 106:
//#line 238 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      							Triplet t = createTriplet("/",val_peek(2).obj, val_peek(0).obj);
      							yyval.obj = new Operand(Operand.TRIPLET_POINTER,t.getId());}
break;
case 107:
//#line 241 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 	yyval.obj = val_peek(0).obj;}
break;
case 108:
//#line 243 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 109:
//#line 244 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 110:
//#line 245 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 111:
//#line 246 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 112:
//#line 247 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 113:
//#line 248 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 114:
//#line 251 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 115:
//#line 252 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 116:
//#line 253 "specification.y"
{
						 /* Manejo la entrada positiva de esta constante		    				*/
	    				 Symbol positivo = la.getSymbolsTable().getSymbol(val_peek(0).sval);
	    				 if (positivo.getType()==Symbol._ULONGINT)
	    				 	showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: una constante del tipo entero largo sin signo no puede ser negativa");
	    				 else{
		    				 if(positivo.subtractReference() == 0){ /* Remove reference and if it reaches 0, remove SyboleTable entry*/
		    				 	la.getSymbolsTable().removeSymbol(positivo.getLexeme());
		    				 }
		    				 	    				 
		    				 /* Creo nueva entrada o actualizo la existente con una referencia*/
		    				 Symbol negativo = la.getSymbolsTable().getSymbol("-"+val_peek(0).sval);
		    				 if (negativo != null){
		    				 	negativo.addReference();  /* Ya existe la entrada*/
		    				 }else{
		    				 	String lexema = "-"+positivo.getLexeme();
		    				 	Symbol nuevoNegativo = new Symbol(lexema,positivo.getType());
		    				 	la.getSymbolsTable().addSymbol(lexema,nuevoNegativo);
		    				 }
		    				 val_peek(0).sval = "-"+val_peek(0).sval;
		    				 
		    				 yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval);
	    				 }		    				 		
	    			 	}
break;
//#line 1185 "Parser.java"
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
