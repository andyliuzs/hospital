package me.hospital.interceptor;

import java.util.List;

import me.hospital.model.Admin;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * PostInterceptor
 */
public class PostInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		
		Controller controller = ai.getController();
		
		ai.invoke();
		
		// 读取所有的管理员帐号
		List<Admin> authors = Admin.dao.find("select account from admin ");
		controller.setAttr("authors", authors);

	}
}
