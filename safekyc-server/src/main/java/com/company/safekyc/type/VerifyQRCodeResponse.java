package com.company.safekyc.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VerifyQRCodeResponse {

    private String result;
    private String name;
    private String email;
    private String phoneNumber;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}