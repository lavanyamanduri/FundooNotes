package com.bridgelabz.fundoonotes.service;

/*
 *  author : Lavanya Manduri
 */

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.bridgelabz.fundoonotes.dto.NotesDto;
import com.bridgelabz.fundoonotes.dto.RemindDto;
import com.bridgelabz.fundoonotes.model.Notes;

public interface NoteService {
	
	 Notes addNotes(NotesDto notes,String token);
	 boolean changeColor(String color,Long id,String token);
	 Integer changingPin(Long noteId,String token);
	 Integer archievingStatus(Long noteId,String token);
	 Long updateNotes(NotesDto notes,String token,Long noteId) throws IOException;
	 Integer setTrash(Long noteId,String token);
	 Long deletePermanent(Long noteId,String token);
	 Notes remind(RemindDto remindDto,Long noteId,String token);
	 List<Notes> getListOfNotes(String token);
	 List<Notes> sortByName(String token);
	 List<LocalDateTime> sortByDate(String token);
	 List<Notes> getAllarchieveNotes(String token);
	 Notes searchById(Long noteId);
	
}