package com.alexander.probes.controller;

import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexander.probes.entity.Role;
import com.alexander.probes.entity.User;
import com.alexander.probes.service.UserServiceImpl;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@PostMapping
	public ResponseEntity<?> create (@RequestBody User user){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
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
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update (@RequestBody User userDetails, @PathVariable Long id){
		Optional<User> oUser = userService.findById(id);
		
		if(!oUser.isPresent()){

			return ResponseEntity.notFound().build() ;
		
		} else {
			
			oUser.get().setEmail(userDetails.getEmail());
			oUser.get().setName(userDetails.getName());
			oUser.get().setPassword(userDetails.getPassword());
			oUser.get().setRoles(userDetails.getRoles());
		
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(oUser.get()));
		}
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE, consumes = "application/json", produces="application/json")
	public ResponseEntity<Object> delete (@RequestBody User user) throws JSONException{
		
		User adminUser = userService.findByToken(user.getToken());
		Optional<User> oUser = userService.findById(user.getId());
		
		if (!oUser.isPresent() || adminUser.getId() == null) {
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
			Optional<Role> isAdmin = user.getRoles().stream().filter(r -> r.getId() == 1).findFirst();
			model.addAttribute("users", users);
			model.addAttribute("isAdmin", (isAdmin.isPresent() ? true : false));
			model.addAttribute("currentUser", user);
			return "users";
		}
	}
	

}
