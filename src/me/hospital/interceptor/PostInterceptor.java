package me.hospital.interceptor;

import java.util.List;

import me.hospital.model.PostCategory;

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
		
		// 读取所有的公告分类
		List<PostCategory> categories = PostCategory.dao.getCategories();
		controller.setAttr("categories", categories);
		
	}
}
