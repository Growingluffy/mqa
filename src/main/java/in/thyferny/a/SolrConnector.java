package in.thyferny.a;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.GroupParams;

public class SolrConnector {
	public static void main(String[] args) throws SolrServerException, IOException {
			QueryResponse rsp = search(new String[]{"name","symptom"}, new String[]{"腹泻","呕吐"}, 0, 10, new String[]{}, new Boolean[]{}, true);
			System.out.println(rsp);
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

	public static QueryResponse search(String[] field, String[] key, int start,

	int count, String[] sortfield, Boolean[] flag, Boolean hightlight) {

		if (null == field || null == key || field.length != key.length) {
			return null;
		}

		if (null == sortfield || null == flag || sortfield.length != flag.length) {

			return null;

		}

		SolrQuery query = null;

		SolrClient solr = createSolrServer();
		try {

			// 初始化查询对象

			query = new SolrQuery(field[0] + ":" + key[0]);

			for (int i = 0; i < field.length; i++) {

				query.addFilterQuery(field[i] + ":" + key[i]);

			}

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
