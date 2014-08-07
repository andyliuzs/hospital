package me.hospital.controller.home;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringEscapeUtils;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.home.ResearchInterceptor;
import me.hospital.model.Post;
import me.hospital.util.ParamUtil;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * ResearchController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
@Before(ResearchInterceptor.class)
public class ResearchController extends Controller {

	
	public void index() {

		// 默认查询科研教学分类的第一个子类
		int cid = ParamUtil.paramToInt(getPara(0), 9);
		int page = ParamUtil.paramToInt(getPara(1), 1);
		
		if (page < 1) {
			page = 1;
		}

		// 读取所有的新闻列表
		Page<Post> researchList = Post.dao.paginate(page, CoreConstants.PAGE_SIZE,
				cid);

		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String formatDate = null;
		for (Post post : researchList.getList()) {
			formatDate = post.getStr("time");
			post.set("time", formatter.format(Long.parseLong(formatDate)));
		}

		setAttr("cid", cid);
		setAttr("researchList", researchList);

		render("/research.html");
	}

	public void detail() {

		int cid = ParamUtil.paramToInt(getPara(0), 9);
		int id = ParamUtil.paramToInt(getPara(1), 1);
		
		Post post = Post.dao.findById(id);
		
		if(post == null) {
			// 跳转至错误页面
			redirect("/error.html");
			return;
		}
		
		// 修改点击量
		int hit = post.getInt("hits");
		hit++;
		post.set("hits", hit);
		post.update();

		// 格式化内容
		post.set("content",
				StringEscapeUtils.unescapeHtml4(post.getStr("content")));

		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String formatDate = post.getStr("time");
		post.set("time", formatter.format(Long.parseLong(formatDate)));

		setAttr("research", post);
		setAttr("cid", cid);
		render("/research_detail.html");
	}
}
