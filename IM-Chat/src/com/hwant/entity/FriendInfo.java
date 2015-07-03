package com.hwant.entity;

import java.io.Serializable;

import org.wind.annotation.Field;
import org.wind.annotation.Table;
import org.wind.database.DataType;

@Table(DTname = "FriendInfo")
public class FriendInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Field(name = "jid", size = 20)
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

}
