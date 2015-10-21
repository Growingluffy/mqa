package com.ibaguo.mqa.json;

import java.util.List;

public class JsonWordScore {
	Status status;
	List<KeywordScore> wordscore;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<KeywordScore> getWordscore() {
		return wordscore;
	}
	public void setWordscore(List<KeywordScore> wordscore) {
		this.wordscore = wordscore;
	}
	public JsonWordScore(Status status, List<KeywordScore> wordscore) {
		super();
		this.status = status;
		this.wordscore = wordscore;
	}
}
