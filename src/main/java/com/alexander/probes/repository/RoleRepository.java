package com.alexander.probes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexander.probes.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
