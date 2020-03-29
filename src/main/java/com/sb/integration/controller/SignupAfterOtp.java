package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
import com.sb.integration.service.SecurityService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.SecurityServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class SignupAfterOtp
 */
@WebServlet("/signup_after_otp")
public class SignupAfterOtp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupAfterOtp() {
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
		HttpSession session = request.getSession();
		try{
			con.setAutoCommit(false);
			
			
			UserVo userVo = (UserVo) session.getAttribute("signp_data");
			String randomNumber = (String) session.getAttribute("signp_otp_random");
			
			String userRandomNumber = request.getParameter("otp");
			
			if(!randomNumber.equals(userRandomNumber)){
				throw new Exception("Otp number is not valid, Please try again");
			}
			
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(userVo.getPassword());

			userVo.setPassword(encryptedPassword);

			// If validation is getting successful then create new record in the
			// table called USERS.
			UserService userService = new UserServiceImpl();
			userService.registerNewUser(userVo, con);

			// Then create an empty cart for the new registered user.
			CartService cartService = new CartServiceImpl();
			cartService.createEmptyCart(con, userVo.getUserId());
			
			// Assign the role to new user.
			userService.createUserRole(con, userVo.getUserId(), Constants.REG_USER_ROLE_ID);

			session.removeAttribute("signp_data");
			session.removeAttribute("signp_otp_random");
			session.removeAttribute("otp_wrong_count");
			con.commit();

			RequestDispatcher rd = request.getRequestDispatcher("jsp/signup_success.jsp");
			request.setAttribute("username", userVo.getFullName());
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
			try {
				if(con!=null)
					con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(e.getMessage().equals("Otp number is not valid, Please try again")){
				Integer wrongOtpCount = (Integer)session.getAttribute("otp_wrong_count");
				if(wrongOtpCount==null){
					session.setAttribute("otp_wrong_count", new Integer(1));
				}else if(wrongOtpCount>1){
					RequestDispatcher rd = request.getRequestDispatcher("jsp/signup.jsp");
					request.setAttribute("error_msg", "Your two wrong attempt for otp is crossed.");
					rd.forward(request, response);
				}else{
					session.setAttribute("otp_wrong_count", wrongOtpCount+1);
				}
				
				RequestDispatcher rd = request.getRequestDispatcher("jsp/signup_otp.jsp");
				request.setAttribute("error_msg", e.getMessage());
				rd.forward(request, response);
			}else{
				request.setAttribute("error_msg", e.getMessage());
				RequestDispatcher rd = request.getRequestDispatcher("jsp/signup.jsp");
				rd.forward(request, response);
				e.printStackTrace();
			}
		}
		
	}
}
