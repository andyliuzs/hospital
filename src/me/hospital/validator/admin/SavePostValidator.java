package me.hospital.validator.admin;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created with IntelliJ IDEA. Author: andy Date: 14-3-28
 */
public class SavePostValidator extends Validator {
	@Override
	protected void validate(Controller controller) {

		validateString("content", 1, 8000, "contentMsg", "不能为空且长度不超过8000");
	}

	@Override
	protected void handleError(Controller controller) {

		controller.keepPara();

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("add.html");
	}
}
