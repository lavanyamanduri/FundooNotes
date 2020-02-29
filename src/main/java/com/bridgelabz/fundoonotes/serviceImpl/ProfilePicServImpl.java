package com.bridgelabz.fundoonotes.serviceImpl;

/*
 *  author : Lavanya Manduri
 */

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgelabz.fundoonotes.model.ProfilePic;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.repository.ProfilePicRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.ProfilePicService;
import com.bridgelabz.fundoonotes.utility.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfilePicServImpl implements ProfilePicService {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.bucket.name}")
	private String bucketName;

	@Autowired
	private JwtUtil jwt;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProfilePicRepository profileRepo;

	/* Method for Uploading the Profile Picture */
	
	@Override
	public ProfilePic uploadProfilePic(MultipartFile multiFile, String fileName, String token) {
		String getfile = multiFile.getOriginalFilename();
		try {
			String tokenId = jwt.parse(token);
			UserDetails user = userRepo.findByEmail(tokenId);
			Long id = user.getId();
			if (user != null) {
				// creating the file in the server temporarily
				File file = new File(fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(multiFile.getBytes());
				fos.close();
	
				ProfilePic picture = new ProfilePic();
				picture.setProfileName(getfile);
				picture.setUser(user);
		
				profileRepo.saveData(picture.getProfileName(), id);
				Long pid = profileRepo.getPicByUser(id);
				PutObjectRequest objectRequest = new PutObjectRequest(bucketName, pid + getfile, file);
				amazonS3.putObject(objectRequest);
				
				// delete the file from the server
				file.delete();
				return picture;
			}
		} catch (Exception e) {
			log.error("error "+e.getMessage()+" occured while uploading the picture");
		}
		return null;
	}

	/* Method for Deleting the Profile */
	@Override
	public boolean deleteFileName(Long profileId) {
		try {
			ProfilePic profile = profileRepo.searchById(profileId);
			if (profile != null) {
				String fileName = profile.getProfileName();
				amazonS3.deleteObject(new DeleteObjectRequest(bucketName, profileId+fileName));
				profileRepo.deleteData(profileId);
				return true;
			}
		} catch (Exception e) {
			log.error("error [" + e.getMessage() + "] occured while removing file");
		}
		return false;
	}


	
}