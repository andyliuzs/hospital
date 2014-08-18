package me.hospital.controller.home;

import me.hospital.model.User;
import me.hospital.util.ParamUtil;
import me.hospital.validator.home.SaveUserMessageValidator;
import me.hospital.validator.home.UpdatePasswordValidator;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.Base64Encoder;
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
			String updateTip =getPara("updateTip");
			if(!ParamUtil.isEmpty(updateTip)){
				setAttr("updateTip", updateTip);
			}
			render("/user_info.html");
		} else {
			redirect("/error.html");
		}
		
	}

	/**
	 * 修改用户的基本信息
	 */
	@Before(SaveUserMessageValidator.class)
	public void updateInfo() {
		User user = getModel(User.class);
		// 逻辑处理
		boolean result = user.update();
		
		if(result){
			// 将用户的账号用Base64加密后保存到cookie中
			setCookie("ACCOUNT", Base64Encoder.encode(user.getStr("account")), -1);
			getSession().removeAttribute(user.getStr("account"));
			setSessionAttr(user.getStr("account"), user);
			setAttr("updateTip", "修改信息成功！");
		}else{
			setAttr("updateTip", "修改信息失败！");
		}
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
	@Before(UpdatePasswordValidator.class)
	public void updatePassword() {
		// 密码的一致性以及常规验证在UpdatePasswordValidator中进行
		String oldPassword = getPara("oldPassword");
		String newPassword = getPara("newPassword");
		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user != null) {
			setAttr("user",user);
			if(!user.get("password").equals(oldPassword)) {
				setAttr("oldMsg", "原密码不正确！");
				keepPara();
				render("/user_password.html");
				return;
			}
		
			user.set("password", newPassword);
			if(user.update()) {
				setAttr("result", "修改成功！");
				render("/user_password.html");
			}
			
		}
		// 逻辑处理
		
		redirect("/user");
	}
	
}
