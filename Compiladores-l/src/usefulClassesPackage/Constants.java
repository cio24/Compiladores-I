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
		case 901:
			return "IF";
		case 902:
			return "THEN";
		case 903:
			return "ELSE";
		case 904:
			return "END_IF";
		case 905:
			return "OUT";
		case 906:
			return "FUNC";
		case 907:
			return "RETURN";
		case 908:
			return "ULONGINT";
		case 909:
			return "DOUBLE";
		case 910:
			return "LOOP";
		case 911:
			return "UNTIL";
		case 401:
			return "IDENTIFICADOR";
		case 402:
			return "CONSTANTE_NUMERICA";
		case 403:
			return "CONSTANTE_CADENA_CARACTERES";
		case 501:
			return "COMPARADOR_MEN_IGUAL";
		case 502:
			return "COMPARADOR_MAY_IGUAL";
		case 503:
			return "COMPARADOR_IGUAL";
		case 504:
			return "COMPARADOR_DISTINTO";
		default:
			return null;
		}
	}
}
