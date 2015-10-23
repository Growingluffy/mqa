package com.ibaguo.mqa.json;

import java.util.List;

public class JsonWord {
	Status status;
	List<String> word;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<String> getWord() {
		return word;
	}
	public void setWord(List<String> word) {
		this.word = word;
	}
	public JsonWord(Status status, List<String> word) {
		super();
		this.status = status;
		this.word = word;
	}
}
