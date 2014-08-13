package me.hospital.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.admin.RegisterInterceptor;
import me.hospital.model.Doctor;
import me.hospital.model.Register;
import me.hospital.model.Role;
import me.hospital.model.User;
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

		List<String> futureDays = DateUtil.getFutureDays(10, "yyyy-MM-dd");
		// HashMap<String, String> dayMap = new HashMap<String, String>();
		//
		// try {
		//
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		// SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		// for (String day : futureDays) {
		// dayMap.put(day, formatter2.format(formatter.parse(day)));
		// }
		//
		// for (String key : dayMap.keySet()) {
		// System.out
		// .println("Key: " + key + " value: " + dayMap.get(key));
		// }
		//
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }

		setAttr("futureDays", futureDays);

		// 获取当前用户
		// Doctor doctor = getDoctor();

		// 读取当前用户所有的预约
		Page<Register> registerList = null;
		// 获取权限
		// Role role = null;

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

		// int roleId = 0;
		// if (doctor != null) {
		// roleId = doctor.get("roleId") != null ? Integer.valueOf(doctor.get(
		// "roleId").toString()) : -1;
		// }
		// if (roleId > 0) {
		// role = Role.dao.findById(roleId);
		//
		// }

		// if (roleId == 0) {
		// // 管理员获取数据
		// registerList = Register.dao.paginate(page, CoreConstants.PAGE_SIZE);
		//
		// } else {
		// // 医生获取数据status0待审核 1未通过 2通过 3处理
		// registerList = Register.dao.paginateForDoctor(page,
		// CoreConstants.PAGE_SIZE, String.valueOf(doctor.get("id")),
		// "2");
		// }

		// 格式化日期
		// String formatDate = null;
		// for (Register register : registerList.getList()) {
		// formatDate = register.getStr("date");
		// String formatDate2 = formatDate.substring(0, 4) + "-"
		// + formatDate.substring(4, 6) + "-"
		// + formatDate.substring(6, 8);
		// register.set("date", formatDate2);
		// }

		// 获取今天
		// Date datee = new Date();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(datee);
		// String nowDate = calendar.get(calendar.YEAR) + "-"
		// + (calendar.get(calendar.MONTH) + 1) + "-"
		// + calendar.get(calendar.DAY_OF_MONTH);

		// 管理员获取数据
		registerList = Register.dao.paginate(page, CoreConstants.PAGE_SIZE);

		// 格式化日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = null;
		for (Register register : registerList.getList()) {
			formatDate = register.getStr("date");
			register.set("date", formatter.format(Long.parseLong(formatDate)));
		}

		String today = DateUtil.getToday("yyyy-MM-dd");

		setAttr("registerList", registerList);
		setAttr("searchUserName", "");
		setAttr("searchDoctorId", -1);
		setAttr("searchDepartmentId", -1);
		setAttr("today", today);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);

		render("index.html");

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
		// 获取当前用户
		Doctor doctor = getDoctor();

		// 读取当前用户所有的预约
		Page<Register> registerList = null;
		// 获取权限
		Role role = null;
		if (ParamUtil.isEmpty(getPara("s"))) {

			// 说明当前请求是搜索数据的post请求，并非搜索的分页请求
			// 在这里执行搜索操作，并将结果保存到缓存中

			Map<String, String> queryParams = new HashMap<String, String>();

			queryParams.put("username", getPara("username"));
			queryParams.put("doctorId", getPara("doctorId"));
			queryParams.put("departmentId", getPara("departmentId"));
			queryParams.put("date", getPara("date"));

			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, queryParams);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}
		int roleId = 0;
		if (doctor == null)
			return;
		roleId = doctor.get("roleId") != null ? Integer.valueOf(doctor.get(
				"roleId").toString()) : -1;

		if (roleId > 0) {
			role = Role.dao.findById(roleId);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("from register where id > 0");

		HashMap<String, String> queryParams = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);
		List<Object> params = new ArrayList<Object>();

		if (roleId == 0) {

			// 管理员与挂号管理员权限

			if (queryParams != null) {

				String userName = queryParams.get("username");
				List<User> users = User.dao.find(
						"select * from user where name like ?", userName);
				int userId = -1;
				if (users.size() > 0) {
					userId = users.get(0).get("id");
				}
				if (userId > -1) {
					sb.append(" and userId = ?");
					params.add(userId);
				}

				int doctorId = Integer.parseInt(queryParams.get("doctorId"));
				if (doctorId > -1) {
					sb.append(" and doctorId = ?");
					params.add(doctorId);
				}

				int departmentId = Integer.parseInt(queryParams
						.get("departmentId"));
				if (departmentId > -1) {
					sb.append(" and departmentId = ?");
					params.add(departmentId);
				}
				String date = queryParams.get("date");
				if (!ParamUtil.isEmpty(date)) {
					sb.append(" and date =?");
					params.add(date);
				}

				// 读取当前用户所有的预约
				registerList = Register.dao.paginate(page,
						CoreConstants.PAGE_SIZE, "select *", sb.toString(),
						params.toArray());
				// 格式化日期
				String formatDate = null;
				for (Register register : registerList.getList()) {
					formatDate = register.getStr("date");
					String formatDate2 = formatDate.substring(0, 4) + "-"
							+ formatDate.substring(4, 6) + "-"
							+ formatDate.substring(6, 8);
					register.set("date", formatDate2);
				}
				// 获取今天
				Date datee = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(datee);
				String nowDate = calendar.get(calendar.YEAR) + "-"
						+ (calendar.get(calendar.MONTH) + 1) + "-"
						+ calendar.get(calendar.DAY_OF_MONTH);
				setAttr("nowDate", nowDate);
				setAttr("registerList", registerList);
				setAttr("searchUserName", userName);
				setAttr("searchDoctorId", doctorId);
				setAttr("searchDepartmentId", departmentId);
				setAttr("searchDate", date);
				setAttr("searchPage", CoreConstants.SEARCH_PAGE);

			}
			render("index.html");
		} else {
			// /医生权限
			// 医生本人的id
			queryParams.put("doctorId", doctor.get("id").toString());
			// 状态位0待审核 1未通过 2通过 3处理
			queryParams.put("status", "2");

			String userName = queryParams.get("username");
			List<User> users = User.dao.find(
					"select * from user where name like ?", userName);
			int userId = -1;
			if (users.size() > 0) {
				userId = users.get(0).get("id");
			}
			if (userId > -1) {
				sb.append(" and userId = ?");
				params.add(userId);
			}

			String date = queryParams.get("date");
			if (!ParamUtil.isEmpty(date)) {
				sb.append(" and date =?");
				params.add(date);
			}

			int doctorId = Integer.valueOf(queryParams.get("doctorId"));
			if (doctorId > -1) {
				sb.append(" and doctorId =?");
				params.add(doctorId);
			}

			int status = Integer.valueOf(queryParams.get("status").toString());
			if (status > -1) {
				sb.append(" and status >=?");
				params.add(status);
			}
			// 读取当前用户所有的预约
			registerList = Register.dao.paginate(page, CoreConstants.PAGE_SIZE,
					"select *", sb.toString(), params.toArray());
			// 格式化日期
			String formatDate = null;
			for (Register register : registerList.getList()) {
				formatDate = register.getStr("date");
				String formatDate2 = formatDate.substring(0, 4) + "-"
						+ formatDate.substring(4, 6) + "-"
						+ formatDate.substring(6, 8);
				register.set("date", formatDate2);
			}
			// 获取今天
			Date datee = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(datee);
			String nowDate = calendar.get(calendar.YEAR) + "-"
					+ (calendar.get(calendar.MONTH) + 1) + "-"
					+ calendar.get(calendar.DAY_OF_MONTH);
			setAttr("nowDate", nowDate);
			setAttr("registerList", registerList);
			setAttr("searchUserName", userName);
			setAttr("searchDate", date);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);
			render("doctor_index.html");
		}

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
		int registerId = getPara("regId") != null ? Integer.valueOf(getPara(
				"regId").toString()) : -1;
		Register register = Register.dao.findById(registerId);

		// 格式化日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = register.getStr("date");
		register.set("date", formatter.format(Long.parseLong(formatDate)));

		setAttr("register", register);
		render("visit.html");
	}

	/***
	 * 医生处理预约（就诊）
	 */
	public void visit() {
		int registerId = getPara("registerId") != null ? Integer
				.valueOf(getPara("registerId").toString()) : -1;
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

		// 获取权限
		// Role role = null;

		// 判断当前是否是搜索的数据进行的分页
		// 如果是搜索的数据，则跳转至search方法处理
		// if (!ParamUtil.isEmpty(getPara("s"))) {
		//
		// search();
		//
		// return;
		// }
		int page = ParamUtil.paramToInt(getPara(1), 1);

		if (page < 1) {
			page = 1;
		}

		registerList = Register.dao.paginateForDoctor(page,
				CoreConstants.PAGE_SIZE, String.valueOf(doctor.get("id")), "1");

		// 格式化日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = null;
		for (Register register : registerList.getList()) {
			formatDate = register.getStr("date");
			register.set("date", formatter.format(Long.parseLong(formatDate)));
		}

		String today = DateUtil.getToday("yyyy-MM-dd");

		setAttr("registerList", registerList);
		setAttr("searchUserName", "");
		setAttr("today", today);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);

		render("process.html");
	}

}
