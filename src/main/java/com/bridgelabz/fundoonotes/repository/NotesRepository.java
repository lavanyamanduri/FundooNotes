package com.bridgelabz.fundoonotes.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.Notes;

@Repository
@Transactional
public interface NotesRepository extends JpaRepository<Notes, Long> {

	@Modifying
	@Query(value = "insert into notes(title,content,created_time,user_id) values(?,?,?,?)", nativeQuery = true)
	void insertNotes(String title, String content, LocalDateTime created_time, Long id);

	@Query(value = "select * from notes where note_id=?", nativeQuery = true)
	Notes findByNoteId(Long note_id);

	@Modifying
	@Query(value = "update notes set color=? where note_id=? AND uesr_id=?", nativeQuery = true)
	void updateNoteColor(String color, Long note_id, Long uesr_id);

	@Modifying
	@Query(value = "update notes set pin_note=? where note_id=? AND uesr_id=?", nativeQuery = true)
	void updatePin(boolean pin_note, Long note_id, Long user_id);

	@Modifying
	@Query(value = "update notes set archiev_note=? where note_id=? AND uesr_id=?", nativeQuery = true)
	void updateArchieve(boolean archiev_note, Long note_id, Long user_id);

	@Modifying
	@Query(value = "update notes set title=?,content=? where note_id=? AND uesr_id=?", nativeQuery = true)
	void updateNotes(String title, String content, Long note_id, Long uesr_id);

	@Modifying
	@Query(value = "update notes set delete_note=? where  note_id=? AND uesr_id=?", nativeQuery = true)
	void updateTrash(boolean delete_note, Long note_id, Long user_id);

	@Modifying
	@Query(value = "delete from notes where note_id=? AND uesr_id=?", nativeQuery = true)
	void deleteNotes(Long note_id, Long user_id);

	@Modifying
	@Query(value = "update notes set remind_at=?,remind_me=? where note_id=? AND uesr_id=? ", nativeQuery = true)
	void updateRemind(LocalDateTime remind_at, String remind_me, Long note_id, Long user_id);

	@Query(value = "select * from notes where uesr_id=?", nativeQuery = true)
	List<Notes> getAllNotes(Long user_id);

	@Query(value = "select * from notes where uesr_id=? ORDER BY title", nativeQuery = true)
	List<Notes> getAllNotesByName(Long user_id);

	@Query(value = "select * from notes where note_id=(select Max(note_id) from notes)", nativeQuery = true)
	Notes selectLastNotes();

	@Query(value = "select * from notes where archiev_note = true AND uesr_id=? ", nativeQuery = true)
	List<Notes> selectArchieve(Long id);

}