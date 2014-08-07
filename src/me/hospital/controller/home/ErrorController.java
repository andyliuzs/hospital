package me.hospital.controller.home;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;

/**
 * ErrorController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
public class ErrorController extends Controller {

	/**
	 * 默认404错误
	 */
	public void index() {
		_404();
	}

	/**
	 * 404错误
	 */
	public void _404() {
		render("/error/404.html");
	}
	
	/**
	 * 500错误
	 */
	public void _500() {
		render("/error/500.html");
	}
}
