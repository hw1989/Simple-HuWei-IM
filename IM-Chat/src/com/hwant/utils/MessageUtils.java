package com.hwant.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.location.BDLocation;
import com.hwant.entity.ContentEntity;

public class MessageUtils {
	// 是图片
	public static final String TYPE_IMG = "img";
	// 定位信息
	public static final String TYPE_LOC = "loc";
	// 语音信息
	public static final String TYPE_VOICE = "voice";
	// 表情图片
	public static final String TYPE_FACE = "face";

	/**
	 * 将信息转化为发送的信息
	 */
	public static String setLocation(BDLocation location) {

		StringBuilder builder = new StringBuilder("(").append(TYPE_LOC);
		builder.append(":");
		// 经度
		builder.append(location.getLongitude()).append(",");
		// 纬度
		builder.append(location.getLatitude()).append(",");
		// 地址
		builder.append(location.getAddrStr()).append(")");
		return builder.toString();
	}

	/**
	 * 将语音信息转化为发送的信息
	 */
	public static String setVoice(String voice2str) {
		StringBuilder builder = new StringBuilder("(").append(TYPE_VOICE);
		builder.append(":");
		builder.append(voice2str).append(")");
		return builder.toString();
	}

	/**
	 * 使用正则表达式解析出 (type:content)里的信息
	 * 
	 * @param content服务器返回的字符串
	 * @return 解析出的信息
	 */
	public static ContentEntity getMessageContent(String content) {
		ContentEntity entity = null;
		try {
			// Pattern pattern=Pattern.compile("(?<=\\()(.+?)(?=\\))");
			Pattern pattern = Pattern.compile("(?<=\\()(.+?)\\:(.+?)(?=\\))");
			// Pattern
			// pattern=Pattern.compile("(?<=\\()(loc|img)\\:(.+?)(?=\\))");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				entity = new ContentEntity();
				entity.setType(matcher.group(0));
				entity.setMessage(matcher.group(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
