package com.ibaguo.mqa.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
	private static final String SEP1 = "=";  
    private static final String SEP2 = ",";  
  
    public static String toJson(Object obj){
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return  gson.toJson(obj);
    }
    
    public static <T> T fromJson(String str, Type type) {  
        Gson gson = new Gson();  
        return gson.fromJson(str, type);  
    }
    
    public static String listToString(List<?> list) {  
        StringBuffer sb = new StringBuffer();  
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                if (list.get(i) == null || list.get(i) == "") {  
                    continue;  
                }  
                // 如果值是list类型则调用自己  
                if (list.get(i) instanceof List) {  
                    sb.append(listToString((List<?>) list.get(i)));  
                    sb.append(SEP1);  
                } else if (list.get(i) instanceof Map) {  
                    sb.append(mapToString((Map<?, ?>) list.get(i)));  
                    sb.append(SEP1);  
                } else {  
                    sb.append(list.get(i));  
                    sb.append(SEP1);  
                }  
            }  
        }  
        return sb.toString();  
    }  
  
    public static String mapToString(Map<?, ?> map) {  
        StringBuffer sb = new StringBuffer();  
        for (Object obj : map.keySet()) {  
            if (obj == null) {  
                continue;  
            }  
            Object key = obj;  
            Object value = map.get(key);  
            if (value instanceof List<?>) {  
                sb.append(key.toString() + SEP1 + listToString((List<?>) value));  
                sb.append(SEP2);  
            } else if (value instanceof Map<?, ?>) {  
                sb.append(key.toString() + SEP1  
                        + mapToString((Map<?, ?>) value));  
                sb.append(SEP2);  
            } else {  
                sb.append(key.toString() + SEP1 + value.toString());  
                sb.append(SEP2);  
            }  
        }  
        return sb.toString();  
    }  

    public static Map<String, Object> stringToMap(String mapText) {  
        if (mapText == null || mapText.equals("")) {  
            return null;  
        }  
        Map<String, Object> map = new HashMap<String, Object>();  
        String[] text = mapText.split("\\" + SEP2); // 转换为数组  
        for (String str : text) {  
            String[] keyText = str.split(SEP1); // 转换key与value的数组  
            if (keyText.length < 1) {  
                continue;  
            }  
            String key = keyText[0]; // key  
            String value = keyText[1]; // value  
            map.put(key, value);  
        }  
        return map;  
    }  
  
    public static List<Object> stringToList(String listText) {  
        if (listText == null || listText.equals("")) {  
            return null;  
        }  
  
        List<Object> list = new ArrayList<Object>();  
        String[] text = listText.split(SEP1);  
        for (String str : text) {  
        	list.add(str);  
        }  
        return list;  
    }  
}
