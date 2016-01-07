package com.ibaguo.mqa.pack.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.json.DocRank;
import com.ibaguo.nlp.suggest.Suggester;

public class SolrSearcher3{

	public static SolrClient createSolrServer() {
		HttpSolrClient solr = null;
		try {
			solr = new HttpSolrClient("http://127.0.0.1:8983/solr/qa120");
			solr.setConnectionTimeout(100);
			solr.setDefaultMaxConnectionsPerHost(100);
			solr.setMaxTotalConnections(100);
		} catch (Exception e) {
			System.out.println("请检查tomcat服务器或端口是否开启!");
			e.printStackTrace();
		}
		return solr;
	}
	
	public static QueryResponse search(String q, int count) {
		SolrQuery query = null;
		SolrClient solr = createSolrServer();
		try {
			query = new SolrQuery();
			query.setQuery(q);
			// 设置起始位置与返回结果数
			query.setRows(count);
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
	
	public static List<DocRank> search(String q) {
		QueryResponse rsp = search(q,100);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
		Map<Doc, Double> listDoc = new HashMap<>();
		Map<String, Doc> contentToDoc = new HashMap<>();
		Suggester suggester = new Suggester();
		double MAX = Integer.MAX_VALUE*1.0;
		for (SolrDocument sd : sdl) {
			try {
				StringBuffer sb = new StringBuffer();
				Doc doc = new Doc(sd.getFieldValue("name").toString(),sd.getFieldValue("id").toString());
				for(String fn:sd.getFieldNames()){
					if(fn.equals("name")||fn.equals("id")) continue;
					Object obj = sd.getFieldValue(fn);
					if(obj instanceof String){
						String value = (String)obj;
						if(!value.equals("")){
							doc.putFieldVal(fn, value);
							sb.append(value+";");
							suggester.addSentence(value);
						}
					}else if(obj instanceof ArrayList){
						List<String> ans = (ArrayList<String>)obj ;
						if(ans.size()!=0&&!ans.get(0).equals("")){
							doc.putFieldVal(fn, ans.get(0));
							sb.append(ans.get(0)+";");
						}
					}
				}
				suggester.addSentence(sb.toString());
				if(contentToDoc.containsKey(sb.toString())){
//					MAX++;
				}else{
					contentToDoc.put(sb.toString(), doc);
					listDoc.put(doc, MAX);
					MAX--;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<DocRank> ret = new ArrayList<>();
		for(Doc doc:listDoc.keySet()){
			ret.add(new DocRank(doc, listDoc.get(doc)));
		}
		return ret ;
	}

}
