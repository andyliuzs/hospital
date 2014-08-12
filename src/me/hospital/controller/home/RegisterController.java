package me.hospital.controller.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hospital.config.CoreConstants;
import me.hospital.interceptor.home.LoginInterceptor;
import me.hospital.model.Department;
import me.hospital.model.Doctor;
import me.hospital.model.Post;
import me.hospital.model.Register;
import me.hospital.model.Schedule;
import me.hospital.model.ScheduleStatus;
import me.hospital.model.User;
import me.hospital.util.DateUtil;
import me.hospital.util.ParamUtil;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.oreilly.servlet.Base64Decoder;

/**
 * RegisterController
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@ClearInterceptor(ClearLayer.ALL)
@Before(LoginInterceptor.class)
public class RegisterController extends Controller {

	public void index() {

		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user == null) {
			redirect("/error.html");
			return;
			
		} 
		
		setAttr("user", user);
		
		int departmentId = ParamUtil.paramToInt(getPara(0), -1);

		// 读取所有的科室信息，真实医院的科室也分级，这里简化数据，不分级
		List<Department> departments = Department.dao.getDepartments();
		setAttr("departments", departments);

		// 获取未来10天，组合成list
		List<String> futureDays = DateUtil.getFutureDays(CoreConstants.SCHEDULE_DAYS);

		// 未来10天是否已经排班了
		List<String> futurePlans = new ArrayList<String>();

		boolean hasScheduled = false;
		for (String date : futureDays) {
			hasScheduled = ScheduleStatus.dao.hasScheduled(departmentId, date);
			futurePlans.add(String.valueOf(hasScheduled));
		}

		setAttr("futureDays", futureDays);
		setAttr("futurePlans", futurePlans);
		setAttr("departmentId", departmentId);
		
		
		render("/register.html");

	}
	
	
	/**
	 * 选择科室的时候，载入该科室下的排班信息
	 */
	public void getSchedule() {

		int departmentId = ParamUtil.paramToInt(getPara(0), -1);

		List<String> futureDays = DateUtil.getFutureDays(CoreConstants.SCHEDULE_DAYS);
		boolean hasScheduled = false;

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		for (String date : futureDays) {
			hasScheduled = ScheduleStatus.dao.hasScheduled(departmentId, date);
			result.put(date, hasScheduled);
		}

		renderJson("json", result);

	}
	
	
	
	/**
	 * 管理当前用户的预约
	 */
	public void manage() {
		
		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		if(user == null) {
			redirect("/error.html");
			return;
			
		} 
		String userId = String.valueOf(user.get("id"));
		
		
		int page = ParamUtil.paramToInt(getPara(1), 1);
	
		if (page < 1) {
			page = 1;
		}
	
		// 读取当前用户所有的预约
		Page<Register> registerList = Register.dao.paginateSig(page, CoreConstants.PAGE_SIZE,String.valueOf(user.get("id")));
	
		//格式化日期
		String formatDate = null;
		for (Register register : registerList.getList()) {
			formatDate = register.getStr("date");
			String formatDate2 = formatDate.substring(0,4)+"-"+formatDate.substring(4,6)+"-"+formatDate.substring(6,8);
			register.set("date", formatDate2);
		}
		//获取今天
		Date datee = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datee);
		String nowDate = calendar.get(calendar.YEAR)+"-"+(calendar.get(calendar.MONTH)+1)+"-"+calendar.get(calendar.DAY_OF_MONTH);
		//System.out.println("this date is "+nowDate);
		setAttr("user", user);
		setAttr("registerList",registerList);
		setAttr("nowDate", nowDate);
		setAttr("searchPage", CoreConstants.NOT_SEARCH_PAGE);
		render("/register_manage.html");
		
	}
	
	/**
	 * 取消预约
	 */
	public void cancel() {
		//取消预约
		Register register = Register.dao.findById(getParaToInt("registerid"));
		//rstatus代表预约状态0已预约1已取消预约
		register.set("rstatus", 1);
		
		//排班表数据加1
		Schedule schedule = Schedule.dao.getSchedule(register.get("doctorId").toString(),register.get("date").toString());
		//判断上午或者下午预约0上午1下午
		if(register.getInt("period")==0){
			schedule.set("amNum", schedule.getInt("amNum")-1);
		}else{
			schedule.set("pmNum", schedule.getInt("pmNum")-1);
		}
		register.update();
		schedule.update();
		redirect("/register/manage");
	}
	
	/**
	 * 显示预约页面
	 */
	public void show() {
		
		int departmentId = ParamUtil.paramToInt(getPara("depart"), -1);
		String date = getPara("date");

		// 查找该科室下所有的医生
		List<Doctor> doctors = Department.dao.getDoctors(departmentId);
		setAttr("doctors", doctors);

		Department department = Department.dao.findById(departmentId);
		
		setAttr("department", department);
		setAttr("date", date);
		
		render("/register_handle.html");
	}
	
	
	/**
	 * 保存预约
	 */
	
	public void save() {
//		userid,date,daya,departmentid;
		
		
		String account = getCookie("ACCOUNT");
		User user = null;
		if(!ParamUtil.isEmpty(account)) {
			account = Base64Decoder.decode(account);
			user = getSessionAttr(account);
		}
		long userId = Long.valueOf(user.get("id").toString());
		String date = getPara("date");
		String daya = getPara("daya");
		long doctorId =  Long.valueOf(getPara("doctorid").toString());
		long departmentid =  Long.valueOf(getPara("departmentid").toString());
		boolean registerIsNull = false;
		
		Register register = Register.dao.getRegister(String.valueOf(user.get("id")),String.valueOf(doctorId), date);
		Schedule schedule = Schedule.dao.getSchedule(String.valueOf(doctorId), date);
		if(schedule!=null){
			if(register!=null){
				if(register.getInt("rstatus")==0){
				renderJson("data","一位大夫一天您只能预约一次!");
				return;
				}
			}

				//如果预约表为空则初始化并记录下来
				if(register==null){
					register = new Register();
					registerIsNull = true;
				}
				
				
				register.set("userid",userId);
				register.set("date",date);
				register.set("doctorid",doctorId);
				register.set("departmentid",departmentid);
				register.set("doctorid",doctorId);
				//0待审核
				register.set("status",0);
				//0已预约1取消预约
				register.set("rstatus", 0);
				if(daya.equals("am")){
					//赋值预约时间段
					register.set("period",0);
					//更新排班表上午预约数据
					schedule.set("amNum", schedule.getInt("amNum")+1);
				}else{
					//赋值预约时间段
					register.set("period",1);
					//更新排班表下午预约数据
					schedule.set("pmNum", schedule.getInt("pmNum")+1);
				}
				schedule.update();
				if(registerIsNull==true){
					register.save();
				}else{
					register.update();
				}
				
				renderJson("data", "预约成功！");
			
		}
		
	}
	
}
