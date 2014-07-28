package me.hospital.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.PostInterceptor;
import me.hospital.model.Admin;
import me.hospital.model.Post;
import me.hospital.util.ParamUtil;
import me.hospital.validator.SavePostValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * PostController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(PostInterceptor.class)
public class PostController extends Controller {
	
	
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
		// 读取所有的公告列表。
		Page<Post> postList =Post.dao.paginate(page, CoreConstants.PAGE_SIZE);
		setAttr("postList", postList);
		setAttr("searchTitle", "");
		setAttr("searchAuthor", "");
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);
		render("index.html");
	}

	
	public void add() {
		render("add.html");
	}
	
	/**
	 * 跳转编辑页面
	 * 
	 */
	public void edit() {
		int postId = getParaToInt(0);
		setAttr("post", Post.dao.findById(postId));
		render("add.html");
	}
	
	@Before(SavePostValidator.class)
	public void save() {
		Post post  = getModel(Post.class);
		Admin admin  = (Admin)getSession().getAttribute("admin");
		post.set("author", admin.get("account"));
		post.set("del", 1);
		if (null == post.getInt("id")) {
			post.save();
		} else {
			post.update();
		}

		System.out.println("post: " + post);

		redirect("index");

	}
	
	public void update() {
		getModel(Post.class).update();
		redirect("/admin/post");
	}
	
	public void delete() {
	
		int postId = ParamUtil.paramToInt(getPara(0), -1);

		if(postId > -1) {
			if(Post.dao.deleteById(postId)) {
				renderJson("msg", "删除成功！");	
			}
		} else {
			renderJson("msg", "删除失败！");
		}
	}
	
	/**
	 * 搜索
	 */
	public void search() {

		if (ParamUtil.isEmpty(getPara("s"))) {

			// 说明当前请求是搜索数据的post请求，并非搜索的分页请求
			// 在这里执行搜索操作，并将结果保存到缓存中

			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("title", getPara("title"));
			queryParams.put("author", getPara("author"));

			setSessionAttr(CoreConstants.SEARCH_SESSION_KEY, queryParams);

		}

		int page = ParamUtil.paramToInt(getPara("p"), 1);

		if (page < 1) {
			page = 1;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("from post where id > 0");

		HashMap<String, String> queryParams = getSessionAttr(CoreConstants.SEARCH_SESSION_KEY);
		List<Object> params = new ArrayList<Object>();

		if (queryParams != null) {

			String title = queryParams.get("title");

			if (!ParamUtil.isEmpty(title)) {
				sb.append(" and title like ?");
				params.add("%" + title + "%");
			}
			
			String author  = getPara("author");
			if (!ParamUtil.isEmpty(author)) {
				if(!"-1".equals(author)){
					sb.append(" and author like ?");
					params.add("%" + author + "%");
				}
			}
			
			setAttr("searchTitle", title);
			setAttr("searchAuthor", author);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}
		
		

		// 公告信息列表
		Page<Post> postList = Post.dao.paginate(page, CoreConstants.PAGE_SIZE, "select *",
				sb.toString(), params.toArray());

		setAttr("postList", postList);

		render("index.html");

	}
}

