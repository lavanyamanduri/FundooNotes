package com.bridgelabz.fundoonotes.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.UserDetails;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserDetails, Long> {

	@Query(value = "select * from user where user_Mail=?", nativeQuery = true)
	UserDetails findByEmail(String user_mail);

	@Modifying
	@Query(value = "insert into user(first_name,last_name,user_mail,password,mobile_number,created_time)"
			+ " values(?,?,?,?,?,?)", nativeQuery = true)
	void saveData(String first_name, String last_name, String user_mail, String password, String mobile_number,
			Date created_time);

	@Modifying
	@Query(value = "update user set is_verified=true where user_mail=?", nativeQuery = true)
	void updateIsVerified(String user_mail);

	@Modifying
	@Query(value = "update user set password=? where user_mail=?", nativeQuery = true)
	void updatePassword(String password, String email);

	@Query(value = "select * from user", nativeQuery = true)
	List<UserDetails> getUserList();

}