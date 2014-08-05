package me.hospital.config;

public class CoreConstants {

	// 每页数据条数
	public static final int PAGE_SIZE = 5;

	// 角色的类型，管理类型
	public static final int ROLE_MANAGER_TYPE = 0;

	// 角色的类型，医生类型
	public static final int ROLE_DOCTOR_TYPE = 1;

	// 搜索的结果分页
	public static final int SEARCH_PAGE = 1;

	// 非搜索的结果分页
	public static final int NOT_SEARCH_PAGE = 0;

	// 存放在session中的搜索条件
	public static final String SEARCH_SESSION_KEY = "search";

	// 上传文件的最大容量
	public static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

	// 上传文件的总目录
	public static final String ATTACHMENT_UPLOAD_PATH = "/upload/";

	// 上传文件的临时路径
	public static final String ATTACHMENT_TEMP_PATH = ATTACHMENT_UPLOAD_PATH;

	// 头像文件的保存路径
	public static final String ATTACHMENT_AVATAR_PATH = ATTACHMENT_UPLOAD_PATH + "avatar/";

	// 图片文件保存路径（用UEditor上传的图片）
	public static final String ATTACHMENT_IMAGE_PATH = ATTACHMENT_UPLOAD_PATH + "images/";

	// 排班的天数，默认为10天
	public static final int SCHEDULE_DAYS = 10;

	// 首页显示的医生的数量
	public static final int RECOMMEND_DOCTOR_SIZE = 4;

	// 首页显示的科室的数量
	public static final int RECOMMEND_DEPARTMENT_SIZE = 4;

}
