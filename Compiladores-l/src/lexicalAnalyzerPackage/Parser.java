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
   16,   16,   16,   16,   17,   17,   17,   17,   17,   18,
   18,   18,   15,   15,   15,   15,   15,   15,   15,   15,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   23,
   23,   23,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    7,
    6,    6,    5,    6,    5,    7,    6,    5,    5,    5,
    5,    5,    3,    3,    2,    1,    3,    5,    7,    2,
    1,    1,    1,    3,    5,    7,    4,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    2,    3,    3,
    3,    2,    2,    8,    6,    8,    6,    8,    6,    8,
    8,    6,    6,    4,    6,    3,    6,    6,    4,    4,
    4,    2,    3,    3,    1,    3,    3,    3,    3,    4,
    3,    3,    1,    3,    3,    3,    3,    4,    4,    1,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   55,   53,   54,   56,
    7,    0,    0,  110,  111,    0,    0,    0,    0,    0,
  103,   92,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    6,    4,    0,   10,    0,
    0,    0,    0,    0,    0,    0,  112,    0,    0,   57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   73,    0,   72,   86,    0,    0,
   15,    0,   48,    0,    0,    0,    0,   42,    0,    0,
    0,    0,    5,    0,    0,    0,  106,  107,    0,    0,
   65,   66,   67,    0,   63,   68,   64,    0,    0,    0,
    0,    0,    0,    0,   84,  104,    0,  101,  105,    0,
  102,   91,   90,    0,   71,    0,   89,    0,   47,    0,
    0,    0,    0,    0,    0,   40,    0,    0,    0,    0,
    0,    0,    0,  108,  109,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,    0,    0,    0,
   79,    0,   82,   77,    0,   75,   83,   88,   87,   85,
    0,    0,   21,    0,    0,    0,    0,    0,    0,   35,
    0,   24,    0,    0,    0,    0,    0,    0,   26,   20,
    0,    0,    0,    0,    0,   34,   33,    0,   78,   80,
   81,   76,   74,    0,   18,   19,   29,    0,   32,   31,
   30,   28,    0,   46,   27,   39,
};
final static short yydgoto[] = {                          9,
   10,   36,   12,   13,   14,   15,   16,  207,   91,  135,
  157,   92,   84,   17,   28,   18,   19,   20,   67,   29,
   37,   30,   31,
};
final static short yysindex[] = {                       359,
   18,  -28,  -35,    0,    0,  244,  -34,  -39,    0,  365,
  -46,    0,    0, -213,    0,    0,    0,    0,    0,    0,
    0,   60,   29,    0,    0,   82, -200,  157,  257,   99,
    0,    0, -218,   -1,  301,   14, -145,   60, -213,  -25,
  104,   49,   15,   42,   40,    0,    0,   71,    0,   78,
  126,  126,   33,   33,  136,   84,    0,   72,   79,    0,
   94,   60,  143,   57,   38,  176,   60,  -70,   16,  166,
  102,  125,  129,   18,    0,  316,    0,    0,   -6,   78,
    0,  160,    0,  246,   29,   78,  156,    0,  -54,   37,
  268,  279,    0,  118,   99,   99,    0,    0,  257,  270,
    0,    0,    0,   78,    0,    0,    0,  118,  126,   99,
  118,   99,   78,  257,    0,    0,   33,    0,    0,   33,
    0,    0,    0,   60,    0,  181,    0,   56,    0,  286,
  -54,  290,  -12,   67,  216,    0,  -54, -232,  -64,  -53,
 -163,   99,   85,    0,    0,  305,  437,  308,  309,  -54,
  216,  -54,   77, -240,   81,  331,    0,  216,  317,  257,
    0,  257,    0,    0,  285,    0,    0,    0,    0,    0,
  100,  216,    0,  216,  310,  -16,  314,  315,  -31,    0,
  344,    0, -232,  123,  130,  -52, -203,  345,    0,    0,
  -80,  119,  -80,  -80,  -80,    0,    0,  358,    0,    0,
    0,    0,    0,  137,    0,    0,    0,  -80,    0,    0,
    0,    0, -232,    0,    0,    0,
};
final static short yyrindex[] = {                       404,
  409,    0,    0,    0,    0,    0,  -44,    0,    0,  413,
    0,    0,    0,   32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -44,    0,   36,
    0,    0,    0,    0,    0,    0,    0,  199,  203,    0,
  206,    0,  220,  225,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   39,
    0,  376,    0,    0,   54,   58,    0,    0,    0,   98,
    0,  378,    0,    0,  -19,    3,    0,    0,    0,    0,
    0,    0,    0,  135,    0,    0,    0,   25,    0,   47,
   69,   91,  150,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  113,    0,    0,    0,    0,    0,    0,  386,    0,
   61,    0,    0,    0,    0,    0,    0,    0,  395,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   76,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  399,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  401,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -32,  423,    0,    0,  -20,   11,    0,   -7,  -76,    7,
   10, -124,  239,    0,   13,    0,    0,    0,    0,   21,
    2,  387,  494,
};
final static int YYTABLESIZE=640;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         95,
   43,   95,   76,   95,   33,   40,  134,   22,   22,   39,
  132,   26,   47,  159,   14,   83,   27,   95,   95,   95,
   95,   98,   90,   98,   49,   98,   41,   21,  176,   22,
   68,    4,    5,  126,   50,   88,  177,   71,   27,   98,
   98,   98,   98,   99,  193,   99,   56,   99,  154,   81,
   80,   72,  202,   86,   48,   89,  203,  117,  198,   22,
   27,   99,   99,   99,   99,   96,   90,   96,   57,   96,
   53,   51,   77,   52,  104,   54,   21,   27,   22,  113,
  109,   95,   27,   96,   96,   96,   96,   93,   87,   93,
   13,   93,  164,  196,   51,  165,  166,   52,   93,  127,
  139,  141,   22,   98,   27,   93,   93,   93,   93,   97,
   78,   97,   50,   97,   39,  143,   49,   90,  107,   23,
   65,   79,   66,  181,  100,   99,   27,   97,   97,   97,
   97,   94,  101,   94,   22,   94,  216,  151,   41,  102,
   69,   41,  122,  158,  146,   70,  148,   96,   27,   94,
   94,   94,   94,  100,  103,  100,  172,  100,  174,   53,
  173,  184,   90,  185,   54,  123,  187,  182,  124,   93,
   27,  100,  100,  100,  100,   70,   99,   53,   51,  208,
   52,  189,   54,  190,  209,  210,  211,  212,  114,  115,
   69,   97,   90,   70,  160,  161,  131,  205,  206,   65,
  215,   66,  105,  128,  106,  162,  163,  201,   69,   46,
   27,   14,  120,   94,   95,   95,   63,   62,   64,   95,
   27,  133,   95,   95,   95,   27,   95,   23,   42,   95,
   95,   95,   95,   95,   32,  100,   98,   98,   38,   24,
   25,   98,   82,   59,   98,   98,   98,   60,   98,   23,
   58,   98,   98,   98,   98,   98,  153,   70,   99,   99,
  192,   24,   25,   99,   61,   73,   99,   99,   99,   62,
   99,  116,   69,   99,   99,   99,   99,   99,    4,    5,
   96,   96,   88,   24,   25,   96,  129,   13,   96,   96,
   96,   51,   96,  108,   52,   96,   96,   96,   96,   96,
   24,   25,   93,   93,  136,   24,   25,   93,  137,   50,
   93,   93,   93,   49,   93,   23,   23,   93,   93,   93,
   93,   93,  138,  149,   97,   97,  150,   24,   25,   97,
  152,   22,   97,   97,   97,  155,   97,   55,  156,   97,
   97,   97,   97,   97,  167,  168,   94,   94,  170,   24,
   25,   94,  171,  175,   94,   94,   94,  178,   94,   85,
  183,   94,   94,   94,   94,   94,   35,  188,  100,  100,
  191,   24,   25,  100,  194,  195,  100,  100,  100,   35,
  100,   94,  199,  100,  100,  100,  100,  100,  204,  200,
   70,   70,   35,   24,   25,   70,  205,  206,   70,   70,
   70,  213,   70,    1,   82,   69,   69,   35,    3,   70,
   69,  130,    2,   69,   69,   69,   43,   69,   36,    4,
    5,  119,   11,   88,   69,   75,   44,   58,   59,   60,
   61,  111,   45,   24,   25,   37,  147,   95,   96,   45,
  125,   38,  214,   24,   25,    0,    0,    0,   24,   25,
    0,  110,  112,    0,   59,  180,    0,   11,   60,    0,
    0,   58,    0,    0,    0,    0,   59,   59,  197,    0,
   60,   60,    0,   58,   58,   61,    0,  169,   53,   51,
   62,   52,    0,   54,    0,    0,    0,   61,   61,    0,
    0,    0,   62,   62,    0,  142,    0,    0,   45,   34,
    2,    0,    0,    0,    3,    0,    0,    4,    5,    6,
    0,    7,   44,    2,    0,    0,    0,    3,    8,    0,
    4,    5,    6,    0,    7,  140,    2,    0,    0,    0,
    3,    8,    0,    4,    5,    6,    0,    7,    0,    0,
  186,    2,    0,    0,    8,    3,   97,   98,    4,    5,
    6,    0,    7,    0,    0,    0,   74,    2,    0,    8,
    0,    3,  118,  121,    4,    5,    6,    0,    7,    0,
    0,   44,    2,    0,    0,    8,    3,    0,   11,    4,
    5,    6,    0,    7,    0,    0,  179,    2,    0,    0,
    8,    3,    0,    0,    4,    5,    6,    0,    7,   44,
    2,    0,    0,   45,    3,    8,    0,    4,    5,    6,
  144,    7,    0,  145,    1,    2,    0,    0,    8,    3,
   44,    2,    4,    5,    6,    3,    7,    0,    4,    5,
    6,    0,    7,    8,    0,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   35,   45,   40,   40,   61,   61,   61,   44,
   87,   40,   59,  138,   59,   41,   45,   59,   60,   61,
   62,   41,   43,   43,   14,   45,   61,   59,  269,   61,
   29,  264,  265,   40,   22,  268,  277,  256,   45,   59,
   60,   61,   62,   41,   61,   43,   26,   45,   61,   39,
   38,  270,  256,   41,  268,   41,  260,   42,  183,   61,
   45,   59,   60,   61,   62,   41,   87,   43,  269,   45,
   42,   43,   59,   45,   62,   47,   59,   45,   61,   67,
   43,  123,   45,   59,   60,   61,   62,   41,   40,   43,
   59,   45,  256,  125,   59,  259,  260,   59,   59,   79,
   99,  100,   61,  123,   45,   59,   60,   61,   62,   41,
  256,   43,   59,   45,   44,  114,   59,  138,   62,   59,
   43,  267,   45,  156,   41,  123,   45,   59,   60,   61,
   62,   41,   61,   43,   59,   45,  213,  131,   41,   61,
   42,   44,   41,  137,  124,   47,  126,  123,   45,   59,
   60,   61,   62,   41,   61,   43,  150,   45,  152,   42,
  151,  160,  183,  162,   47,   41,  165,  158,   40,  123,
   45,   59,   60,   61,   62,   41,   41,   42,   43,   61,
   45,  172,   47,  174,  192,  193,  194,  195,  259,  260,
   41,  123,  213,   59,  259,  260,   41,  278,  279,   43,
  208,   45,   60,   44,   62,  259,  260,  260,   59,  256,
   45,  256,   47,  123,  256,  257,   60,   61,   62,  261,
   45,  276,  264,  265,  266,   45,  268,  256,  268,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  268,
  269,  261,  268,   45,  264,  265,  266,   45,  268,  256,
   45,  271,  272,  273,  274,  275,  269,  123,  256,  257,
  277,  268,  269,  261,   45,  267,  264,  265,  266,   45,
  268,  256,  123,  271,  272,  273,  274,  275,  264,  265,
  256,  257,  268,  268,  269,  261,   41,  256,  264,  265,
  266,  256,  268,  256,  256,  271,  272,  273,  274,  275,
  268,  269,  256,  257,  268,  268,  269,  261,   41,  256,
  264,  265,  266,  256,  268,  256,  256,  271,  272,  273,
  274,  275,   44,  268,  256,  257,   41,  268,  269,  261,
   41,  256,  264,  265,  266,  269,  268,  256,  123,  271,
  272,  273,  274,  275,  260,   41,  256,  257,   41,  268,
  269,  261,   44,  277,  264,  265,  266,  277,  268,  256,
   44,  271,  272,  273,  274,  275,  123,  268,  256,  257,
   61,  268,  269,  261,   61,   61,  264,  265,  266,  123,
  268,  256,  260,  271,  272,  273,  274,  275,   44,  260,
  256,  257,  123,  268,  269,  261,  278,  279,  264,  265,
  266,   44,  268,    0,  268,  256,  257,  123,    0,  275,
  261,  256,    0,  264,  265,  266,   41,  268,   41,  264,
  265,  256,    0,  268,  275,  125,   41,  271,  272,  273,
  274,  256,   10,  268,  269,   41,  256,   51,   52,   41,
  125,   41,  204,  268,  269,   -1,   -1,   -1,  268,  269,
   -1,   65,   66,   -1,  256,  125,   -1,   35,  256,   -1,
   -1,  256,   -1,   -1,   -1,   -1,  268,  269,  125,   -1,
  268,  269,   -1,  268,  269,  256,   -1,   41,   42,   43,
  256,   45,   -1,   47,   -1,   -1,   -1,  268,  269,   -1,
   -1,   -1,  268,  269,   -1,  109,   -1,   -1,   76,  256,
  257,   -1,   -1,   -1,  261,   -1,   -1,  264,  265,  266,
   -1,  268,  256,  257,   -1,   -1,   -1,  261,  275,   -1,
  264,  265,  266,   -1,  268,  256,  257,   -1,   -1,   -1,
  261,  275,   -1,  264,  265,  266,   -1,  268,   -1,   -1,
  256,  257,   -1,   -1,  275,  261,   53,   54,  264,  265,
  266,   -1,  268,   -1,   -1,   -1,  256,  257,   -1,  275,
   -1,  261,   69,   70,  264,  265,  266,   -1,  268,   -1,
   -1,  256,  257,   -1,   -1,  275,  261,   -1,  156,  264,
  265,  266,   -1,  268,   -1,   -1,  256,  257,   -1,   -1,
  275,  261,   -1,   -1,  264,  265,  266,   -1,  268,  256,
  257,   -1,   -1,  181,  261,  275,   -1,  264,  265,  266,
  117,  268,   -1,  120,  256,  257,   -1,   -1,  275,  261,
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

//#line 257 "specification.y"

public LexicalAnalyzer la;
public IntermediateCode ic;

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
	ic = new IntermediateCode();
}

public void parse(){
	yyparse();
	System.out.print(ic);
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
	//System.out.println(message);
}
//#line 545 "Parser.java"
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
case 49:
//#line 101 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");
										Operand op1 = new Operand(Operand.ST_POINTER,val_peek(2).sval); 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("=");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 50:
//#line 108 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 51:
//#line 109 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 52:
//#line 110 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 53:
//#line 111 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 54:
//#line 112 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 55:
//#line 113 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 56:
//#line 114 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 63:
//#line 123 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 64:
//#line 124 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 65:
//#line 125 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 66:
//#line 126 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 67:
//#line 127 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 68:
//#line 128 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 69:
//#line 131 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");}
break;
case 70:
//#line 132 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 76:
//#line 146 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 77:
//#line 147 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 78:
//#line 148 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
break;
case 79:
//#line 149 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
break;
case 80:
//#line 150 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
break;
case 81:
//#line 151 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula ELSE_IF.");}
break;
case 82:
//#line 152 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
break;
case 83:
//#line 153 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 84:
//#line 154 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 86:
//#line 158 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 87:
//#line 159 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la clausula UNTIL debe incluir una condicion entre parentesis");}
break;
case 88:
//#line 160 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 89:
//#line 161 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 91:
//#line 169 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 92:
//#line 170 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 93:
//#line 177 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
										Operand op1 = (Operand) val_peek(2).obj; 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("+");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 94:
//#line 184 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
										Operand op1 = (Operand) val_peek(2).obj; 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("-");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 95:
//#line 191 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
										 yyval.obj = val_peek(0).obj;}
break;
case 96:
//#line 193 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 97:
//#line 194 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 98:
//#line 195 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 99:
//#line 196 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 100:
//#line 197 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 101:
//#line 200 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
							Operand op1 = (Operand) val_peek(2).obj; 
      						Operand op2 = (Operand) val_peek(0).obj; 
      						Operator opt = new Operator("*");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 102:
//#line 207 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      						Operand op1 = (Operand) val_peek(2).obj; 
      						Operand op2 = (Operand) val_peek(0).obj; 
      						Operator opt = new Operator("/");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 103:
//#line 214 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 yyval.obj = val_peek(0).obj;}
break;
case 104:
//#line 216 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 105:
//#line 217 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 106:
//#line 218 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 107:
//#line 219 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 108:
//#line 220 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 109:
//#line 221 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 110:
//#line 224 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 111:
//#line 225 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 112:
//#line 226 "specification.y"
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
//#line 1082 "Parser.java"
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
