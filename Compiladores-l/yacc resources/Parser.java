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
    9,    7,    7,    7,    7,   11,   11,   11,   11,   11,
   11,   12,   12,   12,   10,   10,   10,   10,   13,   13,
   13,   14,   14,   14,   14,   14,   15,   15,    4,    4,
    4,    4,    4,    4,    4,    4,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   21,   21,
   22,   22,   22,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   18,   18,   18,   18,   18,   19,
   19,   19,   16,   16,   16,   16,   16,   16,   16,   16,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   24,
   24,   24,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    2,
    3,    7,    6,    6,    7,    6,    5,    5,    5,    5,
    5,    3,    3,    2,    1,    3,    5,    7,    2,    1,
    1,    1,    3,    5,    7,    1,    4,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    2,    3,    3,
    3,    1,    2,    8,    6,    8,    6,    8,    6,    8,
    8,    6,    6,    4,    6,    3,    6,    6,    4,    4,
    4,    2,    3,    3,    1,    3,    3,    3,    3,    4,
    3,    3,    1,    3,    3,    3,    3,    4,    4,    1,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   55,   53,   54,   56,
    7,    0,    0,  110,  111,    0,    0,    0,    0,    0,
  103,   92,    0,    0,    0,   72,    0,    0,    0,    0,
    0,    0,    0,    0,    6,    4,    0,   10,    0,    0,
    0,    0,    0,    0,    0,  112,    0,    0,   57,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   73,    0,   86,    0,    0,   15,   46,
    0,   48,    0,    0,    0,    0,    5,    0,    0,    0,
  106,  107,    0,    0,   65,   66,   67,    0,   63,   68,
   64,    0,    0,    0,    0,    0,    0,    0,   84,  104,
    0,  101,  105,    0,  102,   91,   90,    0,   71,    0,
   89,    0,   47,    0,   41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  108,  109,    0,    0,    0,    0,
    0,    0,    0,    0,   39,    0,    0,    0,   79,    0,
   82,   77,    0,   75,   83,   88,   87,   85,    0,    0,
    0,    0,    0,    0,   24,    0,    0,    0,    0,    0,
    0,    0,   25,    0,    0,    0,    0,    0,   34,    0,
    0,   22,    0,   78,   80,   81,   76,   74,    0,    0,
    0,    0,    0,    0,   33,    0,   32,    0,   45,   18,
   19,   28,    0,   31,   30,   29,   27,   21,    0,   26,
   38,
};
final static short yydgoto[] = {                          9,
   10,   36,   12,   13,   14,   15,   16,  202,  181,  128,
  144,  165,  129,   83,   17,   28,   18,   19,   20,   66,
   29,   37,   30,   31,
};
final static short yysindex[] = {                       436,
    8,  137,  -35,    0,    0,  335,  -34, -256,    0,  451,
  -44,    0,    0, -243,    0,    0,    0,    0,    0,    0,
    0,  168,   99,    0,    0,  183, -237,  140,  356,   -8,
    0,    0, -239,  -38,  -97,    0, -218,  168, -243,  165,
  198,   13,   -2,   17,    0,    0,   52,    0,  104,  205,
  205,   26,   26,   80,   50,    0,   39,   54,    0,   57,
  168,   21,   49,  166,  212,  168,  -73,  162,  378,   83,
   96,  103,    8,    0,  405,    0,  148,  104,    0,    0,
  111,    0,  116,   99,  104,  164,    0,   14,   -8,   -8,
    0,    0,  356,  376,    0,    0,    0,  104,    0,    0,
    0,   14,  205,   -8,   14,   -8,  104,  356,    0,    0,
   26,    0,    0,   26,    0,    0,    0,  168,    0,  216,
    0, -107,    0,  122,    0,  -54,  -89,  156,  145,  -69,
   42,  -94,   -8,  -62,    0,    0,  158,  471,  169,  159,
  -54,  -48,  -25,  128,    0,  -54,  -84,  356,    0,  356,
    0,    0,  392,    0,    0,    0,    0,    0,   12,  128,
  -11, -217,   -4,  430,    0,  128,  244,   45,   64,  -52,
 -162,  265,    0,  249,  -60,  270,  277,  -53,    0,  280,
  221,    0,  -84,    0,    0,    0,    0,    0, -223,  -83,
   38,  -83,  -83,  -83,    0,  451,    0,  305,    0,    0,
    0,    0,  -83,    0,    0,    0,    0,    0,  -84,    0,
    0,
};
final static short yyrindex[] = {                       353,
  354,    0,    0,    0,    0,    0,  264,    0,    0,  360,
    0,    0,    0,  273,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  264,    0,  308,    0,
    0,    0,    0,    0,    0,    0,  220,  227,    0,  234,
    0,  238,  242,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  317,    0,    0,
  320,    0,    0,  323,  338,    0,    0,    0,  -19,    3,
    0,    0,    0,    0,    0,    0,    0,  135,    0,    0,
    0,   25,    0,   47,   69,   91,  286,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   61,    0,  327,    0,
    0,    0,  113,    0,    0,    0,    0,    0,    0,  330,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  348,  334,    0,    0,    0,
    0,  342,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  283,    0,  349,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  363,  447,    0,    0,  -70,   16,    0,  250,  193,  206,
  -66, -131, -136,  230,    0,   79,    0,    0,    0,    0,
  -23,  -15,  -14,    5,
};
final static int YYTABLESIZE=726;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         95,
  192,   95,   55,   95,   33,   40,  143,   22,   22,   39,
  167,   42,  162,   67,   46,  127,   70,   95,   95,   95,
   95,   98,   22,   98,   47,   98,   41,   74,  173,   48,
   71,   56,   80,   68,  182,   89,   90,   76,   69,   98,
   98,   98,   98,   99,   81,   99,  198,   99,   77,  104,
  106,  175,   86,  121,   79,   52,   91,   92,   22,  176,
   53,   99,   99,   99,   99,   96,   21,   96,   22,   96,
   27,  195,  112,  115,  160,   87,  127,  130,  132,  166,
   99,   95,  100,   96,   96,   96,   96,   93,  133,   93,
   94,   93,  134,  187,  137,   39,  139,  188,  203,   95,
   49,   40,   22,   98,   40,   93,   93,   93,   93,   97,
  101,   97,  127,   97,   96,  135,   78,   97,  136,   85,
   93,   52,   50,  116,   51,   99,   53,   97,   97,   97,
   97,   94,  168,   94,  169,   94,  117,  171,  127,   98,
   52,   50,  118,   51,  107,   53,   64,   96,   65,   94,
   94,   94,   94,  100,  122,  100,  123,  100,   73,    2,
  140,  152,  141,    3,  153,  154,    4,    5,    6,   93,
    7,  100,  100,  100,  100,   70,   26,    8,  145,    4,
    5,   27,   64,  125,   65,  108,  109,  120,  147,  148,
  149,   97,   27,   70,  200,  201,  146,  155,  156,   62,
   61,   63,  159,  111,  126,   82,   27,  186,  103,  158,
   27,   45,   27,   94,   95,   95,  191,   95,   95,   95,
  161,  142,   95,   95,   95,   95,   95,   27,   72,   95,
   95,   95,   95,   95,   32,  100,   98,   98,   38,   98,
   98,   98,   27,  163,   98,   98,   98,   98,   98,   27,
  164,   98,   98,   98,   98,   98,   27,   70,   99,   99,
   27,   99,   99,   99,   59,  174,   99,   99,   99,   99,
   99,   60,  177,   99,   99,   99,   99,   99,   58,  172,
   96,   96,   61,   96,   96,   96,   62,  183,   96,   96,
   96,   96,   96,   24,   25,   96,   96,   96,   96,   96,
  150,  151,   93,   93,  184,   93,   93,   93,  189,  190,
   93,   93,   93,   93,   93,  200,  201,   93,   93,   93,
   93,   93,   14,  185,   97,   97,   69,   97,   97,   97,
  193,   13,   97,   97,   97,   97,   97,  194,  196,   97,
   97,   97,   97,   97,   69,  197,   94,   94,  209,   94,
   94,   94,    1,    3,   94,   94,   94,   94,   94,    2,
   42,   94,   94,   94,   94,   94,   51,   35,  100,  100,
   43,  100,  100,  100,   36,   52,  100,  100,  100,  100,
  100,   50,   44,  100,  100,  100,  100,  100,  208,   37,
   70,   70,   23,   70,   70,   70,   49,   75,   70,   70,
   70,   70,   70,   23,   24,   25,   23,   20,   69,   70,
   57,   58,   59,   60,  211,   24,   25,  110,  199,  124,
   80,  102,   27,   23,  114,    0,    0,    4,    5,   24,
   25,  125,   81,   24,   25,   24,   25,    0,   54,    0,
  204,  205,  206,  207,    0,    0,   11,    0,    0,    0,
   24,   25,  210,   84,    0,    0,   44,   35,    0,    0,
   88,    0,    0,    0,    0,   24,   25,  105,    0,    0,
    0,  138,   24,   25,    0,   59,    0,    0,   35,   24,
   25,   11,   60,   24,   25,    0,    0,   59,   59,   58,
    0,    0,    0,   61,   60,   60,    0,   62,   35,    0,
    0,   58,   58,    0,    0,   61,   61,    0,    0,   62,
   62,  157,   52,   50,   35,   51,    0,   53,    0,   14,
    0,   44,   14,   14,    0,    0,    0,    0,   13,  119,
   14,   13,   13,    0,    0,    0,    0,    0,    0,   13,
    0,   69,   69,    0,   69,   69,   69,    0,    0,   69,
   69,   69,   69,   69,  179,    0,    0,    0,    0,    0,
   69,    0,    0,   51,    0,    0,   51,   51,    0,    0,
    0,    0,   52,    0,   51,   52,   52,    0,   50,    0,
    0,   50,   50,   52,    0,    0,    0,    0,    0,   50,
   34,    2,    0,   49,    0,    3,   49,   49,    4,    5,
    6,    0,    7,   23,   49,    0,   23,   23,    0,    8,
  180,   43,    2,    0,   23,    0,    3,    0,    0,    4,
    5,    6,    0,    7,    0,    0,    0,    0,    0,    0,
    8,  131,    2,  113,    0,    0,    3,    0,    0,    4,
    5,    6,  180,    7,    0,   24,   25,  170,    2,    0,
    8,    0,    3,    0,    0,    4,    5,    6,    0,    7,
   43,    2,    0,    0,    0,    3,    8,    0,    4,    5,
    6,    0,    7,    0,    0,    0,    0,    0,    0,    8,
    0,    0,    0,    0,    0,  178,    2,    0,    0,    0,
    3,    1,    2,    4,    5,    6,    3,    7,    0,    4,
    5,    6,    0,    7,    8,    0,   43,    2,    0,    0,
    8,    3,    0,    0,    4,    5,    6,    0,    7,    0,
    0,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   61,   43,   26,   45,   40,   40,   61,   61,   61,   44,
  147,  268,   61,   29,   59,   86,  256,   59,   60,   61,
   62,   41,   61,   43,  268,   45,   61,  125,  160,   14,
  270,  269,  256,   42,  166,   50,   51,  256,   47,   59,
   60,   61,   62,   41,  268,   43,  183,   45,  267,   64,
   65,  269,   40,   77,   39,   42,   52,   53,   61,  277,
   47,   59,   60,   61,   62,   41,   59,   43,   61,   45,
   45,  125,   68,   69,  141,   59,  147,   93,   94,  146,
   60,  123,   62,   59,   60,   61,   62,   41,  103,   43,
   41,   45,  108,  256,  118,   44,  120,  260,   61,   61,
   22,   41,   61,  123,   44,   59,   60,   61,   62,   41,
   62,   43,  183,   45,   61,  111,   38,   61,  114,   41,
   41,   42,   43,   41,   45,  123,   47,   59,   60,   61,
   62,   41,  148,   43,  150,   45,   41,  153,  209,   61,
   42,   43,   40,   45,   66,   47,   43,  123,   45,   59,
   60,   61,   62,   41,   44,   43,   41,   45,  256,  257,
  268,  256,   41,  261,  259,  260,  264,  265,  266,  123,
  268,   59,   60,   61,   62,   41,   40,  275,  268,  264,
  265,   45,   43,  268,   45,  259,  260,   40,   44,  259,
  260,  123,   45,   59,  278,  279,   41,  260,   41,   60,
   61,   62,   44,   42,   41,   41,   45,  260,   43,   41,
   45,  256,   45,  123,  256,  257,  277,  259,  260,  261,
  269,  276,  264,  265,  266,  267,  268,   45,  267,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  259,
  260,  261,   45,  269,  264,  265,  266,  267,  268,   45,
  123,  271,  272,  273,  274,  275,   45,  123,  256,  257,
   45,  259,  260,  261,   45,  277,  264,  265,  266,  267,
  268,   45,  277,  271,  272,  273,  274,  275,   45,  268,
  256,  257,   45,  259,  260,  261,   45,   44,  264,  265,
  266,  267,  268,  268,  269,  271,  272,  273,  274,  275,
  259,  260,  256,  257,  260,  259,  260,  261,   44,   61,
  264,  265,  266,  267,  268,  278,  279,  271,  272,  273,
  274,  275,   59,  260,  256,  257,   41,  259,  260,  261,
   61,   59,  264,  265,  266,  267,  268,   61,   59,  271,
  272,  273,  274,  275,   59,  125,  256,  257,   44,  259,
  260,  261,    0,    0,  264,  265,  266,  267,  268,    0,
   41,  271,  272,  273,  274,  275,   59,   41,  256,  257,
   41,  259,  260,  261,   41,   59,  264,  265,  266,  267,
  268,   59,   41,  271,  272,  273,  274,  275,  196,   41,
  256,  257,  256,  259,  260,  261,   59,   35,  264,  265,
  266,  267,  268,  256,  268,  269,   59,  125,  123,  275,
  271,  272,  273,  274,  209,  268,  269,  256,  189,  256,
  256,  256,   45,  256,   47,   -1,   -1,  264,  265,  268,
  269,  268,  268,  268,  269,  268,  269,   -1,  256,   -1,
  191,  192,  193,  194,   -1,   -1,    0,   -1,   -1,   -1,
  268,  269,  203,  256,   -1,   -1,   10,  123,   -1,   -1,
  256,   -1,   -1,   -1,   -1,  268,  269,  256,   -1,   -1,
   -1,  256,  268,  269,   -1,  256,   -1,   -1,  123,  268,
  269,   35,  256,  268,  269,   -1,   -1,  268,  269,  256,
   -1,   -1,   -1,  256,  268,  269,   -1,  256,  123,   -1,
   -1,  268,  269,   -1,   -1,  268,  269,   -1,   -1,  268,
  269,   41,   42,   43,  123,   45,   -1,   47,   -1,  256,
   -1,   75,  259,  260,   -1,   -1,   -1,   -1,  256,  125,
  267,  259,  260,   -1,   -1,   -1,   -1,   -1,   -1,  267,
   -1,  256,  257,   -1,  259,  260,  261,   -1,   -1,  264,
  265,  266,  267,  268,  125,   -1,   -1,   -1,   -1,   -1,
  275,   -1,   -1,  256,   -1,   -1,  259,  260,   -1,   -1,
   -1,   -1,  256,   -1,  267,  259,  260,   -1,  256,   -1,
   -1,  259,  260,  267,   -1,   -1,   -1,   -1,   -1,  267,
  256,  257,   -1,  256,   -1,  261,  259,  260,  264,  265,
  266,   -1,  268,  256,  267,   -1,  259,  260,   -1,  275,
  164,  256,  257,   -1,  267,   -1,  261,   -1,   -1,  264,
  265,  266,   -1,  268,   -1,   -1,   -1,   -1,   -1,   -1,
  275,  256,  257,  256,   -1,   -1,  261,   -1,   -1,  264,
  265,  266,  196,  268,   -1,  268,  269,  256,  257,   -1,
  275,   -1,  261,   -1,   -1,  264,  265,  266,   -1,  268,
  256,  257,   -1,   -1,   -1,  261,  275,   -1,  264,  265,
  266,   -1,  268,   -1,   -1,   -1,   -1,   -1,   -1,  275,
   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,
  261,  256,  257,  264,  265,  266,  261,  268,   -1,  264,
  265,  266,   -1,  268,  275,   -1,  256,  257,   -1,   -1,
  275,  261,   -1,   -1,  264,  265,  266,   -1,  268,   -1,
   -1,   -1,   -1,   -1,   -1,  275,
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

//#line 221 "specification.y"

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
//#line 562 "Parser.java"
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
case 22:
//#line 59 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 23:
//#line 60 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR: sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 24:
//#line 61 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 25:
//#line 62 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se definio mal la lista de parametros.");}
break;
case 27:
//#line 66 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra NA");}
break;
case 28:
//#line 67 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del NA.");}
break;
case 29:
//#line 68 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta definir el valor del NA.");}
break;
case 30:
//#line 69 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra SHADOWING.");}
break;
case 31:
//#line 70 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del SHADOWING.");}
break;
case 33:
//#line 74 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: cuerpo del procedimiento mal definido.");}
break;
case 38:
//#line 81 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un máximo de tres parametros.");}
break;
case 40:
//#line 85 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 41:
//#line 86 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 43:
//#line 90 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 45:
//#line 92 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 46:
//#line 93 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la lista de identificadores esta mal conformada.");}
break;
case 49:
//#line 100 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Asignacion.");}
break;
case 50:
//#line 101 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 51:
//#line 102 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 52:
//#line 103 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 53:
//#line 104 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 54:
//#line 105 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 55:
//#line 106 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 56:
//#line 107 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 63:
//#line 116 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 64:
//#line 117 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 65:
//#line 118 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 66:
//#line 119 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 67:
//#line 120 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 68:
//#line 121 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 69:
//#line 124 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Condicion.");}
break;
case 70:
//#line 125 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 76:
//#line 139 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 77:
//#line 140 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 78:
//#line 141 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 79:
//#line 142 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condición entre parentesis.");}
break;
case 80:
//#line 143 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 81:
//#line 144 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula ELSE_IF.");}
break;
case 82:
//#line 145 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la cláusula IF.");}
break;
case 83:
//#line 146 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 84:
//#line 147 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 86:
//#line 151 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 87:
//#line 152 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la cláusula UNTIL debe incluir una condicion entre parentesis");}
break;
case 88:
//#line 153 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 89:
//#line 154 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 91:
//#line 162 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 92:
//#line 163 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintáctico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 93:
//#line 170 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Suma.");}
break;
case 94:
//#line 171 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Resta.");}
break;
case 95:
//#line 172 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Termino.");}
break;
case 96:
//#line 173 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 97:
//#line 174 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 98:
//#line 175 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 99:
//#line 176 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 100:
//#line 177 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 101:
//#line 180 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 102:
//#line 181 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Division.");}
break;
case 103:
//#line 182 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] Factor.");}
break;
case 104:
//#line 183 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 105:
//#line 184 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 106:
//#line 185 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 107:
//#line 186 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 108:
//#line 187 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 109:
//#line 188 "specification.y"
{showMessage("[Line " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 112:
//#line 194 "specification.y"
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
//#line 1027 "Parser.java"
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
