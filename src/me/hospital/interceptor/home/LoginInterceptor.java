package me.hospital.interceptor.home;

import me.hospital.model.User;
import me.hospital.util.ParamUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.oreilly.servlet.Base64Decoder;

public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {

		Controller controller = ai.getController();

		String account = controller.getCookie("ACCOUNT");
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			User user = controller.getSessionAttr(account);
			if(user != null) {
				ai.invoke();
			} else {
				controller.redirect("/index.html/?error=1");
			}
		} else {
			controller.redirect("/index.html/?error=1");
		}
	}

}
