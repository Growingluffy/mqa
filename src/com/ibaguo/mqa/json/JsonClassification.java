package com.ibaguo.mqa.json;

import java.util.List;

public class JsonClassification {
	Status status;
	List<ClassificationScore> cs;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<ClassificationScore> getCs() {
		return cs;
	}
	public void setCs(List<ClassificationScore> cs) {
		this.cs = cs;
	}
	public JsonClassification(Status status, List<ClassificationScore> cs) {
		super();
		this.status = status;
		this.cs = cs;
	}
}
