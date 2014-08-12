package me.hospital.validator.home;

import me.hospital.model.User;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class SaveUserMessageValidator extends Validator {

	protected void validate(Controller controller) {

		validateRegex("user.phone","1[0-9]{10}", "phoneMsg", "请输入合法的手机号!");
		//("user.email", "emailMsg", "请输入合法的email!");
	 //   validateRegex("user.address", "[a-zA-Z0-9_\\u4e00-\\u9fa5]{6,100}", "addressMsg", "地址的的长度介于6-100之间，只能包含中文，数字，字母，下划线");
		//validateRegex("user.qq","[1-9]{5,10}", "qqMsg", "请输入合法的QQ号!");

	}

	protected void handleError(Controller controller) {
		controller.keepModel(User.class);
		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("/user_info.html");

	}
}

	

