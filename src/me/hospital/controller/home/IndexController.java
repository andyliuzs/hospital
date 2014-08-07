package me.hospital.controller.home;

import me.hospital.interceptor.home.HomeInterceptor;
import me.hospital.model.User;
import me.hospital.util.ParamUtil;
import me.hospital.validator.home.RegistValidator;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.Base64Encoder;

/**
 * HomeController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
public class IndexController extends Controller {

	@Before(HomeInterceptor.class)
	public void index() {

		int error = ParamUtil.paramToInt(getPara("error"), -1);
		if(error == 1) {
			setAttr("msg", "登录系统后才能预约");
			render("/index.html");
			return;
		} 
		
		String account = getCookie("ACCOUNT");
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			User user = getSessionAttr(account);
			setAttr("user", user);
		}
		
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

		getSession().removeAttribute(userName);

		User user = User.dao.getByAccountAndPassword(userName, password);

		if (user != null) {
			
			System.out.println("登录成功：" + user.toJson());
			
			// 登录时间戳
			long time = System.currentTimeMillis();
			user.set("time", String.valueOf(time));
			user.update();

			setSessionAttr(user.getStr("account"), user);

			// 将用户的账号用Base64加密后保存到cookie中
			setCookie("ACCOUNT", Base64Encoder.encode(user.getStr("account")), -1);

			redirect("/index.html");
			
		} else {
			
			System.out.println("登录失败");
			
			setAttr("errorMsg", "用户名或密码错误！");
			keepPara("account");
			render("/index.html");
		}
	}

	/**
	 * 跳转至注册页面
	 */
	public void signup() {
		render("/signup.html");
	}
	
	/**
	 * 用户注册
	 */
	@Before(RegistValidator.class)
	public void regist() {
		
		User user = getModel(User.class);
		user.set("time", String.valueOf(System.currentTimeMillis()));
		
		if (user.save()) {
			
			setSessionAttr(user.getStr("account"), user);
			
			// 登录时间戳
			long time = System.currentTimeMillis();
			user.set("time", String.valueOf(time));
			user.update();
			
			// 将用户的账号保存到cookie中，后面需要对cookie加密
			setCookie("ACCOUNT", Base64Encoder.encode(user.getStr("account")), -1);
			
			redirect("/index.html");
			
		} else {
			keepPara(User.class);
		}

	}

	/**
	 * 注销登录
	 */
	public void logout() {
		
		String account = getCookie("ACCOUNT");
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			getSession().removeAttribute(account);
		}
		
		redirect("/index.html");
	}

	/**
	 * 医院简介
	 */
	public void intro() {
		render("/intro.html");
	}
	

}
