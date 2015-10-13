package com.ibaguo.mqa.intefaces;

import java.util.List;

import com.ibaguo.mqa.solr.SolrReturn;

public interface AnswerSearcher {
	List<SolrReturn> search(List<String> key, int start, int count, boolean sortDesc);
	List<SolrReturn> search(List<String> key, boolean sortDesc);
}
