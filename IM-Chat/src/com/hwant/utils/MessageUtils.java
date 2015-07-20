package com.hwant.utils;

import com.baidu.location.BDLocation;

public class MessageUtils {
	/**
	 * 将信息转化为发送的信息
	 */
	public static String setLocation(BDLocation location) {
		// 纬度
		// application.getBdLocation().getLatitude();
		// 经度
		// application.getBdLocation().getLongitude();
		// 地址
		// application.getBdLocation().getAddrStr();
	    StringBuilder builder=new StringBuilder("[");
	    builder.append(location.getLongitude()).append(",");
	    builder.append(location.getLatitude()).append(",");
	    builder.append(location.getAddrStr()).append("]");
		return builder.toString();
	}
//	public String get
}
