package me.hospital.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.model.User;
import me.hospital.util.ParamUtil;
import me.hospital.validator.admin.SaveUserValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * UserController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class UserController extends Controller {

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

		// 读取用户信息
		Page<User> userList = User.dao.paginate(page, CoreConstants.PAGE_SIZE);

		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String formatDate = null;
		for (User user : userList.getList()) {
			formatDate = user.getStr("time");
			user.set("time", formatter.format(Long.parseLong(formatDate)));
		}

		setAttr("userList", userList);
		setAttr("searchName", "");
		setAttr("searchSex", -1);
		setAttr("searchAddress", "");
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);
		render("index.html");
	}

	/**
	 * 删除医生信息
	 */
	public void delete() {

		int userId = ParamUtil.paramToInt(getPara(0), -1);

		if (userId > -1) {
			if (User.dao.deleteById(userId)) {
				renderJson("msg", "删除成功！");
			}
		} else {
			renderJson("msg", "删除失败！");
		}

	}

	/**
	 * 跳转修改页面
	 * 
	 */
	public void edit() {
		int userId = getParaToInt(0);
		setAttr("user", User.dao.findById(userId));
		render("edit.html");
	}

	/**
	 * 添加/修改用户信息处理方法
	 */
	@Before(SaveUserValidator.class)
	public void save() {

		User user = getModel(User.class);
		user.set("time", new Date());
		if (null == user.getInt("id")) {
			user.save();
		} else {
			user.update();
		}

		System.out.println("user: " + user);

		redirect("index");

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
			queryParams.put("sex", getPara("sex"));
			queryParams.put("address", getPara("address"));
			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, queryParams);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("from user u where id > 0");

		HashMap<String, String> queryParams = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);
		List<Object> params = new ArrayList<Object>();

		if (queryParams != null) {

			String name = queryParams.get("name");

			if (!ParamUtil.isEmpty(name)) {
				sb.append(" and u.name like ?");
				params.add("%" + name + "%");
			}

			int sex = Integer.parseInt(queryParams.get("sex"));
			if (sex > -1) {
				sb.append(" and u.sex = ?");
				params.add(sex);
			}

			String address = queryParams.get("address");
			if (!ParamUtil.isEmpty(address)) {
				sb.append(" and u.address like ?");
				params.add(address);
			}

			setAttr("searchName", name);
			setAttr("searchSex", sex);
			setAttr("searchAddress", address);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}

		// 医生列表
		Page<User> userList = User.dao.paginate(page, CoreConstants.PAGE_SIZE,
				"select *", sb.toString(), params.toArray());

		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String formatDate = null;
		for (User user : userList.getList()) {
			formatDate = user.getStr("time");
			user.set("time", formatter.format(Long.parseLong(formatDate)));
		}

		setAttr("userList", userList);

		render("index.html");

	}
}
