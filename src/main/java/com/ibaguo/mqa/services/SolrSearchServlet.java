package com.ibaguo.mqa.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.ibaguo.mqa.solr.SolrConnector;

public class SolrSearchServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public SolrSearchServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String[] kw = request.getParameter("kw").split(",");
			int size = Integer.valueOf(request.getParameter("size"));
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			QueryResponse rsp = SolrConnector.search(kw, 0, 10, new String[] {}, new Boolean[] {}, true);
			SolrDocumentList sdl = (SolrDocumentList) rsp.getResponse().get("response");
			List<String> answers = new ArrayList<>();
			for (SolrDocument sd : sdl) {
				response.getWriter().println("<h1>" + sd.getFieldValue("name") + "</h1>");
				for(String fn:sd.getFieldNames()){
					if(fn.equals("name")) continue;
					Object obj = sd.getFieldValue(fn);
					if(obj instanceof String){
						String value = (String)obj;
						if(!value.equals("")){
							response.getWriter().println("\t"+value);
						}
					}else if(obj instanceof ArrayList){
						ArrayList<String> ans = (ArrayList<String>)obj ;
						if(ans.size()!=0&&!ans.get(0).equals("")){
//							List<String> summary = MyNLP.extractSummary(ans.get(0), 1);//depends on if need to summary the answer
							response.getWriter().println("\t"+ans.get(0));
						}
					}
				};
			}
			response.getWriter().println("<h1></h1>");
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}