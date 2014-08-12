package me.hospital.validator.home;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * UserValidator.
 */
public class UpdatePasswordValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("oldPassword", "oldMsg", "请输入原密码!");
		validateRequiredString("newPassword", "newMsg", "请输入新密码!");
		validateRequiredString("newPassword2", "newMsg2", "请输入确认密码!");
		
		String newPassword = controller.getPara("newPassword");
		String newPassword2 = controller.getPara("newPassword2");
		if(!newPassword.equals(newPassword2)) {
			addError("newMsg", "两次密码不一致！");
		}
		
	}
	
	protected void handleError(Controller controller) {
		
		controller.keepPara();
		controller.render("/user_password.html");
			
	}
}
