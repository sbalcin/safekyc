package com.company.safekyc.payload;

import lombok.Data;

import java.util.Date;

@Data
public class ActionItemResponse {

	private Long actionItemId;
	private String sourceUserName;
	private String targetUserName;
	private String actionType;
	private Date creationDate;

}