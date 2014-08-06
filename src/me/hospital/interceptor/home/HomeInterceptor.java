package me.hospital.interceptor.home;

import java.text.SimpleDateFormat;
import java.util.List;

import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Post;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * DoctorInterceptor
 */
public class HomeInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		
		Controller controller = ai.getController();
		
		ai.invoke();
		
		// 读取首页要显示的医生的信息
		List<Doctor> doctors = Doctor.dao.getRecommends();
		controller.setAttr("doctors", doctors);

		// 读取首页要显示的科室的信息
		List<Department> departments = Department.dao.getRecommends();
		controller.setAttr("departments", departments);
		
		// 读取首页要显示的公告通知
		List<Post> notices = Post.dao.getNew(4, 10);
		controller.setAttr("notices", notices);

		// 读取首页要显示的就医指南
		List<Post> guides = Post.dao.getNew(8, 5);
		
		// 格式化时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = null;
		for (Post post : guides) {
			formatDate = post.getStr("time");
			post.set("time", formatter.format(Long.parseLong(formatDate)));
		}
		
		controller.setAttr("guides", guides);
		
	}
}
