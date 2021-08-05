package com.etz.authorisationserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class RestClientUtil {
	
	@Autowired
	private static RestTemplate restTemplate;

	
	public static <T> T get(String path, Class<?> responseClassObject) {
		return get(path, new HashMap<String, String>(), responseClassObject);
	}
	
	public static <T> T get(String path, HashMap<String, String> extraHeaders, Class<?> responseClassObject) {
		URI uri = URI.create(path);
		HttpEntity<String> requestEntity = new HttpEntity<String>(computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.GET, requestEntity, responseClassObject);
	}
	
	public static <T> T post(String path, Object requestClassOrJsonString, Class<?> responseClassObject) {
		return post(path, new HashMap<String, String>(), requestClassOrJsonString, responseClassObject);
	}
	
	public static <T> T post(String path, HashMap<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject) {
		URI uri = URI.create(path);
		String requestBody = convertObjectToJson(requestClassOrJsonString);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.POST, requestEntity, responseClassObject);
	}
	
	public static <T> T put(String path, Object requestClassOrJsonString, Class<?> responseClassObject) {
		return put(path, new HashMap<String, String>(), requestClassOrJsonString, responseClassObject, new HashMap<String, String>());
	}
	
	public static <T> T put(String path, Object requestClassOrJsonString, Class<?> responseClassObject, HashMap<String, String> parameter) {
		return put(path, new HashMap<String, String>(), requestClassOrJsonString, responseClassObject, parameter);
	}
	
	public static <T> T put(String path, HashMap<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject, HashMap<String, String> parameters) {
		URI uri = URI.create(path);
		String requestBody = convertObjectToJson(requestClassOrJsonString);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.PUT, requestEntity, responseClassObject, parameters);
	}
	
	public static <T> T delete(String path, HashMap<String, String> parameters) {
		return delete(path, new HashMap<String, String>(), "{}", String.class, parameters);
	}
	
	public static <T> T delete(String path, HashMap<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject, HashMap<String, String> parameters) {
		URI uri = URI.create(path);
		String requestBody = convertObjectToJson(requestClassOrJsonString);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.DELETE, requestEntity, responseClassObject, parameters);
	}
	
	
	
	private static <T> T returnContent(URI uri, HttpMethod httpMethod, HttpEntity<String> requestEntity, Class<?> responseClassObject) {	
		ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, requestEntity, String.class);
		return convertJsonToObject(response.getBody(), responseClassObject);
    }

	private static <T> T returnContent(URI uri, HttpMethod httpMethod, HttpEntity<String> requestEntity, Class<?> responseClassObject, HashMap<String, String> parameters) {	
		ResponseEntity<String> response = restTemplate.exchange(uri.toString(), httpMethod, requestEntity, String.class, parameters);
		return convertJsonToObject(response.getBody(), responseClassObject);
    }
	
	private static HttpHeaders computeHeaders(HashMap<String, String> extraHeaders) {
		
		HttpHeaders headers = new HttpHeaders();

		//Compute headers
		if ((extraHeaders != null) && (extraHeaders.size() > 0)){
	      for(Map.Entry<String, String> pair : extraHeaders.entrySet()) {
			headers.set(pair.getKey(), pair.getValue());
	      }
	    }
				
		return headers;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convertJsonToObject(String jsonStr, Class<?> responseClassObject) {
		ObjectMapper objMapper = new ObjectMapper();
		T JavaObject = null;
		try {
			JavaObject = (T) objMapper.readValue(jsonStr, responseClassObject);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return JavaObject;
	}
	
	public static String convertObjectToJson(Object object) {
		ObjectMapper objMapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT);
				//.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
		
		String json = "";
		try {
			json = objMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
		}
		return json;
	}
	
}





