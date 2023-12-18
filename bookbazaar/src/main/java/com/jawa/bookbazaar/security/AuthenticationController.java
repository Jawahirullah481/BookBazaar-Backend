package com.jawa.bookbazaar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jawa.bookbazaar.exception.DuplicateUserException;
import com.jawa.bookbazaar.exception.InvalidUserException;
import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.repository.UserRepository;
import com.jawa.bookbazaar.service.UserService;

@RestController
public class AuthenticationController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/users/signup")
	public MappingJacksonValue signupUser(@RequestBody User user) {

		if (userRepository.existsByUsername(user.getUsername())) {
			throw new DuplicateUserException("Username already exists");
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new DuplicateUserException("Email already exists");
		}
		userService.addUser(user);
		
		
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "email", "role"));
		mappingJacksonValue.setFilters(filterProvider);
		return mappingJacksonValue;
	}

	@PostMapping("/users/login")
	public MappingJacksonValue loginUser(@RequestBody User user, Model model) {

		if (!(userRepository.existsByUsernameAndPassword(user.getUsername(), user.getPassword())
				|| userRepository.existsByEmailAndPassword(user.getUsername(), user.getPassword()))) {
			throw new InvalidUserException("There is no user for the requested username/email and password");
		}

		User loggedInUser = userRepository.getByUsernameOrEmail(user.getUsername(), user.getUsername());
		
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(loggedInUser);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "email", "role"));
		mappingJacksonValue.setFilters(filterProvider);
		return mappingJacksonValue;

	}

	@PostMapping("/admin/login")
	public MappingJacksonValue loginAdmin(@RequestBody User user, Model model) {

		if (!(userRepository.existsByUsernameAndPassword(user.getUsername(), user.getPassword())
				|| userRepository.existsByEmailAndPassword(user.getUsername(), user.getPassword()))) {
			throw new InvalidUserException("There is no user for the requested username/email and password");
		}

		User loggedInUser = userRepository.getByUsernameOrEmail(user.getUsername(), user.getUsername());
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(loggedInUser);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "email", "role"));
		mappingJacksonValue.setFilters(filterProvider);
		return mappingJacksonValue;
	}

	@PutMapping("/users/{userid}/userdetails")
	public ResponseEntity<Object> updateUserAccountDetails(@PathVariable int userid, @RequestBody User user) {
		User updatedUser = userService.updateUserDetails(userid, user);
		System.out.println("After update : " + updatedUser);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserAuthDetails userAuthDetails = (UserAuthDetails) authentication.getPrincipal();
		userAuthDetails.setUser(updatedUser);

		return successResponseEntity();
	}
	
	
	
	
	@PostMapping("/account")
	public ResponseEntity<Object> updateAccountDetails(@RequestBody User updateDetails) {
		UserAuthDetails userAuthDetails = (UserAuthDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int adminId = userAuthDetails.getUserId();
		User admin = userRepository.findById(adminId).get();
		
		admin.setUsername(updateDetails.getUsername());
		admin.setEmail(updateDetails.getEmail());
		admin.setPassword(updateDetails.getPassword());
		
		userAuthDetails.setUser(admin);
		
		return successResponseEntity();
	}

	private ResponseEntity<Object> successResponseEntity() {
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
