package me.hospital.util;

public class StrUtil {
	
	/**
	 * 判断空串
	 */
	public static boolean isEmpty(String s){
		
		if(s != null && !"".equals(s)){
			return true;
		}
		
		return false;
	}
	
}
