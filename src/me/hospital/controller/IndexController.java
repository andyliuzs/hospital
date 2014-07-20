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
 * CommonController
 */
public class IndexController extends Controller {

	public void index() {

		Admin admin = getSessionAttr("admin");
		if (admin != null) {

			setAttr("userName", admin.get("account"));

			System.out.println("userName: " + admin.get("account"));

		} else {

			Doctor doctor = getSessionAttr("doctor");
			setAttr("userName", doctor.get("name"));

			System.out.println("userName: " + doctor.get("name"));

		}

		System.out.println("roleName: " + getSessionAttr("role"));
		System.out.println("access: " + getSessionAttr("accesses"));

		setAttr("roleName", getSessionAttr("role"));
		setAttr("accesses", getSessionAttr("accesses"));

		render("/admin/index.html");
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void login() {

		String userName = getPara("username");
		String password = getPara("password");

		System.out.println("------username-------- " + userName);
		System.out.println("------password-------- " + password);

		Admin admin = Admin.dao.getByAccountAndPassword(userName, password);

		if (admin != null) {

			Role role = admin.getRole();

			// System.out.println("roleName = " + role.getStr("name"));

			List<Permission> accesses = admin.getPermissions();

			setSessionAttr("admin", admin);
			setSessionAttr("role", role.getStr("name"));
			setSessionAttr("accesses", accesses);

			// System.out.println("userName: " + admin.get("account"));
			// System.out.println("roleName: " + getSessionAttr("role"));
			// System.out.println("access: " + getSessionAttr("accesses"));

			// setAttr("userName", admin.get("account"));
			// setAttr("roleName", role.getStr("name"));
			// setAttr("accesses", accesses);

			redirect("/admin/index");

		} else {

			Doctor doctor = Doctor.dao.getByAccountAndPassword(userName,
					password);
			if (doctor != null) {

				Role role = doctor.getRole();

				System.out.println("roleName = " + role.getStr("name"));

				List<Permission> accesses = doctor.getAccesses();

				setSessionAttr("doctor", doctor);
				setSessionAttr("role", role.getStr("name"));
				setSessionAttr("accesses", accesses);

				// setAttr("userName", doctor.get("name"));
				// setAttr("roleName", role.getStr("name"));
				// setAttr("access", accesses);

				redirect("/admin/index");

			} else {

				System.out.println("ccccccc");

				setAttr("msg", "用户名或密码错误");
				redirect("/admin/login.html");
			}
		}

	}
}
