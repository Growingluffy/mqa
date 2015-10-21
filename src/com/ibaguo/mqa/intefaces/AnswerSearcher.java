package com.ibaguo.mqa.intefaces;

import java.util.List;

import com.ibaguo.mqa.json.DocRank;

public interface AnswerSearcher {
	List<DocRank> search(String[] kw);
}
