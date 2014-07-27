package me.hospital.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Admin model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@SuppressWarnings("serial")
public class Admin extends Model<Admin> {
	public static final Admin dao = new Admin();

	public Admin getByAccountAndPassword(String account, String password) {

		String encodePassword = password;// MD5.digest(password);

		System.out.println("encodePassword: " + encodePassword);

		return dao.findFirst("select * from admin where account = ? and password = ?", account,
				encodePassword);
	}

	/**
	 * 获取角色信息
	 * 
	 * @return
	 */
	public Role getRole() {
		return Role.dao.findById(get("roleId"));
	}

	/**
	 * 获取管理员的所有权限
	 * 
	 * @return
	 */
	public List<Permission> getPermissions() {
		String sql = "select * from permission where id in ( select permissionId from role_permission where roleId = ?)";
		return Permission.dao.find(sql, get("roleId"));
	}

	public Page<Admin> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from blog order by id asc");
	}
}
