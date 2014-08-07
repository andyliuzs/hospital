package me.hospital.controller.home;

import me.hospital.config.CoreConstants;
import me.hospital.model.Doctor;
import me.hospital.util.ParamUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * DoctorController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
public class DoctorController extends Controller {

	public void index() {
		// 获取首页要显示的医生的信息
		int page = ParamUtil.paramToInt(getPara(0), 1);

		if (page < 1) {
			page = 1;
		}

		// 读取所有的医生列表
		Page<Doctor> doctors = Doctor.dao.paginate(page, CoreConstants.PAGE_SIZE);
		setAttr("doctors", doctors);
		render("/doctor.html");

	}

	/**
	 * 查看医生的详细信息
	 */
	public void detail() {

		int id = ParamUtil.paramToInt(getPara(0), 1);
		
		Doctor doctor = Doctor.dao.findById(id);
		
		if(doctor == null) {
			// 跳转至错误页面
			redirect("/error.html");
			return;
		}
		
		// 格式化内容
		doctor.set("desc",
				StringEscapeUtils.unescapeHtml4(doctor.getStr("desc")));

		setAttr("doctor", doctor);
		
		render("/doctor_detail.html");
	}
	
}
