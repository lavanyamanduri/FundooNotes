package com.bridgelabz.fundoonotes.dto;

/*
 *  author : Lavanya Manduri
 */

import lombok.Data;


@Data
public class ResetPassword {

	private String password;
	private String cnfirmPassword;

}