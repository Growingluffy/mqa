package com.ibaguo.mqa.json;

import java.util.List;

public class AskResult {
	String schema;
	List<Doc> docs;
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public List<Doc> getDoc() {
		return docs;
	}
	public void setDoc(List<Doc> docs) {
		this.docs = docs;
	}
	public AskResult(String schema, List<Doc> docs) {
		super();
		this.schema = schema;
		this.docs = docs;
	}

}
