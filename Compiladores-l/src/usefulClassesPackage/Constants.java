package usefulClassesPackage;

import java.math.BigDecimal;

public class Constants {
	public static final int IF = 901;
	public static final int THEN = 902;
	public static final int ELSE = 903;
	public static final int END_IF = 904;
	public static final int OUT = 905;
	public static final int FUNC = 906;
	public static final int RETURN = 907;
	public static final int ULONGINT = 908;
	public static final int DOUBLE = 909;
	public static final int LOOP = 910;
	public static final int UNTIL = 911;
	public static final int TRUE = 912;
	public static final int FALSE = 913;
	public static final int NA = 914;
	public static final int SHADOWING = 915;
	public static final int PROC = 916;
	
	public static final int IDENTIFICADOR = 401;
	public static final int CONSTANTE_NUMERICA = 402;
	public static final int CONSTANTE_CADENA_CARACTERES = 403;
	
	public static final int COMPARADOR_MEN_IGUAL = 501;
	public static final int COMPARADOR_MAY_IGUAL = 502;
	public static final int COMPARADOR_IGUAL = 503;
	public static final int COMPARADOR_DISTINTO = 504;
	
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
