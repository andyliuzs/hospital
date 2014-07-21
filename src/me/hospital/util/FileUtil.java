package me.hospital.util;

import java.io.File;

public class FileUtil {

	/**
	 * 保存添加医生时上传的头像文件
	 * @param file
	 * @return
	 */
	public static boolean saveDoctorImage(File file) {
		
		
		return true;
		
		
	}
	
	public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
	
}
