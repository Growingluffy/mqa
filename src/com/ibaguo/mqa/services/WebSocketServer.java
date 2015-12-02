package com.ibaguo.mqa.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Example of setting up a Jetty WebSocket server
 * <p>
 * Note: this uses the Jetty WebSocket API, not the javax.websocket API.
 */
public class WebSocketServer {

	public static class HelloServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		private String msg = "Hello World!";

		public HelloServlet() {
		}

		public HelloServlet(String msg) {
			this.msg = msg;
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String key = request.getParameter("key");
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>" + key + "</h1>");
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}

	public static class GoodbyeServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		private String msg = "Goodbye!";

		public GoodbyeServlet() {
		}

		public GoodbyeServlet(String msg) {
			this.msg = msg;
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>" + msg + "</h1>");
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		// Add the echo socket servlet to the /echo path map
		context.addServlet(new ServletHolder(new HelloServlet()), "/hello");
		context.addServlet(new ServletHolder(new GoodbyeServlet()), "/goodbye");
		context.addServlet(new ServletHolder(new KeyWordServlet()), "/keyword");
		context.addServlet(new ServletHolder(new MaxEntServlet()), "/type");
		context.addServlet(new ServletHolder(new SolrSearchServlet()), "/solrquery");
		context.addServlet(new ServletHolder(new SegmentServlet()), "/seg");
		context.addServlet(new ServletHolder(new AskServlet()), "/ask");
		context.addServlet(new ServletHolder(new SynonymServlet()), "/synonym");
		context.addServlet(new ServletHolder(new RankWordServlet()), "/rankword");
		context.addServlet(new ServletHolder(new AskServlet2()), "/ask2");
		server.start();
		context.dumpStdErr();
		server.join();
	}
}