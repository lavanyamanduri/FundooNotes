package com.bridgelabz.fundoonotes.service;

/*
 *  author : Lavanya Manduri
 */

import java.io.IOException;
import java.util.Map;

import com.bridgelabz.fundoonotes.model.Notes;

public interface ElasticSearchService {

	String createData(Notes notes);
	Map<String,Object> searchById(String id);
	Map<String, Object> updateById(String id,Notes notes) throws IOException;
	void deleteById(String id);
}