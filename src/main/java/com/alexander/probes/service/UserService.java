package com.alexander.probes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alexander.probes.entity.User;

public interface UserService {

	public List<User> findAll();

	public Page<User> findAll(Pageable pageable);

	public Optional<User> findById(Long id);

	public User findByToken(String token);
	
	public User save(User user);
	
	public void deleteById(Long Id);
	
	public Optional<User> findByEmailAndPassword(String name, String email);
}
