package com.sb.integration.controller;

import java.io.IOException;
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
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.SellerVo;

/**
 * Servlet implementation class ViewNewOrdersForSellerController
 */
@WebServlet("/viewnewordersforseller")
public class ViewNewOrdersForSellerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewNewOrdersForSellerController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

			HttpSession session = request.getSession();
			SellerVo sellerDetails = (SellerVo) session.getAttribute("sellerDetails");
			
			System.out.println(sellerDetails);

			OrderService orderService = new OrderServiceImpl();
			String newOrders = orderService.getNewPlacedOrdersForSeller(con, Constants.ORDER_PROCESSED_BY_COADMIN,
					sellerDetails.getSellerId());

			response.setContentType("application/json");
			response.getWriter().write(newOrders);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}