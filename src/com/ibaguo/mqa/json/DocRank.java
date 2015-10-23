package com.ibaguo.mqa.json;

public class DocRank {
	Doc doc;
	double rank;
	public Doc getDoc() {
		return doc;
	}
	public void setDoc(Doc doc) {
		this.doc = doc;
	}
	public double getRank() {
		return rank;
	}
	public void setRank(double rank) {
		this.rank = rank;
	}
	public DocRank(Doc doc, double rank) {
		super();
		this.doc = doc;
		this.rank = rank;
	}
}
