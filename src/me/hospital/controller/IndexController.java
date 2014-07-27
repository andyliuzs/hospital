package me.hospital.controller;

import java.util.List;

import me.hospital.model.Admin;
import me.hospital.model.Doctor;
import me.hospital.model.Permission;
import me.hospital.model.Role;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;

/**
 * IndexController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class IndexController extends Controller {

	public void index() {

		String userName = "";
		Admin admin = getSessionAttr("admin");
		if (admin != null) {

			userName = admin.get("account");

		} else {

			Doctor doctor = getSessionAttr("doctor");

			if (doctor != null) {
				userName = doctor.get("name");
			}
		}

		System.out.println("userName: " + userName);
		System.out.println("roleName: " + getSessionAttr("roleName"));

		setAttr("userName", userName);
		setAttr("roleName", getSessionAttr("roleName"));
		setAttr("permissions", getSessionAttr("permissions"));

		render("/admin/index.html");
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void login() {

		String userName = getPara("username");
		String password = getPara("password");

		getSession().removeAttribute("admin");
		getSession().removeAttribute("doctor");
		getSession().removeAttribute("roleName");
		getSession().removeAttribute("permissions");

		Admin admin = Admin.dao.getByAccountAndPassword(userName, password);
		Doctor doctor = Doctor.dao.getByAccountAndPassword(userName, password);

		Role role = null;
		List<Permission> permissions = null;

		if (admin != null) {

			role = admin.getRole();
			permissions = admin.getPermissions();

			setSessionAttr("admin", admin);

		} else if (doctor != null) {

			role = doctor.getRole();
			permissions = doctor.getPermissions();

			setSessionAttr("doctor", doctor);

		} else {

			setAttr("msg", "用户名或密码错误");
			redirect("/admin/login.html");

			return;
		}

		if (permissions != null && permissions.size() > 0) {

			for (Permission p : permissions) {
				p.initializeSubPermissions();
			}

		}

		setSessionAttr("roleName", role.getStr("name"));
		setSessionAttr("permissions", permissions);

		redirect("/admin/index");

	}

	/**
	 * 注销登录
	 */
	public void logout() {
		getSession().removeAttribute("admin");
		getSession().removeAttribute("doctor");
		getSession().removeAttribute("roleName");
		getSession().removeAttribute("permissions");
		redirect("/admin/index");
	}
}
