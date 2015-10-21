package com.ibaguo.mqa.json;

import java.util.HashMap;
import java.util.Map;

import com.ibaguo.mqa.util.Utils;

public class Doc {
	String name;
	Map<String,String> fieldVal = new HashMap<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Doc(String name) {
		super();
		this.name = name;
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
