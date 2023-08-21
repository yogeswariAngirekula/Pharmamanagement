package com.ctel.model;

public class LoginResponse{

	private String token;

	private String message;

	public LoginResponse(String token, String message) {
		super();
		this.token = token;
		this.message = message;
	}

	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}