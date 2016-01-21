package com.ibaguo.mqa.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.json.Status;

public class AskServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AskServlet2() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sent = request.getParameter("sent");
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		System.out.println("-1");
//		QuestionToAnswer qa = IBaguoAsk2.getInstance();
//		List<Doc> result = qa.makeQa(sent);
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Status status = new Status(1, "Success", now);
//		response.getWriter().println(Utils.toJson(new JsonAskResult(status, new AskResult("solr-qa" , result))));
	}
}