package me.hospital.controller;

import java.io.File;
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
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * DoctorController 所有 sql 写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
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

			String name = getPara("name");
			int roleId = ParamUtil.paramToInt(getPara("roleId"), -1);
			int sex = ParamUtil.paramToInt(getPara("sex"), -1);
			int departmentId = ParamUtil.paramToInt(getPara("departmentId"), -1);

			StringBuilder sb = new StringBuilder();
			sb.append("from doctor where del = 1");

			Map<String, Object> query = new HashMap<String, Object>();

			if (!ParamUtil.isEmpty(name)) {
				sb.append(" and name like ?");
				query.put("name", "%" + name + "%");
			}

			if (roleId > -1) {
				sb.append(" and roleId = ?");
				query.put("roleId", roleId);
			}

			if (sex > -1) {
				sb.append(" and sex = ?");
				query.put("sex", sex);
			}

			if (departmentId > -1) {
				sb.append(" and departmentId = ?");
				query.put("departmentId", departmentId);
			}

			query.put("query", sb.toString());

			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, query);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		String queryStr = "form doctor where del = 1";
		HashMap<String, Object> query = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);

		List<Object> params = new ArrayList<Object>();
		if (query != null) {

			queryStr = (String) query.get("query");

			for (String key : query.keySet()) {
				if (!"query".equals(key)) {
					params.add(query.get(key));
				}
			}

			String name = query.get("name") == null ? "" : (String) query.get("name");
			name = name.replace("%", "");

			int roleId = query.get("roleId") == null ? -1 : (Integer) query.get("roleId");
			int sex = query.get("sex") == null ? -1 : (Integer) query.get("sex");
			int departmentId = query.get("departmentId") == null ? -1 : (Integer) query
					.get("departmentId");

			// for search
			setAttr("searchName", name);
			setAttr("searchRoleId", roleId);
			setAttr("searchSex", sex);
			setAttr("searchDepartmentId", departmentId);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}

		// 医生列表
		Page<Doctor> doctorList = Doctor.dao.paginate(page, CoreConstants.PAGE_SIZE, "select *",
				queryStr, params.toArray());
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

		// 暂时用不到了
		String contextPath = PathKit.getWebRootPath();
		String filePath = contextPath + CoreConstants.ATTACHMENGT_AVATAR_PATH;

		FileUtil.createDirectory(new File(filePath));
		
		// 上传的头像文件
		UploadFile file = getFile("doctor.image", filePath, CoreConstants.MAX_FILE_SIZE);

		// 存储文件名称
		String newFileName = String.valueOf(System.currentTimeMillis()) + "."
				+ FileUtil.getFileExtension(file.getFile());

		// 存储路径
		String url = filePath + newFileName;

		file.getFile().renameTo(new File(url));
		
		System.out.println("url: " + url);
		System.out.println("url: " + file.getSaveDirectory());
		
		// 保存在数据库中的路径
		String savePath = CoreConstants.ATTACHMENGT_AVATAR_PATH + newFileName;
		System.out.println("savePath: " + savePath);
		
		Doctor doctor = getModel(Doctor.class);
		
		System.out.println("doctor: " + doctor);
		
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
		int doctorId = getParaToInt(0);
		Doctor.dao.deleteById(doctorId);
		redirect("index");
	}

	/**
	 * 查看图片
	 */
	public void showImage() {
		System.out
				.println("*************************this is out put  image function！*****************");
		getResponse().setContentType("text/html; charset=UTF-8");
		getResponse().setContentType("image/jpeg");
		String fname = Doctor.dao.findById(getPara(0)).getStr("image");
		OutputStream os = null;
		FileInputStream fis = null;
		try {
			
			String absolutePath = CoreConstants.ATTACHMENGT_AVATAR_PATH + fname;
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
