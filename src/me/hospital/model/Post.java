package me.hospital.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Post model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@SuppressWarnings("serial")
public class Post extends Model<Post> {
	public static final Post dao = new Post();

	/**
	 * 获取当前文章的分类信息
	 * 
	 * @return
	 */
	public PostCategory getCategory() {
		return PostCategory.dao.findById(get("cid"));
	}

	/**
	 * 分页获取数据
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Post> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *",
				"from post order by id asc");
	}

	/**
	 * 带分类分页获取数据
	 */
	public Page<Post> paginate(int pageNumber, int pageSize, int cid) {

		return paginate(pageNumber, pageSize, "select *",
				"from post where cid = ? order by id asc", cid);

	}

}
