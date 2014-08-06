package me.hospital.controller.home;

import me.hospital.interceptor.home.HomeInterceptor;
import me.hospital.model.User;
import me.hospital.validator.home.RegistValidator;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;

/**
 * HomeController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
public class IndexController extends Controller {

	@Before(HomeInterceptor.class)
	public void index() {

		render("/index.html");

	}
	
	/**
	 * 用户登录处理
	 */
	@Before(HomeInterceptor.class)
	public void login() {
		
		System.out.println("username: " + getPara("username"));
		System.out.println("password: " + getPara("password"));
		
		String userName = getPara("username");
		String password = getPara("password");

		getSession().removeAttribute("user");

		User user = User.dao.getByAccountAndPassword(userName, password);

		if (user != null) {

			// 登录时间戳
			long time = System.currentTimeMillis();
			user.set("time", String.valueOf(time));
			user.update();
			
			setSessionAttr("user", user);

		} else {

			setAttr("errorMsg", "用户名或密码错误！");
			keepPara("account");
			render("/index.html");

			return;
		}
		
		redirect("index.html");
	}
	
	/**
	 * 用户注册
	 */
	@Before(RegistValidator.class)
	public void regist() {
		
		User user = getModel(User.class);
		
		if(user.save()) {
			setAttr("account", user.get("account"));
			redirect("index.html");
		} else {
			keepPara();
			render("/regist.html");
		}
		
	}
	
	/**
	 * 医院简介
	 */
	public void intro() {
		render("/intro.html");
	}
	
}
