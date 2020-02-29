package com.bridgelabz.fundoonotes.serviceImpl;

/*
 *  author : Lavanya Manduri
 */

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.configuration.RabbitMqProducer;
import com.bridgelabz.fundoonotes.dto.LoginDetails;
import com.bridgelabz.fundoonotes.dto.MailDto;
import com.bridgelabz.fundoonotes.dto.ResetPassword;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.EmailAlreadyExists;
import com.bridgelabz.fundoonotes.exception.UserNotVerifiedException;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.UserServ;
import com.bridgelabz.fundoonotes.utility.JwtUtil;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserServImpl implements UserServ {

	private UserDetails userdetails = new UserDetails();

	@Autowired
	private MailDto mailDto;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RabbitMqProducer sendMail;

	@Autowired
	private JwtUtil jwt;
              
	Date date = new Date();
	
	/* Method for generating the UserDetails */

	public UserDetails save(UserDto user) {
		UserDetails checkMail = userRepo.findByEmail(user.getUserMail());
		log.info("mail" + checkMail);
		if (checkMail == null) {
			userdetails.setFirstName(user.getFirstName());                            
			userdetails.setLastName(user.getLastName());
			userdetails.setUserMail(user.getUserMail());
			userdetails.setMobileNumber(user.getMobileNumber());
			userdetails.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userRepo.saveData(userdetails.getFirstName(), userdetails.getLastName(), userdetails.getUserMail(),
					userdetails.getPassword(), userdetails.getMobileNumber(), date);
			mailDto.setEmail(userdetails.getUserMail());
			mailDto.setSubject("sendig by fundoo app admin click below link to verify");
			mailDto.setResponse("http://localhost:8082/checking/" + jwt.jwtGenerateToken(userdetails.getUserMail()));
			sendMail.produceMsg(mailDto);
			log.info("registered user details is" + userdetails);
			return userdetails;
		} else {
			return null;
		}
	}

	/* Method for generating the LoginDetails */
	
	@Override
	public UserDetails login(LoginDetails login) throws Exception{
		UserDetails getMail = userRepo.findByEmail(login.getEmail());
		
		if(getMail.is_Verified()) {
			
		
		log.info("login details from database " + getMail);
		try {
		if (getMail.getUserMail().equals(login.getEmail())) {
			if (getMail.is_Verified()) {
				boolean passwordCheck = bCryptPasswordEncoder.matches(login.getPassword(), getMail.getPassword());
				if (passwordCheck) {
					mailDto.setEmail(getMail.getUserMail());
					mailDto.setSubject("sendig by fundoo app admin");
					mailDto.setResponse("susccessfully login to fundoo app");
					sendMail.produceMsg(mailDto);
					return getMail;
				} else {
					mailDto.setEmail(getMail.getUserMail());
					mailDto.setSubject("sendig by fundoo app admin");
					mailDto.setResponse("login attempt failed");
					sendMail.produceMsg(mailDto);
				}
			}
			return null;
		} else {
			throw new EmailAlreadyExists("email already exists..");
		}
		}
		catch(Exception e) {
			log.error("error " + e.getMessage() + " occured while adding the email");
		}
		return null;
		}
		else {
			throw new EmailAlreadyExists(login.getEmail()+"");
		}
	}
	
	/* Method for generating the Mail Verification */

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
				throw new UserNotVerifiedException(isValidMail.getUserMail()+" not verified");
				
			}
		} catch (Exception e) {
			log.error("error " + e.getMessage() + " occured while verifying the mail");
		}
		return null;
	}
	
	/* Method for Forgot Password */

	@Override
	public UserDetails forgotPassword(String email) {
		UserDetails userMail = userRepo.findByEmail(email);
		log.info("userdetails for forgetpassword" + userMail);
		if (userMail != null) {
			if (userMail.is_Verified()) {
				mailDto.setEmail(userMail.getUserMail());
				mailDto.setSubject("sending by admin");
				mailDto.setResponse("http://localhost:8082/updatePassword/" + jwt.jwtGenerateToken(email));
				sendMail.produceMsg(mailDto);
				return userdetails;
			}
		} else {
			log.error("check the mail once for forget password");
		}
		return null;
	}

	/* Method for Updating the Password */
	
	@Override
	public boolean updatePassword(ResetPassword password, String token) {
		String email = jwt.parse(token);
		if (password.getPassword().equals(password.getCnfirmPassword())) {
			UserDetails userMail = userRepo.findByEmail(email);
			if (userMail.is_Verified()) {
				userMail.setPassword(bCryptPasswordEncoder.encode(password.getPassword()));
				userRepo.updatePassword(userMail.getPassword(), email);
				return true;
			}
			return true;
		}
		return false;
	}
	
	/* Method for Listing all the Users */

	@Override
	public List<UserDetails> getAllUsers(String str) {
		if (str.equalsIgnoreCase("admin")) {
			return userRepo.getUserList();
		}
		return null;
	}

}