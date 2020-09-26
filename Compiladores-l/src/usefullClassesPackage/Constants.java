package usefullClassesPackage;

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
	public static final int IDENTIFICADOR = 268;
	public static final int CONSTANTE_NUMERICA = 269;
	public static final int CONSTANTE_CADENA_CARACTERES = 270;	
	public static final int COMPARADOR_MEN_IGUAL = 271;
	public static final int COMPARADOR_MAY_IGUAL = 272;
	public static final int COMPARADOR_IGUAL = 273;
	public static final int COMPARADOR_DISTINTO = 274;
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
			return "IDENTIFICADOR";
		case 269:
			return "CONSTANTE_NUMERICA";
		case 270:
			return "CONSTANTE_CADENA_CARACTERES";
		case 271:
			return "COMPARADOR_MEN_IGUAL";
		case 272:
			return "COMPARADOR_MAY_IGUAL";
		case 273:
			return "COMPARADOR_IGUAL";
		case 274:
			return "COMPARADOR_DISTINTO";
		default:
			return null;
		}
	}
}
