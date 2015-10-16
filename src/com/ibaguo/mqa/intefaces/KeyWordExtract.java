package com.ibaguo.mqa.intefaces;

import java.util.List;

import com.ibaguo.mqa.pack.impl.KeywordScore;

public interface KeyWordExtract {
	List<String> extractKeyword(String document, int size);
	List<KeywordScore> rankWordScore(String document);
}
