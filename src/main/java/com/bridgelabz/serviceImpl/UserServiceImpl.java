package com.bridgelabz.serviceImpl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.configuration.RabbitMqProducer;
import com.bridgelabz.dto.LoginDetails;
import com.bridgelabz.dto.MailDto;
import com.bridgelabz.dto.ResetPassword;
import com.bridgelabz.dto.UserDto;
import com.bridgelabz.model.UserDetails;
import com.bridgelabz.repository.UserRepository;
import com.bridgelabz.service.UserService;
import com.bridgelabz.utility.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private UserDetails userdetails=new UserDetails();
	
	@Autowired
	private MailDto mailDto;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RabbitMqProducer sendMail;
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private JwtUtil jwt;
	
	Date date=new Date();
	
	@Override
	public UserDetails save(UserDto user) {
		UserDetails checkMail = userRepo.findByEmail(user.getEmail());
		log.info("mail" + checkMail);
		if (checkMail == null) {
			userdetails.setFirstname(user.getFirstname());                            
			userdetails.setLastname(user.getLastname());
			userdetails.setEmail(user.getEmail());
			userdetails.setMobileNumber(user.getMobileNumber());
			userdetails.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userRepo.saveData(userdetails.getFirstname(), userdetails.getLastname(), userdetails.getEmail(),
					userdetails.getPassword(), userdetails.getMobileNumber(), date);
			mailDto.setEmail(userdetails.getEmail());
			mailDto.setSubject("Sendig by fundoo app admin click below link to verify");
			mailDto.setResponse("http://localhost:8080/checking/" + jwt.jwtGenerateToken(userdetails.getEmail()));
			sendMail.produceMsg(mailDto);
			log.info("registered user details is" + userdetails);
			return userdetails;
		} else {
			return null;
		}
		
	}

	@Override
	public UserDetails login(LoginDetails login) {
		
		UserDetails getMail = userRepo.findByEmail(login.getEmail());
		log.info("Login Details" + 	getMail);  
		
		if(getMail.getEmail().equals(login.getEmail())) {
		
			if(getMail.is_Verified()) {
				boolean passwordCheck=bCryptPasswordEncoder.matches(login.getPassword(),getMail.getPassword());
				if(passwordCheck) {
					mailDto.setEmail(getMail.getEmail());
					mailDto.setSubject("Sending...");
					mailDto.setResponse("Sucessfully login.");
					return getMail;
				}
				else {
					mailDto.setEmail(getMail.getEmail());
					mailDto.setSubject("Sending...");
					mailDto.setResponse(" Login Failed ");
					sendMail.produceMsg(mailDto);
				}
			}
			return null;
		}
		else {
			return null;
		}
	}

	@Override
	public UserDetails mailVerification(String token) {
		try {
			String mail = jwt.parse(token);
			log.info("parsing the token to mail " + mail);
			UserDetails isValidMail = userRepo.findByEmail(mail);
			if (!isValidMail.is_Verified()) {
				userRepo.updateIsVerified(mail);
				return userdetails;
			} else {
				log.info("user already verified");
				return userdetails;
			}
		} catch (Exception e) {
			log.error("error " + e.getMessage() + " occured while verifying the mail");
		}
		return null;
		
	}

	@Override
	public UserDetails forgotPassword(String email) {
		UserDetails userMail = userRepo.findByEmail(email);
		log.info("Login Info for ForgotPassword " + userMail);
		if(userMail!=null) {
			if(userMail.is_Verified()) {
				mailDto.setEmail(userMail.getEmail());
				mailDto.setResponse("http://localhost:8080/checking/" + jwt.jwtGenerateToken(email));
				sendMail.produceMsg(mailDto);
				return userdetails;
			}
			else {
				log.error("Check mail");
			}
		}	
		
		return null;
	}

	@Override
	public boolean updatePassword(ResetPassword password, String token) {
		String email=jwt.parse(token);
		if(password.getPassword().equals(password.getConfirmPassword())) {
			UserDetails mail=userRepo.findByEmail(email);
			if(mail.is_Verified()) {
				mail.setPassword(bCryptPasswordEncoder.encode(password.getPassword()));
				userRepo.updatePassword(mail.getPassword(),email);
				return true;
			}
			return true;
		}
		return false;
	}

	@Override
	public List<UserDetails> getAllUsers(String str) {
			if(str.equalsIgnoreCase("admin")) {
				return userRepo.getUserList();
			}
		return null;
	}
	
}
