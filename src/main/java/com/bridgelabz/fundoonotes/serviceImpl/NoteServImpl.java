package com.bridgelabz.fundoonotes.serviceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.NotesDto;
import com.bridgelabz.fundoonotes.dto.RemindDto;
import com.bridgelabz.fundoonotes.model.Notes;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.repository.NotesRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.ElasticSearchService;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteServImpl implements NoteService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwt;

	@Autowired
	private ElasticSearchService elasticSearchService;

	Notes noteModel = new Notes();

	LocalDate currentDate = LocalDate.now();
	LocalTime currentTime = LocalTime.now();

	LocalDateTime dateTime = LocalDateTime.of(currentDate, currentTime);

	@SuppressWarnings("unused")
	@Cacheable(value = "note", key = "#result.getNoteId()", condition = "#result!=null")
	public Notes addNotes(NotesDto notes, String token) {
		String tokenDetail = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(tokenDetail);
		log.info("creating the notes for " + user.getUserMail());
		Long id = user.getId();
		if (user != null) {
			noteModel.setPin(false);
			noteModel.setTitle(notes.getNoteTitle());
			noteModel.setContent(notes.getContent());
			noteModel.setUser(user);
			notesRepository.insertNotes(noteModel.getTitle(), noteModel.getContent(), dateTime, id);
			Notes notes2 = notesRepository.selectLastNotes();
			elasticSearchService.createData(notes2);
			return notes2;
		}
		return null;

	}

	@Override
	public boolean changeColor(String color, Long noteId, String token) {
		String token1 = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(token1);
		log.info("changing the note color for this note "+noteId);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note != null) {
			notesRepository.updateNoteColor(color, noteId, id);
			return true;
		}
		return false;
	}

	@Override
	public Integer changingPin(Long noteId, String token) {
		String parseToken = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(parseToken);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note.isPin()) {
			notesRepository.updatePin(false, noteId, id);
			return 1;
		} else if (!note.isPin()) {
			notesRepository.updatePin(true, noteId, id);
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public Integer archievingStatus(Long noteId, String token) {
		String parseToken = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(parseToken);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note.isArchieve()) {
			notesRepository.updateArchieve(false, noteId, id);
			return 1;
		} else if (!note.isArchieve()) {
			notesRepository.updatePin(false, noteId, id);
			notesRepository.updateArchieve(true, noteId, id);
			return 0;
		}
		return -1;
	}

	@CachePut(value = "note", key = "#noteId", condition = "#result!=null")
	@Override
	public Long updateNotes(NotesDto notes, String token, Long noteId) throws IOException {
		String parseToken = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(parseToken);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note != null) {
			note.setTitle(notes.getNoteTitle());
			note.setContent(notes.getContent());
			notesRepository.updateNotes(note.getTitle(), note.getContent(), noteId, id);
			String elasticId = String.valueOf(noteId);
			elasticSearchService.updateById(elasticId, note);
			return noteId;
		}
		return null;
	}

	@Override
	public Integer setTrash(Long noteId, String token) {
		String parseToken = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(parseToken);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note.isTrash()) {
			notesRepository.updatePin(false, noteId, id);
			notesRepository.updateTrash(false, noteId, id);
			return 1;
		} else if (!note.isTrash()) {
			notesRepository.updatePin(false, noteId, id);
			notesRepository.updateTrash(true, noteId, id);
			return 0;
		}
		return -1;
	}

	@CacheEvict(value = "note", key = "#noteId", condition = "#result!=null")
	@Override
	public Long deletePermanent(Long noteId, String token) {
		String parseToken = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(parseToken);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note != null) {
			if (note.isTrash()) {
				notesRepository.deleteNotes(noteId, id);
				String elasticId = String.valueOf(noteId);
				elasticSearchService.deleteById(elasticId);
				return noteId;
			}
			return null;
		}
		return null;
	}

	@Override
	public Notes remind(RemindDto remindDto, Long noteId, String token) {
		String tokenDetail = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(tokenDetail);
		Long id = user.getId();
		Notes note = notesRepository.findByNoteId(noteId);
		if (note != null) {
			note.setRemindAt(remindDto.getRemindTime());
			note.setRemind(remindDto.getRemindMe());
			notesRepository.updateRemind(note.getRemindAt(), note.getRemind(), noteId, id);
			return note;
		}
		return null;
	}

	@Override
	public List<Notes> getListOfNotes(String token) {
		String tokenDetail = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(tokenDetail);
		if (user != null) {
			Long id = user.getId();
			return notesRepository.getAllNotes(id);
		}
		return null;
	}

	@Override
	public List<Notes> sortByName(String token) {
		String tokenDetail = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(tokenDetail);
		if (user != null) {
			Long id = user.getId();
			List<Notes> notes = notesRepository.getAllNotesByName(id);
			notes.stream().map(note -> note.getTitle()).collect(Collectors.toList());
			return notes;
		}
		return null;
	}

	@Override
	public List<LocalDateTime> sortByDate(String token) {
		String tokenDetail = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(tokenDetail);
		if (user != null) {
			Long id = user.getId();
			List<Notes> notes = notesRepository.getAllNotes(id);

			return notes.stream().map(note -> note.getCreatedAt()).sorted(Comparator.reverseOrder())
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public List<Notes> getAllarchieveNotes(String token) {
		String tokenDetail = jwt.parse(token);
		UserDetails user = userRepo.findByEmail(tokenDetail);
		if (user != null) {
			Long id = user.getId();
			return notesRepository.selectArchieve(id);
		}
		return null;
	}

	@Override
	public Notes searchById(Long noteId) {
		Notes note = notesRepository.findByNoteId(noteId);
		if (note != null) {
			String searchId = String.valueOf(note.getNoteId());
			elasticSearchService.searchById(searchId);
			return note;
		}
		return null;
	}

}