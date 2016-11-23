package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;

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
import com.cigital.integration.vo.CartDetails;
import com.google.gson.Gson;

/**
 * Servlet implementation class QuantityChangeController
 */
public class QuantityChangeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuantityChangeController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cartItemId = request.getParameter("cartItemId");
		String quantityId = request.getParameter("quantity");
		
		Boolean isQuantityRupee = false;
		if(quantityId.contains("Rs")){
			isQuantityRupee = true;
			quantityId = quantityId.split(" ")[0];
		}

		System.out.println(cartItemId + "....." + quantityId);

		try {
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			HttpSession session = request.getSession();

			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.updateQuantityCartItem(con, Long.parseLong(quantityId),
					Long.parseLong(cartItemId), session, isQuantityRupee);

			session.setAttribute("itemCount", null);

			Gson gson = new Gson();
			String cartDetailsJson = gson.toJson(cartDetails);
			response.setContentType("application/json");
			response.getWriter().write(cartDetailsJson);
			response.setStatus(200);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
