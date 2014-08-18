package me.hospital.interceptor.admin;

import java.util.List;

import me.hospital.model.Department;
import me.hospital.util.DateUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * RegisterInterceptor
 */
public class RegisterInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		
		Controller controller = ai.getController();
		
		ai.invoke();
		
		List<String> futureDays = DateUtil.getFutureDays(10, "yyyy-MM-dd");
		controller.setAttr("futureDays", futureDays);
		
		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getDepartments();
		controller.setAttr("departments", departments);

	}
}
