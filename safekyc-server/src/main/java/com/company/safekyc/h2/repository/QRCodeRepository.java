package com.company.safekyc.h2.repository;

import com.company.safekyc.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {

	List<QRCode> findByUserUserId(Long userId);
	QRCode findByUuid(String uuid);
}