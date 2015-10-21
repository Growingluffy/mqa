package com.ibaguo.mqa.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.json.JsonSolrResult;
import com.ibaguo.mqa.json.SolrResult;
import com.ibaguo.mqa.json.Status;
import com.ibaguo.mqa.pack.impl.SolrSearcher;
import com.ibaguo.mqa.util.Utils;

public class SolrSearchServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public SolrSearchServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String[] kw = request.getParameter("kw").split(",");
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			Map<Doc, Double> listDoc = new SolrSearcher().search(kw);
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Status status = new Status(1, "Success", now);
			response.getWriter().println(Utils.toJson(new JsonSolrResult(status, new SolrResult("solr-qa" , listDoc))));
		}

	}