package com.ibaguo.mqa.intefaces;

import java.util.Map;

import com.ibaguo.mqa.json.Doc;

public interface AnswerSearcher {
	Map<Doc, Double> search(String[] kw);
}
