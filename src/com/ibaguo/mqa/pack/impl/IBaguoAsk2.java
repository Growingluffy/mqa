package com.ibaguo.mqa.pack.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ibaguo.mqa.intefaces.QuestionToAnswer;
import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.solr.QAObj;
import com.ibaguo.mqa.solr.QAScored;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.corpus.tag.Nature;
import com.ibaguo.nlp.dictionary.CoreSynonymDictionary;
import com.ibaguo.nlp.seg.common.Term;

public class IBaguoAsk2 implements QuestionToAnswer {
	static String[] TYPE;
	public static Map<String, String> map = new HashMap<>();
	public static Map<String, String> newMap = new HashMap<>();
	public static List<QAObj> objs;
	static Map<String,Integer> dzzName = new HashMap<>();
	public static IBaguoAsk2 INSTANCE;
	public static IBaguoAsk2 getInstance(){
		if(INSTANCE==null){
			INSTANCE = new IBaguoAsk2();
		}
		return INSTANCE;
	}
	static{
//		System.out.println(0);
		objs = load300k();
//		System.out.println(1);
		try {
			FileReader fr = new FileReader(System.getProperty("user.dir")+"/dzz-name.txt");
			BufferedReader br = new BufferedReader(fr);
			String tmp;
			while((tmp = br.readLine())!=null){
				dzzName.put(tmp, 0);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		String realDzzName = "";
		int matchLength = 0;
		List<Term> terms = MyNLP.segment("什么叫多发性糖尿病？");
		for(Term t:terms){
			for(String name:dzzName.keySet()){
				if(name.indexOf(t.word)>-1){
					if(t.word.length()>matchLength){
						matchLength = t.word.length();
						realDzzName = name;
					}
				}
			}
		}
		System.out.println(realDzzName);
	}
	
	private static List<QAScored> getSimilarQAObj(String question,List<QAObj> objs,int size) {
		List<QAScored> qascored = new ArrayList<>();
		List<Term> terms = MyNLP.segment(question);
		String realDzzName = "";
		int matchLength = 0;
		for(Term t:terms){
			for(String name:dzzName.keySet()){
				if(name.indexOf(t.word)>-1){
					if(t.word.length()>matchLength){
						matchLength = t.word.length();
						realDzzName = name;
					}
				}
			}
		}
		for(QAObj obj:objs){
			String target = obj.getQuestion();
			double sentenceSimilar = 0;
			int index = 0;
			for (int i = 0; i < terms.size(); i++) {
				if(inFilterNature(terms.get(i).nature)){
					continue;
				}
				double val = wordSimilarInSent(terms.get(i).word,target);
				sentenceSimilar+= val;
				index++;
			}
			double score = Math.sqrt(sentenceSimilar/index);
			if(realDzzName.equals(obj.getDisease_name())){
				score+=0.3;
			}
//			System.out.println(Math.sqrt(sentenceSimilar/index));
			if(qascored.size()<size){
				qascored.add(new QAScored(obj,score));
			}else{
				for(int i=0;i<qascored.size();i++){
					boolean changed = false;
					QAScored qas = qascored.get(i);
					if(qas.score<score){
						qascored.remove(i);
						qascored.add(new QAScored(obj,score));
						changed = true;
					}
					if(changed) break;
				}
			}
			
		}
		return qascored;
	}

	public static List<QAObj> load300k(){
		Object temp=null;
	    File file =new File(System.getProperty("user.dir")+"/QAObj.dat");
	    FileInputStream in;
	    try {
	        in = new FileInputStream(file);
	        ObjectInputStream objIn=new ObjectInputStream(in);
	        temp=objIn.readObject();
	        objIn.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return (List<QAObj>)temp;
	}

	public static double wordSimilarInSent(String word,String sent){
		List<Term> terms = MyNLP.segment(sent);
		int length = terms.size();
		for (int i = 0; i < length; i++) {
			if(word.equals(terms.get(i).word)){
				return 1;
			}
		}
		double sentenceSimilar = 0;
		for (int i = 0; i < length; i++) {
			Term term = terms.get(i);
			if(inFilterNature(term.nature)){
				continue;
			}
			double distance = CoreSynonymDictionary.distance(word, term.word);
			double val = (7429892869.0-distance)/7429892869.0;
			if(val>sentenceSimilar){
				sentenceSimilar = val;
			}
		}
		return sentenceSimilar;
	}

	private static boolean inFilterNature(Nature nature) {
		List<Nature> nl = new ArrayList<>();
		nl.add(Nature.cc);
		nl.add(Nature.v);
		nl.add(Nature.vshi);
		nl.add(Nature.vd);
		nl.add(Nature.vf);
		nl.add(Nature.vx);
		nl.add(Nature.vl);
		nl.add(Nature.vg);
		nl.add(Nature.w);
		for(Nature n:nl){
			if(nature.equals(n)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<Doc> makeQa(String q) {
//		KeyWordExtract kwe = new NlpKeyWordExtract();
		List<QAScored> out = getSimilarQAObj(q, objs, 10);
		List<Doc> ans = new ArrayList<>();
		
		for(QAScored qas:out){
			Doc sr = new Doc(qas.obj.getDisease_name(), UUID.randomUUID().toString());
			sr.putFieldVal("answer", qas.obj.getAnswer());
			sr.putFieldVal("doctor", qas.obj.getDoctor());
			sr.putFieldVal("hospital", qas.obj.getHospital());
			sr.putFieldVal("position", qas.obj.getPosition());
			sr.putFieldVal("resume", qas.obj.getResume());
			sr.putFieldVal("question", qas.obj.getQuestion());
			sr.putFieldVal("score", String.valueOf(qas.score));
			ans.add(sr);
		}
		return ans;
	}
}
