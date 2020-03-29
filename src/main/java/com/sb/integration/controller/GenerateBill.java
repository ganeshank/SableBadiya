package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sb.integration.service.CartService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.CartDetails;

/**
 * Servlet implementation class GenerateBill
 */
@WebServlet("/generate_bill")
public class GenerateBill extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenerateBill() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String cartId = request.getParameter("cartId");
			
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.getOrderById(con, Long.parseLong(cartId));
			
			RequestDispatcher rd = request.getRequestDispatcher("jsp/generate_bill.jsp");
			request.setAttribute("orderDetails", cartDetails);
			rd.forward(request, response);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
