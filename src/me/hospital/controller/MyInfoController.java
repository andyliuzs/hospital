package me.hospital.controller;

import com.jfinal.core.Controller;

/**
 * MyInfoController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class MyInfoController extends Controller {

	public void index() {

		render("index.html");
	}

	public void password() {
		render("password.html");
	}
}
