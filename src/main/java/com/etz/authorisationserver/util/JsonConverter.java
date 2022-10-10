package com.etz.authorisationserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	
	private JsonConverter() {}
    
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String jsonStr, Class<?> responseClassObject) {
		if("String".equalsIgnoreCase(responseClassObject.getSimpleName())) {
			return (T)jsonStr;
		}
		ObjectMapper objMapper = new ObjectMapper();
		T javaObject = null;
		try {
			javaObject = (T) objMapper.readValue(jsonStr, responseClassObject);
		} catch (JsonProcessingException e) {
			// do nothing
		}
		return javaObject;
	}
	
	public static String objectToJson(Object object) {
		ObjectMapper objMapper = new ObjectMapper();
				//.enable(SerializationFeature.INDENT_OUTPUT);
				//.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		
		String json = "";
		try {
			json = objMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// do nothing
		}
		return json;
	}
}
