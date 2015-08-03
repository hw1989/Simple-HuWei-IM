package com.hwant.common;

import java.io.File;

public class Common {
	public static final String GMAIL_SERVER = "talk.google.com";
	public static final String Service_IP = "192.168.192.20";
	public static final int Service_Port = 5222;
	public static final String SP_UserName="username"; 
	public static final String SP_UserPsw="userpsw"; 
	//工作线程池的大小
	public static final int XMPP_Thread_Pool_Size = 3;
	// 数据库名称
	public static final String DB_Name = "im_db";
	// sharepreference
	public static final String IM_Config = "config";
	// 数据库文件
	public static final String DB_File_Name = "IM_DB.db3";
	// 设置数据库查询的排列
	public static final String Table_Order_ASC = " _id asc ";
	// 域名
	public static final String DomainName = "gta-huwei6";
	// 图片存放位置
	public static final String Path_Image = File.separator + "HWim"
			+ File.separator + "image" + File.separator;
	// 媒体的缓存存放位置
	public static final String Path_Media = File.separator + "HWim"
			+ File.separator + "media" + File.separator;
	// 放缓存文件的位置
	public static final String Path_Cache = File.separator + "HWim"
			+ File.separator + "cache" + File.separator;

}
