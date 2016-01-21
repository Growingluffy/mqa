package com.ibaguo.mqa.json;

import java.util.List;

public class JsonAskResult {
	Status status;
	List<AskResult> answer;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<AskResult> getAnswer() {
		return answer;
	}
	public void setAnswer(List<AskResult> answer) {
		this.answer = answer;
	}
	public JsonAskResult(Status status, List<AskResult> answer) {
		super();
		this.status = status;
		this.answer = answer;
	}
}
