package com.ibaguo.mqa.json;

public class JsonSolrResult {
	Status status;
	SolrResult solr;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public SolrResult getSolr() {
		return solr;
	}
	public void setSolr(SolrResult solr) {
		this.solr = solr;
	}
	public JsonSolrResult(Status status, SolrResult solr) {
		super();
		this.status = status;
		this.solr = solr;
	}
}
