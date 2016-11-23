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
import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class AddressSelectedController
 */
public class AddressSelectedController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddressSelectedController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			
			String addressId = request.getParameter("addressId");

			HttpSession session = request.getSession();
			UserVo userDetails = (UserVo)session.getAttribute("userDetails");
			CartDetails cartDetails = new CartDetails();
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			if(UserUtil.isGuestUser(session)){
				cartDetails = (CartDetails)session.getAttribute("guestCart");
			}else{
				CartService cartService = new CartServiceImpl();
				cartDetails = cartService.getActiveCartForUser(con, userDetails.getUserId());
			}
			
			
			OrderService orderService = new OrderServiceImpl();
			AddressVo orderShippingAddress = orderService.getAddressForId(con, Long.parseLong(addressId));
			
			cartDetails.setShippingAddress(orderShippingAddress);
			
			session.setAttribute("placeOrderCart", cartDetails);
			
			response.setContentType("text/plain");
		    response.getWriter().write("Address is selected for this order..");
		    response.setStatus(200);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
