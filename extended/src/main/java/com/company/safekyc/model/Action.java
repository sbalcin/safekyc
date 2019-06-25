package com.company.safekyc.model;

import com.company.safekyc.model.audit.UserDateAudit;
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
public class Action  extends UserDateAudit {

	@Id
	@GeneratedValue
	@Column(name = "action_id", nullable = false)
	private Long actionId;

	private String actionType;

	@ManyToOne(fetch= FetchType.LAZY , optional=false)
	@JoinColumn(name="source_user", referencedColumnName = "id" , nullable=false , unique=false , insertable=true, updatable=true)
	private User sourceUser;

	@ManyToOne(fetch= FetchType.LAZY , optional=false)
	@JoinColumn(name="source_qr_code", referencedColumnName = "qr_code_id" , nullable=false , unique=false , insertable=true, updatable=true)
	private QRCode sourceQRCode;

	@ManyToOne(fetch= FetchType.LAZY , optional=true)
	@JoinColumn(name="target_user", referencedColumnName = "id" , nullable=true , unique=false , insertable=true, updatable=true)
	private User targetUser;

	@ManyToOne(fetch= FetchType.LAZY , optional=true)
	@JoinColumn(name="target_qr_code", referencedColumnName = "qr_code_id" , nullable=true , unique=false , insertable=true, updatable=true)
	private QRCode targetQRCode;

	private Date creationDate;

}