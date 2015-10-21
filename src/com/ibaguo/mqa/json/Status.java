package com.ibaguo.mqa.json;

public class Status {
	int code;
	String message;
	String createAt;
	public Status(int code, String message, String createAt) {
		super();
		this.code = code;
		this.message = message;
		this.createAt = createAt;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
}
