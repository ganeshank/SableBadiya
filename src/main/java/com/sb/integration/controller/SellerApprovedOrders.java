package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;

/**
 * Servlet implementation class SellerApprovedOrders
 */
@WebServlet("/sellerapprovedorders")
public class SellerApprovedOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SellerApprovedOrders() {
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
			String newOrders = orderService.getNewPlacedOrders(con, Constants.ORDER_CONFIRMED_BY_SELLER);

			response.setContentType("application/json");
	        response.getWriter().write(newOrders);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
