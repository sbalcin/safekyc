package com.company.safekyc.enumeration;

public enum StatusCodes {


	UNEXPECTED_ERROR(100, "unexpected error"),
	USER_ALREADY_EXIST(101, "user already exist");


	private int code;
	private String message;

	private StatusCodes(int code, String message) {
		this.code = code;
		this.message = message;
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
	
	@Override 
	public String toString() {
		if(message == null)
			message = getStatusCode(code).getMessage();
		
		return code + " : " + message;
	}

	public static StatusCodes getStatusCode(int code) {
		for (StatusCodes statusCode : StatusCodes.values()) {
			if (statusCode.code == code) {
				return statusCode;
			}
		}
		throw new IllegalArgumentException(String.valueOf(code));
	}

}
