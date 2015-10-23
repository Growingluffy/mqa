package com.ibaguo.mqa.json;

import java.util.HashMap;
import java.util.Map;

import com.ibaguo.mqa.util.Utils;

public class Doc {
	String name;
	String id;
	Map<String,String> fieldVal = new HashMap<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Doc(String name,String id) {
		super();
		this.name = name;
		this.id = id;
	}
	public void putFieldVal(String key, String value) {
		this.fieldVal.put(key, value);
	}
	public String getFieldVal(String key) {
		return this.fieldVal.get(key);
	}
	
	public Map<String, String> getFieldVal() {
		return fieldVal;
	}
	@Override
	public String toString() {
		return Utils.toJson(this);
	}
}
