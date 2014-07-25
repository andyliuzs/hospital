package me.hospital.config;

import java.io.File;

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
	
	// 头像文件的保存路径
	public static final String ATTACHMENGT_AVATAR_PATH = File.separator + "upload" + File.separator + "avatar" + File.separator;
	
}
