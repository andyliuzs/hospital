package me.hospital.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created with IntelliJ IDEA.
 * Author: andy
 * Date: 14-3-28
 */
public class SavePostValidator extends Validator {
    @Override
    protected void validate(Controller c) {
        validateString("post.content", 1, 8000, "msg", "不能为空且长度不超过8000");
    }

    @Override
    protected void handleError(Controller c) {
        c.renderError(500);
    }
}
