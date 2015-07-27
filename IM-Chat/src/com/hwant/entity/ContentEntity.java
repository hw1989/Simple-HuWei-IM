package com.hwant.entity;

import java.io.Serializable;

public class ContentEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String message;
	private int start;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	private int end;
}
