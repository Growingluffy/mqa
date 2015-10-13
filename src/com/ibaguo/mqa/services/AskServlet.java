package com.ibaguo.mqa.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.AnswerSearcher;
import com.ibaguo.mqa.intefaces.KeyWordExtract;
import com.ibaguo.mqa.intefaces.QuestionClassifier;
import com.ibaguo.mqa.pack.impl.NlpKeyWordExtract;
import com.ibaguo.mqa.pack.impl.SolrSearcher;
import com.ibaguo.mqa.util.Utils;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.model.maxent.MaxEnt;

public class AskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AskServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sent = request.getParameter("sent");
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		KeyWordExtract kwe = new NlpKeyWordExtract();
		List<String> kwList = kwe.extractKeyword(sent, 3);
		QuestionClassifier qc = MaxEnt.loadModel("QMaxEnt.dat");
		String questionType = qc.eval(kwList);
		AnswerSearcher as = new SolrSearcher();
		Map<String, String> kv = as.search(kwList, true);
		
		response.getWriter().println("<h1>" + Utils.toJson(kwList) + "</h1>");
		response.getWriter().println("session=" + request.getSession(true).getId());
	}
}