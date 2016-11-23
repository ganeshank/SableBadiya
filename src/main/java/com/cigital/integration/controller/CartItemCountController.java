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
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class CartItemCountController
 */
public class CartItemCountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartItemCountController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
		
		try{
			HttpSession session = request.getSession();
			Boolean isGuestUser = UserUtil.isGuestUser(session);
			
			Integer itemCount = null;
			if(!isGuestUser){
				// Registered user.
				itemCount = (Integer)session.getAttribute("itemCount");
				if(itemCount==null){
					UserVo userDetails = (UserVo)session.getAttribute("userDetails");
					
					CartService cartService = new CartServiceImpl();
					itemCount = cartService.getActiveCartItemCountForUser(con, userDetails.getUserId());
					session.setAttribute("itemCount", itemCount);
				}
			}
			else{
				// Guest user.
				CartDetails cartDetails = (CartDetails)session.getAttribute("guestCart");
				if(cartDetails!=null){
					if(cartDetails.getCartItems()!=null){
						itemCount = cartDetails.getCartItems().size();
					}else{
						itemCount = 0;
					}
				}else{
					itemCount = 0;
				}
			}
			
			System.out.println(itemCount);
			response.setContentType("text/plain");
	        response.getWriter().write(itemCount.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
