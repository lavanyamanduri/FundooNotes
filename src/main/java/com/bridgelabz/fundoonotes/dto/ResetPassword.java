package com.bridgelabz.fundoonotes.dto;

import lombok.Data;


@Data
public class ResetPassword {

	private String password;
	private String cnfirmPassword;

}