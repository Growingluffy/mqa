package com.ibaguo.mqa.json;

import java.util.Map;

public class SolrResult {
	String schema;
	Map<Doc, Double> doc2rank;
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public Map<Doc, Double> getDoc2rank() {
		return doc2rank;
	}
	public void setDoc2rank(Map<Doc, Double> doc2rank) {
		this.doc2rank = doc2rank;
	}
	public SolrResult(String schema, Map<Doc, Double> doc2rank) {
		super();
		this.schema = schema;
		this.doc2rank = doc2rank;
	}
}
