package com.sb.integration.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.util.DataSourceUtil;

/**
 * Servlet implementation class GetNewOrdersController
 */
@WebServlet("/getneworders")
public class GetNewOrdersController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetNewOrdersController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
		
		HttpSession session = request.getSession();
		Boolean isFirstCall = (Boolean) session.getAttribute("isFirstCall");
		PrintWriter writer = null;
		try {
			OrderService orderService = new OrderServiceImpl();
			int newOrderCount = orderService.getNewPlacedOrderCount(con);

			response.setContentType("text/event-stream");
			response.setCharacterEncoding("UTF-8");

			writer = response.getWriter();

			writer.write("data: " + newOrderCount + "\n\n");

			if(isFirstCall!=null){
				Thread.sleep(5000);
			}
			else{
				session.setAttribute("isFirstCall", false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

}
