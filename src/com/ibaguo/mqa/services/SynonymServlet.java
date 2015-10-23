package com.ibaguo.mqa.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.WordSynonym;
import com.ibaguo.mqa.json.JsonWordScore;
import com.ibaguo.mqa.json.KeywordScore;
import com.ibaguo.mqa.json.Status;
import com.ibaguo.mqa.pack.impl.NlpWordSynonym;
import com.ibaguo.mqa.util.Utils;

public class SynonymServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public SynonymServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String word = request.getParameter("word");
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			WordSynonym synonym = new NlpWordSynonym();
			List<KeywordScore> aa = new ArrayList<>();
			Map<String, Double> synonymMap = synonym.getSynonymList(word);
			for(String type:synonymMap.keySet()){
				aa.add(new KeywordScore(type, synonymMap.get(type)));
			}
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Status status = new Status(1, "Success", now);
			response.getWriter().println(Utils.toJson(new JsonWordScore(status, aa)));
		}
	}