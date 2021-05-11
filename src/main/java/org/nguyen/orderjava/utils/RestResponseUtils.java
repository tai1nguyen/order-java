package org.nguyen.orderjava.utils;

import java.util.HashMap;
import java.util.Map;

public class RestResponseUtils {
    
    public static Map<String, String> getResponseJson(String key, String value) {
        Map<String, String> response = new HashMap<>();
        response.put(key, value);

        return response;
    }
}