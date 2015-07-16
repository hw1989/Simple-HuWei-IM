package com.hwant.entity;

import java.io.Serializable;

import org.wind.annotation.Field;
import org.wind.annotation.Table;
import org.wind.database.DataType;

/**
 * 多用户登录的表
 */
@Table(DTname = "usertable")
public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Field(name = "jid", size = 20, unique = true)
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Field(name = "name", size = 20)
	private String name;
	@Field(name = "nickname", size = 20)
	private String nickname;
	@Field(name = "status", type = DataType.Type_Int)
	private int status;
	// 用户头像
	@Field(name = "userimg", size = 25)
	private String userimg;

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	// 用户头像
	@Field(name = "password", size = 35)
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
