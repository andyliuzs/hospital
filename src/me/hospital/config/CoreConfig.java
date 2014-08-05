package me.hospital.config;

import me.hospital.interceptor.SessionInterceptor;
import me.hospital.model.Admin;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Permission;
import me.hospital.model.Post;
import me.hospital.model.PostCategory;
import me.hospital.model.Register;
import me.hospital.model.Role;
import me.hospital.model.RolePermission;
import me.hospital.model.Schedule;
import me.hospital.model.ScheduleStatus;
import me.hospital.model.User;
import me.hospital.routes.AdminRoutes;
import me.hospital.routes.HomeRoutes;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * API引导式配置
 */
public class CoreConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("db_config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));

	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add(new AdminRoutes());
		me.add(new HomeRoutes());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setShowSql(true);
		
		me.add(arp);
		
		 // 配置属性名(字段名)大小写不敏感容器工厂
        arp.setContainerFactory(new CaseInsensitiveContainerFactory());
        
        // 映射模型
		arp.addMapping("admin", Admin.class);	
		arp.addMapping("permission", Permission.class);
		arp.addMapping("department", Department.class);
		arp.addMapping("doctor", Doctor.class);
		arp.addMapping("post", Post.class);
		arp.addMapping("post_category", PostCategory.class);
		arp.addMapping("register", Register.class);
		arp.addMapping("role", Role.class);
		arp.addMapping("role_permission", "roleId", RolePermission.class);
		arp.addMapping("user", User.class);
		arp.addMapping("schedule", Schedule.class);
		arp.addMapping("schedule_status", "departmentId", ScheduleStatus.class);
		
		
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
		me.add(new SessionInterceptor());
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}
}
