package me.hospital.routes;

import me.hospital.controller.CommonController;
import me.hospital.controller.IndexController;

import com.jfinal.config.Routes;

public class AdminRoutes extends Routes{

	@Override
	public void config() {

		add("/admin", IndexController.class);
		
	}

}
