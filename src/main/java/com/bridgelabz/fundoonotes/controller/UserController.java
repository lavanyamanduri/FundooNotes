package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LoginDetails;
import com.bridgelabz.fundoonotes.dto.ResetPassword;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.responses.Responses;
import com.bridgelabz.fundoonotes.service.UserServ;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("User")
public class UserController {

		@Autowired
		private UserServ userService;
	    @ApiOperation(value = "Registration for new user")
		@PostMapping("/registration")
		public ResponseEntity<Responses> getDetails(@RequestBody @Valid UserDto user) {
			UserDetails result = userService.save(user);
			if (result != null) {
			
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Sucessfully registered", 200, result));
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new Responses("User already exists", 400, result));
				
			}
		}
		public void valid(BindingResult res) {
			
		}
		@PostMapping("/login")
	    @ApiOperation(value = "Login with the credential details")
		public ResponseEntity<Responses> logging(@RequestBody LoginDetails details) {
			UserDetails result = userService.login(details);
			if (result != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Successfully login", 200));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
		}

		@GetMapping("/verify/{token}")
	    @ApiOperation(value = "To verify a particular user ")
		public ResponseEntity<Responses> jwt( @PathVariable String token) {
			UserDetails result = userService.mailVerification(token);
			if (result != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Successfully verified", 200));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("user already verified..", 400));
		}

		@PostMapping("/forgot/{email}")
	    @ApiOperation(value = "ForgotPassword of a user")
		public ResponseEntity<Responses> forget( @PathVariable("email") String email) {
			UserDetails result = userService.forgotPassword(email);
			if (result != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Successfully sent link..", 200));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Responses("Something went wrong with this..", 400));
		}

		@PostMapping("/updatePassword/{token}")
	    @ApiOperation(value = "Updation of the user Password")
		public ResponseEntity<Responses> updatePwd( @RequestBody ResetPassword password,
				@PathVariable("token") String token) {
			boolean result = userService.updatePassword(password, token);
			if (result) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Successfully updated password", 200));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Responses("Something went wrong with this..", 400));
		}

		@GetMapping("/getAllUsers")
	    @ApiOperation(value = "Gets the list of all the user")

		public ResponseEntity<Responses> getAllUsers(@RequestParam String typeOfUser) {
			List<UserDetails> result = userService.getAllUsers(typeOfUser);
			if (result != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("all users list", 200, result));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Responses("Something went wrong with this..", 400, result));
		}
	}