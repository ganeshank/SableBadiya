package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.FeService;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.SecurityService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.FeServiceImpl;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SecurityServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.RoleVo;
import com.sb.integration.vo.SellerVo;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class LoginController
 */
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession httpSession = request.getSession();
		UserVo userDetails = (UserVo) httpSession.getAttribute("userDetails");
		
		if (userDetails != null) {
			response.sendRedirect("homepage");

		} else {

			RequestDispatcher rd = request.getRequestDispatcher("jsp/login.jsp");
			rd.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String emailContact = request.getParameter("email_contact");
		String password = request.getParameter("password");

		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

		try {
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);

			UserService userService = new UserServiceImpl();
			UserVo userDetails = userService.checkLoginCredential(emailContact, encryptedPassword, con);

			if (userDetails != null) {
				RoleVo roleVo = userService.getRoleForUser(con, userDetails.getUserId());
				HttpSession session = request.getSession();

				userDetails.setUserRole(roleVo);
				session.setAttribute("userDetails", userDetails);
				session.setAttribute("itemCount", null);

				System.out.println(roleVo.getRoleId());

				if (roleVo.getRoleId().equals(Constants.REG_USER_ROLE_ID.longValue())) {
					String isPlaceOrderRedirect = (String)session.getAttribute("place_order_redirect");
					if(isPlaceOrderRedirect==null){
						response.sendRedirect("homepage");
					}else if(isPlaceOrderRedirect.equals("true")){
						session.removeAttribute("place_order_redirect");
						CartDetails cartDetails = (CartDetails)session.getAttribute("guestCart");
						
						OrderService orderService = new OrderServiceImpl();
						orderService.replaceGuestCartToRegCart(con, userDetails, cartDetails);
						
						response.sendRedirect("placeorder");
					}else{
						response.sendRedirect("homepage");
					}
				} else if (roleVo.getRoleId().equals(Constants.ADMIN_USER_ROLE_ID.longValue())
						|| roleVo.getRoleId().equals(Constants.COADMIN_USER_ROLE_ID.longValue())) {
					session.setAttribute("pageName", "Admin Home");
					session.setAttribute("isFirstCall", null);
					response.sendRedirect("adminhome");
				} else if (roleVo.getRoleId().equals(Constants.SELLER_USER_ROLE_ID.longValue())) {
					SellerService sellerService = new SellerServiceImpl();
					SellerVo sellerDetails = sellerService.getSellerByUserId(con, userDetails.getUserId());
					
					session.setAttribute("sellerDetails", sellerDetails);
					session.setAttribute("pageName", "Seller Home");
					session.setAttribute("isFirstCall", null);
					response.sendRedirect("sellerhome");
				} else if (roleVo.getRoleId().equals(Constants.SALES_USER_ROLE_ID.longValue())) {
					FeService feService = new FeServiceImpl();
					FieldExecutiveVo feDetails = feService.getFeByUserId(con, userDetails.getUserId());
					
					session.setAttribute("feDetails", feDetails);
					session.setAttribute("pageName", "FE Home");
					session.setAttribute("isFirstCall", null);
					response.sendRedirect("fehome");
				} else {
					request.setAttribute("error_msg", "Something goes wrong, Please contact to customer care.");
					RequestDispatcher rd = request.getRequestDispatcher("jsp/login.jsp");
					rd.forward(request, response);
				}

			} else {
				request.setAttribute("error_msg", "Invalid credential. Please try again.");
				RequestDispatcher rd = request.getRequestDispatcher("jsp/login.jsp");
				rd.forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error_msg", "Something goes wrong, Please conatct to customer care.");
			RequestDispatcher rd = request.getRequestDispatcher("jsp/login.jsp");
			rd.forward(request, response);
		}
	}
}
