package me.hospital.util;

public class ParamUtil {
	
	/**
	 * 判断空串
	 */
	public static boolean isEmpty(String s){
		
		if(s == null || "".equals(s)){
			return true;
		}
		
		return false;
	}
	
	public static int paramToInt(String param, int defaultValue) {
		if (param == null || "".equals(param.trim()))
			return defaultValue;
		
		int result = defaultValue;
		
		try{
			
			if (param.startsWith("N") || param.startsWith("n")) {
				result = -Integer.parseInt(param.substring(1));
			} else {
				result = Integer.parseInt(param);
			}
			
		} catch(NumberFormatException e) {
			result = defaultValue;
		}
		
		return result;
	}
	
}
