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

import com.sb.integration.service.CartService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;

/**
 * Servlet implementation class CreateOfflineOrder
 */
@WebServlet("/create_offline_order")
public class CreateOfflineOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateOfflineOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String customerName = request.getParameter("customer_name");
			String contactNumber = request.getParameter("mobile_number");
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			String orderDate = request.getParameter("order_date");
			
			// TODO: Validate input data is not done.
			
			HttpSession session = request.getSession();
			CartDetails cartDetails = (CartDetails)session.getAttribute("offline_order");
			
			cartDetails.setOrderedBy(customerName);
			cartDetails.setOrderDate(TimeStamp.getTimeStampForString(orderDate));
			
			AddressVo shippingAddress = new AddressVo();
			shippingAddress.setContactName(customerName);
			shippingAddress.setContactNumber(contactNumber);
			shippingAddress.setEmail(email);
			shippingAddress.setAddressLine1(address);
			
			cartDetails.setShippingAddress(shippingAddress);
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			cartDetails.setIsOfflineOrder(true);
			CartService cartService = new CartServiceImpl();
			cartService.placeOrder(con, cartDetails, session);
			
			response.sendRedirect("adminhome");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
