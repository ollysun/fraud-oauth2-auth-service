package com.etz.authorisationserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	
    public static <T> String getJsonRecursive(T element) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString;
        try {
            jsonInString = mapper.writeValueAsString(element);
            return jsonInString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*private static Gson getGson(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(dateFormat.toPattern());
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
        return  gsonBuilder.create();
    }*/

}
