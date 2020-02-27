package com.bridgelabz.fundoonotes.service;

/*
 *  author : Lavanya Manduri
 */

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.model.ProfilePic;

public interface ProfilePicService {
	


	ProfilePic uploadProfilePic(MultipartFile file,String fileName,String token);
	
	boolean deleteFileName(Long profileId);
}