package com.hwant.common;

public class RecevierConst {
	// 网络关闭
	public static final String Net_Stauts_Failure = "com.hw.net.failure";
	// 网络打开
	public static final String Net_Stauts_OK = "com.hw.net.ok";
	// 广播
	public static final String Server_Root = "com.hw.server";
	// 连接服务器
	// public static final String Server_Connect = Server_Root + ".connect";
	// 连接服务器
	// public static final String Server_Login = Server_Root + ".login";
	// 接收到单聊的信息
	@Deprecated
	public static final String Chat_One_Get = "com.hw.chat.chat";
	// 接收到群聊的信息
	@Deprecated
	public static final String Chat_Group_Get = "com.hw.chat.group";
	// 通知从数据库中获取
	public static final String Chat_DB_Get = "com.hw.chat.db";
	// 个人信息改变的广播
	public static final String User_Info_Icon = "com.hw.chat.userinfo.icon";
	// 好友申请
	public static final String Connect_Subscribe = "com.hw.connect.subscribe";
	// 同意添加为好友
	public static final String Connect_Subscribed = "com.hw.connect.subscribed";
	// 拒绝添加联系人
	public static final String Connect_Unsubscribe = "com.hw.connect.unsubscribe";
	// 删除联系人
	public static final String Connect_Unsubscribed = "com.hw.connect.unsubscribed";
	// 联系人离线
	public static final String Connect_Unavailable = "com.hw.connect.unavailable";
	// 联系人zai线
	public static final String Connect_Available = "com.hw.connect.available";
}
