package com.bridgelabz.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MailDto {

	private String email;
	private String subject;
	private String response;
}
