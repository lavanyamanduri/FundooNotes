package com.bridgelabz.fundoonotes.service;

/*
 *  author : Lavanya Manduri
 */

import java.util.List;

import com.bridgelabz.fundoonotes.model.Collaborator;

public interface CollaboratorService {

	Collaborator saveData(String collaboratorMail,Long noteId);
	
	boolean deleteData(String collaboratorMail);
	List<Collaborator> getListOfCollaberators(Long noteId);
}