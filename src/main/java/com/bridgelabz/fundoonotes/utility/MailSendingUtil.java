package com.bridgelabz.fundoonotes.utility;

/*
 *  author : Lavanya Manduri
 */

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.MailDto;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class MailSendingUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	JavaMailSender mailSender;

	public void sendMail(MailDto mailDto) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(mailDto.getEmail());
			message.setSubject(mailDto.getSubject());
			message.setText(mailDto.getResponse());
			mailSender.send(message);
		} catch (Exception e) {
			log.error("error " + e.getMessage() + " occured while sending the mail");
		}
	}
}