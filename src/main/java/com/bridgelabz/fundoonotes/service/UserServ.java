package com.bridgelabz.fundoonotes.service;

/*
 *  author : Lavanya Manduri
 */

import java.util.List;

import com.bridgelabz.fundoonotes.dto.LoginDetails;
import com.bridgelabz.fundoonotes.dto.ResetPassword;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.UserDetails;

public interface UserServ {
	
	/* Methods for Registration and login */
    
	UserDetails save(UserDto user);
	UserDetails login(LoginDetails login);
	UserDetails mailVerification(String token);
	UserDetails forgotPassword(String email);
	boolean updatePassword(ResetPassword password, String token);
	List<UserDetails> getAllUsers(String str);
}