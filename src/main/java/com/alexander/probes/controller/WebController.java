package com.alexander.probes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alexander.probes.entity.User;
import com.alexander.probes.service.UserServiceImpl;

@Controller
public class WebController {
	
	@Autowired
	private UserServiceImpl userService;
	
	
}
