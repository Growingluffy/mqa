package com.ibaguo.mqa.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.QuestionClassifier;
import com.ibaguo.mqa.json.ClassificationScore;
import com.ibaguo.mqa.json.JsonClassification;
import com.ibaguo.mqa.json.Status;
import com.ibaguo.mqa.util.Utils;
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
			QuestionClassifier maxEnt = MaxEnt.loadModel("NEWDZZTYP-Trained2.dat");
			Map<String, Double> result = maxEnt.predict(Arrays.asList(kwList));
			List<ClassificationScore> aa= new ArrayList<>();
			for(String type:result.keySet()){
				aa.add(new ClassificationScore(type, result.get(type)));
			}
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Status status = new Status(1, "Success", now);
			response.getWriter().println(Utils.toJson(new JsonClassification(status, aa)));
//			response.getWriter().println(maxEnt.eval(Arrays.asList(kwList)));
		}
	}