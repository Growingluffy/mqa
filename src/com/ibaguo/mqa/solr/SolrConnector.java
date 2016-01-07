package com.ibaguo.mqa.solr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.ibaguo.mqa.util.Utils;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.model.maxent.MaxEnt;

public class SolrConnector {
	public static void main(String[] args) throws SolrServerException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// String content =
		// "平时无不适，昨天晚上聚餐，喝了些酒，有点高了；早上感觉有些头晕，肚子也难受，上了几次厕所，便溏；然后伴有恶心，面色苍白，呕吐，这是怎么了？";
		// List<String> keywordList = MyNLP.extractKeyword(content, 5);
		// System.out.println(keywordList);
		//
		// QueryResponse rsp = search(new String[]{"name","symptom"}, new
		// String[]{"腹泻","呕吐"}, 0, 10, new String[]{}, new Boolean[]{}, true);
		// System.out.println(rsp.getHighlighting());
		// System.out.println(rsp);
		// writeToJsonFile();
//		String[] answers = allInOneQuery("溃疡病穿孔有哪些表现？");//臀肌挛缩症3个月后可以做剧烈运动了吗？？
//		
//		for(String ans:answers){
//			System.out.println(ans);
//		}
		uploadQA120();
//		deleteAll();
	}

	private static void writeToJsonFile() throws IOException {
		Object temp = null;
		File file = new File("Disease.dat");
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<DiseaseDescription> dds = (List<DiseaseDescription>) temp;

		FileWriter f = new FileWriter("D:/mqa-answer");

		for (DiseaseDescription dd : dds) {
			f.write(dd.toString());
			f.flush();
		}
		f.close();
	}

	private static void uploadQAObj() throws SolrServerException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object temp = null;
		File file = new File("QAObj.dat");
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<QAObj> dds = (List<QAObj>) temp;

		HttpSolrClient solr = new HttpSolrClient("http://localhost:8983/solr/qa2");
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
		solr.setMaxTotalConnections(100);

		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (int i =0;i<dds.size();i++) {
			QAObj dd = dds.get(i);
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", UUID.randomUUID().toString());
			for(Field f:QAObj.class.getDeclaredFields()){
				String fieldName = f.getName();
				if(fieldName.equals("serialVersionUID")){
					continue;
				}
				String first = fieldName.substring(0, 1).toUpperCase();
				String rest = fieldName.substring(1, fieldName.length());
				String upperCaseFieldName = new StringBuffer(first).append(rest).toString(); 
				Method method = QAObj.class.getMethod("get"+upperCaseFieldName, null);
//				System.out.println(fieldName+":"+method.invoke(dd, null));
				doc.addField(fieldName, method.invoke(dd, null));
			}
			docs.add(doc);
			if(i%2000==0){
				solr.add(docs);
				solr.commit();
				docs.clear();
				System.out.println("flushing...");
			}
		}
		solr.add(docs);
		solr.commit();
		docs.clear();
	}
	
	private static void uploadNewDisease() throws SolrServerException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object temp = null;
		File file = new File("NewDisease.dat");
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<NewDiseaseDescription> dds = (List<NewDiseaseDescription>) temp;

		for (NewDiseaseDescription dd : dds) {
			System.out.println(dd.getName());
		}
//		HttpSolrClient solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
//		solr.setConnectionTimeout(100);
//		solr.setDefaultMaxConnectionsPerHost(100);
//		solr.setMaxTotalConnections(100);
//
//		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
//		for (NewDiseaseDescription dd : dds) {
//			SolrInputDocument doc = new SolrInputDocument();
//			doc.addField("id", UUID.randomUUID().toString());
//			for(Field f:NewDiseaseDescription.class.getDeclaredFields()){
//				String fieldName = f.getName();
//				if(fieldName.equals("serialVersionUID")){
//					continue;
//				}
//				String first = fieldName.substring(0, 1).toUpperCase();
//				String rest = fieldName.substring(1, fieldName.length());
//				String upperCaseFieldName = new StringBuffer(first).append(rest).toString(); 
//				Method method = NewDiseaseDescription.class.getMethod("get"+upperCaseFieldName, null);
////				System.out.println(fieldName+":"+method.invoke(dd, null));
//				doc.addField(fieldName, method.invoke(dd, null));
//			}
//			docs.add(doc);
//		}
//		solr.add(docs);
//		solr.commit();
	}
	
	private static void uploadQA120() throws SolrServerException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object temp = null;
		File file = new File("QA120.dat");
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<QA120> dds = (List<QA120>) temp;

		for (QA120 dd : dds) {
			System.out.println(dd.getName());
		}
		HttpSolrClient solr = new HttpSolrClient("http://127.0.0.1:8983/solr/qa120");
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
		solr.setMaxTotalConnections(100);

		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (QA120 dd : dds) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", UUID.randomUUID().toString());
			for(Field f:QA120.class.getDeclaredFields()){
				String fieldName = f.getName();
				if(fieldName.equals("serialVersionUID")){
					continue;
				}
				String first = fieldName.substring(0, 1).toUpperCase();
				String rest = fieldName.substring(1, fieldName.length());
				String upperCaseFieldName = new StringBuffer(first).append(rest).toString(); 
				Method method = QA120.class.getMethod("get"+upperCaseFieldName, null);
//				System.out.println(fieldName+":"+method.invoke(dd, null));
				doc.addField(fieldName, method.invoke(dd, null));
			}
			docs.add(doc);
		}
		solr.add(docs);
		solr.commit();
	}
	
	private static void deleteAll() throws SolrServerException, IOException {

		HttpSolrClient solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
		solr.setMaxTotalConnections(100);

		solr.deleteByQuery("*:*");
		solr.commit();
	}
	
//	private static void upload() throws SolrServerException, IOException {
//		Object temp = null;
//		File file = new File("Disease.dat");
//		FileInputStream in;
//		try {
//			in = new FileInputStream(file);
//			ObjectInputStream objIn = new ObjectInputStream(in);
//			temp = objIn.readObject();
//			objIn.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		List<DiseaseDescription> dds = (List<DiseaseDescription>) temp;
//
//		HttpSolrClient solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
//		solr.setConnectionTimeout(100);
//		solr.setDefaultMaxConnectionsPerHost(100);
//		solr.setMaxTotalConnections(100);
//
//		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
//		for (DiseaseDescription dd : dds) {
//			SolrInputDocument doc = new SolrInputDocument();
//			doc.addField("id", UUID.randomUUID().toString());
//			doc.addField("name", dd.getName());
//			doc.addField("pathogeny", dd.getPathogeny());
//			doc.addField("symptom", dd.getSymptom());
//			doc.addField("treatment", dd.getTreatment());
//			doc.addField("other", dd.getOther());
//			docs.add(doc);
//		}
//		solr.add(docs);
//		solr.commit();
//	}

	public static String loadMQAPredictMaxEnt(List<String> fieldList) throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("QMaxEnt.dat");
		Map<String, Double> result = maxEnt.predict(fieldList);
		System.out.println(Utils.mapToString(result));
		System.out.println(maxEnt.eval(fieldList));
		return maxEnt.eval(fieldList);
	}

	public static String[] allInOneQuery(String sentence) throws IOException {
		List<String> keywordList = MyNLP.extractKeyword(sentence, 3);
		System.out.println(keywordList);
		String answerType = loadMQAPredictMaxEnt(keywordList);
		String aType = "";
		if (answerType.equals("症状")) {
			aType = "symptom";
		} else if (answerType.equals("病因")) {
			aType = "pathogeny";
		} else if (answerType.equals("治疗")) {
			aType = "treatment";
		} else if (answerType.equals("其他")) {
			aType = "other";
		}
		
		QueryResponse rsp = search(
				keywordList.toArray(new String[keywordList.size()]), 0, 10, new String[] {}, new Boolean[] {}, true);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
		List<String> answers = new ArrayList<>();
		for (SolrDocument sd : sdl) {
			ArrayList<String> ans = (ArrayList<String>) sd.getFieldValue(aType);
			if(ans.size()!=0&&!ans.get(0).equals("")){
				List<String> summary = MyNLP.extractSummary(ans.get(0), 1);//输出的结果也用自然语言处理提取摘要
				answers.add(ans.get(0));
			}
//			System.out.println();
//			System.out.println(sd);
		}
		return answers.toArray(new String[answers.size()]);
	}

	public static SolrClient createSolrServer() {
		HttpSolrClient solr = null;
		try {
			solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
			solr.setConnectionTimeout(100);
			solr.setDefaultMaxConnectionsPerHost(100);
			solr.setMaxTotalConnections(100);
		} catch (Exception e) {
			System.out.println("请检查tomcat服务器或端口是否开启!");
			e.printStackTrace();
		}
		return solr;
	}

	public static QueryResponse search(String[] key, int start, int count, String[] sortfield,
			Boolean[] flag, Boolean hightlight) {
		if (null == sortfield || null == flag || sortfield.length != flag.length) {
			return null;
		}
		String[] fields = new String[NewDiseaseDescription.class.getDeclaredFields().length];
		List<String> tmp = new ArrayList<>();
		for(Field f:NewDiseaseDescription.class.getDeclaredFields()){
			String fieldName = f.getName();
			if(fieldName.equals("serialVersionUID")){
				continue;
			}
			tmp.add(fieldName);
		}
		tmp.add("id");
		tmp.toArray(fields);
		SolrQuery query = null;
		SolrClient solr = createSolrServer();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(key[0]);
			query = new SolrQuery();
			for (int i = 1; i < key.length; i++) {
				sb.append(" AND ");
				sb.append(key[i]);
			}
			query.setQuery(sb.toString());
			query.setFields(fields);
			// 设置起始位置与返回结果数
			query.setStart(start);
			query.setRows(count);
			// 设置排序
			for (int i = 0; i < sortfield.length; i++) {
				if (flag[i]) {
					query.addSort(sortfield[i], SolrQuery.ORDER.asc);
				} else {
					query.addSort(sortfield[i], SolrQuery.ORDER.desc);
				}
			}
			// 设置高亮
			if (null != hightlight) {
				query.setHighlight(true); // 开启高亮组件
				query.addHighlightField("title");// 高亮字段
				query.setHighlightSimplePre("<font color=\"red\">");// 标记
				query.setHighlightSimplePost("</font>");
				query.setHighlightSnippets(1);// 结果分片数，默认为1
				query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		QueryResponse rsp = null;
		try {
			rsp = solr.query(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 返回查询结果
		return rsp;
	}

}
