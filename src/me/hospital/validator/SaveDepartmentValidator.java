package me.hospital.validator;

import me.hospital.model.Department;
import me.hospital.util.ParamUtil;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * SaveDepartmentValidator
 */
public class SaveDepartmentValidator extends Validator {

	protected void validate(Controller controller) {

		// 是否填写了姓名
		validateRequiredString("department.name", "nameMsg", "请输入姓名!");

		// 判断是否选择了科室主任
		if (ParamUtil.paramToInt(controller.getPara("department.directorId"), -1) == -1) {
			addError("doctorMsg", "请选择科室主任！");
		}
	}

	protected void handleError(Controller controller) {

		controller.keepModel(Department.class);

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("add.html");

	}
}
