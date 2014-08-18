package me.hospital.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.admin.RegisterInterceptor;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Register;
import me.hospital.util.DateUtil;
import me.hospital.util.ParamUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * RegisterController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class RegisterController extends Controller {

	@Before(RegisterInterceptor.class)
	public void index() {

		// 读取当前用户所有的预约
		Page<Register> registerList = null;

		// 判断当前是否是搜索的数据进行的分页
		// 如果是搜索的数据，则跳转至search方法处理
		if (!ParamUtil.isEmpty(getPara("s"))) {

			search();

			return;
		}

		int page = ParamUtil.paramToInt(getPara(1), 1);

		if (page < 1) {
			page = 1;
		}

		// 管理员获取数据
		registerList = Register.dao.paginate(page, CoreConstants.PAGE_SIZE);

		try {
			// 格式化日期
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			String formatDate = null;
			for (Register register : registerList.getList()) {
				formatDate = register.getStr("date");
				register.set("date", formatter.format(formatter2.parse(formatDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String today = DateUtil.getToday("yyyy-MM-dd");

		setAttr("registerList", registerList);
		setAttr("searchDoctorId", -1);
		setAttr("searchDepartmentId", -1);
		setAttr("searchDate", "-1");
		setAttr("today", today);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);

		render("index.html");

	}

	/**
	 * 根据科室ID异步获取该科室下的所有医生信息
	 */
	public void getDoctors() {
		
		int departmentId = ParamUtil.paramToInt(getPara(0), -1);
		if(departmentId > -1) {
			List<Doctor> doctors = Department.dao.getDoctors(departmentId);
			
			Map<String, String> result = new HashMap<String, String>();

			for (Doctor doctor : doctors) {
				result.put(String.valueOf(doctor.get("id")), doctor.getStr("name"));
			}

			renderJson("json", result);
			
		} else {
			renderJson("json", "");
		}
		
	}
	
	
	/**
	 * 
	 * 获取当前医生
	 */
	public Doctor getDoctor() {
		Doctor doctor = getSessionAttr("doctor");
		if (doctor == null) {
			return null;
		}
		return doctor;
	}

	/**
	 * 查询
	 */
	@Before(RegisterInterceptor.class)
	public void search() {

		// 读取当前用户所有的预约
		Page<Register> registerList = null;

		if (ParamUtil.isEmpty(getPara("s"))) {

			// 说明当前请求是搜索数据的post请求，并非搜索的分页请求
			// 在这里执行搜索操作，并将结果保存到缓存中

			Map<String, String> queryParams = new HashMap<String, String>();

			queryParams.put("doctorId", getPara("doctorId"));
			queryParams.put("departmentId", getPara("departmentId"));
			queryParams.put("date", getPara("date"));

			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, queryParams);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("from register where id > 0");

		HashMap<String, String> queryParams = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);
		List<Object> params = new ArrayList<Object>();

		// 管理员与挂号管理员权限

		if (queryParams != null) {

			int departmentId = Integer.parseInt(queryParams.get("departmentId"));
			if (departmentId > -1) {
				sb.append(" and departmentId = ?");
				params.add(departmentId);
			}
			
			int doctorId = Integer.parseInt(queryParams.get("doctorId"));
			if (doctorId > -1) {
				sb.append(" and doctorId = ?");
				params.add(doctorId);
			}
			
			String date = queryParams.get("date");
			if (!ParamUtil.isEmpty(date) && !"-1".equals(date)) {
				
				try {
					// 格式化日期
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
					sb.append(" and date =?");
					params.add(formatter.format(formatter2.parse(date)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
			// 读取当前用户所有的预约
			registerList = Register.dao.paginate(page, CoreConstants.PAGE_SIZE, "select *",
					sb.toString(), params.toArray());

			try {
				// 格式化日期
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
				String formatDate = null;
				for (Register register : registerList.getList()) {
					formatDate = register.getStr("date");
					register.set("date", formatter.format(formatter2.parse(formatDate)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String today = DateUtil.getToday("yyyy-MM-dd");

			setAttr("today", today);
			setAttr("registerList", registerList);
			setAttr("searchDoctorId", doctorId);
			setAttr("searchDepartmentId", departmentId);
			setAttr("searchDate", date);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}
		render("index.html");
	}

	/***
	 * 预约审核
	 */
	public void verify() {
		try {
			int status = getParaToInt("status");
			int regId = getParaToInt("regId");
			System.out.println("verify: " + status + " regid: " + regId);
			Register register = Register.dao.findById(regId);
			register.set("verify", status);
			System.out.println(register.update());
			renderJson("data", "审核成功!");

		} catch (Exception e) {
			renderJson("data", "审核失败！");
		}

	}

	/***
	 * 医生查看预约修改状态
	 */
	public void show() {
		int registerId = getPara("regId") != null ? Integer.valueOf(getPara("regId").toString())
				: -1;
		Register register = Register.dao.findById(registerId);

		try {
			// 格式化日期
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			String formatDate = register.getStr("date");
			register.set("date", formatter.format(formatter2.parse(formatDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		setAttr("register", register);
		render("visit.html");
	}

	/***
	 * 医生处理预约（就诊）
	 */
	public void visit() {
		int registerId = getPara("registerId") != null ? Integer.valueOf(getPara("registerId")
				.toString()) : -1;
		String remark = getPara("remark");
		try {
			Register register = Register.dao.findById(registerId);
			register.set("remark", remark);
			// 3已经处理
			register.set("status", 3);
			register.update();
			renderJson("data", "处理成功！");
		} catch (Exception e) {
			renderJson("data", "处理失败！");
		}

	}

	public void process() {

		// 获取当前用户
		Doctor doctor = getDoctor();

		// 读取当前用户所有的预约
		Page<Register> registerList = null;

		int page = ParamUtil.paramToInt(getPara(1), 1);

		if (page < 1) {
			page = 1;
		}

		registerList = Register.dao.paginateForDoctor(page, CoreConstants.PAGE_SIZE,
				String.valueOf(doctor.get("id")), "1");

		try {
			// 格式化日期
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			String formatDate = null;
			for (Register register : registerList.getList()) {
				formatDate = register.getStr("date");
				register.set("date", formatter.format(formatter2.parse(formatDate)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String today = DateUtil.getToday("yyyy-MM-dd");

		setAttr("registerList", registerList);
		setAttr("today", today);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);

		render("process.html");
	}

}
