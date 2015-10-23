package com.ibaguo.mqa.json;

import java.util.List;

import com.ibaguo.nlp.seg.common.Term;

public class JsonTerm {
	Status status;
	List<Term> term;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<Term> getWord() {
		return term;
	}
	public void setWord(List<Term> term) {
		this.term = term;
	}
	public JsonTerm(Status status, List<Term> term) {
		super();
		this.status = status;
		this.term = term;
	}
}
