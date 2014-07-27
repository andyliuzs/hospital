package me.hospital.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Schedule model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@SuppressWarnings("serial")
public class Schedule extends Model<Schedule> {
	public static final Schedule dao = new Schedule();

	/**
	 * 根据医生Id和日期返回排班信息
	 * 
	 * @param doctorId
	 * @param date
	 * @return
	 */
	public Schedule getSchedule(String doctorId, String date) {
		return dao.findFirst("select * from schedule where doctorId = ? and date = ?", doctorId,
				date);
	}

	public Page<Schedule> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from schedule order by id asc");
	}
}
