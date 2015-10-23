package com.ibaguo.mqa.json;

import java.util.List;

public class ClassificationScore {
	String type;
	double score;
	public String getLabel() {
		return type;
	}
	public void setLabel(String type) {
		this.type = type;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ClassificationScore(String type, double score) {
		super();
		this.type = type;
		this.score = score;
	}
	@Override
	public String toString() {
		return "MaxEntScore [type=" + type + ", score=" + score + "]";
	}
}
