package com.bridgelabz.fundoonotes.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.LabelNameAlreadyExistsException;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Notes;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NotesRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.utility.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LabelServImpl implements LabelService {

	@Autowired
	private LabelRepository labelRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwt;

	@Autowired
	private NotesRepository noteRepo;

	@Cacheable(value = "label", key = "#result.getLabelId()", condition = "#result!=null")
	@Override
	public Label createLabel(LabelDto labelDto, String token) {
		try {
			String mail = jwt.parse(token);
			log.info("creating label for this mail " + mail);
			UserDetails user = userRepo.findByEmail(mail);
			Long userId = user.getId();
			if (user != null && user.is_Verified()) {
				String labelName = labelDto.getLabelName();
				Label label = labelRepo.findByLabeName(labelName, userId);
				if (label == null) {
					Label newLabel = new Label();
					newLabel.setLabelName(labelDto.getLabelName());
					newLabel.setLabelUser(user);
					labelRepo.insertData(newLabel.getLabelName(), userId);
					return labelRepo.selectLastLabel();
				} else {
					throw new LabelNameAlreadyExistsException("label name already exists..");
				}
			}
		} catch (Exception e) {
			log.error("error " + e.getMessage() + " occured while creating the label");
		}
		return null;
	}

	@CachePut(value = "label", key = "#id", condition = "#result!=null")
	@Override
	public Long updateLabel(Long id, String labelName, String token) {
		String mail = jwt.parse(token);
		log.info("updating the label name for this mail " + mail);
		UserDetails user = userRepo.findByEmail(mail);
		Long userId = user.getId();
		if (user != null) {
			if (user.is_Verified()) {
				Label label = labelRepo.findByLabelId(id, userId);
				Long labelId = label.getLabelId();
				if (label != null) {
					labelRepo.updateData(labelName, labelId, userId);
					return id;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	@CacheEvict(value = "label", key = "#labelId", condition = "#result!=null")
	@Override
	public Long deleteLabel(Long labelId, String token) {
		String mail = jwt.parse(token);
		log.info("deleting the label of this mail " + mail);
		UserDetails user = userRepo.findByEmail(mail);
		Long userId = user.getId();
		Label label = labelRepo.findByLabelId(labelId, userId);
		if (user != null) {
			if (user.is_Verified()) {
				if (label != null) {
					labelRepo.deleteData(labelId, userId);
					return labelId;
				}
			}
			return labelId;
		}
		return null;
	}

	@Override
	public Label mapWithNotes(String labelName, Long noteId, String token) {
		String mail = jwt.parse(token);
		log.info("mapping the label with notes " + mail);
		UserDetails user = userRepo.findByEmail(mail);
		Long userId = user.getId();
		if (user != null) {
			Notes notes = noteRepo.findByNoteId(noteId);
			if (notes != null) {
				Label label = labelRepo.findByLabeName(labelName, userId);
				if (label == null) {
					Label newLabel = new Label();
					newLabel.setLabelUser(user);
					newLabel.setLabelName(labelName);
					labelRepo.insertData(newLabel.getLabelName(), userId);
					Label findLabel = labelRepo.findByLabeName(newLabel.getLabelName(), userId);
					labelRepo.insertMappingData(noteId, findLabel.getLabelId());
					return findLabel;
				} else {
					Label labelnew = labelRepo.findByLabeName(labelName, userId);
					Long labelId = labelnew.getLabelId();
					Object map = labelRepo.findByLabelIdAndNoteId(labelId, noteId);
					if (map == null) {
						labelRepo.insertMappingData(noteId, labelId);
					}
					return label;
				}
			}
		}
		return null;
	}

	@Override
	public List<Label> getAllLabelsList(String token) {
		String mail = jwt.parse(token);
		log.info("list of all labels of this user " + mail);
		UserDetails user = userRepo.findByEmail(mail);
		if (user != null) {
			Long userId = user.getId();
			return labelRepo.getAllLabels(userId);
		}
		return null;
	}

}