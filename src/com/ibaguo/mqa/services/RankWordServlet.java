package com.ibaguo.mqa.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.KeyWordExtract;
import com.ibaguo.mqa.json.JsonWordScore;
import com.ibaguo.mqa.json.KeywordScore;
import com.ibaguo.mqa.json.Status;
import com.ibaguo.mqa.pack.impl.NlpKeyWordExtract;
import com.ibaguo.mqa.util.Utils;

public class RankWordServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public RankWordServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String sent = request.getParameter("sent");
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			KeyWordExtract kwe = new NlpKeyWordExtract();
			List<KeywordScore> wordscore = kwe.rankWordScore(sent);
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Status status = new Status(1, "Success", now);
			response.getWriter().println(Utils.toJson(new JsonWordScore(status , wordscore)));
//			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}