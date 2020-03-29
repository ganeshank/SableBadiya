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
import com.sb.integration.service.SecurityService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.SecurityServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class ChangePasswordController
 */
@WebServlet("/change_password")
public class ChangePasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePasswordController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			
			HttpSession session = request.getSession();
			UserVo userDetails = (UserVo)session.getAttribute("userDetails");
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			// Reset the password for following users.
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedOldPassword = securityService.getEncryptedPassword(oldPassword);
			
			UserService userService = new UserServiceImpl();
			UserVo user = userService.checkLoginCredential(userDetails.getMobileNumber(), encryptedOldPassword, con);
			
			if (user != null) {
				String encryptedNewPassword = securityService.getEncryptedPassword(newPassword);
				cartService.resetPassword(con, userDetails.getEmail(), encryptedNewPassword);
				
				String subject = "Changed Password!";
				String body = "Congratulations your password is reset successfully, Your new password is now : " + newPassword + ".";
				
				// Send an email for change password.
				/*MailConfig mailConfig = new MailConfig();
				mailConfig.sendTextMail(subject, body, userDetails.getEmail());*/
				
				response.setContentType("text/plain");
		        response.getWriter().write("password is modified successfully.");
			}else{
				response.setContentType("text/plain");
		        response.getWriter().write("given user credentials is wrong.");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			response.setContentType("text/plain");
	        response.getWriter().write("Something went wrong, Please contact to customer care.");
		}
	}

}
