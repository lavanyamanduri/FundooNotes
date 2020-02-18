package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.model.Collaborator;
import com.bridgelabz.fundoonotes.responses.Responses;
import com.bridgelabz.fundoonotes.service.CollaboratorService;

@RestController
public class CollaboratorController {

	@Autowired
	private CollaboratorService collaboratorServ;

	@PostMapping("/AddCollaborator/")
	public ResponseEntity<Responses> insertData(@RequestParam String collaboratorMailId, @RequestParam Long noteId) {
		Collaborator result = collaboratorServ.saveData(collaboratorMailId, noteId);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("added collaborator", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong!!", 400));
	}

	@DeleteMapping("/delete-collaborator/")
	public ResponseEntity<Responses> deleteData(@RequestParam String collaboratorMailId) {
		boolean result = collaboratorServ.deleteData(collaboratorMailId);
		if (result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("deleted collaborator..!", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong!!", 400));
	}

	@GetMapping("/ListOfCollaborators/")
	public ResponseEntity<Responses> getAllCollaborators(@RequestParam Long noteId) {
		List<Collaborator> result = collaboratorServ.getListOfCollaberators(noteId);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("List Of all collaborators", 200, result));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong!!", 400));
	}
}
