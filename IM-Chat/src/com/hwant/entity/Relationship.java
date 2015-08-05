package com.hwant.entity;

import java.io.Serializable;

import org.wind.annotation.Field;
import org.wind.annotation.Table;
import org.wind.database.DataType;

/*
 *联系人员的操作的表 
 */
@Table(DTname = "relationship")
public class Relationship implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Field(name = "mfrom", size = 20)
	private String mfrom;

	public String getMfrom() {
		return mfrom;
	}

	public void setMfrom(String mfrom) {
		this.mfrom = mfrom;
	}

	public String getMto() {
		return mto;
	}

	public void setMto(String mto) {
		this.mto = mto;
	}

	public String getMuser() {
		return muser;
	}

	public void setMuser(String muser) {
		this.muser = muser;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Field(name = "mto", size = 20)
	private String mto;
	// 所属登陆人员
	@Field(name = "muser", size = 20)
	private String muser;
	@Field(name = "nickname", size = 25)
	private String nickname;
	@Field(name = "image", size = 25)
	private String image;
	@Field(name = "date", size = 25)
	private String date;
	// 状态
	@Field(name = "state", size = 20, type = DataType.Type_Int)
	private int state;
	//发送人的对象ID
	@Field(name = "objid", size = 20)
	private String objid;
}
