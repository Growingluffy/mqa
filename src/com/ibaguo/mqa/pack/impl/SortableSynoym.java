package com.ibaguo.mqa.pack.impl;

import com.ibaguo.nlp.dictionary.CoreSynonymDictionary;

public class SortableSynoym implements Comparable<SortableSynoym> {
	public String fromWord;
	public String synonym;
	
	public SortableSynoym(String fromWord, String synonym) {
		super();
		this.fromWord = fromWord;
		this.synonym = synonym;
	}

	public int compareTo(SortableSynoym o) {
		Double similar1 = CoreSynonymDictionary.similarity(fromWord, synonym);
		Double similar2 = CoreSynonymDictionary.similarity(o.fromWord, o.synonym);
		return similar1.compareTo(similar2);
	}
}
