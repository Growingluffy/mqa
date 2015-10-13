package com.ibaguo.mqa.intefaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface QuestionClassifier {
	void save(String path);
	void loadData(String path) throws IOException;
	void train(int maxIt);
	String eval(List<String> fieldList);
	Map<String, Double> predict(List<String> fieldList);
}
