package com.hwant.entity;

import java.io.Serializable;

import org.wind.annotation.Field;
import org.wind.annotation.Table;
import org.wind.database.DataType;

@Table(DTname = "connect", UnionKey = "jid,user")
public class ConnectInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Field(name = "jid", size = 25)
	private String jid;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// 登陆名
	@Field(name = "name")
	private String name;
	// 昵称
	@Field(name = "nickname", size = 20)
	private String nickname;
	// 组
	@Field(name = "fgroup", size = 20)
	private String group;
	// 状态
	@Field(name = "status", type = DataType.Type_Int)
	private int status;
	// 请求时间
	@Field(name = "date", size = 25)
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	// 多用户处理 登陆人的jid
	@Field(name = "user", size = 25)
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// 用户的头像
	@Field(name = "userimg", size = 25)
	private String userimg;

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

}
