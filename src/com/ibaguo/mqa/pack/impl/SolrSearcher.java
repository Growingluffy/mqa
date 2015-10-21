package com.ibaguo.mqa.pack.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.ibaguo.mqa.intefaces.AnswerSearcher;
import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.solr.SolrConnector;
import com.ibaguo.nlp.suggest.Suggester;

public class SolrSearcher implements AnswerSearcher{

	public Map<Doc, Double> search(String[] kw) {
		QueryResponse rsp = SolrConnector.search(kw, 0, 20, new String[] {}, new Boolean[] {}, true);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
		Map<Doc, Double> listDoc = new HashMap<>();
		Map<String, Doc> contentToDoc = new HashMap<>();
		Suggester suggester = new Suggester();
		for (SolrDocument sd : sdl) {
			StringBuffer sb = new StringBuffer();
			Doc doc = new Doc(sd.getFieldValue("name").toString());
			for(String fn:sd.getFieldNames()){
				if(fn.equals("name")) continue;
				Object obj = sd.getFieldValue(fn);
				if(obj instanceof String){
					String value = (String)obj;
					if(!value.equals("")){
//						jsonMap.put(fn, value);
						doc.putFieldVal(fn, value);
						sb.append(value+";");
						suggester.addSentence(value);
					}
				}else if(obj instanceof ArrayList){
					List<String> ans = (ArrayList<String>)obj ;
					if(ans.size()!=0&&!ans.get(0).equals("")){
						doc.putFieldVal(fn, ans.get(0));
						sb.append(ans.get(0)+";");
//						jsonMap.put(fn, ans.get(0));
					}
				}
			}
			suggester.addSentence(sb.toString());
			contentToDoc.put(sb.toString(), doc);
			listDoc.put(doc, 0.0);
		}
		for(String k:kw){
			TreeMap<String, Double> tmp = suggester.boostScorer(k);
			for(String content:tmp.keySet()){
				Double d1 = listDoc.get(contentToDoc.get(content));
				if(d1==null){
					d1=new Double(0);
				}
				Double d2 = tmp.get(content);
				listDoc.put(contentToDoc.get(content), d1+d2);
			}
		}
		listDoc.remove(null);
		return listDoc;
	}

//	public static SolrClient createSolrServer() {
//		HttpSolrClient solr = null;
//		try {
//			solr = new HttpSolrClient("http://127.0.0.1:8983/solr/solr-qa");
//			solr.setConnectionTimeout(100);
//			solr.setDefaultMaxConnectionsPerHost(100);
//			solr.setMaxTotalConnections(100);
//		} catch (Exception e) {
//			System.out.println("请检查tomcat服务器或端口是否开启!");
//			e.printStackTrace();
//		}
//		return solr;
//	}
//
//	public static QueryResponse search(List<String> key, int start, int count, String[] sortfield,
//			Boolean[] flag, Boolean hightlight) {
//		if (null == sortfield || null == flag || sortfield.length != flag.length) {
//			return null;
//		}
//		String[] fields = new String[NewDiseaseDescription.class.getDeclaredFields().length-1];
//		List<String> tmp = new ArrayList<>();
//		for(Field f:NewDiseaseDescription.class.getDeclaredFields()){
//			String fieldName = f.getName();
//			if(fieldName.equals("serialVersionUID")){
//				continue;
//			}
//			tmp.add(fieldName);
//		}
//		tmp.toArray(fields);
//		SolrQuery query = null;
//		SolrClient solr = createSolrServer();
//		try {
//			StringBuffer sb = new StringBuffer();
//			sb.append(key.get(0));
//			query = new SolrQuery();
//			for (int i = 1; i < key.size(); i++) {
//				sb.append(" AND ");
//				sb.append(key.get(i));
//			}
//			query.setQuery(sb.toString());
//			query.setFields(fields);
//			// 设置起始位置与返回结果数
//			query.setStart(start);
//			query.setRows(count);
//			// 设置排序
//			for (int i = 0; i < sortfield.length; i++) {
//				if (flag[i]) {
//					query.addSort(sortfield[i], SolrQuery.ORDER.asc);
//				} else {
//					query.addSort(sortfield[i], SolrQuery.ORDER.desc);
//				}
//			}
//			// 设置高亮
//			if (null != hightlight) {
//				query.setHighlight(true); // 开启高亮组件
//				query.addHighlightField("title");// 高亮字段
//				query.setHighlightSimplePre("<font color=\"red\">");// 标记
//				query.setHighlightSimplePost("</font>");
//				query.setHighlightSnippets(1);// 结果分片数，默认为1
//				query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		QueryResponse rsp = null;
//		try {
//			rsp = solr.query(query);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		// 返回查询结果
//		return rsp;
//	}
}
