package me.hospital.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Role;
import me.hospital.util.CN2SpellUtil;
import me.hospital.util.FileUtil;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * AdminController 所有 sql 写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
 */

public class DoctorController extends Controller {
	public void index() {
		render("index.html");
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
		UploadFile file = getFile("image", "/images/", 10 * 1024 * 1024);// , "D:/", 100 * 1024 * 1024, "utf-8"
		
		String newFileName = System.currentTimeMillis() + "." + FileUtil.getFileExtension(file.getFile());
		
		
		String url =  file.getSaveDirectory() + newFileName;
		
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

		new Doctor().set("name", name).set("account", account)
				.set("password", password).set("desc", desc)
				.set("roleId", roleId).set("sex", sex).set("age", age)
				.set("del", del).set("departmentId", departmentId)
				.set("image", url).save();

		redirect("index");

	}

}
