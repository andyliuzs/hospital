package me.hospital.controller.admin;

import com.jfinal.core.Controller;

/**
 * RegisterController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class RegisterController extends Controller {

	public void index() {
		render("index.html");
	}

	public void process() {
		render("process.html");
	}

}
