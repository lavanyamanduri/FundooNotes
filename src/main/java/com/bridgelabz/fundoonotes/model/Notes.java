package com.bridgelabz.fundoonotes.model;

/*
 *  author : Lavanya Manduri
 */

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Notes")
@Document(indexName = "FundooNotes",type = "notes",shards = 1)
public class Notes {

	/* Fields required for Note creation */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noteId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "createdTime")
	private LocalDateTime createdAt;

	@Column(name = "pinNote", columnDefinition = "boolean default false")
	private boolean isPin;

	@Column(name = "archievNote", columnDefinition = "boolean default false")
	private boolean isArchieve;

	@Column(name = "color", columnDefinition = "varchar(100) default '#ffffff'")
	private String color;

	@Column(name = "remindAt")
	private LocalDateTime remindAt;

	@Column(name = "remindMe", columnDefinition = "varchar(100) default 'Does not repeat'")
	private String remind;

	@Column(name = "deleteNote", columnDefinition = "boolean default false")
	private boolean isTrash;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserDetails user;

	@JsonIgnore
	@ManyToMany
	private List<Label> labels;

}