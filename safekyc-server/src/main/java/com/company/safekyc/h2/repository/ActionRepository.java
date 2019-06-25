package com.company.safekyc.h2.repository;

import com.company.safekyc.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

	Action findByActionId(Long actionId);
	List<Action> findBySourceUserUserId(Long sourceUserId);
	List<Action> findByTargetUserUserId(Long targetUserId);

}