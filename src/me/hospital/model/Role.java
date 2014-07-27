package me.hospital.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Role model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@SuppressWarnings("serial")
public class Role extends Model<Role> {
	public static final Role dao = new Role();

	/**
	 * 根据类型获取角色 这里的type只有两种:
	 * 
	 * 0：非医生类（管理员、预约审核员等） 1：医生类（主任医师、副主任医师、医生等）
	 * 
	 * @param type
	 * @return
	 */
	public List<Role> getRolesByType(int type) {
		String sql = "select * from role where type = ?";
		return Role.dao.find(sql, type);
	}

	public Page<Role> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from blog order by id asc");
	}
}
