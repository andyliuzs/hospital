package me.hospital.controller.home;

import me.hospital.config.CoreConstants;
import me.hospital.model.Department;
import me.hospital.util.ParamUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * DepartmentController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
public class DepartmentController extends Controller {

	public void index() {

		// 获取首页要显示的科室的信息
		int page = ParamUtil.paramToInt(getPara(0), 1);

		if (page < 1) {
			page = 1;
		}

		// 读取所有的医生列表
		Page<Department> departments = Department.dao.paginate(page, CoreConstants.PAGE_SIZE);
		setAttr("departments", departments);
		render("/department.html");

	}

	/**
	 * 查看科室的详细信息
	 */
	public void detail() {

		int id = ParamUtil.paramToInt(getPara(0), 1);

		Department department = Department.dao.findById(id);

		// 格式化内容
		department.set("desc", StringEscapeUtils.unescapeHtml4(department.getStr("desc")));

		setAttr("department", department);

		render("/department_detail.html");
	}

}
