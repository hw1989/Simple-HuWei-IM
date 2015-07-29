package com.hwant.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.baidu.location.BDLocation;
import com.baidu.navisdk.util.common.ScreenUtil;
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

	public static String setFace(String face) {
		StringBuilder builder = new StringBuilder("(").append(TYPE_FACE);
		builder.append(":");
		builder.append(face).append(")");
		return builder.toString();
	}
	public static String setImage(String path) {
		StringBuilder builder = new StringBuilder("(").append(TYPE_IMG);
		builder.append(":");
		builder.append(path).append(")");
		return builder.toString();
	}
	/**
	 * 使用正则表达式解析出 (type:content)里的信息
	 * 
	 * @param content服务器返回的字符串
	 * @return 解析出的信息
	 */
	public static ArrayList<ContentEntity> getMessageContent(String content) {
		ArrayList<ContentEntity> list = new ArrayList<ContentEntity>();
		try {
			// Pattern pattern=Pattern.compile("(?<=\\()(.+?)(?=\\))");
			Pattern pattern = Pattern.compile("(?<=\\()(.+?)\\:(.+?)(?=\\))");
			// Pattern
			// pattern=Pattern.compile("(?<=\\()(loc|img)\\:(.+?)(?=\\))");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				ContentEntity entity = null;
				entity = new ContentEntity();
				entity.setType(matcher.group(1));
				entity.setMessage(matcher.group(2));
				entity.setStart(matcher.start() - 1);
				entity.setEnd(matcher.end() + 1);
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static SpannableStringBuilder getFaceContent(Context context,
			String content, ArrayList<ContentEntity> list) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		try {
			if (list != null) {
				for (ContentEntity entity : list) {
					if (!TYPE_FACE.equalsIgnoreCase(entity.getType())) {
						continue;
					}
					int id = context.getResources().getIdentifier(
							entity.getMessage(), "drawable",
							context.getPackageName());
					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), id);
					BitmapDrawable drawable = new BitmapDrawable(
							context.getResources(), bitmap);
					// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					// drawable.getIntrinsicHeight());
					drawable.setBounds(0, 0, ScreenUtil.dip2px(context, 30),
							ScreenUtil.dip2px(context, 30));
					ImageSpan span = new ImageSpan(drawable);
					builder.setSpan(span, entity.getStart(), entity.getEnd(),
							SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
		} catch (Exception ex) {

		}
		return builder;
	}

	// public static CharSequence getFaceContent(Context context,
	// String content, ArrayList<ContentEntity> list) {
	// SpannableString builder = new SpannableString(content);
	// try {
	// if (list != null) {
	// for (ContentEntity entity : list) {
	// if (!TYPE_FACE.equalsIgnoreCase(entity.getType())) {
	// continue;
	// }
	// int id = context.getResources().getIdentifier(
	// entity.getMessage(), "drawable",
	// context.getPackageName());
	// Bitmap bitmap = BitmapFactory.decodeResource(
	// context.getResources(), id);
	// BitmapDrawable drawable = new BitmapDrawable(
	// context.getResources(), bitmap);
	// // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
	// // drawable.getIntrinsicHeight());
	// drawable.setBounds(0, 0,50,50);
	// // drawable.setBounds(0, 0, ScreenUtil.dip2px(context, 30),
	// // ScreenUtil.dip2px(context, 30));
	// ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
	// builder.setSpan(span, entity.getStart(), entity.getEnd(),
	// SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
	// }
	// }
	// } catch (Exception ex) {
	//
	// }
	// return builder;
	// }
	public static SpannableStringBuilder getFaceContent(Context context,
			String content, ContentEntity entity) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		try {
			int id = context.getResources().getIdentifier(entity.getMessage(),
					"drawable", context.getPackageName());
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), id);
			BitmapDrawable drawable = new BitmapDrawable(
					context.getResources(), bitmap);
			// drawable.setBounds(0, 0,
			// drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			drawable.setBounds(0, 0, 45, 45);
			ImageSpan span = new ImageSpan(drawable);
			builder.setSpan(span, entity.getStart(), entity.getEnd(),
					SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
		} catch (Exception ex) {

		}
		return builder;
	}
}
