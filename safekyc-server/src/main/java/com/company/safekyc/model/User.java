package com.company.safekyc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

	@Id
	@GeneratedValue
	@Column(name = "user_id", nullable = false)
	private Long userId;

	private String name;
	private Date birthDate;
	private String nationality;
	private String email;
	private String phoneNumber;
	private String password;
	private String clientIp;

	private String token;
	private Date tokenExpire;
	private Date creationDate;


}