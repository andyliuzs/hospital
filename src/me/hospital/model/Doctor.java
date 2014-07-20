package me.hospital.model;

import java.util.List;

import org.eclipse.jetty.util.security.Credential.MD5;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Blog model.

将表结构放在此，消除记忆负担
mysql> desc blog;
+---------+--------------+------+-----+---------+----------------+
| Field   | Type         | Null | Key | Default | Extra          |
+---------+--------------+------+-----+---------+----------------+
| id      | int(11)      | NO   | PRI | NULL    | auto_increment |
| title   | varchar(200) | NO   |     | NULL    |                |
| content | mediumtext   | NO   |     | NULL    |                |
+---------+--------------+------+-----+---------+----------------+

数据库字段名建议使用驼峰命名规则，便于与 java 代码保持一致，如字段名： userId
 */
@SuppressWarnings("serial")
public class Doctor extends Model<Doctor> {
	public static final Doctor dao = new Doctor();
	
	
	public Doctor getByAccountAndPassword(String account, String password){
		
		String encodePassword = MD5.digest(password);
		
		System.out.println("encodePassword: " + encodePassword);
		
        return dao.findFirst("select * from doctor where account = ? and password = ?", account, encodePassword);
    }
	
	
	/**
	 * 获取角色信息
	 * @return
	 */
	public Role getRole() {
		return Role.dao.findById(get("roleId"));
	}
	
	/**
	 * 获取管理员的所有权限
	 * @return
	 */
	public List<Permission> getAccesses() {
		String sql = "select * from access where id in ( select accessId from role_access where roleId = ?)";
		return Permission.dao.find(sql, get("roleId"));
	}
	
	
	/**
	 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Doctor> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from blog order by id asc");
	}
}
