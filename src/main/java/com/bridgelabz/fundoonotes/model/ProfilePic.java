package com.bridgelabz.fundoonotes.model;

/*
 *  author : Lavanya Manduri
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "profilepicture")
public class ProfilePic {

	/* Fields Required for Profile Picture */
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;

	@Column(name = "profile")
	private String profileName;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserDetails user;
}