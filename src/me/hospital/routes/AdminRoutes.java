package me.hospital.routes;

import me.hospital.controller.admin.AdminController;
import me.hospital.controller.admin.DepartmentController;
import me.hospital.controller.admin.DoctorController;
import me.hospital.controller.admin.MyInfoController;
import me.hospital.controller.admin.PostController;
import me.hospital.controller.admin.RegisterController;
import me.hospital.controller.admin.ScheduleController;
import me.hospital.controller.admin.UserController;

import com.jfinal.config.Routes;

public class AdminRoutes extends Routes{

	@Override
	public void config() {

		// 后台首页
		add("/admin", AdminController.class);
		
		// 我的信息
		add("/admin/myinfo", MyInfoController.class);
		
		// 公告管理
		add("/admin/post", PostController.class);
		
		// 预约管理
		add("/admin/register", RegisterController.class);
		
		// 科室管理
		add("/admin/department", DepartmentController.class);
		
		// 用户信息
		add("/admin/user", UserController.class);
		
		// 医生管理
		add("/admin/doctor", DoctorController.class);
		
		// 排班管理
		add("/admin/schedule", ScheduleController.class);
	}

}
