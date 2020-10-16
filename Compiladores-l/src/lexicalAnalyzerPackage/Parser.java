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
   21,   21,   21,   16,   16,   22,   23,   24,   17,   17,
   17,   17,   17,   18,   18,   18,   15,   15,   15,   15,
   15,   15,   15,   15,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   26,   26,   26,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    3,    2,    2,    1,    1,    2,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    7,
    6,    6,    5,    6,    5,    7,    6,    5,    5,    5,
    5,    5,    3,    3,    2,    1,    3,    5,    7,    2,
    1,    1,    1,    3,    5,    7,    4,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    2,    3,    3,
    3,    2,    2,    6,    4,    3,    1,    1,    6,    3,
    6,    6,    4,    4,    4,    2,    3,    3,    1,    3,
    3,    3,    3,    4,    3,    3,    1,    3,    3,    3,
    3,    4,    4,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   55,   53,   54,   56,
    7,    0,    0,    0,   86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    6,    4,
    0,   10,    0,  104,  105,    0,    0,    0,   97,    0,
    0,   77,    0,    0,    0,    0,    0,   73,    0,   72,
   80,    0,    0,   15,    0,   48,    0,    0,    0,    0,
   42,    0,    0,    0,    0,    5,    0,    0,    0,    0,
  106,    0,    0,    0,    0,    0,    0,   57,    0,    0,
    0,    0,    0,   76,    0,   75,   85,   84,    0,   71,
    0,   83,    0,   47,    0,    0,    0,    0,    0,    0,
   40,    0,    0,    0,    0,    0,  100,  101,    0,    0,
    0,    0,    0,   98,    0,   95,   99,    0,   96,   65,
   66,   67,    0,   63,   68,   64,    0,   78,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   25,    0,    0,    0,  102,  103,   74,   82,   81,   79,
    0,    0,   21,    0,    0,    0,    0,    0,    0,   35,
    0,   24,    0,    0,   26,   20,    0,    0,    0,    0,
    0,   34,   33,    0,    0,   18,   19,   29,    0,   32,
   31,   30,   28,    0,   46,   27,   39,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   14,   15,   16,  188,   74,  110,
  151,   75,   67,   17,   50,   18,   19,   20,   93,   51,
   30,   24,   53,  139,   48,   49,
};
final static short yysindex[] = {                       -57,
  115,    8,  -25,    0,    0,  -78,   30,  -39,    0,  177,
  -43,    0,    0, -214,    0,    0,    0,    0,    0,    0,
    0,  -42,  -42,  111,    0, -143,  -50,  124,   46, -181,
  -42, -214,  -24,   22,   71,  -14,   69,   82,    0,    0,
   37,    0,  404,    0,    0, -105,   97,   89,    0,   33,
  127,    0, -126,  134,  136,  145,  115,    0,  141,    0,
    0,   52,   97,    0,  149,    0,  148,  404,   97,   24,
    0,  -48,  -70,  164,  178,    0,   59,   59,   14,   14,
    0,  -10,   61,   54,   56,  160,  187,    0,  192,  -42,
  150,  193,  -42,    0,  111,    0,    0,    0,  -42,    0,
   63,    0,  -29,    0,  219,  -48,  220,  -53,    2,  163,
    0,  -48,  -62,  114,   89,   89,    0,    0,  114,   59,
   89,  114,   89,    0,   14,    0,    0,   14,    0,    0,
    0,    0,   97,    0,    0,    0,   97,    0,   25,  252,
  315,  257,  272,  -48,  163,  -48,   64, -159,   65,  156,
    0,  163,  299,   89,    0,    0,    0,    0,    0,    0,
   86,  163,    0,  163,  283,  -21,  298,  300,   41,    0,
  162,    0,  -62,  319,    0,    0, -130,  -55, -130, -130,
 -130,    0,    0,  320,   98,    0,    0,    0, -130,    0,
    0,    0,    0,  -62,    0,    0,    0,
};
final static short yyrindex[] = {                       365,
  370,    0,    0,    0,    0,    0,  -37,    0,    0,  371,
    0,    0,    0,   20,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -37,    0,    0,    0,    0,    0,   21,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   53,    0,  332,    0,    0,   55,   58,    0,
    0,    0,  119,    0,  333,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   70,   77,    0,   79,    0,
   81,   84,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -36,  -31,    0,    0,   -9,    0,
   -4,    1,   23,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   57,    0,    0,    0,   78,    0,    0,    0,
    0,    0,  337,    0,   62,    0,    0,    0,    0,    0,
    0,    0,  341,   28,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   80,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  342,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  343,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   19,  345,    0,    0,    7,   93,    0,   -8,  -32,   38,
   -7,  -64,  202,    0,  101,    0,    0,    0,    0,   66,
   48,    0,    0,    0,   76,   67,
};
final static int YYTABLESIZE=516;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         89,
   36,   89,   46,   89,   92,  189,   92,  148,   92,   93,
   22,   93,  109,   93,   26,   40,   66,   89,   89,   89,
   89,   14,   92,   92,   92,   92,   72,   93,   93,   93,
   93,   90,  120,   90,   46,   90,   87,  107,   87,  179,
   87,   91,   73,   91,   28,   91,   59,   23,  153,   90,
   90,   90,   90,   41,   87,   87,   87,   87,   46,   91,
   91,   91,   91,   88,  106,   88,   46,   88,   94,   33,
   94,   52,   94,   32,   61,   82,   73,   83,   13,   51,
   32,   88,   88,   88,   88,   62,   94,   94,   94,   94,
   34,  101,   91,   90,   92,  125,   46,   70,   46,   21,
   46,   22,  128,   46,   60,   46,   42,   46,  184,  166,
   70,   52,   54,   50,   59,   70,   49,  167,   69,   73,
   23,   60,   47,   58,   64,   61,   55,  102,   62,   22,
   84,   63,   95,   96,   69,   85,   69,  163,   22,   82,
   76,   83,  138,  145,  172,  117,  118,  186,  187,  152,
  126,  129,  115,  116,  175,   79,  176,  121,  123,   41,
   80,  197,   41,   81,  140,  182,  142,   94,  171,  190,
  191,  192,  193,   21,   97,   22,   98,   27,    2,   73,
  196,  162,    3,  164,   99,    4,    5,    6,  104,    7,
  133,  155,  103,  137,  156,  154,    8,  111,    1,    2,
   73,    4,    5,    3,  112,   71,    4,    5,    6,  134,
    7,  135,   39,   43,   89,  147,   56,    8,   14,   92,
  130,  113,  186,  187,   93,   44,   45,  108,   35,   89,
   89,   89,   89,   28,   92,   92,   92,   92,  143,   93,
   93,   93,   93,   65,   25,  119,   90,  131,   58,    4,
    5,   87,  132,   71,  136,  178,   91,   44,   45,  144,
  146,   90,   90,   90,   90,  100,   87,   87,   87,   87,
  149,   91,   91,   91,   91,   13,   51,   68,   88,  105,
  170,   44,   45,   94,  157,  150,  183,    4,    5,   44,
   45,   71,  158,   88,   88,   88,   88,  160,   94,   94,
   94,   94,   31,   86,   87,   88,   89,   43,   52,  124,
   50,  127,   70,   49,  114,  161,  122,   23,  141,   44,
   45,   44,   45,   44,   45,   59,   44,   45,   44,   45,
   44,   45,   60,   69,   58,   22,   61,   59,   59,   62,
  165,  168,  173,  177,   60,   60,   58,   58,   61,   61,
   29,   62,   62,  174,   38,  159,   79,   77,  180,   78,
  181,   80,  185,  194,    1,   65,   37,    2,   29,    3,
    2,    3,   43,   36,    4,    5,    6,   44,    7,   57,
    2,   37,   45,   38,    3,    8,  195,    4,    5,    6,
    0,    7,    0,    0,    0,    0,   37,    2,    8,    0,
    0,    3,    0,   38,    4,    5,    6,    0,    7,    0,
    0,  169,    2,    0,    0,    8,    3,   37,    2,    4,
    5,    6,    3,    7,    0,    4,    5,    6,    0,    7,
    8,    0,   37,    2,    0,    0,    8,    3,    0,   29,
    4,    5,    6,    0,    7,   79,   77,    0,   78,    0,
   80,    8,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   38,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   45,   45,   41,   61,   43,   61,   45,   41,
   61,   43,   61,   45,   40,   59,   41,   59,   60,   61,
   62,   59,   59,   60,   61,   62,   41,   59,   60,   61,
   62,   41,   43,   43,   45,   45,   41,   70,   43,   61,
   45,   41,   36,   43,  123,   45,   28,   40,  113,   59,
   60,   61,   62,  268,   59,   60,   61,   62,   45,   59,
   60,   61,   62,   41,   41,   43,   45,   45,   41,   40,
   43,   24,   45,   44,  256,   43,   70,   45,   59,   59,
   44,   59,   60,   61,   62,  267,   59,   60,   61,   62,
   61,   40,   60,   61,   62,   42,   45,   41,   45,   59,
   45,   61,   47,   45,   59,   45,   14,   45,  173,  269,
   40,   59,  256,   59,   45,   59,   59,  277,   41,  113,
   59,   45,   22,   45,   32,   45,  270,   62,   45,   61,
   42,   31,  259,  260,   34,   47,   59,  145,   59,   43,
   59,   45,   95,  106,  152,   79,   80,  278,  279,  112,
   84,   85,   77,   78,  162,   42,  164,   82,   83,   41,
   47,  194,   44,  269,   99,  125,  101,   41,  150,  178,
  179,  180,  181,   59,   41,   61,   41,  256,  257,  173,
  189,  144,  261,  146,   40,  264,  265,  266,   41,  268,
   90,  125,   44,   93,  128,  120,  275,  268,  256,  257,
  194,  264,  265,  261,   41,  268,  264,  265,  266,   60,
  268,   62,  256,  256,  256,  269,  267,  275,  256,  256,
   61,   44,  278,  279,  256,  268,  269,  276,  268,  271,
  272,  273,  274,  123,  271,  272,  273,  274,  268,  271,
  272,  273,  274,  268,  270,  256,  256,   61,  125,  264,
  265,  256,   61,  268,   62,  277,  256,  268,  269,   41,
   41,  271,  272,  273,  274,  125,  271,  272,  273,  274,
  269,  271,  272,  273,  274,  256,  256,  256,  256,  256,
  125,  268,  269,  256,  260,  123,  125,  264,  265,  268,
  269,  268,   41,  271,  272,  273,  274,   41,  271,  272,
  273,  274,  273,  271,  272,  273,  274,  256,  256,  256,
  256,  256,  256,  256,  256,   44,  256,  256,  256,  268,
  269,  268,  269,  268,  269,  256,  268,  269,  268,  269,
  268,  269,  256,  256,  256,  256,  256,  268,  269,  256,
  277,  277,   44,   61,  268,  269,  268,  269,  268,  269,
    6,  268,  269,  268,   10,   41,   42,   43,   61,   45,
   61,   47,   44,   44,    0,  268,  256,  257,   24,    0,
    0,  261,   41,   41,  264,  265,  266,   41,  268,  256,
  257,   41,   41,   41,  261,  275,  185,  264,  265,  266,
   -1,  268,   -1,   -1,   -1,   -1,  256,  257,  275,   -1,
   -1,  261,   -1,   59,  264,  265,  266,   -1,  268,   -1,
   -1,  256,  257,   -1,   -1,  275,  261,  256,  257,  264,
  265,  266,  261,  268,   -1,  264,  265,  266,   -1,  268,
  275,   -1,  256,  257,   -1,   -1,  275,  261,   -1,   95,
  264,  265,  266,   -1,  268,   42,   43,   -1,   45,   -1,
   47,  275,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  171,
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
"if_clause : IF if_condition cpo_then ELSE cpo_else END_IF",
"if_clause : IF if_condition cpo_then END_IF",
"if_condition : '(' condition ')'",
"cpo_then : sentence_block",
"cpo_else : sentence_block",
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

//#line 303 "specification.y"

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
//#line 506 "Parser.java"
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
case 74:
//#line 144 "specification.y"
{
                                                                  int unstacked = ic.topOfStack();
																  ic.popFromStack();
							 	                                  Triplet trip = ic.getTriplet(unstacked);
								                                  trip.modifyFirstOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+1)));
                                                                  }
break;
case 75:
//#line 151 "specification.y"
{
                                                                  int unstacked = ic.topOfStack();
																  ic.popFromStack();
							 	                                  Triplet trip = ic.getTriplet(unstacked);
								                                  trip.modifyFirstOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+1)));
 		                                                          }
break;
case 76:
//#line 170 "specification.y"
{
										Operand op1 = (Operand) val_peek(1).obj; 
      									Operand op2 = new Operand(Operand.TOBEDEFINED,"-1");
      									Operator opt = new Operator("BF");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									ic.pushToStack(t.getNumId());
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());
										}
break;
case 77:
//#line 181 "specification.y"
{
								int unstacked = ic.topOfStack();
								ic.popFromStack();
								Triplet trip = ic.getTriplet(unstacked);
								trip.modifySecondOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+2)));
								Operand op1 = new Operand(Operand.TOBEDEFINED,"-1");
								Operand op2 = null;
								Operator opt = new Operator("BI");
								Triplet t = new Triplet (opt,op1,op2);
								ic.addTriplet(t);
								ic.pushToStack(t.getNumId());
								}
break;
case 80:
//#line 199 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 81:
//#line 200 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la clausula UNTIL debe incluir una condicion entre parentesis");}
break;
case 82:
//#line 201 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 83:
//#line 202 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 84:
//#line 209 "specification.y"
{ Operand op = new Operand(Operand.TOBEDEFINED,val_peek(1).sval);
						   Operator opt = new Operator("OUT");
						   Triplet t = new Triplet(opt,op);
						   ic.addTriplet(t);

 						 }
break;
case 85:
//#line 215 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 86:
//#line 216 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 87:
//#line 223 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
										Operand op1 = (Operand) val_peek(2).obj; 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("+");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 88:
//#line 230 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
										Operand op1 = (Operand) val_peek(2).obj; 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("-");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 89:
//#line 237 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
										 yyval.obj = val_peek(0).obj;}
break;
case 90:
//#line 239 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 91:
//#line 240 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 92:
//#line 241 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 93:
//#line 242 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 94:
//#line 243 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 95:
//#line 246 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
							Operand op1 = (Operand) val_peek(2).obj; 
      						Operand op2 = (Operand) val_peek(0).obj; 
      						Operator opt = new Operator("*");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 96:
//#line 253 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      						Operand op1 = (Operand) val_peek(2).obj; 
      						Operand op2 = (Operand) val_peek(0).obj; 
      						Operator opt = new Operator("/");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 97:
//#line 260 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 yyval.obj = val_peek(0).obj;}
break;
case 98:
//#line 262 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 99:
//#line 263 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 100:
//#line 264 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 101:
//#line 265 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 102:
//#line 266 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 103:
//#line 267 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 104:
//#line 270 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 105:
//#line 271 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 106:
//#line 272 "specification.y"
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
//#line 1061 "Parser.java"
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
