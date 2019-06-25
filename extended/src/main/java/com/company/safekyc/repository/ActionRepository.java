package com.company.safekyc.repository;

import com.company.safekyc.model.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {

	Action findByActionId(Long actionId);
	Page<Action> findBySourceUserId(Long sourceUserId, Pageable pageable);
	Page<Action> findByTargetUserId(Long targetUserId, Pageable pageable);

}

