package com.company.safekyc.type;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SignUpRequest {
    private String name;
    private Date birthDate;
    private String nationality;
    private String email;
    private String phoneNumber;
    private String password;
    private String clientIp;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}