package me.hospital.controller;

import me.hospital.config.CoreConstants;
import me.hospital.util.FileUtil;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;

/**
 * 百度编辑器
 * 
 * @author L.cm
 * @date Nov 27, 2013 9:48:07 PM
 */
public class UEditorController extends Controller {

	private static final Logger logger = Logger
			.getLogger(UEditorController.class);

	// 上传图片
	public void uploadImage() {

		// 选择保存目录
		// String fetch = getPara("fetch");
		// if (StrKit.notBlank(fetch)) {
		// renderJavascript("updateSavePath( [\"" + Consts.QINIU_BUCKET +
		// "\"] );");
		// return;
		// }

		try {

			UploadFile file = getFile("upfile", "/",
					CoreConstants.MAX_FILE_SIZE);

			// 保存文件并获取保存在数据库中的路径
			String savePath = FileUtil.saveUploadImage(file.getFile());
			
			logger.info("savePath: " + savePath);
			
			// 上传文件
			logger.error("上传文件");
			setAttr("url", "");
			setAttr("state", "SUCCESS");

			setAttr("original", FileUtil.getFileName(file.getFile()));
		} catch (Exception e) {
			logger.error(e.getMessage());
			setAttr("state", "图片上传失败，请稍后重试！");
		}
		// "SUCCESS", "SUCCESS"
		setAttr("title", getPara("pictitle"));
		renderJson(new String[] { "original", "url", "title", "state" });
	}

}