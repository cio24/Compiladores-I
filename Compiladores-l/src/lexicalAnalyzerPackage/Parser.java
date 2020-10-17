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
   17,   17,   17,   25,   18,   18,   18,   15,   15,   15,
   15,   15,   15,   15,   15,   26,   26,   26,   26,   26,
   26,   26,   26,   26,   27,   27,   27,
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
    6,    6,    4,    1,    4,    4,    2,    3,    3,    1,
    3,    3,    3,    3,    4,    3,    3,    1,    3,    3,
    3,    3,    4,    4,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,   17,    0,    0,    0,    0,    0,
    0,    8,    9,    0,   12,   11,   55,   53,   54,   56,
    0,    7,    0,    0,    0,   87,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    6,
    4,    0,   10,    0,    0,  105,  106,    0,    0,    0,
   98,    0,    0,   77,    0,    0,    0,    0,    0,   73,
    0,   72,   80,    0,    0,   15,    0,   48,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    5,    0,    0,
    0,    0,    0,  107,    0,    0,    0,    0,    0,    0,
   57,    0,    0,    0,    0,    0,   76,    0,   75,   86,
   85,    0,   71,    0,   83,    0,   47,    0,    0,    0,
    0,    0,    0,   40,    0,    0,    0,    0,    0,    0,
  101,  102,    0,    0,    0,    0,    0,   99,    0,   96,
  100,    0,   97,   65,   66,   67,    0,   63,   68,   64,
    0,   78,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0,    0,  103,  104,
   74,   82,   81,    0,    0,   21,    0,    0,    0,    0,
    0,    0,   35,    0,   24,    0,   79,    0,   26,   20,
    0,    0,    0,    0,    0,   34,   33,    0,    0,   18,
   19,   29,    0,   32,   31,   30,   28,    0,   46,   27,
   39,
};
final static short yydgoto[] = {                          9,
   10,   30,   12,   13,   14,   15,   16,  192,   76,  113,
  154,   77,   69,   17,   52,   18,   19,   20,   96,   53,
   31,   25,   55,  143,   21,   50,   51,
};
final static short yysindex[] = {                       153,
  -12,   67,  -25,    0,    0,  -85,   30,  -39,    0,  159,
  -43,    0,    0, -187,    0,    0,    0,    0,    0,    0,
  -69,    0,  -42,  -42,  -69,    0, -129,  -50,   96,   85,
 -181,  -42, -187,  -24,   22,   89,  -14,   87,   92,    0,
    0,  116,    0, -106,  336,    0,    0, -102,   75,  115,
    0,   33,  129,    0, -124,  134,  137,  145,  -12,    0,
  109,    0,    0,   52,   75,    0,  133,    0,  150,  336,
   75,   24,    0,  -48,  -75,  157,  161,    0,  154,   59,
   59,   14,   14,    0,  -10,   61,   54,   56,  141,  149,
    0,  178,  -42,  124,  156,  -42,    0,  -69,    0,    0,
    0,  -42,    0,  -45,    0,  -46,    0,  207,  -48,  212,
  -53,   12,  162,    0,  -48,  -56,  -42,  126,  115,  115,
    0,    0,  126,   59,  115,  126,  115,    0,   14,    0,
    0,   14,    0,    0,    0,    0,   75,    0,    0,    0,
   75,    0,   26,  219,  252,  254,  -48,  162,  -48,   39,
 -139,   82,  130,    0,  162,  300,  313,  115,    0,    0,
    0,    0,    0,   88,  162,    0,  162,  294,  -21,  297,
  302,   41,    0,  136,    0,  -56,    0,  323,    0,    0,
 -123,  -55, -123, -123, -123,    0,    0,  324,  101,    0,
    0,    0, -123,    0,    0,    0,    0,  -56,    0,    0,
    0,
};
final static short yyrindex[] = {                       372,
  376,    0,    0,    0,    0,    0,  -37,    0,    0,  380,
    0,    0,    0,   20,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -37,    0,    0,    0,    0,    0,    0,   21,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   53,    0,  341,    0,    0,   55,
   58,    0,    0,    0,    4,    0,  344,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   63,   70,
    0,   77,    0,   79,   81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -36,  -31,
    0,    0,   -9,    0,   -4,    1,   23,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   57,    0,    0,    0,
   78,    0,    0,    0,    0,  347,    0,   62,    0,    0,
    0,    0,    0,    0,    0,  348,    0,   28,    0,    0,
    0,    0,    0,    0,    0,    0,   80,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  349,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  358,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   48,  113,    0,    0,   73,  114,    0,  158,  -29,   16,
  -15,  -44,  214,    0,  111,    0,    0,    0,    0,   47,
   84,    0,    0,    0,    0,  327,   71,
};
final static int YYTABLESIZE=451;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         90,
   37,   90,   48,   90,   93,  193,   93,  151,   93,   94,
   23,   94,  112,   94,   27,   41,   68,   90,   90,   90,
   90,   14,   93,   93,   93,   93,   74,   94,   94,   94,
   94,   91,  124,   91,   48,   91,   88,   29,   88,  183,
   88,   92,  110,   92,   41,   92,   22,   41,   23,   91,
   91,   91,   91,   29,   88,   88,   88,   88,   48,   92,
   92,   92,   92,   89,  109,   89,   48,   89,   95,   34,
   95,  156,   95,   33,   63,   85,   61,   86,   13,   51,
   42,   89,   89,   89,   89,   64,   95,   95,   95,   95,
   35,  104,   94,   93,   95,  129,   48,   70,   48,   22,
   48,   23,  132,   48,   44,   48,   24,   59,   54,   75,
  105,   52,   11,   50,   60,   70,   49,   85,   69,   86,
   23,   58,   39,   61,  148,   62,   56,   43,   72,  169,
  155,  188,  166,   49,   98,   99,   69,  170,   22,  175,
   57,   11,   65,   62,   75,   71,   66,   23,  144,  179,
   78,  180,  121,  122,  190,  191,   87,  130,  133,   33,
   79,   88,  165,  157,  167,  186,   84,   82,  201,   97,
   28,    2,   83,   39,  100,    3,  106,  101,    4,    5,
    6,  142,    7,  138,  102,  139,   38,    2,   75,    8,
  107,    3,  114,  117,    4,    5,    6,  115,    7,  159,
  174,  134,  160,  137,  116,    8,  141,    4,    5,  135,
  145,   73,   40,   45,   90,  150,   58,  140,   14,   93,
   60,  146,  190,  191,   94,   46,   47,  111,   36,   90,
   90,   90,   90,  103,   93,   93,   93,   93,  136,   94,
   94,   94,   94,   67,   26,  123,   91,  147,   75,    4,
    5,   88,  149,   73,  173,  182,   92,   46,   47,  162,
  187,   91,   91,   91,   91,   11,   88,   88,   88,   88,
   75,   92,   92,   92,   92,   13,   51,   70,   89,  108,
  152,   46,   47,   95,  153,  161,   39,    4,    5,   46,
   47,   73,  163,   89,   89,   89,   89,  164,   95,   95,
   95,   95,   32,   89,   90,   91,   92,   45,   52,  128,
   50,  131,   70,   49,  118,  168,  126,   23,   59,   46,
   47,   46,   47,   46,   47,   60,   46,   47,   46,   47,
   59,   59,   58,   69,   61,   22,   62,   60,   60,  194,
  195,  196,  197,  176,   58,   58,   61,   61,   62,   62,
  200,   59,    2,  177,  181,  178,    3,  184,  171,    4,
    5,    6,  185,    7,   38,    2,  189,  198,   67,    3,
    8,    1,    4,    5,    6,    3,    7,   82,   80,    2,
   81,   43,   83,    8,   36,  172,    2,   44,   37,   45,
    3,   38,    2,    4,    5,    6,    3,    7,   38,    4,
    5,    6,  199,    7,    8,    0,  119,  120,    1,    2,
    8,  125,  127,    3,   38,    2,    4,    5,    6,    3,
    7,    0,    4,    5,    6,    0,    7,    8,    0,    0,
    0,    0,    0,    8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  158,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   45,   45,   41,   61,   43,   61,   45,   41,
   61,   43,   61,   45,   40,   59,   41,   59,   60,   61,
   62,   59,   59,   60,   61,   62,   41,   59,   60,   61,
   62,   41,   43,   43,   45,   45,   41,  123,   43,   61,
   45,   41,   72,   43,   41,   45,   59,   44,   61,   59,
   60,   61,   62,  123,   59,   60,   61,   62,   45,   59,
   60,   61,   62,   41,   41,   43,   45,   45,   41,   40,
   43,  116,   45,   44,  256,   43,   29,   45,   59,   59,
  268,   59,   60,   61,   62,  267,   59,   60,   61,   62,
   61,   40,   60,   61,   62,   42,   45,   41,   45,   59,
   45,   61,   47,   45,   21,   45,   40,   45,   25,   37,
   64,   59,    0,   59,   45,   59,   59,   43,   41,   45,
   59,   45,   10,   45,  109,   45,  256,   14,   40,  269,
  115,  176,  148,   23,  259,  260,   59,  277,   59,  155,
  270,   29,   32,   59,   72,   35,   33,   61,  102,  165,
   59,  167,   82,   83,  278,  279,   42,   87,   88,   44,
  267,   47,  147,  117,  149,  125,  269,   42,  198,   41,
  256,  257,   47,   61,   41,  261,   44,   41,  264,  265,
  266,   98,  268,   60,   40,   62,  256,  257,  116,  275,
   41,  261,  268,   40,  264,  265,  266,   41,  268,  129,
  153,   61,  132,   93,   44,  275,   96,  264,  265,   61,
  256,  268,  256,  256,  256,  269,  267,   62,  256,  256,
  125,  268,  278,  279,  256,  268,  269,  276,  268,  271,
  272,  273,  274,  125,  271,  272,  273,  274,   61,  271,
  272,  273,  274,  268,  270,  256,  256,   41,  176,  264,
  265,  256,   41,  268,  125,  277,  256,  268,  269,   41,
  125,  271,  272,  273,  274,  153,  271,  272,  273,  274,
  198,  271,  272,  273,  274,  256,  256,  256,  256,  256,
  269,  268,  269,  256,  123,  260,  174,  264,  265,  268,
  269,  268,   41,  271,  272,  273,  274,   44,  271,  272,
  273,  274,  273,  271,  272,  273,  274,  256,  256,  256,
  256,  256,  256,  256,  256,  277,  256,  256,  256,  268,
  269,  268,  269,  268,  269,  256,  268,  269,  268,  269,
  268,  269,  256,  256,  256,  256,  256,  268,  269,  182,
  183,  184,  185,   44,  268,  269,  268,  269,  268,  269,
  193,  256,  257,   41,   61,  268,  261,   61,  277,  264,
  265,  266,   61,  268,  256,  257,   44,   44,  268,  261,
  275,    0,  264,  265,  266,    0,  268,   42,   43,    0,
   45,   41,   47,  275,   41,  256,  257,   41,   41,   41,
  261,  256,  257,  264,  265,  266,  261,  268,   41,  264,
  265,  266,  189,  268,  275,   -1,   80,   81,  256,  257,
  275,   85,   86,  261,  256,  257,  264,  265,  266,  261,
  268,   -1,  264,  265,  266,   -1,  268,  275,   -1,   -1,
   -1,   -1,   -1,  275,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  124,
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
"loop_clause : loop_begin sentence_block UNTIL '(' condition ')'",
"loop_clause : LOOP sentence_block error",
"loop_clause : LOOP sentence_block UNTIL '(' error ')'",
"loop_clause : LOOP error UNTIL '(' condition ')'",
"loop_clause : LOOP sentence_block UNTIL condition",
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

//#line 320 "specification.y"

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
//#line 498 "Parser.java"
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
								int unstacked = ic.topOfStack(); /*we get the id of the triplet on the top of the stack*/
								ic.popFromStack(); /*we remove the id triplet from the top of the stack*/
								Triplet trip = ic.getTriplet(unstacked); /*then we get the triplet so we can write in the second operand*/
								trip.modifySecondOperand(new Operand(Operand.TRIPLET,String.valueOf(ic.currentTripletIndex()+2))); /*the adress of the jump*/
								Operand op1 = new Operand(Operand.TOBEDEFINED,"-1");
								Operand op2 = null;
								Operator opt = new Operator("BI");
								Triplet t = new Triplet (opt,op1,op2);
								ic.addTriplet(t);
								ic.pushToStack(t.getNumId());
								}
break;
case 79:
//#line 198 "specification.y"
{       int unstacked = ic.topOfStack(); /*we get the id of the triplet that represent the adress of the tag that we need to jump*/
                                                                          ic.popFromStack(); /*we remove the id triplet from the top of the stack*/
                                                                          Operand op1 = (Operand) val_peek(4).obj; /*we get the triplet asociate to the condition*/
                                                                          Operand op2 = new Operand(Operand.TRIPLET,ic.getTriplet(unstacked).getId()); /*this will contain the jump adress*/
                                                                          Operator opt = new Operator("BF"); /*the operation of the tiplet is the branch not equal*/
                                                                          Triplet t = new Triplet(opt,op1,op2);
                                                                          ic.addTriplet(t); /*then we save the triplet in order to retrieve it later for the generation of the code*/
                                                                          yyval.obj = new Operand(Operand.TRIPLET,t.getId()); /*finally we associate an operand created with the tiplet to the loop_condition*/
                                                                          }
break;
case 80:
//#line 207 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: falta la clausula UNTIL en la sentencia LOOP");}
break;
case 81:
//#line 208 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la clausula UNTIL debe incluir una condicion entre parentesis");}
break;
case 82:
//#line 209 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir un bloque de sentencias");}
break;
case 83:
//#line 210 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia LOOP debe incluir una condicion encerrada por '(' ')'");}
break;
case 84:
//#line 213 "specification.y"
{
                        				       Operator opt = new Operator("TAG TO JUMP");
                        				       Triplet t = new Triplet(opt); /*this triplet is used as a tag to jump*/
                        				       ic.pushToStack(t.getNumId()); /*we have to stack this triplet so we can get the adress jump when we make the triplet associate to the condition*/
                        				       ic.addTriplet(t); /*then we save the triplet in order to retrieve it later for the generation of the code*/
                        				       yyval.obj = new Operand(Operand.TRIPLET,t.getId()); /*finally we associate an operand created with the tiplet to the loop_begin*/
                        				     }
break;
case 85:
//#line 226 "specification.y"
{ Operand op = new Operand(Operand.TOBEDEFINED,val_peek(1).sval);
						   Operator opt = new Operator("OUT");
						   Triplet t = new Triplet(opt,op);
						   ic.addTriplet(t);

 						 }
break;
case 86:
//#line 232 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT solo acepta cadenas de caracteres.");}
break;
case 87:
//#line 233 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: la sentencia OUT debe incluir una cadena de caracteres encerrada por '(' ')'");}
break;
case 88:
//#line 240 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Suma.");
										Operand op1 = (Operand) val_peek(2).obj; 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("+");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 89:
//#line 247 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Resta.");
										Operand op1 = (Operand) val_peek(2).obj; 
      									Operand op2 = (Operand) val_peek(0).obj; 
      									Operator opt = new Operator("-");
      									Triplet t = new Triplet(opt,op1,op2);
      									ic.addTriplet(t);
      									yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 90:
//#line 254 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Termino.");
										 yyval.obj = val_peek(0).obj;}
break;
case 91:
//#line 256 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la suma debe contener un termino valido.");}
break;
case 92:
//#line 257 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la resta debe contener un termino valido.");}
break;
case 93:
//#line 258 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la suma debe contener una expresion valida.");}
break;
case 94:
//#line 259 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la resta debe contener una expresion valida.");}
break;
case 95:
//#line 260 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '+' sobrante.");}
break;
case 96:
//#line 263 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Multiplicacion.");
							Operand op1 = (Operand) val_peek(2).obj; 
      						Operand op2 = (Operand) val_peek(0).obj; 
      						Operator opt = new Operator("*");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 97:
//#line 270 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Division.");
      						Operand op1 = (Operand) val_peek(2).obj; 
      						Operand op2 = (Operand) val_peek(0).obj; 
      						Operator opt = new Operator("/");
      						Triplet t = new Triplet(opt,op1,op2);
      						ic.addTriplet(t);
      						yyval.obj = new Operand(Operand.TRIPLET,t.getId());}
break;
case 98:
//#line 277 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] Factor.");
	  						 yyval.obj = val_peek(0).obj;}
break;
case 99:
//#line 279 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la multiplicacion debe llevar una constante o un identificador");}
break;
case 100:
//#line 280 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado derecho de la division debe llevar una constante o un identificador");}
break;
case 101:
//#line 281 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la multiplicacion debe llevar una termino o un factor");}
break;
case 102:
//#line 282 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: el lado izquierdo de la division debe llevar un termino o un factor");}
break;
case 103:
//#line 283 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '*' sobrante");}
break;
case 104:
//#line 284 "specification.y"
{showMessage("[Linea " + la.getCurrentLine() + "] ERROR sintactico: operador '/' sobrante");}
break;
case 105:
//#line 287 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 106:
//#line 288 "specification.y"
{  yyval.obj = new Operand(Operand.ST_POINTER,val_peek(0).sval); }
break;
case 107:
//#line 289 "specification.y"
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
//#line 1075 "Parser.java"
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
