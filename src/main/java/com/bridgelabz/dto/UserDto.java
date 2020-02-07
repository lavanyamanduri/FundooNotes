package com.bridgelabz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String mobileNumber;
	
}
