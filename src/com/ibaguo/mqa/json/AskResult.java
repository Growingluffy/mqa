package com.ibaguo.mqa.json;

import java.util.List;

public class AskResult {
	String schema;
	List<Doc> doc;
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public List<Doc> getDoc() {
		return doc;
	}
	public void setDoc(List<Doc> doc) {
		this.doc = doc;
	}
	public AskResult(String schema, List<Doc> doc) {
		super();
		this.schema = schema;
		this.doc = doc;
	}

}
