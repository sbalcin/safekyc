package com.company.safekyc.repository;

import com.company.safekyc.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {

	List<QRCode> findByUserId(Long userId);
	QRCode findByUuid(String uuid);
}