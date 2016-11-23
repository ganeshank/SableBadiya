package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.util.Constants;
import com.cigital.integration.util.DataSourceUtil;

/**
 * Servlet implementation class ViewNewOrdersController
 */
@WebServlet("/viewneworders")
public class ViewNewOrdersController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewNewOrdersController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			OrderService orderService = new OrderServiceImpl();
			//String newOrders = orderService.getNewPlacedOrders(con, Constants.ORDER_PLACED_BY_CUSTOMER_STATUS);
			String newOrders = orderService.getNewPlacedOrders(con, Constants.ORDER_CONFIRMED_BY_SELLER);
			
			response.setContentType("application/json");
	        response.getWriter().write(newOrders);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
