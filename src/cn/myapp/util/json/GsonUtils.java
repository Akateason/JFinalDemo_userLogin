package cn.myapp.util.json;

import com.google.gson.*;
import java.lang.reflect.Type;

public class GsonUtils {
	
    public static Gson getGson() {
    	GsonBuilder gsonBuilder = new GsonBuilder();
    	gsonBuilder.registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {

    	    public JsonElement serialize(Double src, Type typeOfSrc,
    	                JsonSerializationContext context) {
    	            Integer value = (int)Math.round(src);
    	            return new JsonPrimitive(value);
    	        }
    	    });

    	Gson gson = gsonBuilder.create();
    	 
    	return gson ;
    }

}
