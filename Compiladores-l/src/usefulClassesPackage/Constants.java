package usefulClassesPackage;

import java.math.BigDecimal;

public class Constants {
	public final static int IF=257;
	public final static int THEN=258;
	public final static int ELSE=259;
	public final static int END_IF=260;
	public final static int OUT=261;
	public final static int FUNC=262;
	public final static int RETURN=263;
	public final static int ULONGINT=264;
	public final static int DOUBLE=265;
	public final static int LOOP=266;
	public static final int UNTIL=267;
	public static final int ID = 268;
	public static final int CONSTANT = 269;
	public static final int CSTRING = 270;	
	public static final int LESSEQUAL = 271;
	public static final int GREATEQUAL = 272;
	public static final int EQUAL = 273;
	public static final int NEQUAL = 274;
	public static final int PROC = 275;	
	public static final int NA = 276;
	public static final int SHADOWING = 277;
	public static final int TRUE = 278;
	public static final int FALSE = 279;
	public final static short UP=280;
	public final static short DOWN=281;
	public final static short YYERRCODE=256;
		
	public static final BigDecimal MIN_RANGE_DOUBLE = new BigDecimal("2.2250738585072014e-308");
	public static final BigDecimal MAX_RANGE_DOUBLE = new BigDecimal("1.7976931348623157e+308");

	public static final String getConstantName(int id) {
		switch(id) {
		case 257:
			return "IF";
		case 258:
			return "THEN";
		case 259:
			return "ELSE";
		case 260:
			return "END_IF";
		case 261:
			return "OUT";
		case 262:
			return "FUNC";
		case 263:
			return "RETURN";
		case 264:
			return "ULONGINT";
		case 265:
			return "DOUBLE";
		case 266:
			return "LOOP";
		case 267:
			return "UNTIL";
		case 268:
			return "ID";
		case 269:
			return "CONSTANT";
		case 270:
			return "CSTRING";
		case 271:
			return "LESSEQUAL";
		case 272:
			return "GREATEQUAL";
		case 273:
			return "EQUAL";
		case 274:
			return "NEQUAL";
		case 275:
			return "PROC";
		case 276:
			return "NA";
		case 277:
			return "SHADOWING";
		case 278:
			return "TRUE";
		case 279:
			return "FALSE";
		default:
			return null;
		}
	}
}
