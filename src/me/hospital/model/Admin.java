package me.hospital.model;

import java.util.List;

import org.eclipse.jetty.util.security.Credential.MD5;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Admin model.

CREATE TABLE IF NOT EXISTS `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(45) NOT NULL COMMENT '管理员账号',
  `password` varchar(45) NOT NULL COMMENT '管理员密码',
  `roleId` int(11) NOT NULL COMMENT '角色ID',
  `time` varchar(45) NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_UNIQUE` (`account`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='管理员表' AUTO_INCREMENT=2 ;

数据库字段名建议使用驼峰命名规则，便于与 java 代码保持一致，如字段名： userId
 */

@SuppressWarnings("serial")
public class Admin extends Model<Admin> {
	public static final Admin dao = new Admin();
	
	
	public Admin getByAccountAndPassword(String account, String password){
		
		String encodePassword = password;//MD5.digest(password);
		
		System.out.println("encodePassword: " + encodePassword);
		
        return dao.findFirst("select * from admin where account = ? and password = ?", account, encodePassword);
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
	public List<Permission> getPermissions() {
		String sql = "select * from permission where id in ( select permissionId from role_permission where roleId = ?)";
		return Permission.dao.find(sql, get("roleId"));
	}
	
	
	
	/**
	 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Admin> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from blog order by id asc");
	}
}
