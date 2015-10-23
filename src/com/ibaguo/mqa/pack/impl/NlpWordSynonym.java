package com.ibaguo.mqa.pack.impl;

import java.util.HashMap;
import java.util.Map;

import com.ibaguo.mqa.intefaces.WordSynonym;
import com.ibaguo.nlp.corpus.synonym.Synonym;
import com.ibaguo.nlp.dictionary.CoreSynonymDictionary;
import com.ibaguo.nlp.dictionary.common.CommonSynonymDictionary.SynonymItem;

public class NlpWordSynonym implements WordSynonym {

	@Override
	public Map<String, Double> getSynonymList(String word) {
		SynonymItem lst = CoreSynonymDictionary.get(word);
		Map<String,Double> synonymMap = new HashMap<>();
		for (Synonym kk : lst.synonymList) {
			synonymMap.put(kk.getRealWord(), CoreSynonymDictionary.similarity(word,kk.getRealWord()));
		}
//		Collections.sort(sortedSynonymList);
//		if(sortDesc){
//			for(SortableSynoym ss:sortedSynonymList){
//				synonymList.add(ss.synonym);
//			}
//		}else{
//			for(int i=sortedSynonymList.size();i>=0;i--){
//				synonymList.add(sortedSynonymList.get(i).synonym);
//			}
//		}
		
		return synonymMap;
	}

}
