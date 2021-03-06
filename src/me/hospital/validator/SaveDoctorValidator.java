package me.hospital.validator;

import me.hospital.model.Doctor;
import me.hospital.util.ParamUtil;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.jfinal.validate.Validator;

/**
 * AdminValidator.
 */
public class SaveDoctorValidator extends Validator {

	protected void validate(Controller controller) {

		// 表单中包含文件，所以先调用getFile方法，剩余的参数才可用
		UploadFile file = controller.getFile();
		if (file == null) {
			addError("imageMsg", "请选择头像！");
		}

		System.out.println("name: " + controller.getPara("doctor.name"));
		System.out.println("roleId: " + controller.getPara("doctor.roleId"));
		System.out.println("departmentId: " + controller.getPara("doctor.departmentId"));
		System.out.println("sex: " + controller.getPara("doctor.sex"));
		System.out.println("age: " + controller.getPara("doctor.age"));

		// 是否填写了姓名
		validateRequiredString("doctor.name", "nameMsg", "请输入姓名!");

		// 判断是否选择了职称
		if (ParamUtil.paramToInt(controller.getPara("doctor.roleId"), -1) == -1) {
			addError("roleMsg", "请选择职称！");
		}

		// 判断是否选择了科室
		if (ParamUtil.paramToInt(controller.getPara("doctor.departmentId"), -1) == -1) {
			addError("departmentMsg", "请选择科室！");
		}

		// 15 ~ 65 岁为合法的年龄
		validateInteger("doctor.age", 15, 65, "ageMsg", "请输入正确的年龄！");

	}

	protected void handleError(Controller controller) {

		controller.keepModel(Doctor.class);

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("add.html");

	}
}
