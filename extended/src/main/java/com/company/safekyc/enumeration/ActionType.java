package com.company.safekyc.enumeration;

public enum ActionType {


	SHARE_QR_CODE("share", "Share your qr code with another user"),
	VERIFY_QR_CODE("verify", "Verify user from qr code");


	private String name;
	private String description;

	private ActionType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public static ActionType getStatusCode(String name) {
		for (ActionType statusCode : ActionType.values()) {
			if (statusCode.name == name) {
				return statusCode;
			}
		}
		throw new IllegalArgumentException(String.valueOf(name));
	}

}
