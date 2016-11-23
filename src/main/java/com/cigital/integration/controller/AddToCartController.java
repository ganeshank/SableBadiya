package com.cigital.integration.controller;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class AddToCartController
 */
public class AddToCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToCartController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String goodsId = request.getParameter("goodsId");
		String quantityId = request.getParameter("quantityId");
		String sellerId = request.getParameter("sellerId");
		String rupee = request.getParameter("rupee");
		
		System.out.println(goodsId+"....."+quantityId+"...."+sellerId);
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			cartService.addToCart(con, Long.parseLong(goodsId), Long.parseLong(quantityId), Long.parseLong(sellerId), request, new BigDecimal(rupee));
			
			Integer itemCount = null;
			HttpSession session = request.getSession();
			if(!UserUtil.isGuestUser(session)){
				UserVo userDetails = (UserVo) session.getAttribute("userDetails");
				itemCount = cartService.getActiveCartItemCountForUser(con, userDetails.getUserId());
			}else{
				CartDetails cartDetails = (CartDetails) session.getAttribute("guestCart");
				itemCount = cartDetails.getCartItems().size();
			}
			
			session.setAttribute("itemCount", itemCount);
			
			response.setContentType("text/plain");
		    response.getWriter().write(itemCount.toString());
		    response.setStatus(200);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
