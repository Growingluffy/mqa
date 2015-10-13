package com.ibaguo.mqa.pack.impl;

import java.lang.reflect.Field;
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

import com.ibaguo.mqa.intefaces.AnswerSearcher;
import com.ibaguo.mqa.solr.NewDiseaseDescription;

public class SolrSearcher implements AnswerSearcher{

	@Override
	public Map<String,String> search(List<String> key, int start, int count, boolean sortDesc) {
		QueryResponse rsp = search(key, start, count, new String[] {}, new Boolean[] {}, true);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
		Map<String,String>  answers = new HashMap<>();
		for (SolrDocument sd : sdl) {
			for(String fn:sd.getFieldNames()){
				if(fn.equals("name")) continue;
				Object obj = sd.getFieldValue(fn);
				if(obj instanceof String){
					String value = (String)obj;
					if(!value.equals("")){
						answers.put(fn, value);
					}
				}else if(obj instanceof ArrayList){
					List<String> ans = (ArrayList<String>)obj ;
					if(ans.size()!=0&&!ans.get(0).equals("")){
						answers.put(fn,ans.get(0));
					}
				}
			};
		}
		return answers;
	}

	@Override
	public Map<String,String> search(List<String> key, boolean sortDesc) {
		QueryResponse rsp = search(key, 0, 10, new String[] {}, new Boolean[] {}, true);
		SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
		Map<String,String>  answers = new HashMap<>();
		for (SolrDocument sd : sdl) {
			for(String fn:sd.getFieldNames()){
				if(fn.equals("name")) continue;
				Object obj = sd.getFieldValue(fn);
				if(obj instanceof String){
					String value = (String)obj;
					if(!value.equals("")){
						answers.put(fn, value);
					}
				}else if(obj instanceof ArrayList){
					List<String> ans = (ArrayList<String>)obj ;
					if(ans.size()!=0&&!ans.get(0).equals("")){
						answers.put(fn,ans.get(0));
					}
				}
			};
		}
		return answers;
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

	public static QueryResponse search(List<String> key, int start, int count, String[] sortfield,
			Boolean[] flag, Boolean hightlight) {
		if (null == sortfield || null == flag || sortfield.length != flag.length) {
			return null;
		}
		String[] fields = new String[NewDiseaseDescription.class.getDeclaredFields().length-1];
		List<String> tmp = new ArrayList<>();
		for(Field f:NewDiseaseDescription.class.getDeclaredFields()){
			String fieldName = f.getName();
			if(fieldName.equals("serialVersionUID")){
				continue;
			}
			tmp.add(fieldName);
		}
		tmp.toArray(fields);
		SolrQuery query = null;
		SolrClient solr = createSolrServer();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(key.get(0));
			query = new SolrQuery();
			for (int i = 1; i < key.size(); i++) {
				sb.append(" AND ");
				sb.append(key.get(i));
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
