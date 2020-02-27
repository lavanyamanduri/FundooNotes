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

import com.bridgelabz.fundoonotes.model.Label;

@Repository
@Transactional
public interface LabelRepository extends JpaRepository<Label, Long> {

	@Query(value = "select * from label where label_name=? AND user_id=?",nativeQuery = true)
	Label findByLabeName(String label_name,Long user_id );
	
	@Query(value="select * from label where label_id=? AND user_id=?",nativeQuery = true)
	Label findByLabelId(Long labelId,Long userId);
	
	@Modifying
	@Query(value = "insert into label(label_name,user_id) values(?,?)",nativeQuery = true)
	void insertData(String label_name,Long user_id);
	
	@Modifying
	@Query(value = "update label set label_name=? where label_id=? AND user_id=?",nativeQuery = true)
	void updateData(String labelName,Long label_id,Long user_id);
	
	@Modifying
	@Query(value="delete from label where label_id=? AND user_id=?",nativeQuery = true)
	void deleteData(Long labelId,Long user_id);
	
	@Query(value = "select * from note_labels where label_id=? AND note_id=?",nativeQuery = true)
	Object findByLabelIdAndNoteId(Long label_id,Long note_id);
	
	@Modifying
	@Query(value="insert into note_labels(note_id,label_id) values(?,?)",nativeQuery = true)
	void insertMappingData(Long note_id,Long label_id);
	
	@Query(value="select * from label where user_id=?",nativeQuery = true)
	List<Label> getAllLabels(Long user_id);
	
	@Query(value="select * from label where label_id=(select Max(label_id) from notes)",nativeQuery = true)
	Label selectLastLabel();

}