package me.hospital.controller.home;

import me.hospital.interceptor.home.HomeInterceptor;

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
public class ResearchController extends Controller {

	@Before(HomeInterceptor.class)
	public void index() {

		render("/research.html");

	}
	
	
}
