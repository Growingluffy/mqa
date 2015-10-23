package com.ibaguo.mqa.json;

import java.util.List;
import java.util.Map;

public class SolrResult {
	String schema;
	List<DocRank> doc2rank;
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public List<DocRank> getDoc2rank() {
		return doc2rank;
	}
	public void setDoc2rank(List<DocRank> doc2rank) {
		this.doc2rank = doc2rank;
	}
	public SolrResult(String schema, List<DocRank> doc2rank) {
		super();
		this.schema = schema;
		this.doc2rank = doc2rank;
	}
}
