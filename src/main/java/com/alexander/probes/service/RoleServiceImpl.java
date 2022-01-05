package com.alexander.probes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexander.probes.entity.Role;
import com.alexander.probes.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Role> findAll(){
		return roleRepository.findAll();
	}
}
