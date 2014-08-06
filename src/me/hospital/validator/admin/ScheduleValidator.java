package me.hospital.validator.admin;

import me.hospital.util.DateUtil;
import me.hospital.util.ParamUtil;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * SaveDepartmentValidator
 */
public class ScheduleValidator extends Validator {

	protected void validate(Controller controller) {

		validateRequiredString("date", "error", "日期错误！");

		// 判断是否选择了科室
		if (ParamUtil.paramToInt(controller.getPara("depart"), -1) == -1) {
			addError("error", "请选择科室!");
		} else {

			int today = Integer.parseInt(DateUtil.getToday());
			int future = Integer.parseInt(DateUtil.getFutureDay(10));
			int date = ParamUtil.paramToInt(controller.getPara("date"), today);

			if (date <= today) {
				addError("error", "已过期");
			} else if (date > future) {
				addError("error", "未在排班时间范围内");
			}

			System.out.println("date: " + date);
			System.out.println("today: " + today);
			System.out.println("future: " + future);

		}
	}

	protected void handleError(Controller controller) {

		String actionKey = getActionKey();
		System.out.println("actionKey: " + actionKey);

		String error = controller.getAttr("error");
		controller.renderHtml("<label style='color:red'>" + error + "</label>");
	}
}
