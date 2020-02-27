package com.bridgelabz.fundoonotes.repository;

/*
 *  author : Lavanya Manduri
 */

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.Collaborator;

@Repository
@Transactional
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long>{

	@Query(value="select * from collaborators where collaborator_mail=? AND note_id=?",nativeQuery = true)
	Collaborator findByMail(String collaborator_mail,Long note_id);
	
	@Query(value = "select * from collaborators where collaborator_mail=?",nativeQuery = true)
	Collaborator findByCmail(String collaborator_mail);
	
	@Modifying
	@Query(value = "insert into collaborators(collaborator_mail,note_id) values(?,?)",nativeQuery = true)
	void insertData(String collaborator_mail,Long note_id);
	
	@Modifying
	@Query(value="delete from collaborators where collaborator_id=?",nativeQuery = true)
	void deleteData(Long collaborator_id);
	
	@Query(value="select * from collaborators where note_id=?",nativeQuery = true)
	List<Collaborator> getAllCollaborators(Long noteId);
}