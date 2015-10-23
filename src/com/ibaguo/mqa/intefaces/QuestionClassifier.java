package com.ibaguo.mqa.intefaces;

import java.util.List;
import java.util.Map;

public interface QuestionClassifier {
	void save(String path);
	QuestionClassifier initFromFile(String path);
	void train(int maxIt);
	String eval(List<String> fieldList);
	Map<String, Double> predict(List<String> fieldList);
}
