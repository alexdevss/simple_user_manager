package com.alexander.probes.controller;

import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alexander.probes.entity.LoggedUser;
import com.alexander.probes.entity.User;
import com.alexander.probes.service.UserServiceImpl;
import com.fasterxml.jackson.databind.util.JSONPObject;

@Controller
public class LoginController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping("/")
	public String RedirectLogin() {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String Login(Model model, @RequestParam(name = "error", required = false) String error, @RequestParam(name="logOut", required = false) String logOut){
		
		//Model to save error data 
		model.addAttribute("error", error);
		model.addAttribute("logOut", logOut);
		model.addAttribute("user", new User());
		
		return "login";
	}
	
	@RequestMapping(value="/validateLogin", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<Object> Check(@RequestBody User user) throws JSONException {

		String email = user.getEmail();
		String password = user.getPassword();
		Optional<User> oUser = userService.findByEmailAndPassword(email, password);
		
		if(oUser.isPresent()) {
			
			User currentUser = oUser.get();
			Boolean isAdmin = currentUser.getRoles().stream().filter(r -> r.getName().equals("ADMIN")).findFirst().isPresent();
			String token = UUID.randomUUID().toString();
			Long userId = currentUser.getId();
			currentUser.setToken(token);
			userService.save(currentUser);
			
			String data = new JSONObject()
					.put("success", true)
					.put("userId", userId)
					.put("isAdmin", isAdmin)
					.put("token", token).toString();
			return new ResponseEntity<Object>(data, HttpStatus.OK);
			
		} else {
			String data = new JSONObject().put("success", false).toString();
			return new ResponseEntity<Object>(data, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
