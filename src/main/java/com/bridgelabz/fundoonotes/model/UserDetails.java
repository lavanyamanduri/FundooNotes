package com.bridgelabz.fundoonotes.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class UserDetails {

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "First_Name")
	private String firstName;

	@Column(name = "Last_Name")
	private String lastName;

	@Column(name = "User_Mail")
	private String userMail;

	@Column(name = "Password")
	private String password;

	@Column(name = "Mobile_Number")
	private String mobileNumber;

	@Column(name = "Created_Time")
	private Date created_Time;

	@Column(columnDefinition = "boolean default false")
	private boolean is_Verified;

}