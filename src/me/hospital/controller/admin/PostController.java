package me.hospital.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.admin.PostInterceptor;
import me.hospital.model.Admin;
import me.hospital.model.Post;
import me.hospital.util.FileUtil;
import me.hospital.util.ParamUtil;
import me.hospital.validator.admin.SavePostValidator;

import org.apache.commons.lang3.StringEscapeUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

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

		// 读取所有的公告列表
		Page<Post> postList = Post.dao.paginate(page, CoreConstants.PAGE_SIZE);

		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String formatDate = null;
		for (Post post : postList.getList()) {
			formatDate = post.getStr("time");
			post.set("time", formatter.format(Long.parseLong(formatDate)));
		}

		setAttr("postList", postList);

		setAttr("searchTitle", "");
		setAttr("searchCategory", -1);
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
		Post post = Post.dao.findById(postId);
		String content = post.getStr("content");
//		post.set("content", StringEscapeUtils.unescapeHtml4(content));
		setAttr("post", post);
		setAttr("content", StringEscapeUtils.unescapeHtml4(content));
		render("add.html");
	}

	@Before(SavePostValidator.class)
	public void save() {

		Post post = getModel(Post.class);

		String content = getPara("content");

		content = StringEscapeUtils.escapeHtml4(content);

		post.set("content", content);

		Admin admin = (Admin) getSession().getAttribute("admin");

		post.set("author", admin.get("account"));

		post.set("del", 1);

		post.set("time", String.valueOf(System.currentTimeMillis()));

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

		if (postId > -1) {
			if (Post.dao.deleteById(postId)) {
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
			queryParams.put("category", getPara("category"));

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

			int cid = Integer.parseInt(queryParams.get("category"));
			if (cid > -1) {
				sb.append(" and cid = ?");
				params.add(cid);
			}

			setAttr("searchTitle", title);
			setAttr("searchCategory", cid);
			setAttr("searchPage", CoreConstants.SEARCH_PAGE);

		}

		// 公告信息列表
		Page<Post> postList = Post.dao.paginate(page, CoreConstants.PAGE_SIZE, "select *",
				sb.toString(), params.toArray());

		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String formatDate = null;
		for (Post post : postList.getList()) {
			formatDate = post.getStr("time");
			post.set("time", formatter.format(Long.parseLong(formatDate)));
		}

		setAttr("postList", postList);

		render("index.html");

	}

	/**
	 * 上传图片
	 */
	public void uploadImage() {

		UploadFile file = getFile("upfile", CoreConstants.ATTACHMENT_TEMP_PATH,
				CoreConstants.MAX_FILE_SIZE);

		// 保存文件并获取保存在数据库中的路径
		String savePath = FileUtil.saveUploadImage(file.getFile());

		String pictitle = getPara("pictitle");

		Map<String, String> result = new HashMap<String, String>();

		result.put("original", pictitle);
		result.put("url", savePath);
		result.put("title", pictitle);
		result.put("state", "SUCCESS");

		renderJson(result);

	}

}
