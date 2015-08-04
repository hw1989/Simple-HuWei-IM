package com.hwant.entity;

import org.wind.annotation.Field;
import org.wind.annotation.Table;

@Table(DTname = "ugroup")
public class GroupInfo {
	// 多用户处理
	@Field(name = "user", size = 20)
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Field(name = "gname")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
