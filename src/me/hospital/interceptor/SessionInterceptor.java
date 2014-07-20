package me.hospital.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * BlogInterceptor
 */
public class SessionInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {

		Controller controller = ai.getController();

		System.out.println("Before invoking " + ai.getActionKey());

		if (controller.getSessionAttr("admin") != null
				|| controller.getSessionAttr("doctor") != null) {
			ai.invoke();
		} else {
			controller.setAttr("msg", "请登录本系统");
			controller.redirect("/admin/login.html");
		}

	}
}
