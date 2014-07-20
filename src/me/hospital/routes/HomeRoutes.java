package me.hospital.routes;

import me.hospital.controller.CommonController;

import com.jfinal.config.Routes;

public class HomeRoutes extends Routes{

	@Override
	public void config() {
		// TODO Auto-generated method stub
		add("/", CommonController.class);
	}

}
