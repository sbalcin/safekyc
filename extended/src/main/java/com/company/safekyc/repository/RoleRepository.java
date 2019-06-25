package com.company.safekyc.repository;

import com.company.safekyc.model.Role;
import com.company.safekyc.model.RoleName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
