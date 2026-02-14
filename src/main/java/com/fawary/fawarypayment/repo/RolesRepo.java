package com.fawary.fawarypayment.repo;

import com.fawary.fawarypayment.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles, Long>{
    Optional<Roles> findByName(String name);
}
