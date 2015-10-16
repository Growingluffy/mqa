package com.ibaguo.mqa.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.KeyWordSynonym;
import com.ibaguo.mqa.pack.impl.NlpWordSynonym;
import com.ibaguo.mqa.util.Utils;

public class SynonymServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public SynonymServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String word = request.getParameter("word");
			String desc = request.getParameter("desc");
			Boolean sortDesc = Boolean.valueOf(desc);
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			KeyWordSynonym synonym = new NlpWordSynonym();
			List<String> synonymList = synonym.getSynonymList(word, sortDesc);
			response.getWriter().println(Utils.toJson(synonymList));
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}