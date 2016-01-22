package com.ibaguo.mqa.json;

public class DocRank implements Comparable<DocRank>{
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
	@Override
	public int compareTo(DocRank o) {
		if(rank==o.rank) return 0;
		return rank>o.rank?-1:1;
	}
}
