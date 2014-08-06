package me.hospital.interceptor.home;

import java.util.List;

import me.hospital.model.PostCategory;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * ResearchInterceptor
 */
public class ResearchInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();

		ai.invoke();

		List<PostCategory> newsTypes = PostCategory.dao.getSubCategoriesById(2);
		controller.setAttr("newsTypes", newsTypes);

	}
}
