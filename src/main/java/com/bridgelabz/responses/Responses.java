package com.bridgelabz.responses;

public class Responses {

	private String message;
	private int statuscode;
	private Object details;
	
	public Responses(String message,int statuscode ) {
		super();
		this.message=message;
		this.statuscode=statuscode;
	}
	
	public Responses(String message,int statuscode,Object details) {
		super();
		this.message=message;
		this.statuscode=statuscode;
		this.details=details;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public Object getObject() {
		return details;
	}
	public void setObject(Object object) {
		this.details = object;
	}
	
	
	
}
