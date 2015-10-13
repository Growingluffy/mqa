package com.ibaguo.mqa.pack.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibaguo.mqa.intefaces.KeyWordSynonym;
import com.ibaguo.nlp.corpus.synonym.Synonym;
import com.ibaguo.nlp.dictionary.CoreSynonymDictionary;
import com.ibaguo.nlp.dictionary.common.CommonSynonymDictionary.SynonymItem;

public class NlpWordSynonym implements KeyWordSynonym {

	@Override
	public List<String> getSynonymList(String word, boolean sortDesc) {
		SynonymItem lst = CoreSynonymDictionary.get(word);
		List<String> synonymList = new ArrayList<>();
		List<SortableSynoym> sortedSynonymList = new ArrayList<>();
		for (Synonym kk : lst.synonymList) {
			SortableSynoym ss = new SortableSynoym(word,kk.getRealWord());
			sortedSynonymList.add(ss);
		}
		Collections.sort(sortedSynonymList);
		if(sortDesc){
			for(SortableSynoym ss:sortedSynonymList){
				synonymList.add(ss.synonym);
			}
		}else{
			for(int i=sortedSynonymList.size();i>=0;i--){
				synonymList.add(sortedSynonymList.get(i).synonym);
			}
		}
		return synonymList;
	}

}
