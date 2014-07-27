package me.hospital.controller;

import me.hospital.model.Post;

import com.jfinal.core.Controller;

/**
 * PostController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class PostController extends Controller {
	
	
	public void index() {
		
		render("index.html");
	}
	
	public void add() {
		render("add.html");
	}
	
	public void edit() {
		setAttr("post", Post.dao.findById(getParaToInt()));
	}
	
	public void save() {
		getModel(Post.class).save();
		redirect("/admin/post");
	}
	
	public void update() {
		getModel(Post.class).update();
		redirect("/admin/post");
	}
	
	public void delete() {
		Post.dao.deleteById(getParaToInt());
		redirect("/admin/post");
	}
}


