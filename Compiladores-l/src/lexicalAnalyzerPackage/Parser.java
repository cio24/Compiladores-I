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
   18,   18,   18,   18,   18,   18,   18,   18,   19,   19,
   20,   20,   20,   15,   15,   15,   15,   15,   15,   15,
   15,   15,   15,   15,   16,   16,   16,   16,   16,   17,
   17,   17,   14,   14,   14,   14,   14,   14,   14,   14,
   21,   21,   21,   21,   21,   21,   21,   21,   22,   22,
   22,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    3,    1,    1,    2,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    2,    3,
   14,   13,   15,   14,    1,    3,    5,    7,    2,    1,
    1,    1,    3,    5,    7,    1,    4,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    2,    3,    3,
    3,    1,    2,    8,    6,    8,    6,    8,    6,    8,
    8,    6,    6,    4,    6,    3,    6,    6,    4,    4,
    4,    2,    3,    3,    1,    3,    3,    3,    3,    4,
    3,    3,    1,    3,    3,    3,    4,    4,    1,    1,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   15,   16,    0,    0,    0,    0,    0,
    0,    7,    8,    0,   11,   10,   45,   43,   44,   46,
    0,    0,   99,  100,    0,    0,    0,    0,    0,   93,
   82,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    0,    9,    0,    0,    0,
    0,    0,    0,  101,    0,    0,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   63,    0,   76,    0,    0,   14,   36,    0,   38,    0,
    0,    0,    0,    5,    6,    0,    0,    0,   96,    0,
    0,   55,   56,   57,    0,    0,    0,   53,   58,   54,
    0,    0,    0,    0,    0,   74,   94,    0,   91,   95,
    0,   92,   81,   80,    0,   61,    0,   79,    0,   37,
   31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   97,   98,    0,    0,    0,    0,    0,   29,    0,    0,
    0,   69,    0,   72,   67,    0,   65,   73,   78,   77,
   75,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   68,   70,   71,   66,   64,    0,
    0,    0,    0,    0,    0,   35,    0,    0,    0,    0,
    0,   17,   18,    0,    0,    0,    0,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   22,    0,
    0,    0,   20,   24,   21,    0,   23,
};
final static short yydgoto[] = {                          9,
   10,   35,   12,   13,   14,   15,   16,  184,  194,  124,
  125,   80,   17,   27,   18,   19,   20,   64,   28,   36,
   29,   30,
};
final static short yysindex[] = {                       412,
  -32,  137,  -35,    0,    0,  335,  -34, -252,    0,  418,
  -45,    0,    0, -223,    0,    0,    0,    0,    0,    0,
  167,   72,    0,    0,  172, -210,  140,  352,   27,    0,
    0, -231,  -58,  -97,    0, -206,  167, -223,  164,  183,
   62,  -32,    8,   52,    0,   79,    0,   73,  198,  198,
   33,   48,   86,    0,   82,   83,    0,   84,  167,  205,
   75,   71,  165,  167, -179,  162,  377,  101,  105,  107,
    0,  397,    0,  148,   73,    0,    0,  111,    0,  108,
   72,   73,   15,    0,    0,  110,   27,   27,    0,  352,
  374,    0,    0,    0,   73,  110,   27,    0,    0,    0,
  110,  198,   27,   73,  352,    0,    0,   33,    0,    0,
   33,    0,    0,    0,  167,    0,  212,    0, -107,    0,
    0, -114, -105,  124,  122, -121,  -38, -247,   27,  -81,
    0,    0,  139,   58,  143,  142,  120,    0,  -89, -144,
  352,    0,  352,    0,    0,  391,    0,    0,    0,    0,
    0,  -79,    4,  130,  151,  -62,  -47,  -54, -209,  185,
  -33,  -18,   26, -144,    0,    0,    0,    0,    0,   -2,
  244,   11,   32,   41,  272,    0, -138,  256,  263,   54,
 -144,    0,    0,  215, -138, -138,  278,    0,  418,  223,
  226, -138,  294,  229,  418,  418,  237,  418,    0,  236,
  243,  418,    0,    0,    0,  246,    0,
};
final static short yyrindex[] = {                       375,
  383,    0,    0,    0,    0,    0,  264,    0,    0,  389,
    0,    0,    0,  273,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  264,    0,  308,    0,    0,
    0,    0,    0,    0,  216,  220,    0,  227,    0,    0,
  242,  249,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  317,    0,    0,  349,    0,    0,
  323,  338,    0,    0,    0,    0,  -19,    3,    0,    0,
    0,    0,    0,    0,  135,   25,   47,    0,    0,    0,
   69,    0,   91,  286,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   81,    0,  357,    0,    0,    0,  113,    0,
    0,    0,    0,    0,    0,  366,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  367,    0,    0,    0,    0,  378,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  384,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  290,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  392,    1,    0,    0,  -68,   16,    0, -154, -119,    0,
 -106,    0,    0,   -4,    0,    0,    0,    0,  -17,  416,
  399,  -14,
};
final static int YYTABLESIZE=693;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         85,
   11,   85,   21,   85,   32,   39,   21,   53,  145,   38,
   43,  146,  147,   45,  123,   41,   48,   85,   85,   85,
   85,   89,   21,   89,   68,   89,   40,   71,   21,   47,
  190,  191,   75,  155,   11,   82,   89,  197,   69,   89,
   89,   89,   89,   88,   46,   88,  168,   88,  162,   73,
  169,  109,  112,   76,   95,  122,  118,  175,   54,  104,
   74,   88,   88,   88,   88,   87,   84,   87,   66,   87,
  174,  123,   43,   67,  188,  200,  201,   26,  203,  105,
  106,   85,  206,   87,   87,   87,   87,   84,   90,   84,
   50,   84,   49,  131,   51,  123,  132,  133,  150,  135,
   50,   83,   49,   89,   51,   84,   84,   84,   84,   86,
   85,   86,  123,   86,   50,   63,   49,   60,   51,    4,
    5,   30,   38,  121,   30,   88,   91,   86,   86,   86,
   86,   83,  100,   83,   98,   83,   99,  141,  142,  182,
  183,  113,   92,   93,   94,  114,  115,   87,  120,   83,
   83,   83,   83,   90,  119,   90,   51,   90,   42,    2,
  136,  137,  138,    3,  139,  140,    4,    5,    6,   84,
    7,   90,   90,   90,   90,   60,   25,    8,  148,  149,
  153,   26,   63,  151,   60,  152,  154,  117,  160,  193,
  163,   86,   26,   60,  164,  193,  193,  165,  193,   61,
   59,   62,  193,  108,   79,  167,   26,  102,   70,   26,
   44,   26,  166,   83,   85,   85,   26,   85,   85,   85,
  143,  144,   85,   85,   85,   85,   85,   26,  170,   85,
   85,   85,   85,   85,   31,   90,   89,   89,   37,   89,
   89,   89,   26,  171,   89,   89,   89,   89,   89,   26,
  172,   89,   89,   89,   89,   89,   26,   60,   88,   88,
   49,   88,   88,   88,   50,  176,   88,   88,   88,   88,
   88,   48,  161,   88,   88,   88,   88,   88,    4,    5,
   87,   87,  121,   87,   87,   87,   51,  178,   87,   87,
   87,   87,   87,   52,  173,   87,   87,   87,   87,   87,
   23,   24,   84,   84,  177,   84,   84,   84,  179,  180,
   84,   84,   84,   84,   84,  181,  185,   84,   84,   84,
   84,   84,   13,  186,   86,   86,   59,   86,   86,   86,
  187,   12,   86,   86,   86,   86,   86,  189,  192,   86,
   86,   86,   86,   86,   59,  195,   83,   83,  196,   83,
   83,   83,  198,  199,   83,   83,   83,   83,   83,  202,
  204,   83,   83,   83,   83,   83,   41,  205,   90,   90,
  207,   90,   90,   90,    1,   42,   90,   90,   90,   90,
   90,   40,    3,   90,   90,   90,   90,   90,    2,   32,
   60,   60,   22,   60,   60,   60,   39,   25,   60,   60,
   60,   60,   60,   22,   23,   24,   33,   26,   59,   60,
   55,   56,   57,   58,   19,   23,   24,  107,   34,   77,
  101,   26,   22,  111,   27,   72,    0,   52,    0,   23,
   24,   78,   23,   24,   23,   24,    0,    0,   81,   23,
   24,    0,    0,   65,    0,    0,    0,   87,   88,    0,
   23,   24,    0,   86,    0,    0,    0,   34,   97,    0,
   96,  103,    0,    0,    0,   23,   24,  134,    0,    0,
    0,   49,   23,   24,   34,   50,    0,    0,    0,   23,
   24,    0,   48,   49,   49,    0,    0,   50,   50,    0,
    0,    0,    0,    0,   48,   48,   34,   51,    0,    0,
  129,    0,    0,    0,   52,  126,  128,    0,    0,   51,
   51,    0,    0,   34,    0,    0,   52,   52,    0,   13,
  130,  116,   13,   13,    0,    0,    0,    0,   12,    0,
   13,   12,   12,    0,    0,    0,    0,    0,    0,   12,
    0,   59,   59,    0,   59,   59,   59,    0,    0,   59,
   59,   59,   59,   59,    0,    0,  156,    0,  157,    0,
   59,  159,    0,   41,    0,    0,   41,   41,    0,    0,
    0,    0,   42,    0,   41,   42,   42,    0,   40,    0,
    0,   40,   40,   42,    0,    0,    0,    0,    0,   40,
   33,    2,    0,   39,    0,    3,   39,   39,    4,    5,
    6,    0,    7,    0,   39,    0,    0,   42,    2,    8,
    0,    0,    3,    0,    0,    4,    5,    6,    0,    7,
    0,    0,    0,    0,    0,    0,    8,    0,    0,  127,
    2,    0,  110,    0,    3,    0,    0,    4,    5,    6,
    0,    7,    0,    0,   23,   24,  158,    2,    8,    0,
    0,    3,   42,    2,    4,    5,    6,    3,    7,    0,
    4,    5,    6,    0,    7,    8,    0,    1,    2,    0,
    0,    8,    3,   42,    2,    4,    5,    6,    3,    7,
    0,    4,    5,    6,    0,    7,    8,    0,    0,    0,
    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   61,   45,   40,   40,   61,   25,  256,   44,
   10,  259,  260,   59,   83,  268,   21,   59,   60,   61,
   62,   41,   61,   43,  256,   45,   61,  125,   61,   14,
  185,  186,   37,  140,   34,   40,   51,  192,  270,   59,
   60,   61,   62,   41,  268,   43,  256,   45,   45,  256,
  260,   66,   67,   38,   59,   41,   74,  164,  269,   64,
  267,   59,   60,   61,   62,   41,   59,   43,   42,   45,
   45,  140,   72,   47,  181,  195,  196,   45,  198,  259,
  260,  123,  202,   59,   60,   61,   62,   41,   41,   43,
   43,   45,   45,  108,   47,  164,  111,  115,   41,  117,
   43,   40,   45,  123,   47,   59,   60,   61,   62,   41,
   59,   43,  181,   45,   43,   43,   45,   45,   47,  264,
  265,   41,   44,  268,   44,  123,   41,   59,   60,   61,
   62,   41,   62,   43,   60,   45,   62,  259,  260,  278,
  279,   41,   61,   61,   61,   41,   40,  123,   41,   59,
   60,   61,   62,   41,   44,   43,   47,   45,  256,  257,
  268,  276,  268,  261,   41,   44,  264,  265,  266,  123,
  268,   59,   60,   61,   62,   41,   40,  275,  260,   41,
   61,   45,   43,   41,   45,   44,  276,   40,  268,  189,
   61,  123,   45,   59,   44,  195,  196,  260,  198,   60,
   61,   62,  202,   42,   41,  260,   45,   43,  267,   45,
  256,   45,  260,  123,  256,  257,   45,  259,  260,  261,
  259,  260,  264,  265,  266,  267,  268,   45,   44,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  259,
  260,  261,   45,  277,  264,  265,  266,  267,  268,   45,
  269,  271,  272,  273,  274,  275,   45,  123,  256,  257,
   45,  259,  260,  261,   45,  268,  264,  265,  266,  267,
  268,   45,  269,  271,  272,  273,  274,  275,  264,  265,
  256,  257,  268,  259,  260,  261,   45,  277,  264,  265,
  266,  267,  268,   45,  269,  271,  272,  273,  274,  275,
  268,  269,  256,  257,   61,  259,  260,  261,  277,  269,
  264,  265,  266,  267,  268,   44,   61,  271,  272,  273,
  274,  275,   59,   61,  256,  257,   41,  259,  260,  261,
  277,   59,  264,  265,  266,  267,  268,  123,   61,  271,
  272,  273,  274,  275,   59,  123,  256,  257,  123,  259,
  260,  261,   59,  125,  264,  265,  266,  267,  268,  123,
  125,  271,  272,  273,  274,  275,   59,  125,  256,  257,
  125,  259,  260,  261,    0,   59,  264,  265,  266,  267,
  268,   59,    0,  271,  272,  273,  274,  275,    0,   41,
  256,  257,  256,  259,  260,  261,   59,   41,  264,  265,
  266,  267,  268,  256,  268,  269,   41,   41,  123,  275,
  271,  272,  273,  274,  125,  268,  269,  256,   41,  256,
  256,   45,  256,   47,   41,   34,   -1,  256,   -1,  268,
  269,  268,  268,  269,  268,  269,   -1,   -1,  256,  268,
  269,   -1,   -1,   28,   -1,   -1,   -1,   49,   50,   -1,
  268,  269,   -1,  256,   -1,   -1,   -1,  123,   60,   -1,
  256,   63,   -1,   -1,   -1,  268,  269,  256,   -1,   -1,
   -1,  256,  268,  269,  123,  256,   -1,   -1,   -1,  268,
  269,   -1,  256,  268,  269,   -1,   -1,  268,  269,   -1,
   -1,   -1,   -1,   -1,  268,  269,  123,  256,   -1,   -1,
  102,   -1,   -1,   -1,  256,   90,   91,   -1,   -1,  268,
  269,   -1,   -1,  123,   -1,   -1,  268,  269,   -1,  256,
  105,  125,  259,  260,   -1,   -1,   -1,   -1,  256,   -1,
  267,  259,  260,   -1,   -1,   -1,   -1,   -1,   -1,  267,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,  264,
  265,  266,  267,  268,   -1,   -1,  141,   -1,  143,   -1,
  275,  146,   -1,  256,   -1,   -1,  259,  260,   -1,   -1,
   -1,   -1,  256,   -1,  267,  259,  260,   -1,  256,   -1,
   -1,  259,  260,  267,   -1,   -1,   -1,   -1,   -1,  267,
  256,  257,   -1,  256,   -1,  261,  259,  260,  264,  265,
  266,   -1,  268,   -1,  267,   -1,   -1,  256,  257,  275,
   -1,   -1,  261,   -1,   -1,  264,  265,  266,   -1,  268,
   -1,   -1,   -1,   -1,   -1,   -1,  275,   -1,   -1,  256,
  257,   -1,  256,   -1,  261,   -1,   -1,  264,  265,  266,
   -1,  268,   -1,   -1,  268,  269,  256,  257,  275,   -1,
   -1,  261,  256,  257,  264,  265,  266,  261,  268,   -1,
  264,  265,  266,   -1,  268,  275,   -1,  256,  257,   -1,
   -1,  275,  261,  256,  257,  264,  265,  266,  261,  268,
   -1,  264,  265,  266,   -1,  268,  275,   -1,   -1,   -1,
   -1,   -1,  275,
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
"sentences : sentence error ';'",
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
"term : error '/' factor",
"term : term '*' '*' factor",
"term : term '/' '/' factor",
"factor : ID",
"factor : CONSTANT",
"factor : '-' CONSTANT",
};

//#line 205 "specification.y"

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
//#line 539 "Parser.java"
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
//#line 35 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaracion de variable.");}
break;
case 10:
//#line 36 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Declaracion PROC.");}
break;
case 11:
//#line 37 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 12:
//#line 38 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
break;
case 21:
//#line 56 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 22:
//#line 57 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 23:
//#line 58 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 24:
//#line 59 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: no se puede asignar una constante negativa al valor NA.");}
break;
case 28:
//#line 65 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
break;
case 30:
//#line 69 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 31:
//#line 70 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 35:
//#line 76 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 36:
//#line 77 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 39:
//#line 84 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
break;
case 40:
//#line 85 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 41:
//#line 86 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 42:
//#line 87 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 43:
//#line 88 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 44:
//#line 89 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 45:
//#line 90 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 46:
//#line 91 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 53:
//#line 100 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 54:
//#line 101 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 55:
//#line 102 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 56:
//#line 103 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 57:
//#line 104 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 58:
//#line 105 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 59:
//#line 108 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
break;
case 60:
//#line 109 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 66:
//#line 123 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 67:
//#line 124 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 68:
//#line 125 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 69:
//#line 126 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 70:
//#line 127 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 71:
//#line 128 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 72:
//#line 129 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 73:
//#line 130 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 74:
//#line 131 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 76:
//#line 135 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 77:
//#line 136 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
break;
case 78:
//#line 137 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 79:
//#line 138 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 81:
//#line 146 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 82:
//#line 147 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 83:
//#line 154 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 84:
//#line 155 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 85:
//#line 156 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
break;
case 86:
//#line 157 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 87:
//#line 158 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 88:
//#line 159 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 89:
//#line 160 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 90:
//#line 161 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 91:
//#line 164 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 92:
//#line 165 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Division.");}
break;
case 93:
//#line 166 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 94:
//#line 167 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 95:
//#line 168 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 96:
//#line 170 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 97:
//#line 171 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 98:
//#line 172 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 101:
//#line 178 "specification.y"
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
//#line 968 "Parser.java"
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
