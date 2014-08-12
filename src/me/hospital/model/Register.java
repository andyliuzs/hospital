package me.hospital.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Register model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@SuppressWarnings("serial")
public class Register extends Model<Register> {
	public static final Register dao = new Register();

	public Page<Register> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *",
				"from register order by id asc");
	}

	public Register getRegister(String userId, String doctorId, String date) {
		return dao
				.findFirst(
						"select * from register where userId = ? and doctorId=? and date = ?",
						userId, doctorId, date);
	}

	public Page<Register> paginateSig(int pageNumber, int pageSize,
			String userId) {
		return dao.paginate(pageNumber, pageSize, "select *",
				"from register where userId = ? ", userId);
	}

	public Page<Register> paginateForDoctor(int pageNumber, int pageSize,
			String doctorId, String verify) {
		// 状态0待审核 1未通过 2通过 3处理
		// 审核状态（0:待审核，1:通过，2:未通过，3:用户取消预约）
		return dao.paginate(pageNumber, pageSize, "select *",
				"from register where doctorId = ? and verify = ?", doctorId,
				verify);
	}

	/**
	 * 获取科室
	 */
	public Department getDepartment() {
		return Department.dao.findById(get("departmentId"));
	}

	/**
	 * 获取医生姓名
	 */
	public Doctor getDoctor() {
		return Doctor.dao.findById(get("doctorId"));
	}

	/**
	 * 获取用户姓名
	 * 
	 */
	public User getUser() {
		return User.dao.findById(get("userId"));
	}
}
