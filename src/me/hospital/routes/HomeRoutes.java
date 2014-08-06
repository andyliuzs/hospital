package me.hospital.routes;


import me.hospital.controller.home.DepartmentController;
import me.hospital.controller.home.DoctorController;
import me.hospital.controller.home.IndexController;
import me.hospital.controller.home.NewsController;
import me.hospital.controller.home.RegisterController;
import me.hospital.controller.home.ResearchController;

import com.jfinal.config.Routes;

public class HomeRoutes extends Routes{

	@Override
	public void config() {
		
		add("/", IndexController.class);
		add("/news", NewsController.class);
		add("/department", DepartmentController.class);
		add("/doctor", DoctorController.class);
		add("/research", ResearchController.class);
		add("/register", RegisterController.class);
	}

}
