package com.company.safekyc.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ActionResponse {

	private String result;
	private List<ActionItemResponse> actionItemResponses;

}