package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.CartService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.UserUtil;

/**
 * Servlet implementation class EmptyCartController
 */
public class EmptyCartController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmptyCartController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cartId = request.getParameter("cartId");

		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

		try {
			HttpSession session = request.getSession();

			if (!UserUtil.isGuestUser(session)) {
				CartService cartService = new CartServiceImpl();
				cartService.emptyCart(con, Long.parseLong(cartId));

			} else {
				session.setAttribute("guestCart", null);
			}

			session.setAttribute("itemCount", null);

			response.setContentType("text/plain");
			response.getWriter().write("Successfully deleted");
			response.setStatus(Constants.RESPONSE_OK);

		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.getWriter().write("Getting error while deleting the cart items");
			response.setStatus(Constants.RESPONSE_FORBIDDEN);
		}
	}

}
