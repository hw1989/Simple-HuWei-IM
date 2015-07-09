package com.hwant.entity;

import org.wind.annotation.Field;

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
}
