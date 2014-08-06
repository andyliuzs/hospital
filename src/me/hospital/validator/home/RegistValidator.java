package me.hospital.validator.home;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class RegistValidator extends Validator {

	protected void validate(Controller controller) {

//		<input type="text" name="account" size="25"/>
//		<input type="text" name="password" size="25" />
//		<input type="text" name="password2" size="25" />
//		<input type="text" name="name" size="15" />
//		<input type="radio" name="sex" checked="checked" value="1" />
//		男
//		<input type="radio" name="sex"  value="0" />
//		女
//		<input type="text" name="age" size="5"/>
//		<input type="text" name="identity" size="35" />
//		<input type="text" name="phone" size="25" />
//		<input type="text" name="email" size="25" />
//		<input type="text" name="address" size="45" />
//		<input type="text" name="qq" size="25" />
		
		
		validateRequiredString("username", "nameMsg", "账号不能为空！");
		validateRequiredString("password", "passwordMsg", "密码不能为空！");

	}

	protected void handleError(Controller controller) {

		controller.keepPara();

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("login.html");

	}
}

	

