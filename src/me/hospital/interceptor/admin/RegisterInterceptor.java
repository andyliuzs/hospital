package me.hospital.interceptor.admin;

import java.util.List;

import me.hospital.config.CoreConstants;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Role;

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
		//读取所有的科室主任
		List<Doctor> doctors = Doctor.dao.getDoctors();
		controller.setAttr("doctors", doctors);	
		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getDepartments();
		controller.setAttr("departments", departments);

	}
}
