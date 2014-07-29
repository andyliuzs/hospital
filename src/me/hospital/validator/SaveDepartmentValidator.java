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
			addError("imageMsg", "请选择头像！");
		}

		// 是否填写了姓名
		validateRequiredString("department.name", "nameMsg", "请输入科室名称!");

	}

	protected void handleError(Controller controller) {

		controller.keepModel(Department.class);

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("add.html");

	}
}
