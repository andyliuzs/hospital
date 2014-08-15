package me.hospital.controller.admin;

import me.hospital.model.Admin;
import me.hospital.model.Doctor;
import me.hospital.util.DateUtil;
import me.hospital.util.ParamUtil;
import me.hospital.validator.admin.UpdatePasswordValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * MyInfoController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class MyInfoController extends Controller {

	public void index() {

		String loginTime = null;

		Admin admin = getSessionAttr("admin");
		if (admin == null) {
			Doctor doctor = getSessionAttr("doctor");
			if (doctor != null) {
				setAttr("doctor", doctor);
				loginTime = doctor.getStr("time");
			}
		} else {
			setAttr("admin", admin);
			loginTime = admin.getStr("time");
		}

		if (!ParamUtil.isEmpty(loginTime)) {
			setAttr("time",
					DateUtil.getFormatTime("yyyy年MM月dd日 HH:mm", loginTime));
		}

		render("index.html");
	}

	public void password() {
		render("password.html");
	}

	@Before(UpdatePasswordValidator.class)
	public void update() {

		// 密码的一致性以及常规验证在UpdatePasswordValidator中进行
		String oldPassword = getPara("oldPassword");
		String newPassword = getPara("newPassword");

		Admin admin = getSessionAttr("admin");
		if (admin != null) {

			if (!admin.get("password").equals(oldPassword)) {
				setAttr("oldMsg", "原密码不正确！");
				keepPara();
				render("password.html");
				return;
			}

			admin.set("password", newPassword);
			if (admin.update()) {
				setAttr("result", "密码修改成功！");
				render("password.html");
			}

		} else {
			Doctor doctor = getSessionAttr("doctor");
			if (doctor != null) {

				if (!doctor.get("password").equals(oldPassword)) {
					setAttr("oldMsg", "原密码不正确！");
					keepPara();
					render("password.html");
					return;
				}

				doctor.set("password", newPassword);
				if (doctor.update()) {
					setAttr("result", "密码修改成功！");
					render("password.html");
				}
			}
		}
	}
}
