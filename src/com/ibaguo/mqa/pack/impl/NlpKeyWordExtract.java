package com.ibaguo.mqa.pack.impl;

import java.util.List;

import com.ibaguo.mqa.intefaces.KeyWordExtract;
import com.ibaguo.nlp.MyNLP;

public class NlpKeyWordExtract implements KeyWordExtract{

	@Override
	public List<String> extractKeyword(String document, int size) {
		return MyNLP.extractKeyword(document, size);
	}

}
