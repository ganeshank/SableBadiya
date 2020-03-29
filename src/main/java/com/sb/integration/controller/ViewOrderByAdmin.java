package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.CartService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.SellerVo;

/**
 * Servlet implementation class ViewOrderByAdmin
 */
@WebServlet("/vieworderadmin")
public class ViewOrderByAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOrderByAdmin() {
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
			
			String cartId = request.getParameter("cartId");
			
			// Fetch order details
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.getOrderById(con, Long.parseLong(cartId));
			
			// Fetch all sellers details
			SellerService sellerService = new SellerServiceImpl();
			List<SellerVo> sellerList = sellerService.getAllSeller(con);
			
			// Fetch DeliveryBoy details.
			List<FieldExecutiveVo> feList = sellerService.getAllFieldExecutive(con);
			
			request.setAttribute("cartDetails", cartDetails);
			request.setAttribute("sellerList", sellerList);
			request.setAttribute("feList", feList);
			
			// For making the count of new orders be sync.
			HttpSession session = request.getSession();
			session.setAttribute("isFirstCall", null);
			session.setAttribute("pageName", "View Order");
			
			RequestDispatcher rd = request.getRequestDispatcher("jsp/view_order_by_admin.jsp");
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
