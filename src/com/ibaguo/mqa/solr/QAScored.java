package com.ibaguo.mqa.solr;

public class QAScored {
	public QAObj obj;
	public double score;
	public QAObj getObj() {
		return obj;
	}
	public void setObj(QAObj obj) {
		this.obj = obj;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public QAScored(QAObj obj, double score) {
		super();
		this.obj = obj;
		this.score = score;
	}
	
}
