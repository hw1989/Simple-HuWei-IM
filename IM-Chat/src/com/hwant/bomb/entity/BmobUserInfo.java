package com.hwant.bomb.entity;

import cn.bmob.v3.BmobObject;

public class BmobUserInfo extends BmobObject {
    public BmobUserInfo(){
    	this.setTableName("userinfo");
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	private String userimg;
}
