package com.alexander.probes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexander.probes.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
