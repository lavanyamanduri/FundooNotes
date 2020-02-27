package com.bridgelabz.fundoonotes.dto;

/*
 *  author : Lavanya Manduri
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotesDto {

	private String noteTitle;
	private String content;

}