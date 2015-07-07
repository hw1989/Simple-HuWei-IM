package com.hwant.entity;

import java.io.Serializable;

import org.wind.annotation.Field;
import org.wind.annotation.Table;
import org.wind.database.DataType;

@Table(DTname = "chatmessage")
public class ChatMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 来自谁的消息
	@Field(name = "mfrom")
	private String mfrom;

	public String getMfrom() {
		return mfrom;
	}

	public void setMfrom(String mfrom) {
		this.mfrom = mfrom;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMto() {
		return mto;
	}

	public void setMto(String mto) {
		this.mto = mto;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMgroup() {
		return mgroup;
	}

	public void setMgroup(String mgroup) {
		this.mgroup = mgroup;
	}

	// 消息内容
	@Field(name = "message")
	private String message;
	// 给谁发送消息
	@Field(name = "mto")
	private String mto;
	// 是否已读
	@Field(name = "read", type = DataType.Type_Int)
	private boolean read;
	// 消息的时间 
	@Field(name = "time",type=DataType.TYPE_Real)
	private String time;
	// 组消息
	@Field(name = "mgroup",size=20)
	private String mgroup;
}