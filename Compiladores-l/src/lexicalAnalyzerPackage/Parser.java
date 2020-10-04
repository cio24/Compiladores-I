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
    3,    3,    2,    2,    8,    6,    8,    6,    8,    6,
    8,    8,    6,    6,    4,    6,    3,    6,    6,    4,
    4,    4,    2,    3,    3,    1,    3,    3,    3,    3,
    4,    3,    3,    1,    3,    3,    3,    3,    4,    4,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   56,   54,   55,   57,
    7,    0,    0,  111,  112,    0,    0,    0,    0,    0,
  104,   93,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    6,    4,    0,   10,    0,
    0,    0,    0,    0,    0,    0,  113,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   74,    0,   73,   87,    0,    0,
   15,   47,    0,   49,    0,    0,    0,    0,   42,    0,
    0,    0,    0,    5,    0,    0,    0,  107,  108,    0,
    0,   66,   67,   68,    0,   64,   69,   65,    0,    0,
    0,    0,    0,    0,    0,   85,  105,    0,  102,  106,
    0,  103,   92,   91,    0,   72,    0,   90,    0,   48,
    0,    0,    0,    0,    0,    0,   40,    0,    0,    0,
    0,    0,    0,    0,  109,  110,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   25,    0,    0,
    0,   80,    0,   83,   78,    0,   76,   84,   89,   88,
   86,    0,    0,   21,    0,    0,    0,    0,    0,    0,
   35,    0,   24,    0,    0,    0,    0,    0,    0,   26,
   20,    0,    0,    0,    0,    0,   34,   33,    0,   79,
   81,   82,   77,   75,    0,   18,   19,   29,    0,   32,
   31,   30,   28,    0,   46,   27,   39,
};
final static short yydgoto[] = {                          9,
   10,   36,   12,   13,   14,   15,   16,  208,   92,  136,
  158,   93,   85,   17,   28,   18,   19,   20,   67,   29,
   37,   30,   31,
};
final static short yysindex[] = {                       -79,
   86,  -28,  -35,    0,    0,  248,  -34,  -39,    0,  349,
  -46,    0,    0, -239,    0,    0,    0,    0,    0,    0,
    0,   82,   76,    0,    0,  104, -224,  157,  270,   55,
    0,    0, -223,   -1,  264,   -2, -184,   82, -239,   38,
  126,   11,   15,   20,   34,    0,    0,   57,    0,  112,
  176,  176,   33,   33,  578,   70,    0,   64,   77,    0,
   80,   82,  101,   84,   60,  181,   82,  -61,   16,  166,
  143,  154,  161,   86,    0,  315,    0,    0,   -6,  112,
    0,    0,  261,    0,  164,   76,  112,  156,    0,  -54,
   56,  266,  265,    0,   97,   55,   55,    0,    0,  270,
  285,    0,    0,    0,  112,    0,    0,    0,   97,  176,
   55,   97,   55,  112,  270,    0,    0,   33,    0,    0,
   33,    0,    0,    0,   82,    0,  198,    0,   59,    0,
  295,  -54,  298,   -8,   85,  230,    0,  -54,   81,  -56,
  -53,  -91,   55,   98,    0,    0,  320,  585,  326,  331,
  -54,  230,  -54,   99, -200,  103,  321,    0,  230,  339,
  270,    0,  270,    0,    0,  300,    0,    0,    0,    0,
    0,  122,  230,    0,  230,  341,  -33,  343,  344,  -29,
    0,  336,    0,   81,  153,  173,  -52, -189,  365,    0,
    0,   53,  119,   53,   53,   53,    0,    0,  382,    0,
    0,    0,    0,    0, -231,    0,    0,    0,   53,    0,
    0,    0,    0,   81,    0,    0,    0,
};
final static short yyrindex[] = {                       436,
  438,    0,    0,    0,    0,    0,  -44,    0,    0,  439,
    0,    0,    0,   14,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -44,    0,   32,
    0,    0,    0,    0,    0,    0,    0,  203,  206,    0,
  212,    0,  220,  242,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   36,
    0,    0,  400,    0,    0,   39,   54,    0,    0,    0,
  149,    0,  401,    0,    0,  -19,    3,    0,    0,    0,
    0,    0,    0,    0,  135,    0,    0,    0,   25,    0,
   47,   69,   91,  150,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  113,    0,    0,    0,    0,    0,    0,  402,
    0,   58,    0,    0,    0,    0,    0,    0,    0,  406,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   61,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  407,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  410,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -24,  417,    0,    0,  280,   41,    0,  290,  -72,   28,
 -121, -125,  250,    0,  121,    0,    0,    0,    0,   -3,
  -26,  -16,   46,
};
final static int YYTABLESIZE=632;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         96,
   43,   96,   68,   96,   33,   40,  135,   22,   22,   39,
   76,   26,   47,  160,   14,  133,   27,   96,   96,   96,
   96,   99,   56,   99,   82,   99,   41,  194,   48,   21,
  174,   22,   71,  127,   96,   97,   83,  183,   27,   99,
   99,   99,   99,  100,   57,  100,   72,  100,  111,  113,
   88,  190,  155,  191,   49,   90,   77,  118,  199,   22,
   27,  100,  100,  100,  100,   97,  203,   97,  177,   97,
  204,   78,   13,  140,  142,  128,  178,   27,   84,   81,
   22,   96,   79,   97,   97,   97,   97,   94,  144,   94,
   52,   94,   94,  143,   53,  197,   69,   51,   98,   99,
   39,   70,  110,   99,   27,   94,   94,   94,   94,   98,
  101,   98,   50,   98,  119,  122,   23,   53,   51,   22,
   52,  147,   54,  149,  102,  100,   27,   98,   98,   98,
   98,   95,  182,   95,  185,   95,  186,  103,   53,  188,
  104,  217,   50,   54,   21,  108,   22,   97,   27,   95,
   95,   95,   95,  101,   65,  101,   66,  101,   80,  152,
  106,   87,  107,  145,  165,  159,  146,  166,  167,   94,
   27,  101,  101,  101,  101,   71,    1,    2,  173,  209,
  175,    3,  105,  123,    4,    5,    6,  114,    7,   41,
   70,   98,   41,   71,  124,    8,  132,  115,  116,   65,
  125,   66,  161,  162,  130,  163,  164,  202,   70,   46,
   27,   14,  121,   95,   96,   96,   63,   62,   64,   96,
   27,  134,   96,   96,   96,   27,   96,   23,   42,   96,
   96,   96,   96,   96,   32,  101,   99,   99,   38,   24,
   25,   99,   27,  193,   99,   99,   99,   60,   99,   23,
   61,   99,   99,   99,   99,   99,   59,   71,  100,  100,
  154,   24,   25,  100,   62,   73,  100,  100,  100,   13,
  100,  117,   70,  100,  100,  100,  100,  100,    4,    5,
   97,   97,   89,   24,   25,   97,   63,   52,   97,   97,
   97,   53,   97,   82,   51,   97,   97,   97,   97,   97,
   24,   25,   94,   94,  129,   83,  138,   94,  139,   50,
   94,   94,   94,   23,   94,  109,   22,   94,   94,   94,
   94,   94,   91,  137,   98,   98,  150,   24,   25,   98,
  206,  207,   98,   98,   98,  151,   98,   23,  153,   98,
   98,   98,   98,   98,    4,    5,   95,   95,   89,   24,
   25,   95,  157,  156,   95,   95,   95,  168,   95,   55,
  169,   95,   95,   95,   95,   95,  171,   91,  101,  101,
   35,   24,   25,  101,  172,  176,  101,  101,  101,  179,
  101,   86,  184,  101,  101,  101,  101,  101,   75,  189,
   71,   71,   35,   24,   25,   71,  206,  207,   71,   71,
   71,  192,   71,  195,  196,   70,   70,   35,  205,   71,
   70,  131,  200,   70,   70,   70,   11,   70,   91,    4,
    5,  120,   35,   89,   70,  214,   45,   58,   59,   60,
   61,   95,  201,   24,   25,    1,  112,    3,    2,  126,
   43,   36,   44,   24,   25,  181,   37,   45,   24,   25,
   38,   11,    0,  148,  215,    0,    0,    0,   60,    0,
  198,   61,    0,   91,    0,   24,   25,   59,    0,    0,
   60,   60,    0,   61,   61,   62,    0,    0,    0,   59,
   59,    0,  210,  211,  212,  213,    0,   62,   62,    0,
    0,    0,   45,   91,    0,    0,    0,   63,  216,    0,
    0,    0,    0,   34,    2,    0,    0,    0,    3,   63,
   63,    4,    5,    6,    0,    7,    0,    0,    0,   74,
    2,    0,    8,    0,    3,   44,    2,    4,    5,    6,
    3,    7,    0,    4,    5,    6,    0,    7,    8,    0,
  141,    2,    0,    0,    8,    3,    0,    0,    4,    5,
    6,    0,    7,    0,    0,  187,    2,    0,    0,    8,
    3,    0,    0,    4,    5,    6,    0,    7,    0,    0,
   44,    2,    0,   11,    8,    3,  180,    2,    4,    5,
    6,    3,    7,    0,    4,    5,    6,    0,    7,    8,
    0,   44,    2,    0,    0,    8,    3,    0,   45,    4,
    5,    6,    0,    7,   44,    2,    0,    0,    0,    3,
    8,    0,    4,    5,    6,    0,    7,    0,  100,   53,
   51,    0,   52,    8,   54,  170,   53,   51,    0,   52,
    0,   54,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   29,   45,   40,   40,   61,   61,   61,   44,
   35,   40,   59,  139,   59,   88,   45,   59,   60,   61,
   62,   41,   26,   43,  256,   45,   61,   61,  268,   59,
  152,   61,  256,   40,   51,   52,  268,  159,   45,   59,
   60,   61,   62,   41,  269,   43,  270,   45,   65,   66,
   40,  173,   61,  175,   14,   41,   59,   42,  184,   61,
   45,   59,   60,   61,   62,   41,  256,   43,  269,   45,
  260,  256,   59,  100,  101,   79,  277,   45,   41,   39,
   61,  123,  267,   59,   60,   61,   62,   41,  115,   43,
   59,   45,   59,  110,   59,  125,   42,   59,   53,   54,
   44,   47,   43,  123,   45,   59,   60,   61,   62,   41,
   41,   43,   59,   45,   69,   70,   59,   42,   43,   59,
   45,  125,   47,  127,   61,  123,   45,   59,   60,   61,
   62,   41,  157,   43,  161,   45,  163,   61,   42,  166,
   61,  214,   22,   47,   59,   62,   61,  123,   45,   59,
   60,   61,   62,   41,   43,   43,   45,   45,   38,  132,
   60,   41,   62,  118,  256,  138,  121,  259,  260,  123,
   45,   59,   60,   61,   62,   41,  256,  257,  151,   61,
  153,  261,   62,   41,  264,  265,  266,   67,  268,   41,
   41,  123,   44,   59,   41,  275,   41,  259,  260,   43,
   40,   45,  259,  260,   41,  259,  260,  260,   59,  256,
   45,  256,   47,  123,  256,  257,   60,   61,   62,  261,
   45,  276,  264,  265,  266,   45,  268,  256,  268,  271,
  272,  273,  274,  275,  270,  123,  256,  257,  273,  268,
  269,  261,   45,  277,  264,  265,  266,   45,  268,  256,
   45,  271,  272,  273,  274,  275,   45,  123,  256,  257,
  269,  268,  269,  261,   45,  267,  264,  265,  266,  256,
  268,  256,  123,  271,  272,  273,  274,  275,  264,  265,
  256,  257,  268,  268,  269,  261,   45,  256,  264,  265,
  266,  256,  268,  256,  256,  271,  272,  273,  274,  275,
  268,  269,  256,  257,   44,  268,   41,  261,   44,  256,
  264,  265,  266,  256,  268,  256,  256,  271,  272,  273,
  274,  275,   43,  268,  256,  257,  268,  268,  269,  261,
  278,  279,  264,  265,  266,   41,  268,  256,   41,  271,
  272,  273,  274,  275,  264,  265,  256,  257,  268,  268,
  269,  261,  123,  269,  264,  265,  266,  260,  268,  256,
   41,  271,  272,  273,  274,  275,   41,   88,  256,  257,
  123,  268,  269,  261,   44,  277,  264,  265,  266,  277,
  268,  256,   44,  271,  272,  273,  274,  275,  125,  268,
  256,  257,  123,  268,  269,  261,  278,  279,  264,  265,
  266,   61,  268,   61,   61,  256,  257,  123,   44,  275,
  261,  256,  260,  264,  265,  266,    0,  268,  139,  264,
  265,  256,  123,  268,  275,   44,   10,  271,  272,  273,
  274,  256,  260,  268,  269,    0,  256,    0,    0,  125,
   41,   41,   41,  268,  269,  125,   41,   41,  268,  269,
   41,   35,   -1,  256,  205,   -1,   -1,   -1,  256,   -1,
  125,  256,   -1,  184,   -1,  268,  269,  256,   -1,   -1,
  268,  269,   -1,  268,  269,  256,   -1,   -1,   -1,  268,
  269,   -1,  193,  194,  195,  196,   -1,  268,  269,   -1,
   -1,   -1,   76,  214,   -1,   -1,   -1,  256,  209,   -1,
   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,  261,  268,
  269,  264,  265,  266,   -1,  268,   -1,   -1,   -1,  256,
  257,   -1,  275,   -1,  261,  256,  257,  264,  265,  266,
  261,  268,   -1,  264,  265,  266,   -1,  268,  275,   -1,
  256,  257,   -1,   -1,  275,  261,   -1,   -1,  264,  265,
  266,   -1,  268,   -1,   -1,  256,  257,   -1,   -1,  275,
  261,   -1,   -1,  264,  265,  266,   -1,  268,   -1,   -1,
  256,  257,   -1,  157,  275,  261,  256,  257,  264,  265,
  266,  261,  268,   -1,  264,  265,  266,   -1,  268,  275,
   -1,  256,  257,   -1,   -1,  275,  261,   -1,  182,  264,
  265,  266,   -1,  268,  256,  257,   -1,   -1,   -1,  261,
  275,   -1,  264,  265,  266,   -1,  268,   -1,   41,   42,
   43,   -1,   45,  275,   47,   41,   42,   43,   -1,   45,
   -1,   47,
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

//#line 225 "specification.y"

public LexicalAnalyzer la;

public Parser(String path) throws FileNotFoundException {
	la = new LexicalAnalyzer(path);
}

public void parse(){
	yyparse();
}

public void yyerror(String s){
	if(s.equals("syntax error")){
		//System.out.println("[Linea " + la.getCurrentLine()+ "] " + s + ".");
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
//#line 21 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] WARNING sintactico: Programa vacio!");}
break;
case 2:
//#line 22 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] Programa completo.");}
break;
case 3:
//#line 23 "specification.y"
{showMessage( "[Linea " + la.getCurrentLine() + "] ERROR sintactico: no se encontraron sentencias validas.");}
break;
case 6:
//#line 29 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' ausente al final de la sentencia.");}
break;
case 7:
//#line 30 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: ';' sentencia mal construida.");}
break;
case 10:
//#line 38 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de variable/s.");}
break;
case 11:
//#line 39 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Declaracion de procedimiento.");}
break;
case 12:
//#line 40 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: no hay tipo para el identificador\"" + val_peek(0).sval + "\".");}
break;
case 13:
//#line 41 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un identificador y no se encontro.");}
break;
case 20:
//#line 56 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 21:
//#line 57 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Procedimiento declarado.");}
break;
case 22:
//#line 58 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 23:
//#line 59 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el cuerpo del procedimiento.");}
break;
case 24:
//#line 60 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}
break;
case 25:
//#line 61 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el identificador del procedimiento.");}
break;
case 26:
//#line 62 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se definio mal la lista de parametros.");}
break;
case 28:
//#line 66 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra NA");}
break;
case 29:
//#line 67 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del NA.");}
break;
case 30:
//#line 68 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta definir el valor del NA.");}
break;
case 31:
//#line 69 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la palabra SHADOWING.");}
break;
case 32:
//#line 70 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta el '=' del SHADOWING.");}
break;
case 34:
//#line 75 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: cuerpo del procedimiento mal definido.");}
break;
case 36:
//#line 79 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 37:
//#line 80 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 38:
//#line 81 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de parametros detectada.");}
break;
case 39:
//#line 82 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 41:
//#line 86 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta identificador en declaracion de parametro.");}
break;
case 42:
//#line 87 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta tipo en declaracion de parametro.");}
break;
case 43:
//#line 90 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
break;
case 44:
//#line 91 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
break;
case 45:
//#line 92 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Lista de identificadores detectada.");}
break;
case 46:
//#line 93 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: un procedimiento puede recibir un maximo de tres parametros.");}
break;
case 47:
//#line 94 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la lista de identificadores esta mal conformada.");}
break;
case 50:
//#line 101 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Asignacion.");}
break;
case 51:
//#line 102 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera una expresion del lado derecho.");}
break;
case 52:
//#line 103 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. Se espera un identificador del lado izquierdo.");}
break;
case 53:
//#line 104 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: asignacion erronea. ¿Quisiste decir '='?.");}
break;
case 54:
//#line 105 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia IF.");}
break;
case 55:
//#line 106 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia LOOP.");}
break;
case 56:
//#line 107 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Invocacion PROC.");}
break;
case 57:
//#line 108 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia OUT.");}
break;
case 64:
//#line 117 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '<' duplicado.");}
break;
case 65:
//#line 118 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: token '>' duplicado.");}
break;
case 66:
//#line 119 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '<='?.");}
break;
case 67:
//#line 120 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '>='?.");}
break;
case 68:
//#line 121 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 69:
//#line 122 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: comparador erroneo. ¿Quisiste decir '!='?.");}
break;
case 70:
//#line 125 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Condicion.");}
break;
case 71:
//#line 126 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: Comparacion invalida. ¿Quisiste decir '=='?.");}
break;
case 75:
//#line 138 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia if");}
break;
case 76:
//#line 139 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia if");}
break;
case 77:
//#line 140 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 78:
//#line 141 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta palabra reservada END_INF al final de la sentencia IF");}
break;
case 79:
//#line 142 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
break;
case 80:
//#line 143 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: luego de la palabra reservada IF se espera una condicion entre parentesis.");}
break;
case 81:
//#line 144 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
break;
case 82:
//#line 145 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula ELSE_IF.");}
break;
case 83:
//#line 146 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: se esperaba un bloque de sentencias dentro de la clausula IF.");}
break;
case 84:
//#line 147 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 85:
//#line 148 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: La clausula IF requiere una condicion encerrada en '(' ')'.");}
break;
case 86:
//#line 151 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia loop");}
break;
case 87:
//#line 152 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 88:
//#line 153 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la clausula UNTIL debe incluir una condicion entre parentesis");}
break;
case 89:
//#line 154 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 90:
//#line 155 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 91:
//#line 162 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Sentencia out");}
break;
case 92:
//#line 163 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 93:
//#line 164 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 94:
//#line 171 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");}
break;
case 95:
//#line 172 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");}
break;
case 96:
//#line 173 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");}
break;
case 97:
//#line 174 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 98:
//#line 175 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 99:
//#line 176 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 100:
//#line 177 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 101:
//#line 178 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 102:
//#line 181 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");}
break;
case 103:
//#line 182 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Division.");}
break;
case 104:
//#line 183 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");}
break;
case 105:
//#line 184 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 106:
//#line 185 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 107:
//#line 186 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 108:
//#line 187 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 109:
//#line 188 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 110:
//#line 189 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 113:
//#line 194 "specification.y"
{
							/* Manejo la entrada positiva de esta constante		    				*/
		    				 Symbol positivo = la.symbolsTable.getSymbol(val_peek(0).sval);
		    				 if (positivo.getType()==Symbol._ULONGINT)
		    				 	showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: una constante del tipo entero largo sin signo no puede ser negativa");
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
//#line 1062 "Parser.java"
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
