package com.ibaguo.mqa.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.QuestionClassifier;
import com.ibaguo.mqa.util.Utils;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.model.maxent.MaxEnt;

public class MaxEntServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public MaxEntServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String[] kwList = request.getParameter("kwList").split(",");
			response.setContentType("application/json;charset=utf-8"); 
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			QuestionClassifier maxEnt = MaxEnt.loadModel("QMaxEnt.dat");
			Map<String, Double> result = maxEnt.predict(Arrays.asList(kwList));
			response.getWriter().println(Utils.toJson(result));
			response.getWriter().println(maxEnt.eval(Arrays.asList(kwList)));
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}