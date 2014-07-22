package me.hospital.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Role;
import me.hospital.util.CN2SpellUtil;
import me.hospital.util.FileUtil;
import me.hospital.util.ParamUtil;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * AdminController 所有 sql 写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
 */

public class DoctorController extends Controller {

	public void index() {

		// 判断当前是否是搜索的数据进行的分页
		// 如果是搜索的数据，则跳转至search方法处理
		if (!ParamUtil.isEmpty(getPara("s"))) {

			search();

			return;
		}

		System.out.println("page: " + getPara("p"));

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		// 医生列表
		Page<Doctor> doctorList = Doctor.dao.paginate(page, CoreConstants.PAGE_SIZE);
		setAttr("doctorList", doctorList);

		// 读取所有的医生类（主任医师、副主任医师、医生等）职称
		List<Role> roles = Role.dao.getRolesByType(CoreConstants.ROLE_DOCTOR_TYPE);
		setAttr("roles", roles);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getAllDepartments();
		setAttr("departments", departments);

		// for search
		setAttr("searchName", "");
		setAttr("searchRoleId", -1);
		setAttr("searchSex", -1);
		setAttr("searchDepartmentId", -1);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);

		render("index.html");
	}

	/**
	 * 搜索
	 */
	public void search() {

		if (ParamUtil.isEmpty(getPara("s"))) {

			// 说明当前请求是搜索数据的post请求，并非搜索的分页请求
			// 在这里执行搜索操作，并将结果保存到缓存中

			String name = getPara("name");
			int roleId = ParamUtil.paramToInt(getPara("roleId"), -1);
			int sex = ParamUtil.paramToInt(getPara("sex"), -1);
			int departmentId = ParamUtil.paramToInt(getPara("departmentId"), -1);

			StringBuilder sb = new StringBuilder();
			sb.append("from doctor where del = 1");

			Map<String, Object> query = new HashMap<String, Object>();

			if (!ParamUtil.isEmpty(name)) {
				sb.append(" and name like ?");
				query.put("name", name);
			}

			if (roleId > -1) {
				sb.append(" and roleId = ?");
				query.put("roleId", roleId);
			}

			if (sex > -1) {
				sb.append(" and sex = ?");
				query.put("sex", sex);
			}

			if (departmentId > -1) {
				sb.append(" and departmentId = ?");
				query.put("departmentId", departmentId);
			}

			query.put("query", sb.toString());

			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, query);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		String queryStr = "form doctor where del = 1";
		HashMap<String, Object> query = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);

		List<Object> params = new ArrayList<Object>();
		if (query != null) {

			queryStr = (String) query.get("query");

			for (String key : query.keySet()) {
				if (!"query".equals(key)) {
					params.add(query.get(key));
				}
			}

			String name = query.get("name") == null ? "" : (String) query.get("name");
			int roleId = query.get("roleId") == null ? -1 : (Integer) query.get("roleId");
			int sex = query.get("sex") == null ? -1 : (Integer) query.get("sex");
			int departmentId = query.get("departmentId") == null ? -1 : (Integer) query
					.get("departmentId");

			// for search
			setAttr("searchName", name);
			setAttr("searchRoleId", roleId);
			setAttr("searchSex", sex);
			setAttr("searchDepartmentId", departmentId);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}

		// 医生列表
		Page<Doctor> doctorList = Doctor.dao.paginate(page, CoreConstants.PAGE_SIZE, "select *",
				queryStr, params.toArray());

		setAttr("doctorList", doctorList);

		// 读取所有的医生类（主任医师、副主任医师、医生等）职称
		List<Role> roles = Role.dao.getRolesByType(1);
		setAttr("roles", roles);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getAllDepartments();
		setAttr("departments", departments);

		System.out.println("here");

		render("index.html");

	}

	public void page() {

	}

	/**
	 * 渲染页面，并初始化相关数据
	 */
	public void add() {

		// 读取所有的医生类（主任医师、副主任医师、医生等）职称
		List<Role> roles = Role.dao.getRolesByType(1);
		setAttr("roles", roles);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getAllDepartments();
		setAttr("departments", departments);

		render("add.html");
	}

	public void save() {

		// 上传的头像文件
		UploadFile file = getFile("image", "/images/", 10 * 1024 * 1024);// ,
																			// "D:/",
																			// 100
																			// *
																			// 1024
																			// *
																			// 1024,
																			// "utf-8"

		String newFileName = System.currentTimeMillis() + "."
				+ FileUtil.getFileExtension(file.getFile());

		String url = file.getSaveDirectory() + newFileName;

		file.getFile().renameTo(new File(url));

		System.out.println("file: " + url);

		// 姓名
		String name = getPara("name");
		System.out.println("name: " + name);

		// 讲姓名（中文）转换成拼音，作为账号
		String account = CN2SpellUtil.getInstance().getSpelling(name);
		System.out.println("account: " + account);

		// 初始密码跟账号相同
		String password = account;
		System.out.println("password: " + password);

		// 医生简介
		String desc = getPara("desc");
		System.out.println("desc: " + desc);

		// 职称ID
		int roleId = getParaToInt("roleId", 4);

		// 性别 0：女 1：男
		int sex = getParaToInt("sex", 1);

		// 年龄
		int age = getParaToInt("age", 0);

		// 是否被删除 0: 删除 1：未删除
		// 目前没有回收站功能，因此作用不大，可忽略
		int del = 1;

		// 科室ID
		int departmentId = getParaToInt("departmentId", 1);

		System.out.println("roleId: " + roleId);
		System.out.println("sex: " + sex);
		System.out.println("age: " + age);
		System.out.println("del: " + del);
		System.out.println("departmentId: " + departmentId);

		new Doctor().set("name", name).set("account", account).set("password", password)
				.set("desc", desc).set("roleId", roleId).set("sex", sex).set("age", age)
				.set("del", del).set("departmentId", departmentId).set("image", url).save();

		redirect("index");

	}

	/**
	 * 修改医生信息
	 */
	public void edit() {
		// 上传的头像文件
		UploadFile file = getFile("image", "/images/", 10 * 1024 * 1024);// ,
																			// "D:/",
																			// 100
																			// *
																			// 1024
																			// *
																			// 1024,
																			// "utf-8"
		String newFileName = "";
		String url = "";
		if (file != null) {
			newFileName = System.currentTimeMillis() + "."
					+ FileUtil.getFileExtension(file.getFile());
			url = file.getSaveDirectory() + newFileName;
			file.getFile().renameTo(new File(url));
		}
		System.out.println("file: " + url);
		// 姓名
		String name = getPara("name");
		System.out.println("name: " + name);

		// 讲姓名（中文）转换成拼音，作为账号
		String account = CN2SpellUtil.getInstance().getSpelling(name);
		System.out.println("account: " + account);

		// 初始密码跟账号相同
		String password = account;
		System.out.println("password: " + password);

		// 医生简介
		String desc = getPara("desc");
		System.out.println("desc: " + desc);

		// 职称ID
		int roleId = getParaToInt("roleId", 4);

		// 性别 0：女 1：男
		int sex = getParaToInt("sex", 1);

		// 年龄
		int age = getParaToInt("age", 0);

		// 是否被删除 0: 删除 1：未删除
		// 目前没有回收站功能，因此作用不大，可忽略
		int del = 1;

		// 科室ID
		int departmentId = getParaToInt("departmentId", 1);

		System.out.println("roleId: " + roleId);
		System.out.println("sex: " + sex);
		System.out.println("age: " + age);
		System.out.println("del: " + del);
		System.out.println("departmentId: " + departmentId);
		Doctor.dao.findById(getParaToInt("doctorId")).set("name", name).set("account", account)
				.set("password", password).set("desc", desc).set("roleId", roleId).set("sex", sex)
				.set("age", age).set("del", del).set("departmentId", departmentId)
				.set("image", url).update();

		redirect("/admin/doctor");
	}

	/**
	 * 跳转编辑页面
	 * 
	 */
	public void goEditPage() {
		int doctorId = getParaToInt(0);
		setAttr("doctor", Doctor.dao.findById(doctorId));

		// 读取所有的医生类（主任医师、副主任医师、医生等）职称
		List<Role> roles = Role.dao.getRolesByType(1);
		setAttr("roles", roles);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getAllDepartments();
		setAttr("departments", departments);

		render("edit.html");
	}

	/**
	 * 删除医生信息
	 */
	public void delete() {
		int doctorId = getParaToInt(0);
		Doctor.dao.deleteById(doctorId);
		// Doctor doctor = Doctor.dao.get(doctorId);
		// doctor.set("del",1);
		// doctor.update();
		redirect("/admin/doctor");
	}

}
