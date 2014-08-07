package me.hospital.routes;


import me.hospital.controller.home.DepartmentController;
import me.hospital.controller.home.DoctorController;
import me.hospital.controller.home.ErrorController;
import me.hospital.controller.home.IndexController;
import me.hospital.controller.home.NewsController;
import me.hospital.controller.home.RegisterController;
import me.hospital.controller.home.ResearchController;
import me.hospital.controller.home.UserController;

import com.jfinal.config.Routes;

public class HomeRoutes extends Routes{

	@Override
	public void config() {

		// 首页
		add("/", IndexController.class);
		// 新闻中心
		add("/news", NewsController.class);
		// 科室介绍
		add("/department", DepartmentController.class);
		// 名医荟萃
		add("/doctor", DoctorController.class);
		// 科研教学
		add("/research", ResearchController.class);
		// 预约挂号
		add("/register", RegisterController.class);
		// 用户信息修改
		add("/user", UserController.class);
		// 错误页面
		add("/error", ErrorController.class);
	}

}
