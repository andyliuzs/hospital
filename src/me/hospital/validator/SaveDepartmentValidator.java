package me.hospital.validator;

import me.hospital.model.Department;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.jfinal.validate.Validator;

/**
 * SaveDepartmentValidator
 */
public class SaveDepartmentValidator extends Validator {

	protected void validate(Controller controller) {

		// 表单中包含文件，所以先调用getFile方法，剩余的参数才可用
		UploadFile file = controller.getFile();
		if (file == null) {
			addError("imageMsg", "请选择科室图片！");
		}

		// 是否填写了姓名
		validateRequiredString("department.name", "nameMsg", "请输入科室名称!");

		// 0 ~ 1000 之间的整数为合法排序值
		validateInteger("department.sort", 0, 1000, "sortMsg", "请输入0~1000之间的整数");
		
	}

	protected void handleError(Controller controller) {

		controller.keepModel(Department.class);

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("add.html");

	}
}
