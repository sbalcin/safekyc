package com.company.safekyc.h2.repository;

import com.company.safekyc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	User findByToken(String token);

}