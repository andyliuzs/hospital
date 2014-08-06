package me.hospital.validator.admin;

import me.hospital.model.Post;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created with IntelliJ IDEA. Author: andy Date: 14-3-28
 */
public class SavePostValidator extends Validator {
	@Override
	protected void validate(Controller controller) {

		validateString("content", 1, 50000, "contentMsg", "不能为空且长度不超过50000");
	}

	@Override
	protected void handleError(Controller controller) {

		controller.keepPara();
		controller.keepModel(Post.class);

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);
		controller.render("add.html");
	}
}
