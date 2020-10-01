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
    0,    0,    0,    1,    1,    1,    1,    2,    2,    3,
    3,    3,    3,    6,    6,    5,    5,    8,    8,    9,
    9,    9,    7,    7,    7,    7,    7,   10,   10,   10,
   10,   11,   11,   11,   12,   12,   12,   12,   12,   13,
   13,    4,    4,    4,    4,    4,    4,    4,    4,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   18,   18,
   18,   19,   19,   20,   20,   20,   15,   15,   15,   15,
   15,   15,   15,   15,   15,   15,   15,   16,   16,   16,
   16,   16,   17,   17,   17,   14,   14,   14,   14,   14,
   14,   14,   14,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   22,   22,   22,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    2,
    3,    0,   14,   13,   15,   14,   15,    1,    3,    5,
    7,    2,    1,    1,    1,    3,    5,    7,    1,    4,
    3,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    2,    2,    2,    2,
    2,    3,    3,    3,    1,    2,    8,    6,    8,    6,
    8,    6,    8,    8,    6,    6,    4,    6,    3,    6,
    6,    4,    4,    4,    2,    3,    3,    1,    3,    3,
    3,    3,    4,    3,    3,    1,    3,    3,    3,    3,
    4,    4,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   48,   46,   47,   49,
    7,    0,    0,  103,  104,    0,    0,    0,    0,    0,
   96,   85,    0,    0,    0,   65,    0,    0,    0,    0,
    0,    0,    0,    0,    6,    4,    0,   10,    0,    0,
    0,    0,    0,    0,    0,  105,    0,    0,   50,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   66,    0,   79,    0,    0,   15,   39,
    0,   41,    0,    0,    0,    0,    5,    0,    0,    0,
   99,  100,    0,    0,   58,   59,   60,    0,    0,    0,
   56,   61,   57,    0,    0,    0,    0,    0,   77,   97,
    0,   94,   98,    0,   95,   84,   83,    0,   64,    0,
   82,    0,   40,    0,   34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  101,  102,    0,    0,    0,    0,
    0,    0,   32,    0,    0,    0,   72,    0,   75,   70,
    0,   68,   76,   81,   80,   78,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   71,   73,   74,   69,   67,    0,    0,    0,    0,
    0,    0,    0,   38,    0,    0,    0,    0,    0,    0,
    0,   18,   19,    0,    0,    0,    0,   31,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   24,    0,    0,    0,    0,   21,   26,   23,    0,
   27,   25,
};
final static short yydgoto[] = {                          9,
   10,   36,   12,   13,   14,   15,   16,  194,  206,  128,
  129,   83,   17,   28,   18,   19,   20,   66,   29,   37,
   30,   31,
};
final static short yysindex[] = {                       358,
  -28,  137,  -35,    0,    0,  335,  -34, -254,    0,  420,
  -58,    0,    0, -238,    0,    0,    0,    0,    0,    0,
    0,  168,   99,    0,    0,  183, -190,  140,  352,   36,
    0,    0, -253,  -50,  -97,    0, -222,  168, -238,  165,
  198,    7,   28,   32,    0,    0,   55,    0,   24,  205,
  205,   26,   26,  403,   62,    0,   44,   50,    0,   52,
  168,  212,   15,   65,  166,  168, -199,  162,  378,   83,
   96,  105,  -28,    0,  405,    0,  148,   24,    0,    0,
  111,    0,  108,   99,   24,  164,    0,   78,   36,   36,
    0,    0,  352,  374,    0,    0,    0,   24,   78,   36,
    0,    0,    0,   78,  205,   36,   24,  352,    0,    0,
   26,    0,    0,   26,    0,    0,    0,  168,    0,  216,
    0, -111,    0,  120,    0, -113,  -82,  138,  143, -138,
  -38,  -94,   36,  -69,    0,    0,  154,  478,  155,  153,
  -77,  142,    0,  -66,  -84,  352,    0,  352,    0,    0,
  399,    0,    0,    0,    0,    0,  -39,  151,    4,  190,
  200,    6,   41,  -52, -202,  244,  260,   33,   40,   11,
  -84,    0,    0,    0,    0,    0, -231,   48,  241,   46,
   61,   63,  280,    0,   72,  -89,  278,  285,   76,  -84,
  293,    0,    0,  237,  -89,  -89,  300,    0,  -89,  420,
  245,  248,  -89,  252,  324,  264,  420,  420,  267,  420,
  420,    0,  273,  282,  420,  283,    0,    0,    0,  290,
    0,    0,
};
final static short yyrindex[] = {                       419,
  426,    0,    0,    0,    0,    0,  257,    0,    0,  427,
    0,    0,    0,  272,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  257,    0,  308,    0,
    0,    0,    0,    0,    0,    0,  220,  227,    0,  234,
    0,    0,  238,  242,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  317,    0,    0,
  397,    0,    0,  323,  338,    0,    0,    0,  -19,    3,
    0,    0,    0,    0,    0,    0,    0,  135,   25,   47,
    0,    0,    0,   69,    0,   91,  286,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -29,    0,  400,    0,
    0,    0,  113,    0,    0,    0,    0,    0,    0,  401,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  406,    0,    0,    0,    0,  408,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  414,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  315,
    0,    0,    0,    0,    0,    0,  315,  315,    0,  315,
  331,    0,    0,    0,  315,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  422,  443,    0,    0,  -73,   -7,    0,  -56,  -92,    0,
 -116,  287,    0,   35,    0,    0,    0,    0,  -18,  -13,
  -12,  -17,
};
final static int YYTABLESIZE=695;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         88,
   46,   88,   70,   88,   33,   40,   48,   55,   22,   39,
   22,   33,  127,   42,   33,   67,   71,   88,   88,   88,
   88,   92,   22,   92,   80,   92,   41,   74,  161,   47,
   21,   79,   22,   76,   91,   92,   81,   89,   90,   92,
   92,   92,   92,   91,   77,   91,   86,   91,  169,  100,
  112,  115,  106,  175,  183,  182,   49,  176,  121,  108,
  109,   91,   91,   91,   91,   90,   65,   90,   62,   90,
   27,  127,   78,  198,  101,   85,  102,   68,   56,  130,
  132,   88,   69,   90,   90,   90,   90,   87,   22,   87,
   87,   87,  133,  135,  134,   98,  136,  127,   39,  137,
  107,  139,   94,   92,   95,   87,   87,   87,   87,   89,
   96,   89,   97,   89,  213,  214,  127,  216,  217,   52,
  146,  147,  220,  116,   53,   91,  103,   89,   89,   89,
   89,   86,  162,   86,  163,   86,  117,  165,  201,  202,
   52,   51,  204,   50,  118,   53,  209,   90,  123,   86,
   86,   86,   86,   93,  122,   93,  140,   93,   73,    2,
  141,  150,  142,    3,  151,  152,    4,    5,    6,   87,
    7,   93,   93,   93,   93,   63,   26,    8,  144,    4,
    5,   27,   65,  125,   62,  143,  145,  120,  192,  193,
  153,   89,   27,   63,  154,  156,  157,   45,  158,   63,
   61,   64,  159,  111,  126,   82,   27,  174,  105,  160,
   27,  167,   27,   86,   88,   88,   72,   88,   88,   88,
  148,  149,   88,   88,   88,   88,   88,   27,  166,   88,
   88,   88,   88,   88,   32,   93,   92,   92,   38,   92,
   92,   92,   27,  171,   92,   92,   92,   92,   92,   27,
  170,   92,   92,   92,   92,   92,   27,   63,   91,   91,
   27,   91,   91,   91,   52,  172,   91,   91,   91,   91,
   91,   53,  168,   91,   91,   91,   91,   91,   51,  181,
   90,   90,   54,   90,   90,   90,   55,  177,   90,   90,
   90,   90,   90,   24,   25,   90,   90,   90,   90,   90,
  173,  186,   87,   87,  178,   87,   87,   87,  180,  179,
   87,   87,   87,   87,   87,   14,  185,   87,   87,   87,
   87,   87,  187,  190,   89,   89,   62,   89,   89,   89,
   13,  189,   89,   89,   89,   89,   89,  188,  195,   89,
   89,   89,   89,   89,   62,  196,   86,   86,  191,   86,
   86,   86,  197,  199,   86,   86,   86,   86,   86,  200,
  203,   86,   86,   86,   86,   86,   44,  207,   93,   93,
  208,   93,   93,   93,  210,   45,   93,   93,   93,   93,
   93,   43,  211,   93,   93,   93,   93,   93,  212,  215,
   63,   63,   23,   63,   63,   63,   42,  218,   63,   63,
   63,   63,   63,   23,   24,   25,  219,  221,   62,   63,
   57,   58,   59,   60,  222,   24,   25,  110,    1,  124,
   80,  104,   27,   23,  114,    3,    2,    4,    5,   24,
   25,  125,   81,   24,   25,   24,   25,   35,   54,   22,
   28,   36,   11,   93,   52,   51,   29,   50,   37,   53,
   24,   25,   44,   84,   30,   20,   75,   35,    0,    0,
   88,    0,    0,  184,    0,   24,   25,   99,    0,    0,
    0,  138,   24,   25,   35,   52,    0,   11,    0,   24,
   25,    0,   53,   24,   25,    0,    0,   52,   52,   51,
    0,    0,    0,   54,   53,   53,   35,   55,    0,    0,
    0,   51,   51,    0,    0,   54,   54,    0,    0,   55,
   55,    0,   14,    0,    0,   14,   14,   44,  155,   52,
   51,   35,   50,   14,   53,    0,    0,   13,    0,  119,
   13,   13,    0,    0,    0,    0,    0,    0,   13,    0,
    0,   62,   62,    0,   62,   62,   62,    0,    0,   62,
   62,   62,   62,   62,    0,    0,    0,    0,    0,    0,
   62,    0,    0,   44,    0,    0,   44,   44,    0,    0,
    0,    0,   45,    0,   44,   45,   45,    0,   43,    0,
    0,   43,   43,   45,    0,    0,    0,    0,    0,   43,
   34,    2,    0,   42,    0,    3,   42,   42,    4,    5,
    6,    0,    7,    0,   42,    0,    0,   43,    2,    8,
    0,    0,    3,    1,    2,    4,    5,    6,    3,    7,
    0,    4,    5,    6,    0,    7,    8,    0,    0,  131,
    2,    0,    8,  113,    3,    0,    0,    4,    5,    6,
    0,    7,  205,    0,    0,   24,   25,    0,    8,  205,
  205,    0,  205,  205,  164,    2,    0,  205,    0,    3,
   43,    2,    4,    5,    6,    3,    7,    0,    4,    5,
    6,    0,    7,    8,    0,   43,    2,    0,    0,    8,
    3,    0,    0,    4,    5,    6,    0,    7,    0,    0,
    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   59,   43,  256,   45,   40,   40,   14,   26,   61,   44,
   61,   41,   86,  268,   44,   29,  270,   59,   60,   61,
   62,   41,   61,   43,  256,   45,   61,  125,  145,  268,
   59,   39,   61,  256,   52,   53,  268,   50,   51,   59,
   60,   61,   62,   41,  267,   43,   40,   45,   45,   62,
   68,   69,   65,  256,  171,   45,   22,  260,   77,  259,
  260,   59,   60,   61,   62,   41,   43,   43,   45,   45,
   45,  145,   38,  190,   60,   41,   62,   42,  269,   93,
   94,  123,   47,   59,   60,   61,   62,   41,   61,   43,
   59,   45,  105,  111,  108,   61,  114,  171,   44,  118,
   66,  120,   41,  123,   61,   59,   60,   61,   62,   41,
   61,   43,   61,   45,  207,  208,  190,  210,  211,   42,
  259,  260,  215,   41,   47,  123,   62,   59,   60,   61,
   62,   41,  146,   43,  148,   45,   41,  151,  195,  196,
   42,   43,  199,   45,   40,   47,  203,  123,   41,   59,
   60,   61,   62,   41,   44,   43,  268,   45,  256,  257,
   41,  256,  276,  261,  259,  260,  264,  265,  266,  123,
  268,   59,   60,   61,   62,   41,   40,  275,   41,  264,
  265,   45,   43,  268,   45,  268,   44,   40,  278,  279,
  260,  123,   45,   59,   41,   41,   44,  256,  276,   60,
   61,   62,   61,   42,   41,   41,   45,  260,   43,  276,
   45,   61,   45,  123,  256,  257,  267,  259,  260,  261,
  259,  260,  264,  265,  266,  267,  268,   45,  268,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  259,
  260,  261,   45,   44,  264,  265,  266,  267,  268,   45,
   61,  271,  272,  273,  274,  275,   45,  123,  256,  257,
   45,  259,  260,  261,   45,  260,  264,  265,  266,  267,
  268,   45,  269,  271,  272,  273,  274,  275,   45,  269,
  256,  257,   45,  259,  260,  261,   45,   44,  264,  265,
  266,  267,  268,  268,  269,  271,  272,  273,  274,  275,
  260,   61,  256,  257,   45,  259,  260,  261,  269,  277,
  264,  265,  266,  267,  268,   59,  269,  271,  272,  273,
  274,  275,  277,   44,  256,  257,   41,  259,  260,  261,
   59,  269,  264,  265,  266,  267,  268,  277,   61,  271,
  272,  273,  274,  275,   59,   61,  256,  257,  277,  259,
  260,  261,  277,   61,  264,  265,  266,  267,  268,  123,
   61,  271,  272,  273,  274,  275,   59,  123,  256,  257,
  123,  259,  260,  261,  123,   59,  264,  265,  266,  267,
  268,   59,   59,  271,  272,  273,  274,  275,  125,  123,
  256,  257,  256,  259,  260,  261,   59,  125,  264,  265,
  266,  267,  268,  256,  268,  269,  125,  125,  123,  275,
  271,  272,  273,  274,  125,  268,  269,  256,    0,  256,
  256,  256,   45,  256,   47,    0,    0,  264,  265,  268,
  269,  268,  268,  268,  269,  268,  269,   41,  256,  125,
   41,   41,    0,   41,   42,   43,   41,   45,   41,   47,
  268,  269,   10,  256,   41,  125,   35,  123,   -1,   -1,
  256,   -1,   -1,  177,   -1,  268,  269,  256,   -1,   -1,
   -1,  256,  268,  269,  123,  256,   -1,   35,   -1,  268,
  269,   -1,  256,  268,  269,   -1,   -1,  268,  269,  256,
   -1,   -1,   -1,  256,  268,  269,  123,  256,   -1,   -1,
   -1,  268,  269,   -1,   -1,  268,  269,   -1,   -1,  268,
  269,   -1,  256,   -1,   -1,  259,  260,   75,   41,   42,
   43,  123,   45,  267,   47,   -1,   -1,  256,   -1,  125,
  259,  260,   -1,   -1,   -1,   -1,   -1,   -1,  267,   -1,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,  264,
  265,  266,  267,  268,   -1,   -1,   -1,   -1,   -1,   -1,
  275,   -1,   -1,  256,   -1,   -1,  259,  260,   -1,   -1,
   -1,   -1,  256,   -1,  267,  259,  260,   -1,  256,   -1,
   -1,  259,  260,  267,   -1,   -1,   -1,   -1,   -1,  267,
  256,  257,   -1,  256,   -1,  261,  259,  260,  264,  265,
  266,   -1,  268,   -1,  267,   -1,   -1,  256,  257,  275,
   -1,   -1,  261,  256,  257,  264,  265,  266,  261,  268,
   -1,  264,  265,  266,   -1,  268,  275,   -1,   -1,  256,
  257,   -1,  275,  256,  261,   -1,   -1,  264,  265,  266,
   -1,  268,  200,   -1,   -1,  268,  269,   -1,  275,  207,
  208,   -1,  210,  211,  256,  257,   -1,  215,   -1,  261,
  256,  257,  264,  265,  266,  261,  268,   -1,  264,  265,
  266,   -1,  268,  275,   -1,  256,  257,   -1,   -1,  275,
  261,   -1,   -1,  264,  265,  266,   -1,  268,   -1,   -1,
   -1,   -1,   -1,   -1,  275,
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
"sentences_proc : sentence ';'",
"sentences_proc : sentence ';' sentences_proc",
"sentences_proc :",
"procedure : PROC ID '(' parameter_list ')' NA '=' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"procedure : PROC ID '(' ')' NA '=' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"procedure : PROC ID '(' parameter_list ')' NA '=' '-' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"procedure : PROC ID '(' ')' NA '=' '-' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
"procedure : PROC ID '(' error ')' NA '=' '-' CONSTANT SHADOWING '=' true_false '{' sentences_proc '}'",
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

//#line 209 "specification.y"

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
//#line 549 "Parser.java"
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
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mala, maldita.");}
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
case 23:
//#line 59 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 24:
//#line 60 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 25:
//#line 61 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 26:
//#line 62 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 27:
//#line 63 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 31:
//#line 69 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
break;
case 33:
//#line 73 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 34:
//#line 74 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 36:
//#line 78 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 38:
//#line 80 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 39:
//#line 81 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 42:
//#line 88 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
break;
case 43:
//#line 89 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 44:
//#line 90 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 45:
//#line 91 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 46:
//#line 92 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 47:
//#line 93 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 48:
//#line 94 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 49:
//#line 95 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 56:
//#line 104 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 57:
//#line 105 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 58:
//#line 106 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 59:
//#line 107 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 60:
//#line 108 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 61:
//#line 109 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 62:
//#line 112 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
break;
case 63:
//#line 113 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 69:
//#line 127 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 70:
//#line 128 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 71:
//#line 129 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 72:
//#line 130 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 73:
//#line 131 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 74:
//#line 132 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 75:
//#line 133 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 76:
//#line 134 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 77:
//#line 135 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 79:
//#line 139 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 80:
//#line 140 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
break;
case 81:
//#line 141 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 82:
//#line 142 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 84:
//#line 150 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 85:
//#line 151 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 86:
//#line 158 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 87:
//#line 159 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 88:
//#line 160 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
break;
case 89:
//#line 161 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 90:
//#line 162 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 91:
//#line 163 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 92:
//#line 164 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 93:
//#line 165 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 94:
//#line 168 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 95:
//#line 169 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Division.");}
break;
case 96:
//#line 170 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 97:
//#line 171 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 98:
//#line 172 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 99:
//#line 173 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 100:
//#line 174 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 101:
//#line 175 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 102:
//#line 176 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 105:
//#line 182 "specification.y"
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
//#line 994 "Parser.java"
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
