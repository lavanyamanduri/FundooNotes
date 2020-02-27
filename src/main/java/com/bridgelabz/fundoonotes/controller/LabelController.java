package com.bridgelabz.fundoonotes.controller;

/*
 * author: Lavanya Manduri 
 */
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.responses.Responses;
import com.bridgelabz.fundoonotes.service.LabelService;

import io.swagger.annotations.Api;

@Api( description = "API's for the label controller")
@RestController
public class LabelController {

	@Autowired
	private LabelService labelService;
	
	/* API for creating the Label */

	@PostMapping("/create-Label/{token}")
	public ResponseEntity<Responses> createLabel(@RequestBody LabelDto labelDto, @PathVariable("token") String token) {
		Label result = labelService.createLabel(labelDto, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully added..", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}

	/* API for Updating the Label */
	
	@PutMapping("/update-label/{token}")
	public ResponseEntity<Responses> updateLabel(@RequestParam Long id, @RequestParam String labelName,
			@PathVariable("token") String token) {
		Long result = labelService.updateLabel(id, labelName, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("successfully updated Label Name!", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
	
	/* API for Deleting the Label */
	
	@DeleteMapping("/delete-label/{token}")
	public ResponseEntity<Responses> deleteLabel(@RequestParam Long labelId, @PathVariable("token") String token) {
		Long result = labelService.deleteLabel(labelId, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Responses("successfully deleted labelName..", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
	
	/* API for Mapping with the Notes */
	
	@PostMapping("/mappingWithNotes/{token}")
	public ResponseEntity<Responses> mappingWithNotes(@RequestParam String labelName, @RequestParam Long noteId,
			@PathVariable String token) {
		Label result = labelService.mapWithNotes(labelName, noteId, token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("successfully mapped tables", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
	
	/* API for getting the List of Labels */
	
	@GetMapping("/getListOfLabels/{token}")
	public ResponseEntity<Responses> getLabelList(@PathVariable("token") String token) {
		List<Label> result = labelService.getAllLabelsList(token);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Responses("List Of labels is ", 200, result));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Responses("something went wrong..", 400));
	}
}