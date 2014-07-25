package me.hospital.util;

import java.io.File;

import me.hospital.config.CoreConstants;

import com.jfinal.kit.PathKit;

public class FileUtil {

	/**
	 * 保存头像文件 保存上传的文件，并返回文件的相对路径
	 * 
	 * @param file
	 * @return
	 */
	public static String saveAvatarImage(File file) {

		String contextPath = PathKit.getWebRootPath();
		String filePath = contextPath + CoreConstants.ATTACHMENGT_AVATAR_PATH;

		// 路径不存在的话，创建该路径
		FileUtil.createDirectory(new File(filePath));

		// 存储文件名称
		String newFileName = String.valueOf(System.currentTimeMillis()) + "."
				+ FileUtil.getFileExtension(file);

		// 存储路径
		String url = filePath + newFileName;

		if (file.renameTo(new File(url))) {

			// 文件保存到指定位置后，删除源文件
			file.delete();
		}

		// 保存在数据库中的路径
		String savePath = CoreConstants.ATTACHMENGT_AVATAR_PATH + newFileName;

		return savePath;

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
	 * 
	 * @param directory
	 * @return
	 */
	public static boolean createDirectory(File directory) {
		if (!directory.exists() /* && directory.canWrite() */) {
			directory.mkdirs();
			return true;
		} else {
			return false;
		}
	}

}
