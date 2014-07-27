package me.hospital.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Department model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@SuppressWarnings("serial")
public class Department extends Model<Department> {
	public static final Department dao = new Department();

	/**
	 * 返回所有的科室信息
	 * 
	 * @return
	 */
	public List<Department> getAllDepartments() {
		return Department.dao.find("select * from department");
	}

	/**
	 * 获取当前科室主任医师的信息
	 */
	public Doctor getDirector() {
		return Doctor.dao.findById(get("directorId"));
	}

	/**
	 * 获取当前科室下所有的医生信息
	 */
	public List<Doctor> getAllDoctors() {
		return getDoctors(Integer.parseInt(String.valueOf(get("id"))));
	}

	/**
	 * 获取某科室下所有的医生信息
	 * 
	 * @param departmentId
	 * @return
	 */
	public List<Doctor> getDoctors(int departmentId) {
		return Doctor.dao.find("select * from doctor where departmentId = ?", departmentId);
	}

	public Page<Department> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from department order by id asc");
	}
}
