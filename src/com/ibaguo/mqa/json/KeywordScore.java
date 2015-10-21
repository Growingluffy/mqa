package com.ibaguo.mqa.json;

public class KeywordScore {
	public String keyWord;
	public double score;
	
	public KeywordScore(String keyWord, double score) {
		super();
		this.keyWord = keyWord;
		this.score = score;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
}
