package com.company.safekyc.model;

import com.company.safekyc.model.audit.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QRCode extends UserDateAudit {

	@Id
	@GeneratedValue
	@Column(name = "qr_code_id", nullable = false)
	private Long qrCodeId;
	private String uuid;
	private Blob data;
	private String status;
	private String secret;
	private Date secretExpire;
	private Date creationDate;

	@ManyToOne(fetch= FetchType.LAZY )
	@JoinColumn(name="user", referencedColumnName = "id" , nullable=true , unique=false , insertable=true, updatable=true)
	private User user;


}