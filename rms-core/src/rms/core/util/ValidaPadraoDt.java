package rms.core.util;

import java.text.SimpleDateFormat;

public class ValidaPadraoDt {
	public static boolean checkDatePattern(String padrao, String strDt) {
	    SimpleDateFormat sdf = new SimpleDateFormat(padrao);
	    try {
			sdf.parse(strDt);
		}catch (java.text.ParseException e){
			return false;
	    }
	    return true;
	}
}
