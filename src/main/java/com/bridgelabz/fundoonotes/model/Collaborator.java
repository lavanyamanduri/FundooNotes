package com.bridgelabz.fundoonotes.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Collaborators")
public class Collaborator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "collaborator_id")
	private Long cId;

	@Column(name = "collaborator_mail")
	private String collaborator;

	@ManyToOne
	@JoinColumn(name = "note_id")
	private Notes notes;
}