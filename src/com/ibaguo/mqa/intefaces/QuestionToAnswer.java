package com.ibaguo.mqa.intefaces;

import java.util.List;

import com.ibaguo.mqa.json.Doc;

public interface QuestionToAnswer {
	public List<Doc> makeQa(String q);
}
