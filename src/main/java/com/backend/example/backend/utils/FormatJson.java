package com.backend.example.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FormatJson {

	public ObjectNode createJSONResponse(String message) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseJson = mapper.createObjectNode();
		responseJson.put("message", message);
		return responseJson;
	}

}
