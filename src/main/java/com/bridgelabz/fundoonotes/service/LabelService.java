package com.bridgelabz.fundoonotes.service;

import java.util.List;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;

public interface LabelService {

	Label createLabel(LabelDto labelDto, String token);
	
	Long updateLabel(Long id,String labelName,String token);
	
	Long deleteLabel(Long labelId, String token);
	
	Label mapWithNotes(String labelName,Long noteId,String token);
	List<Label> getAllLabelsList(String token);
}