package com.ibaguo.mqa.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.nlp.MyNLP;

public class KeyWordServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public KeyWordServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String sent = request.getParameter("sent");
			String num = request.getParameter("num");
			Integer rNum = Integer.valueOf(num);
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			List<String> kwList = MyNLP.extractKeyword(sent, rNum);
			response.getWriter().println("<h1>" + Arrays.toString(kwList.toArray(new String[kwList.size()])) + "</h1>");
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}