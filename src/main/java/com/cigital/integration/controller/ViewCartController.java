package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.cigital.integration.service.CartService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class ViewCartController
 */
public class ViewCartController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewCartController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartDetails cartDetails = null;
			HttpSession session = request.getSession();
			if(UserUtil.isGuestUser(session)){
				cartDetails = (CartDetails) session.getAttribute("guestCart");
			}else{
				UserVo userdetails = (UserVo) session.getAttribute("userDetails");
				
				CartService cartService = new CartServiceImpl();
				cartDetails = cartService.getActiveCartForUser(con, userdetails.getUserId());
			}
			
			request.setAttribute("cartDetails", cartDetails);

			RequestDispatcher rd = request.getRequestDispatcher("jsp/cart.jsp");
			rd.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
