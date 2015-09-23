package in.thyferny.a;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import in.thyferny.nlp.MyNLP;
import in.thyferny.nlp.model.maxent.MaxEnt;
import javafx.util.Pair;

public class SolrConnector {
	public static void main(String[] args) throws SolrServerException, IOException {
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
		String[] answers = allInOneQuery("臀肌挛缩症3个月后可以做剧烈运动了吗？？");
		for(String ans:answers){
			System.out.println(ans);
		}
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

	private static void upload() throws SolrServerException, IOException {
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

		HttpSolrClient solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
		solr.setMaxTotalConnections(100);

		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (DiseaseDescription dd : dds) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", UUID.randomUUID().toString());
			doc.addField("name", dd.getName());
			doc.addField("pathogeny", dd.getPathogeny());
			doc.addField("symptom", dd.getSymptom());
			doc.addField("treatment", dd.getTreatment());
			doc.addField("other", dd.getOther());
			docs.add(doc);
		}
		solr.add(docs);
		solr.commit();
	}

	public static String loadMQAPredictMaxEnt(List<String> fieldList) throws IOException {
		MaxEnt maxEnt = MaxEnt.loadModel("QMaxEnt.dat");
		Pair<String, Double>[] result = maxEnt.predict(fieldList);
		System.out.println(Arrays.toString(result));
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
		QueryResponse rsp = search(new String[] { "name", "symptom", "pathogeny", "treatment", "other" },
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

	public static QueryResponse search(String[] field, String[] key, int start, int count, String[] sortfield,
			Boolean[] flag, Boolean hightlight) {
		if (null == sortfield || null == flag || sortfield.length != flag.length) {
			return null;
		}
		SolrQuery query = null;
		SolrClient solr = createSolrServer();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(key[0]);
			query = new SolrQuery();
			for (int i = 1; i < key.length; i++) {
				sb.append(" and ");
				sb.append(key[i]);
			}
			query.setQuery(sb.toString());
			query.setFields(field);
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
