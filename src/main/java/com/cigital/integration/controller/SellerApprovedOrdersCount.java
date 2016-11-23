package com.cigital.integration.controller;

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

import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.util.Constants;
import com.cigital.integration.util.DataSourceUtil;

/**
 * Servlet implementation class SellerApprovedOrdersCount
 */
@WebServlet("/getapprovedorderscount")
public class SellerApprovedOrdersCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SellerApprovedOrdersCount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
		
		HttpSession session = request.getSession();
		Boolean isFirstCall = (Boolean) session.getAttribute("isFirstCall");
		PrintWriter writer = null;
		try {
			OrderService orderService = new OrderServiceImpl();
			int newOrderCount = orderService.getSellerApprovedOrderCount(con, Constants.ORDER_CONFIRMED_BY_SELLER);

			response.setContentType("text/event-stream");
			response.setCharacterEncoding("UTF-8");

			writer = response.getWriter();

			writer.write("data: " + newOrderCount + "\n\n");

			if(isFirstCall!=null){
				Thread.sleep(120000);
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
