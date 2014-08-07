package me.hospital.controller.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.home.LoginInterceptor;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.ScheduleStatus;
import me.hospital.model.User;
import me.hospital.util.DateUtil;
import me.hospital.util.ParamUtil;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.oreilly.servlet.Base64Decoder;

/**
 * RegisterController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
@Before(LoginInterceptor.class)
public class RegisterController extends Controller {

	public void index() {

		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user == null) {
			redirect("/error.html");
			return;
			
		} 
		
		setAttr("user", user);
		
		int departmentId = ParamUtil.paramToInt(getPara(0), -1);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getDepartments();
		setAttr("departments", departments);

		// 获取未来10天，组合成list
		List<String> futureDays = DateUtil.getFutureDays(CoreConstants.SCHEDULE_DAYS);

		// 未来10天是否已经排班了
		List<String> futurePlans = new ArrayList<String>();

		boolean hasScheduled = false;
		for (String date : futureDays) {
			hasScheduled = ScheduleStatus.dao.hasScheduled(departmentId, date);
			futurePlans.add(String.valueOf(hasScheduled));
		}

		setAttr("futureDays", futureDays);
		setAttr("futurePlans", futurePlans);
		setAttr("departmentId", departmentId);
		
		
		render("/register.html");

	}
	
	
	/**
	 * 选择科室的时候，载入该科室下的排班信息
	 */
	public void getSchedule() {

		int departmentId = ParamUtil.paramToInt(getPara(0), -1);

		List<String> futureDays = DateUtil.getFutureDays(CoreConstants.SCHEDULE_DAYS);
		boolean hasScheduled = false;

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		for (String date : futureDays) {
			hasScheduled = ScheduleStatus.dao.hasScheduled(departmentId, date);
			result.put(date, hasScheduled);
		}

		renderJson("json", result);

	}
	
	
	
	/**
	 * 管理当前用户的预约
	 */
	public void manage() {
		
		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user == null) {
			redirect("/error.html");
			return;
			
		} 
		
		setAttr("user", user);
		
		render("/register_manage.html");
		
	}
	
	/**
	 * 取消预约
	 */
	public void cancel() {
		render("/register_manage.html");
	}
	
	/**
	 * 显示预约页面
	 */
	public void show() {
		
		int departmentId = ParamUtil.paramToInt(getPara("depart"), -1);
		String date = getPara("date");

		// 查找该科室下所有的医生
		List<Doctor> doctors = Department.dao.getDoctors(departmentId);
		setAttr("doctors", doctors);

		Department department = Department.dao.findById(departmentId);
		
		setAttr("department", department);
		setAttr("date", date);
		
		render("/register_handle.html");
	}
	
}
