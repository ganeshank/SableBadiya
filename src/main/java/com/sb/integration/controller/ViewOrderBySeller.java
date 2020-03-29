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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.SellerVo;

/**
 * Servlet implementation class ViewOrderBySeller
 */
@WebServlet("/vieworderbyseller")
public class ViewOrderBySeller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOrderBySeller() {
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
			
			HttpSession session = request.getSession();
			SellerVo sellerDetails = (SellerVo) session.getAttribute("sellerDetails");
			
			System.out.println(sellerDetails);
			System.out.println(cartId);
			// Fetch order details
			OrderService orderService = new OrderServiceImpl();
			CartDetails cartDetails = orderService.getOrderForSeller(con, Long.parseLong(cartId), sellerDetails.getSellerId());
			
			// Fetch DeliveryBoy details.
			SellerService sellerService = new SellerServiceImpl();
			FieldExecutiveVo feVo = sellerService.getFieldExecutiveForOrder(con, Long.parseLong(cartId));
			
			request.setAttribute("cartDetails", cartDetails);
			request.setAttribute("feVo", feVo);
			
			// For making the count of new orders be sync.
			session.setAttribute("isFirstCall", null);
			session.setAttribute("pageName", "View Order");
			
			RequestDispatcher rd = request.getRequestDispatcher("jsp/view_order_by_seller.jsp");
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
