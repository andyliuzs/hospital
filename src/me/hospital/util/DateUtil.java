package me.hospital.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 获取今天的日期
	 * 
	 * @return
	 */
	public static String getToday() {
		return formatter.format(new Date().getTime());
	}

	/**
	 * 获取今天的日期
	 * @param format
	 * @return
	 */
	public static String getToday(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date().getTime());
	}
	
	/**
	 * 获取未来某天的日期值
	 * 
	 * @param num
	 * @return
	 */
	public static String getFutureDay(int num) {
		// 取时间
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, num);
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

		return formatter.format(date);
	}

	/**
	 * 获取未来几天的日期字符串
	 * 
	 * @param num
	 * @return
	 */
	public static List<String> getFutureDays(int num) {

		// 取时间
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < num; i++) {
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			result.add(formatter.format(date));
		}

		return result;

	}
	
	
	/**
	 * 获取未来几天的日期字符串
	 * 
	 * @param num
	 * @return
	 */
	public static List<String> getFutureDays(int num, String format) {

		// 取时间
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		List<String> result = new ArrayList<String>();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		for (int i = 0; i < num; i++) {
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			result.add(formatter.format(date));
		}

		return result;

	}
	
	/**
	 * 格式化时间
	 * @param formatter
	 * @param time
	 * @return
	 */
	public static String getFormatTime(String format, String time) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(Long.parseLong(time));
	}
}
