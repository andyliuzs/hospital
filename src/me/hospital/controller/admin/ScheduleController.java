package me.hospital.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.admin.DepartmentInterceptor;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Schedule;
import me.hospital.model.ScheduleStatus;
import me.hospital.util.DateUtil;
import me.hospital.util.ParamUtil;
import me.hospital.validator.admin.ScheduleValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * ScheduleController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@Before(DepartmentInterceptor.class)
public class ScheduleController extends Controller {

	public void index() {

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

		render("index.html");
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

	@Before(ScheduleValidator.class)
	public void plan() {

		int departmentId = ParamUtil.paramToInt(getPara("depart"), -1);
		String date = getPara("date");

		String showDate = null;
		try {
			// 格式化日期
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			showDate = formatter.format(formatter2.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Department department = Department.dao.findById(departmentId);
		
		// 查找该科室下所有的医生
		List<Doctor> doctors = Department.dao.getDoctors(departmentId);
		setAttr("doctors2", doctors);

		setAttr("department", department);
		setAttr("date", date);
		setAttr("showDate", showDate);
		
		render("plan.html");

	}

	public void save() {

		String[] doctors = getParaValues("doctorId");
		String[] amTotal = getParaValues("amTotal");
		String[] pmTotal = getParaValues("pmTotal");
		String date = getPara("date");
		int departmentId = ParamUtil.paramToInt(getPara("departmentId"), -1);

		boolean hasScheduled = false;
		if (ScheduleStatus.dao.hasScheduled(departmentId, date)) {
			hasScheduled = true;
		}

		Schedule schedule = null;
		for (int i = 0; i < doctors.length; i++) {

			schedule = Schedule.dao.getSchedule(doctors[i], date);
			if (schedule == null) {
				schedule = new Schedule();
				schedule.set("doctorId", doctors[i]);
				schedule.set("date", date);
			}

			schedule.set("amTotal", ParamUtil.paramToInt(amTotal[i], 0));
			schedule.set("pmTotal", ParamUtil.paramToInt(pmTotal[i], 0));

			if (hasScheduled) {
				schedule.update();
			} else {
				schedule.save();
			}
		}

		if (!hasScheduled) {
			ScheduleStatus status = new ScheduleStatus();
			status.set("departmentId", departmentId);
			status.set("date", date);
			status.save();
		}

		renderJson("data", "success");
	}

}
