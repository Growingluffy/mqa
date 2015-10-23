package com.ibaguo.mqa.json;

public class JsonAskResult {
	Status status;
	AskResult answer;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public AskResult getAnswer() {
		return answer;
	}
	public void setAnswer(AskResult answer) {
		this.answer = answer;
	}
	public JsonAskResult(Status status, AskResult answer) {
		super();
		this.status = status;
		this.answer = answer;
	}
}
