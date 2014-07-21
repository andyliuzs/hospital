package me.hospital.controller;

import com.jfinal.core.Controller;

/**
 * CommonController
 */
public class HomeController extends Controller {

	public void index() {

		redirect("/admin");
	}

}
