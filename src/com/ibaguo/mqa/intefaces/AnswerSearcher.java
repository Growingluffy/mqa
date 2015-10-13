package com.ibaguo.mqa.intefaces;

import java.util.List;
import java.util.Map;

public interface AnswerSearcher {
	Map<String, String> search(List<String> key, int start, int count, boolean sortDesc);
	Map<String, String> search(List<String> key, boolean sortDesc);
}
