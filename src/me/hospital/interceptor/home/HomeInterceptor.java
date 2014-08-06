package me.hospital.interceptor.home;

import java.util.List;

import me.hospital.model.Department;
import me.hospital.model.Doctor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * DoctorInterceptor
 */
public class HomeInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		
		Controller controller = ai.getController();
		
		ai.invoke();
		
		// 获取首页要显示的医生的信息
		List<Doctor> doctors = Doctor.dao.getRecommends();
		controller.setAttr("doctors", doctors);

		// 获取首页要显示的科室的信息
		List<Department> departments = Department.dao.getRecommends();
		controller.setAttr("departments", departments);
		
	}
}
