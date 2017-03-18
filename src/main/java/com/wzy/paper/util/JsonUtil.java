package com.wzy.paper.util;

import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * @Author wzy
 * @Date 2016/11/15 11:22
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * 转换成json对象
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static String generateJson(Object object) throws IOException {
        String json = "";
        json = mapper.writeValueAsString(object);
        return json;
    }

    public static Object generateObject(String json, Class<?> valueType) throws IOException {
        mapper.configure(Feature.AUTO_CLOSE_JSON_CONTENT, Boolean.TRUE);
        Object object = mapper.readValue(json, valueType);
        return object;
    }


//	public static Object generateObject(String json, TypeReference<List<XsPaper>> typeReference) throws JsonParseException, JsonMappingException, IOException {
//	    Object object = mapper.readValue(json, typeReference);
//	    return object;
//	}

}
