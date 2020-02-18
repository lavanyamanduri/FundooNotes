package com.bridgelabz.fundoonotes.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.model.Collaborator;
import com.bridgelabz.fundoonotes.model.Notes;
import com.bridgelabz.fundoonotes.repository.CollaboratorRepository;
import com.bridgelabz.fundoonotes.repository.NotesRepository;
import com.bridgelabz.fundoonotes.service.CollaboratorService;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

	@Autowired
	private NotesRepository noteRepos;
	
	@Autowired
	private CollaboratorRepository collaboRepo;
	
	@Override
	public Collaborator saveData(String collaboratorMail,Long noteId) {
     Notes notes = noteRepos.findByNoteId(noteId);
     if(notes!=null) {
    	 Collaborator collaborator = collaboRepo.findByMail(collaboratorMail, noteId);
    	 if(collaborator==null) {
    		 Collaborator collaboratorModel = new Collaborator();
    		 collaboratorModel.setCollaborator(collaboratorMail);
    		 collaboratorModel.setNotes(notes);
    		collaboRepo.insertData(collaboratorModel.getCollaborator(), noteId);
    		return collaboratorModel;
    	 }
     }
		return null;
	}

	@Override
	public boolean deleteData(String collaboratorMail) {
      Collaborator collaborator = collaboRepo.findByCmail(collaboratorMail);
      if(collaborator!=null) {
    	  Long cId = collaborator.getCId();
    	  collaboRepo.deleteData(cId);
    	  return true;
      }
		return false;
	}

	@Override
	public List<Collaborator> getListOfCollaberators(Long noteId) {
       Notes notes = noteRepos.findByNoteId(noteId);
       if(notes!=null) {
    	   return collaboRepo.getAllCollaborators(noteId);
       }
		return null;
	}

	
}