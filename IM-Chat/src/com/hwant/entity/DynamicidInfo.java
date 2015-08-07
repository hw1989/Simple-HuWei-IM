package com.hwant.entity;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

public class DynamicidInfo extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DynamicidInfo() {
		this.setTableName("dynamic");
	}

//	private String objid;
//
//	public String getObjid() {
//		return objid;
//	}
//
//	public void setObjid(String objid) {
//		this.objid = objid;
//	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	private String content;
	private ArrayList<String> images;
	private String userid;
	private String createtime;
}
