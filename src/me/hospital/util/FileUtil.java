package me.hospital.util;

import java.io.File;

public class FileUtil {

	/**
	 * 保存添加医生时上传的头像文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean saveDoctorImage(File file) {

		return true;

	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	/**
	 * 判断当前上传的文件是否合法
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isValidExtension(File file) {

		String[] extensions = new String[] { "jpg", "jepg", "png", "gif" };
		String fileExtension = getFileExtension(file);
		for (String ext : extensions) {
			if (ext.equals(fileExtension)) {
				return true;
			}
		}

		return false;

	}
	
	/**
	 * 创建目录
	 * @param file
	 * @return
	 */
	public static boolean createDirectory(File file) {
		if(file.isDirectory() && !file.exists() && file.canWrite()) {
			file.mkdir();
			return true;
		} else {
			return false;
		}
	}

}
