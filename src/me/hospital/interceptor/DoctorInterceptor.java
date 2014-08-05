package me.hospital.interceptor;

import java.util.List;

import me.hospital.config.CoreConstants;
import me.hospital.model.Department;
import me.hospital.model.Role;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * DoctorInterceptor
 */
public class DoctorInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		
		Controller controller = ai.getController();
		
		ai.invoke();
		
		// 读取所有的医生类（主任医师、副主任医师、医生等）职称
		List<Role> roles = Role.dao.getRolesByType(CoreConstants.ROLE_DOCTOR_TYPE);
		controller.setAttr("roles", roles);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getDepartments();
		controller.setAttr("departments", departments);

	}
}
