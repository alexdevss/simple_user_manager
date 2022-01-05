package com.alexander.probes.controller;

import java.util.Optional;
import java.util.UUID;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alexander.probes.entity.User;
import com.alexander.probes.service.UserServiceImpl;

@Controller
public class LoginController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping("/")
	public String RedirectLogin() {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String Login(Model model, 
			@RequestParam(name = "error", required = false) String error, 
			@RequestParam(name="logOut", required = false) String logOut,
			@RequestParam(name="noPermissions", required = false) String noPermissions){

		model.addAttribute("error", error);
		model.addAttribute("logOut", logOut);
		model.addAttribute("noPermissions", noPermissions);
		model.addAttribute("user", new User());
		
		return "login";
	}
	
	@RequestMapping(value="/validateLogin", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public ResponseEntity<Object> Check(@RequestBody User user) throws JSONException {

		Optional<User> oUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
		
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
			return new ResponseEntity<Object>(data, HttpStatus.NOT_FOUND);
		}
	}
}
