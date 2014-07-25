package me.hospital.interceptor;

import java.util.List;

import me.hospital.model.Doctor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * DoctorInterceptor
 */
public class DepartmentInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		
		Controller controller = ai.getController();
		
		ai.invoke();
		
		// 读取所有的主任医师
		List<Doctor> doctors = Doctor.dao.find("select * from doctor where roleId = ?", 2);
		controller.setAttr("doctors", doctors);

	}
}
