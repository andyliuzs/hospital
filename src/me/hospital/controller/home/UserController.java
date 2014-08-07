package me.hospital.controller.home;

import me.hospital.model.User;
import me.hospital.util.ParamUtil;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.oreilly.servlet.Base64Decoder;

/**
 * UserController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
public class UserController extends Controller {
	
	public void index() {
		
		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user != null) {
			setAttr("user", user);
			render("/user_info.html");
		} else {
			redirect("/error.html");
		}
		
	}

	/**
	 * 修改用户的基本信息
	 */
	public void updateInfo() {
		
		// 逻辑处理
		
		redirect("/user");
	}
	
	/**
	 * 跳转至修改密码界面
	 */
	public void password() {
		
		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user != null) {
			setAttr("user", user);
			render("/user_password.html");
		} else {
			redirect("/error.html");
		}
		
	}
	
	/**
	 * 修改用户的密码
	 */
	public void updatePassword() {
		
		// 逻辑处理
		
		redirect("/user");
	}
	
}
