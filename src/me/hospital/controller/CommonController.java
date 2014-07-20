package me.hospital.controller;

import java.util.List;

import me.hospital.interceptor.SessionInterceptor;
import me.hospital.model.Admin;
import me.hospital.model.Doctor;
import me.hospital.model.Role;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * CommonController
 */
@Before(SessionInterceptor.class)
public class CommonController extends Controller {

	public void index() {
		
		
		Admin admin = getSessionAttr("admin");
		if(admin != null) {
			
			setAttr("userName", admin.get("account"));
			
			System.out.println("userName: " + admin.get("account"));
			
		} else {
			
			Doctor doctor = getSessionAttr("doctor");
			setAttr("userName", doctor.get("name"));
			
			System.out.println("userName: " + doctor.get("name"));
			
		}
		
		System.out.println("roleName: " +  getSessionAttr("role"));
		System.out.println("access: " + getSessionAttr("access"));
		
		setAttr("roleName", getSessionAttr("role"));
		setAttr("access", getSessionAttr("access"));
		
		
		
		
		render("/admin/index.html");
	}

	public void login() {

		String userName = getPara("username");
		String password = getPara("password");

		System.out.println("------username-------- " + userName);
		System.out.println("------password-------- " + password);

		
		Admin admin = Admin.dao.getByAccountAndPassword(userName, password);

		if (admin != null) {

			String roleId = String.valueOf(admin.getInt("roleId"));
			
			Role role = Role.dao.findById(roleId);
			
			String roleName = "";
			if(role != null) {
				roleName = role.getStr("name");
			}
			System.out.println("roleName = " + roleName);

			String sql = "select name, url from access where id in ( select accessId from role_access where roleId = ?)";
			
			List<Record> result = Db.find(sql, roleId);
			
			
			// setCookie("bbsID", bbsID, 60*60*24*30);//一个月有效
			
			
			setSessionAttr("admin", admin);
			
			setSessionAttr("role", roleName);
			
			setSessionAttr("access", result);
			
			System.out.println("userName: " + admin.get("account"));
			System.out.println("roleName: " +  getSessionAttr("role"));
			System.out.println("access: " + getSessionAttr("access"));
			
			
			setAttr("userName", admin.get("account"));
			setAttr("roleName", roleName);
			setAttr("access", result);
			
			
			redirect("/admin/index.html");
			
		} else {

			System.out.println("bbbbbbbb");
			
			Doctor doctor = Doctor.dao.getByAccountAndPassword(userName, password);
			if (doctor != null) {
	
				// setCookie("bbsID", bbsID, 60*60*24*30);//一个月有效
	
				String roleId = String.valueOf(doctor.getInt("roleId"));
				
				
				Role role = Role.dao.findById(roleId);
				
				String roleName = "";
				if(role != null) {
					roleName = role.getStr("name");
				}
				

				String sql = "select name, url from access where id in ( select accessId from role_access where roleId = ?)";
				
				List<Record> result = Db.find(sql, roleId);
				
				
				// setCookie("bbsID", bbsID, 60*60*24*30);//一个月有效
				System.out.println("aaaaaaaaa");
				
				setSessionAttr("doctor", doctor);
				
				setSessionAttr("role", roleName);
				
				setSessionAttr("access", result);
				
				setAttr("userName", doctor.get("name"));
				setAttr("roleName", roleName);
				setAttr("access", result);
				
				
				redirect("/admin/index.html");
				
			} else {
				
				System.out.println("ccccccc");
				
				setAttr("msg", "用户名或密码错误");
				redirect("/admin/login.html");
			}
		}

	}
}
