package me.hospital.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.DepartmentInterceptor;
import me.hospital.model.Department;
import me.hospital.util.FileUtil;
import me.hospital.util.ParamUtil;
import me.hospital.validator.SaveDepartmentValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * DepartmentController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@Before(DepartmentInterceptor.class)
public class DepartmentController extends Controller {
	
	
	public void index() {
		// 判断当前是否是搜索的数据进行的分页
		// 如果是搜索的数据，则跳转至search方法处理
		if (!ParamUtil.isEmpty(getPara("s"))) {

			search();

			return;
		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}
		// 读取所有的科室信息。
		Page<Department> departmentList = Department.dao.paginate(page, CoreConstants.PAGE_SIZE);
		setAttr("departmentList", departmentList);
		setAttr("searchName", "");
		setAttr("searchDirectorId", -1);
		setAttr("searchKey", -1);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);
		render("index.html");
	}

	public void add() {
		render("add.html");
	}

	/**
	 * 搜索
	 */
	public void search() {

		if (ParamUtil.isEmpty(getPara("s"))) {

			// 说明当前请求是搜索数据的post请求，并非搜索的分页请求
			// 在这里执行搜索操作，并将结果保存到缓存中

			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("name", getPara("name"));
			queryParams.put("directorId", getPara("directorId"));
			queryParams.put("key", getPara("key"));
			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, queryParams);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("from department where id > 0");

		HashMap<String, String> queryParams = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);
		List<Object> params = new ArrayList<Object>();

		if (queryParams != null) {

			String name = queryParams.get("name");

			if (!ParamUtil.isEmpty(name)) {
				sb.append(" and name like ?");
				params.add("%" + name + "%");
			}

			int directorId = Integer.parseInt(queryParams.get("directorId"));
			if (directorId > -1) {
				sb.append(" and directorId = ?");
				params.add(directorId);
			}

			int key = Integer.parseInt(queryParams.get("key"));
			if (key > -1) {
				sb.append(" and department.key = ?");
				params.add(key);
			}
			
			setAttr("searchName", name);
			setAttr("searchDirectorId", directorId);
			setAttr("searchKey", key);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}

		// 医生列表
		Page<Department> departmentList = Department.dao.paginate(page, CoreConstants.PAGE_SIZE,
				"select *", sb.toString(), params.toArray());

		setAttr("departmentList", departmentList);

		render("index.html");

	}

	/**
	 * 添加/修改科室信息处理方法
	 */
	@Before(SaveDepartmentValidator.class)
	public void save() {

		UploadFile file = getFile("department.image", CoreConstants.ATTACHMENT_TEMP_PATH, CoreConstants.MAX_FILE_SIZE);

		// 保存文件并获取保存在数据库中的路径
		String savePath = FileUtil.saveAvatarImage(file.getFile());

		Department department = getModel(Department.class);
		
		System.out.println("savePath: " + savePath);
		
		// 设置头像路径
		department.set("image", savePath);

		if (null == department.getInt("id")) {
			department.save();
		} else {
			department.update();
		}

		System.out.println("department: " + department);

		redirect("index");

	}

	/**
	 * 跳转编辑页面
	 * 
	 */
	public void edit() {
		int departmentId = getParaToInt(0);
		setAttr("department", Department.dao.findById(departmentId));
		render("add.html");
	}

	/**
	 * 删除科室信息
	 */
	public void delete() {
		
		int departmentId = ParamUtil.paramToInt(getPara(0), -1);

		if(departmentId > -1) {
			if(Department.dao.deleteById(departmentId)) {
				renderJson("msg", "删除成功！");	
			}
		} else {
			renderJson("msg", "删除失败！");
		}
		
	}

	
}
