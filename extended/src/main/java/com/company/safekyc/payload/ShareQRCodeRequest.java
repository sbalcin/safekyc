package com.company.safekyc.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ShareQRCodeRequest {
    private String token;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}