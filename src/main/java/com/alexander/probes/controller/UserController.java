package com.alexander.probes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alexander.probes.entity.Role;
import com.alexander.probes.entity.User;
import com.alexander.probes.service.RoleServiceImpl;
import com.alexander.probes.service.UserServiceImpl;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private RoleServiceImpl roleService;
	
	@RequestMapping(value="/{id}/addUser", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public ResponseEntity<Object> create (@RequestBody User user, @PathVariable(name="id") String id) throws JSONException{
		
		User admin = userService.findByToken(user.getToken());
		if (admin.getId() != Long.parseLong(id) || admin.getId() == null) {
			String data = new JSONObject()
					.put("success", false)
					.put("message", "You don't have permissions to create users").toString();
			return new ResponseEntity<Object>(data, HttpStatus.NOT_FOUND);
		} else {
			
			List<Role> defaultRole = new ArrayList<Role>();
			User userSaved = new User();
			
			if(user.getRoles().isEmpty()) {
				defaultRole.add(new Role(Long.parseLong("2"), "STANDARD"));
				userSaved = userService.save(new User(user.getName(), user.getEmail(), user.getPassword(), defaultRole, null));
			} else {
				userSaved = userService.save(new User(user.getName(), user.getEmail(), user.getPassword(), user.getRoles(), null));
			}
			
			if(userSaved.getId() != null) {
				String data = new JSONObject()
						.put("success", true)
						.put("message", "User created succesfully").toString();
				return new ResponseEntity<Object>(data, HttpStatus.OK);
			} else {
				String data = new JSONObject()
						.put("success", false)
						.put("message", "An error has ocurred").toString();
				return new ResponseEntity<Object>(data, HttpStatus.NOT_FOUND);
			}
		}
	}

	@RequestMapping(value="/{id}/users/{idToFind}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Object> findUser (Model model, 
			@PathVariable(name = "id") String currentUserId, 
			@PathVariable(name = "idToFind") String userToFindId, 
			@RequestParam(name="token") String token) throws JSONException {
		
		User admin = userService.findByToken(token);
		Optional<User> userToFind = userService.findById(Long.parseLong(userToFindId));
		
		if (!userToFind.isPresent() || admin.getId() == null) {
			String data = new JSONObject()
					.put("success", false)
					.put("message", "User not found").toString();
			return new ResponseEntity<Object>(data, HttpStatus.NOT_FOUND);
		} else {
			User user = userToFind.get();
			String data = new JSONObject()
					.put("success", true)
					.put("message", "User found")
					.put("userToFind", user.getId()).toString();
			return new ResponseEntity<Object>(data, HttpStatus.OK);		
		}
	}

	@RequestMapping(value="/{id}", method = RequestMethod.DELETE, consumes = "application/json", produces="application/json")
	public ResponseEntity<Object> delete (@RequestBody User user) throws JSONException{
		
		User admin = userService.findByToken(user.getToken());
		Optional<User> oUser = userService.findById(user.getId());
		
		if (!oUser.isPresent() || admin.getId() == null) {
			String data = new JSONObject()
					.put("success", false)
					.put("message", "An error has occurred").toString();
			return new ResponseEntity<Object>(data, HttpStatus.NOT_FOUND);
		} else {
			
			userService.deleteById(oUser.get().getId());
			String data = new JSONObject()
					.put("success", true)
					.put("message", "The user has been deleted succesfully").toString();
			return new ResponseEntity<Object>(data, HttpStatus.OK);		
		}
	}
	
	@GetMapping("/{id}/users")
	public String panel(Model model, @PathVariable(name = "id") String userId, @RequestParam(name="token") String token){
		
		Long id = Long.parseLong(userId);
		User user = userService.findByToken(token);
		
		if(user.getId() == null || user.getId() != id) {
			return "redirect:/login?logOut";
		} else {
			List<User> users = userService.findAll();
			Boolean isAdmin = user.getRoles().stream().filter(r -> r.getName().equals("ADMIN")).findFirst().isPresent();
			model.addAttribute("users", users);
			model.addAttribute("isAdmin", (isAdmin ? true : false));
			model.addAttribute("currentUser", user);
			return "users";
		}
	}
	
	@GetMapping("/{id}/addUser")
	public String addUser(Model model, @PathVariable(name="id") String id, @RequestParam(name="token") String token ) {
		Long userId = Long.parseLong(id);
		User user = userService.findByToken(token);
		
		if(user.getId() == null || user.getId() != userId) {
			return "redirect:/login?logOut";
		} else {
			if(!user.getRoles().stream().filter(r -> r.getName().equals("ADMIN")).findFirst().isPresent()) {
				return "redirect:/login?noPermissions";				
			} else {				
				List<Role> roles = roleService.findAll();
				model.addAttribute("isAdmin", true);
				model.addAttribute("currentUser", user);
				model.addAttribute("roles", roles);
				return "addUser";
			}
		}
	}
}
