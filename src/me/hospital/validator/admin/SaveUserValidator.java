package me.hospital.validator.admin;

import me.hospital.model.User;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * SaveUserValidator
 */
public class SaveUserValidator extends Validator {

	protected void validate(Controller controller) {

		// 是否填写了姓名
		validateRegex("user.account","[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}", "accountMsg", "请输入合法的账号介于2-20!");
		validateRegex("user.password", "[a-zA-Z0-9_]{6,12}", "passwordMsg", "密码的长度介于6-12之间，只能包含数字，字母，下划线");
		validateRegex("password2", "[a-zA-Z0-9_]{6,12}", "password2Msg", "密码的长度介于6-12之间，只能包含数字，字母，下划线");
		validateEqualField("user.password","password2", "pasEqu", "2次输入密码不一致!");
		validateRequired("user.name", "nameMsg", "请输入合法姓名!");
		validateRequired("user.sex", "sexMsg", "请选择性别!");
		validateInteger("user.age",10 ,100,"ageMsg", "请输合法的年龄年龄介于10-100!");
		validateRequired("user.identity", "identityMsg", "请输入合法的身份证号!");
		validateRegex("user.phone","13[0-9]{9}", "phoneMsg", "请输入合法的手机号!");
		validateEmail("user.email", "emailMsg", "请输入合法的email!");
	    validateRegex("user.address", "[a-zA-Z0-9_\\u4e00-\\u9fa5]{6,100}", "addressMsg", "地址的的长度介于6-100之间，只能包含中文，数字，字母，下划线");
		validateRegex("user.qq","^\\d[1-9]{5,10}$", "qqMsg", "请输入合法的QQ号!");
		
	}

	protected void handleError(Controller controller) {
		System.out.print(controller.getAttr("user.account")+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		controller.keepModel(User.class);

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("edit.html");

	}
}
