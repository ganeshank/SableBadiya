package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cigital.integration.config.MailConfig;
import com.cigital.integration.config.RandomStringGeneration;
import com.cigital.integration.service.CartService;
import com.cigital.integration.service.SecurityService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.service.impl.SecurityServiceImpl;
import com.cigital.integration.util.DataSourceUtil;

/**
 * Servlet implementation class ForgotPasswordController
 */
public class ForgotPasswordController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ForgotPasswordController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		try {
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

			CartService cartService = new CartServiceImpl();
			Boolean isEmailValid = cartService.isEmailValid(con, email);
			
			if(isEmailValid){
				// Reset the password for following users.
				SecurityService securityService = new SecurityServiceImpl();
				String randomPass = RandomStringGeneration.getRandomString(15);
				String encryptedPassword = securityService.getEncryptedPassword(randomPass);
				
				System.out.println("random pass---" + randomPass);
				
				cartService.resetPassword(con, email, encryptedPassword);
				
				String subject = "Changed Password!";
				String body = "Congratulations your password is reset successfully, Your new password is now : " + randomPass + ".\n";
				
				body = body + "You can change your password by Login-> Home-> MyAccount-> Password Change.";
				// Send an email for change password.
				MailConfig mailConfig = new MailConfig();
				mailConfig.sendTextMail(subject, body, email);
				
				RequestDispatcher rd = request.getRequestDispatcher("jsp/forgot_password_success.jsp");
				rd.forward(request, response);
				
			}else{
				System.out.println("Entered email id is not found in our records.");
				
				RequestDispatcher rd = request.getRequestDispatcher("jsp/forgot_password.jsp");
				request.setAttribute("errorMsg", "Entered email id is not found in our records.");
				rd.forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
