package com.company.safekyc.type;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActionItemResponse {

	private Long actionItemId;
	private String sourceUserName;
	private String targetUserName;
	private String actionType;
	private Date creationDate;

}