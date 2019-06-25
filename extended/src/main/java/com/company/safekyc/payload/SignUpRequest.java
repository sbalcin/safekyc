package com.company.safekyc.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 2, max = 40)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 20)
    private String password;

    private String nationality;

    private String phoneNumber;

    private Date birthDate;

    private String clientIp;

}
