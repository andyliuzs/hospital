package me.hospital.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * ScheduleStatus model.
 * 
 * 所有 sql 写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

@SuppressWarnings("serial")
public class ScheduleStatus extends Model<ScheduleStatus> {
	public static final ScheduleStatus dao = new ScheduleStatus();

	/**
	 * 根据科室ID和日期来获取是否已经排班了
	 * 
	 * @param departmentId
	 * @param date
	 * @return
	 */
	public boolean hasScheduled(int departmentId, String date) {
		ScheduleStatus status = dao.findFirst(
				"select * from schedule_status where departmentId = ? and date = ?", departmentId,
				date);
		if (status != null) {
			return true;
		}
		return false;
	}

	public Page<ScheduleStatus> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from schedule_status");
	}
}
