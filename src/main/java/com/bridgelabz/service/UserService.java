package com.bridgelabz.service;

import java.util.List;

import com.bridgelabz.dto.LoginDetails;
import com.bridgelabz.dto.ResetPassword;
import com.bridgelabz.dto.UserDto;
import com.bridgelabz.model.UserDetails;

public interface UserService {

	UserDetails save(UserDto user);
	UserDetails login(LoginDetails login);
	UserDetails mailVerification(String token);
	UserDetails forgotPassword(String email);
	boolean updatePassword(ResetPassword password,String token);
	List<UserDetails> getAllUsers(String str);
}
