package com.ibaguo.mqa.pack.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ibaguo.mqa.intefaces.KeyWordExtract;
import com.ibaguo.mqa.util.Utils;
import com.ibaguo.nlp.MyNLP;

public class NlpKeyWordExtract implements KeyWordExtract{

	@Override
	public List<String> extractKeyword(String document, int size) {
		return MyNLP.extractKeyword(document, size);
	}

	@Override
	public List<KeywordScore> rankWordScore(String document) {
		List<Entry<String, Float>> entryList = MyNLP.rankWordScore(document);
		List<KeywordScore> ret = new ArrayList<>();
		for(Entry<String, Float> entry:entryList){
			KeywordScore ks = new KeywordScore(entry.getKey(),entry.getValue());
			ret.add(ks);
		}
		return ret;
	}

	public static void main(String[] args) {
		List<KeywordScore> list = new NlpKeyWordExtract().rankWordScore("今天中午没吃饭，现在肚子好饿,后面的内容其实就没什么用了");
		System.out.println(Utils.toJson(list));
	}
}
