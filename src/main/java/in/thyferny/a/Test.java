package in.thyferny.a;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.thyferny.nlp.model.maxent.MaxEnt;
import javafx.util.Pair;

public class Test {
	public static void main(String[] args) throws IOException {

		// String content =
		// "平时无不适，昨天晚上聚餐，喝了些酒，有点高了；早上感觉有些头晕，肚子也难受，上了几次厕所，便溏；然后伴有恶心，面色苍白，呕吐，这是�?�么了？";
		// List<String> keywordList = MyNLP.extractKeyword(content, 3);
		// System.out.println(keywordList);
		//
		// Segment segment = MyNLP.newSegment();
		// segment.enableIndexMode(true);
		// segment.enablePartOfSpeechTagging(false);
		// segment.enableNameRecognize(true);
		// segment.enablePlaceRecognize(true);
		// segment.enableOrganizationRecognize(true);
		// segment.enableTranslatedNameRecognize(false);
		// segment.enableCustomDictionary(false);
		// segment.enableJapaneseNameRecognize(false);
		// segment.enableAllNamedEntityRecognize(true);
		// List<Term> termList = segment.seg("中文分词测试 这是一段普通的文本");
		// System.out.println(termList);

		// trainSaveMaxEnt();

		// loadPredictMaxEnt();

		// trainSaveQTMaxEnt();
		loadMQAPredictMaxEnt();
		//
		// System.out.println("距离0\t" + CoreSynonymDictionary.distance("香蕉",
		// "苹果"));
		// System.out.println("距离1\t" + CoreSynonymDictionary.distance("香蕉",
		// "�?"));
		// System.out.println("距离2\t" + CoreSynonymDictionary.distance("病痛",
		// "痢疾"));
		// System.out.println("距离3\t" + CoreSynonymDictionary.distance("腹泻",
		// "头疼"));
		// System.out.println("距离4\t" + CoreSynonymDictionary.distance("腹泻",
		// "胃病"));
		// System.out.println("距离5\t" + CoreSynonymDictionary.distance("腹泻",
		// "呕吐"));
		// System.out.println("距离6\t" + CoreSynonymDictionary.distance("晕车",
		// "呕吐"));
		// System.out.println("距离7\t" + CoreSynonymDictionary.distance("晕车",
		// "晕船"));
		// System.out.println("距离8\t" + CoreSynonymDictionary.distance("晕车",
		// "吃药"));
		// System.out.println("距离9\t" + CoreSynonymDictionary.distance("晕车",
		// "山路"));
		// System.out.println("距离X\t" + CoreSynonymDictionary.distance("香蕉",
		// "橙汁"));
	}

	public static void trainSaveMaxEnt() throws IOException {
		String path = "data/questions-train.txt";
		MaxEnt maxEnt = new MaxEnt();
		maxEnt.loadData(path);
		maxEnt.train(5);
		maxEnt.save("MaxEnt.dat");
	}

	public static void trainSaveQTMaxEnt() throws IOException {
		String path = "data/83.txt";
		MaxEnt maxEnt = new MaxEnt();
		maxEnt.loadCSV(path);
		maxEnt.train(5);
		maxEnt.save("QMaxEnt.dat");
	}

	public static void loadMQAPredictMaxEnt() throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("QMaxEnt.dat");
		List<String> fieldList = new ArrayList<String>();
		// fieldList.add("怎么办");
		// fieldList.add("原因");
		// fieldList.add("喉咙肥大");
		fieldList.add("如何");
		fieldList.add("解决");
		Pair<String, Double>[] result = maxEnt.predict(fieldList); // 棰勬祴QuestionType姒傜巼鍚勬槸澶氬�?
		System.out.println(Arrays.toString(result));
		System.out.println(maxEnt.eval(fieldList));
	}

	public static void loadPredictMaxEnt() throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("MaxEnt.dat");
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("When");
		fieldList.add("was");
		Pair<String, Double>[] result = maxEnt.predict(fieldList); // 棰勬祴QuestionType姒傜巼鍚勬槸澶氬�?
		System.out.println(Arrays.toString(result));
		System.out.println(maxEnt.eval(fieldList));
	}
}
