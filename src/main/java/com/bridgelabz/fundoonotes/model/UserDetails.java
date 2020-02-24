package com.bridgelabz.fundoonotes.model;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User",
uniqueConstraints=@UniqueConstraint(columnNames= {"Mobile_Number","User_Mail"})
)

public class UserDetails {

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "First_Name")
	@NotEmpty(message = "Please provide a Firstname")
	private String firstName;

	@Column(name = "Last_Name")
	@NotEmpty(message = "Please provide a Lastname")
	private String lastName;

	@Column(name = "User_Mail")
	@NotEmpty(message = "Email is required")
	private String userMail;

	@Column(name = "Password")
	private String password;

	@Column(name = "Mobile_Number")
	 @NotEmpty(message = "Phone number is required")
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
             message="Mobile number is invalid")
	private String mobileNumber;

	@Column(name = "Created_Time")
	private LocalDateTime created_Time;

	@Column(columnDefinition = "boolean default false")
	private boolean is_Verified;

}