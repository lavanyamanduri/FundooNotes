package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.dto.LoginDetails;
import com.bridgelabz.dto.ResetPassword;
import com.bridgelabz.dto.UserDto;
import com.bridgelabz.model.UserDetails;
import com.bridgelabz.responses.Responses;
import com.bridgelabz.service.UserService;


public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/registration")
	public ResponseEntity<Responses> getDetails(@RequestBody UserDto user) {
		UserDetails result = userService.save(user);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Sucessfully register", 200, result));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Responses("User already exists", 400, result));
		}
	}	
	
	@PostMapping("/login")
	public ResponseEntity<Responses> logging(@RequestBody LoginDetails details) {
		UserDetails result=userService.login(details);
		if(result!=null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Sucessfully loged in",200,result));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("Login Failed ",400,result));
		}		
		
	}
	
	@GetMapping("/checking/{token}")
	public ResponseEntity<Responses> jwt(@PathVariable String token){
		UserDetails result= userService.mailVerification(token);
		if(result!=null){			

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Sucessfully Verified",200,result));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("User Already Verified",400,result));
		}
	}
	
	@PostMapping("/forgot/{email}")
	public ResponseEntity<Responses> forgot(@PathVariable("email") String email){
		UserDetails result=userService.forgotPassword(email);
		if(result!=null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Sucessfully sent the link...",200));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("Something went wrong",400));
		}
	}
	
	@PostMapping("/updatePassword/{token}")
	public ResponseEntity<Responses> updatePwd(@RequestBody ResetPassword password,
			@PathVariable("token") String token) {
		boolean result = userService.updatePassword(password, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Sucessfully Updated the password",200));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("Something went wrong",400));

		}
	}
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<Responses> getAllUsers(@RequestParam String typeOfUser){
		List<UserDetails> result=userService.getAllUsers(typeOfUser);
		if(result!=null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("All UserList",200,result));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("Something went wrong",400,result));
		}
	}
	
}
