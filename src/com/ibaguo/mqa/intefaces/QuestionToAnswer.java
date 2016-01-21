package com.ibaguo.mqa.intefaces;

import java.util.List;

import com.ibaguo.mqa.json.AskResult;

public interface QuestionToAnswer {
	public List<AskResult> makeQa(String q,int size);
}
