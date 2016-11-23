package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.UserService;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.service.impl.UserServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class MyAccountRedirect
 */
@WebServlet("/myaccount")
public class MyAccountRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyAccountRedirect() {
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
			
			HttpSession session = request.getSession();
			Boolean isGuestUser = UserUtil.isGuestUser(session);
			
			if(isGuestUser){
				throw new Exception("You do not have permission for that!!!");
			}else{
				UserVo userDetails = (UserVo)session.getAttribute("userDetails");
				
				UserService userService = new UserServiceImpl();
				List<AddressVo> addressVos = userService.getAddressesForUser(con, userDetails.getUserId());
				
				OrderService orderService = new OrderServiceImpl();
				List<CartDetails> cartDetails = orderService.getOrdersForUser(con, userDetails.getUserId());

				request.setAttribute("userDetails", userDetails);
				request.setAttribute("addressVos", addressVos);
				request.setAttribute("cartDetails", cartDetails);
				
				request.getRequestDispatcher("jsp/myaccount.jsp").forward(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
