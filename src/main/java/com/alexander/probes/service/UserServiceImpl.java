package com.alexander.probes.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexander.probes.entity.User;
import com.alexander.probes.repository.RoleRepository;
import com.alexander.probes.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByToken(String token) {
		List<User> users =  userRepository.findAll();
		Optional<User> oUser = users.stream().filter(u -> u.getToken() != null).filter(u -> u.getToken().equals(token)).findFirst();
		if(!oUser.isPresent()) {
			return new User();
		} else {
			return oUser.get();
		}
	}

	@Override
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void deleteById(Long Id) {
		userRepository.deleteById(Id);
	}
	
	@Override
	@Transactional
	public Optional<User> findByEmailAndPassword(String email, String password) {
		List<User> users = userRepository.findAll();
		
		return users.stream().filter(user -> user.getEmail().equals(email)).filter(user -> user.getPassword().equals(password)).findFirst();
		
	}

}
