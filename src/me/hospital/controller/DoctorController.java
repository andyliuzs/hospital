package me.hospital.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.DoctorInterceptor;
import me.hospital.model.Doctor;
import me.hospital.util.CN2SpellUtil;
import me.hospital.util.FileUtil;
import me.hospital.util.ParamUtil;
import me.hospital.validator.SaveDoctorValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * DoctorController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@Before(DoctorInterceptor.class)
public class DoctorController extends Controller {

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

		// 医生列表
		Page<Doctor> doctorList = Doctor.dao.paginate(page, CoreConstants.PAGE_SIZE);
		setAttr("doctorList", doctorList);

		// for search
		setAttr("searchName", "");
		setAttr("searchRoleId", -1);
		setAttr("searchSex", -1);
		setAttr("searchDepartmentId", -1);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);

		render("index.html");
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
			queryParams.put("roleId", getPara("roleId"));
			queryParams.put("sex", getPara("sex"));
			queryParams.put("departmentId", getPara("departmentId"));

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

			int roleId = Integer.parseInt(queryParams.get("roleId"));
			if (roleId > -1) {
				sb.append(" and roleId = ?");
				params.add(roleId);
			}

			int sex = Integer.parseInt(queryParams.get("sex"));
			if (sex > -1) {
				sb.append(" and sex = ?");
				params.add(sex);
			}

			int departmentId = Integer.parseInt(queryParams.get("departmentId"));
			if (departmentId > -1) {
				sb.append(" and departmentId = ?");
				params.add(departmentId);
			}

			setAttr("searchName", name);
			setAttr("searchRoleId", roleId);
			setAttr("searchSex", sex);
			setAttr("searchDepartmentId", departmentId);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}

		// 医生列表
		Page<Doctor> doctorList = Doctor.dao.paginate(page, CoreConstants.PAGE_SIZE, "select *",
				sb.toString(), params.toArray());

		setAttr("doctorList", doctorList);

		render("index.html");

	}

	/**
	 * 添加医生
	 */
	public void add() {

		render("add.html");

	}

	/**
	 * 添加/修改医生信息处理方法
	 */
	@Before(SaveDoctorValidator.class)
	public void save() {

		UploadFile file = getFile("doctor.image", "/", CoreConstants.MAX_FILE_SIZE);

		// 保存文件并获取保存在数据库中的路径
		String savePath = FileUtil.saveAvatarImage(file.getFile());

		Doctor doctor = getModel(Doctor.class);

		// 将姓名（中文）转换成拼音，作为账号
		String account = CN2SpellUtil.getInstance().getSpelling(doctor.getStr("name"));

		doctor.set("account", account);
		// 初始密码跟账号相同
		doctor.set("password", account);

		// 设置头像路径
		doctor.set("image", savePath);

		if (null == doctor.getInt("id")) {
			doctor.save();
		} else {
			doctor.update();
		}

		System.out.println("doctor: " + doctor);

		redirect("index");

	}

	/**
	 * 跳转编辑页面
	 * 
	 */
	public void edit() {
		int doctorId = getParaToInt(0);
		setAttr("doctor", Doctor.dao.findById(doctorId));
		render("add.html");
	}

	/**
	 * 删除医生信息
	 */
	public void delete() {
		
		int doctorId = ParamUtil.paramToInt(getPara(0), -1);

		if(doctorId > -1) {
			if(Doctor.dao.deleteById(doctorId)) {
				renderJson("msg", "删除成功！");	
			}
		} else {
			renderJson("msg", "删除失败！");
		}
		
	}

	/**
	 * 查看图片
	 */
	public void showImage() {
		System.out.println("******this is output image function！********");
		getResponse().setContentType("text/html; charset=UTF-8");
		getResponse().setContentType("image/jpeg");
		String fname = Doctor.dao.findById(getPara(0)).getStr("image");
		OutputStream os = null;
		FileInputStream fis = null;
		try {

			String absolutePath = CoreConstants.ATTACHMENT_AVATAR_PATH + fname;
			fis = new FileInputStream(absolutePath);
			os = getResponse().getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024 * 1024];
			while ((count = fis.read(buffer)) != -1)
				os.write(buffer, 0, count);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		renderNull();
	}
}
