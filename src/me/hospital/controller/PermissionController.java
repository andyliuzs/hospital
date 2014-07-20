package me.hospital.controller;

import me.hospital.model.Admin;

import com.jfinal.core.Controller;

/**
 * AdminController
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class PermissionController extends Controller {
	public void index() {
		setAttr("blogPage", Admin.dao.paginate(getParaToInt(0, 1), 10));
		render("blog.html");
	}
	
	public void add() {
	}
	
	public void save() {
		getModel(Admin.class).save();
		redirect("/blog");
	}
	
	public void edit() {
		setAttr("blog", Admin.dao.findById(getParaToInt()));
	}
	
	public void update() {
		getModel(Admin.class).update();
		redirect("/blog");
	}
	
	public void delete() {
		Admin.dao.deleteById(getParaToInt());
		redirect("/blog");
	}
}


