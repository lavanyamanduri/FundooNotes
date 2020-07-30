package com.bridgelabz.fundoonotes.controller;

/*
 *  author: Lavanya Manduri
 */
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.NotesDto;
import com.bridgelabz.fundoonotes.dto.RemindDto;
import com.bridgelabz.fundoonotes.model.Notes;
import com.bridgelabz.fundoonotes.responses.Responses;
import com.bridgelabz.fundoonotes.service.NoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
public class NotesController {
	@Autowired
	private NoteService noteService;
	
	/* API for Note Creation */
	
	@PostMapping("/note/create")
	@ApiOperation(value = "Creation of note")
	public ResponseEntity<Responses> createNote(@RequestBody NotesDto note, @RequestHeader("token") String token) {
		Notes result = noteService.addNotes(note, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully added"+result, 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("not added", 400));
	}
	
	/* API for Color Changing */

	@PutMapping("/note/changingcolor/{token}")
	@ApiOperation(value = "Changing color of a note ")
	public ResponseEntity<Responses> changingColor(@RequestParam String color, @RequestParam Long noteId,
			@PathVariable("token") String token) {
		boolean result = noteService.changeColor(color, noteId, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("successfully changed the color", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}

	/* API for Pin And To UnPin */
	
	@PutMapping("/note/pinAndUnpin/{token}")
	@ApiOperation(value = "To pin and Upin the note")
	public ResponseEntity<Responses> changePinning(@RequestParam Long noteId, @PathVariable("token") String token) {
		int result = noteService.changingPin(noteId, token);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully unpinned..", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully pinned..", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
	
	/*  API for Archive or UnArchive */
	
	@PutMapping("note/archive-or-unarchive/{token}")
	@ApiOperation(value = "Archive or UnArchive a note")
	public ResponseEntity<Responses> chageArchieving(@RequestParam Long noteId, @PathVariable("token") String token) {
		int result = noteService.archievingStatus(noteId, token);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully unRchieved..", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("Archieved the notes..", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
	/* API for Updating the note */
	
	@PutMapping("/note/update/{token}")
	@ApiOperation(value = "To update the note")
	public ResponseEntity<Responses> updateNotes(@RequestParam Long noteId, @RequestBody NotesDto notes,
			@PathVariable("token") String token) throws IOException {
		Long result = noteService.updateNotes(notes, token, noteId);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully updated notes..", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("not added", 400));
	}
	
	/* API for Trash */
	
	@DeleteMapping("/note/trash/{token}")
	@ApiOperation(value = "To delete the note")
	public ResponseEntity<Responses> updateTrash(@RequestParam Long noteId, @PathVariable("token") String token) {
		int result = noteService.setTrash(noteId, token);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("successfully restored your details..", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("deleted the details", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
	
	/* API for Deleting the notes Permanently*/
	
	@DeleteMapping("/note/delete-permanantly/{token}")
	@ApiOperation(value = "To delete the notes permanantly")
	public ResponseEntity<Responses> deleteNotes(@RequestParam Long noteId, @PathVariable("token") String token) {
		Long result = noteService.deletePermanent(noteId, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("successfully deleted notes permanantly..", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Responses("notes not deleted something went wrong!", 400));
	}

	/* API for Remind the Notes at Particular Time */
	
	@PutMapping("/note/remind/{token}")
	@ApiOperation(value = "To Remind the not at particular time")
	public ResponseEntity<Responses> remindNotes(@RequestParam Long noteId, @RequestParam RemindDto remindDto,
			@PathVariable("token") String token) {
		Notes result = noteService.remind(remindDto, noteId, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("we will remind you at this time ", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong!", 400));
	}

	/* API for listing all the notes */
	
	@GetMapping("/note/list/{token}")
	@ApiOperation(value = "List of the notes")
	public ResponseEntity<Responses> getAllNotes(@PathVariable("token") String token) {
		List<Notes> result = noteService.getListOfNotes(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("list of notes are ", 201, result));
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("something went wrong..", 201, result));

	}
	
	/* API for the list of Notes by name */
	
	@GetMapping("/listOfNotes-ByName/{token}")
	@ApiOperation(value = "List of the notes by name")
	public ResponseEntity<Responses> sortByName(@PathVariable("token") String token) {
		List<Notes> result = noteService.sortByName(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("list of notes By Name ", 201, result));
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("something went wrong", 201, result));
	}

	/* API for the list of Notes by Date */
	
	@GetMapping("/listOfNotes-ByDate/{token}")
	@ApiOperation(value ="List of notes by date")
	public ResponseEntity<Responses> sortByDate(@PathVariable("token") String token) {
		List<LocalDateTime> result = noteService.sortByDate(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("list of notes By Date ", 201, result));
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("something went wrong", 201, result));
	}
	
	/* API for list out the archive notes */
	
	@GetMapping("/Archieve-notesList/{token}")
	@ApiOperation(value = "Archive the notes list")
	public ResponseEntity<Responses> getAllArchieve(@PathVariable("token") String token) {
		List<Notes> result = noteService.getAllarchieveNotes(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("list of all archieve notes", 201, result));
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("something went wrong", 201, result));
	}
	
	/* API for searching the note by Id */
	
	@GetMapping("/search-ById")
	@ApiOperation(value = "Search the notes by Id")
	public ResponseEntity<Responses> getNotesById(@RequestParam Long noteId) {
		Notes result = noteService.searchById(noteId);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("searched notes..", 201, result));
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("something went wrong", 201, result));
	}
}