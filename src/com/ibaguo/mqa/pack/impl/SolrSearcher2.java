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

public class SolrSearcher2{

	public static SolrClient createSolrServer() {
		HttpSolrClient solr = null;
		try {
			solr = new HttpSolrClient("http://127.0.0.1:8983/solr/qa2");
			solr.setConnectionTimeout(100);
			solr.setDefaultMaxConnectionsPerHost(100);
			solr.setMaxTotalConnections(100);
		} catch (Exception e) {
			System.out.println("请检查tomcat服务器或端口是否开启!");
			e.printStackTrace();
		}
		return solr;
	}
	
	public static QueryResponse search(String q, int count,List<String> symptoms) {
		SolrQuery query = null;
		SolrClient solr = createSolrServer();
		try {
			query = new SolrQuery();
			StringBuffer sb = new StringBuffer();
			if(symptoms.size()>0){
				sb.append(" or answer:").append(symptoms.get(0));
				for(int i=1;i<symptoms.size();i++ ){
					sb.append(" and answer:").append(symptoms.get(i));
				}
			}
			query.setQuery("question:"+q+sb.toString());
			System.out.println("question:"+q+sb.toString());
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
	
	public static List<DocRank> search(String q,List<String> symptoms) {
		QueryResponse rsp = search(q,100,symptoms);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
		double MAX = Integer.MAX_VALUE*1.0;
		List<DocRank> ret = new ArrayList<>();
		for (SolrDocument sd : sdl) {
			try {
				StringBuffer sb = new StringBuffer();
				Doc doc = new Doc(sd.getFieldValue("disease_name").toString(),sd.getFieldValue("id").toString());
				for(String fn:sd.getFieldNames()){
					if(fn.equals("disease_name")||fn.equals("id")) continue;
					Object obj = sd.getFieldValue(fn);
					if(obj instanceof String){
						String value = (String)obj;
						if(!value.equals("")){
							doc.putFieldVal(fn, value);
							sb.append(value+";");
						}
					}else if(obj instanceof ArrayList){
						List<String> ans = (ArrayList<String>)obj ;
						if(ans.size()!=0&&!ans.get(0).equals("")){
							doc.putFieldVal(fn, ans.get(0));
							sb.append(ans.get(0)+";");
						}
					}
				}
				;
				ret.add(new DocRank(doc, MAX--));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret ;
	}

}
