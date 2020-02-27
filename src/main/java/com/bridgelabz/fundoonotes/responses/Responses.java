package com.bridgelabz.fundoonotes.responses;

/*
 *  author : Lavanya Manduri
 */

public class Responses {

	private String message;
	private int statusCode;
	private Object details;

	public Responses(String message, int statusCode) {
		super();
		this.message = message;
		this.statusCode = statusCode;
	}

	public Responses(String message, int statusCode, Object details) {
		super();
		this.message = message;
		this.statusCode = statusCode;
		this.details = details;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Object getObject() {
		return details;
	}

	public void setObject(Object object) {
		this.details = object;
	}

}