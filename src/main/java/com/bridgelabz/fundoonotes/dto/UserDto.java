package com.bridgelabz.fundoonotes.dto;

/*
 *  author : Lavanya Manduri
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private String firstName;
	private String lastName;
	private String userMail;
	private String password;
	private String mobileNumber;

	
}

