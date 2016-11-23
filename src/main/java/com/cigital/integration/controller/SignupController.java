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

import com.cigital.integration.service.CartService;
import com.cigital.integration.service.SecurityService;
import com.cigital.integration.service.UserService;
import com.cigital.integration.service.ValidatorService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.service.impl.SecurityServiceImpl;
import com.cigital.integration.service.impl.UserServiceImpl;
import com.cigital.integration.service.impl.ValidatorServiceImpl;
import com.cigital.integration.util.Constants;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class SignupController
 */
public class SignupController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignupController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher("jsp/signup.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String contact = request.getParameter("contactnumber");
		String password = request.getParameter("password");

		UserVo userVo = new UserVo();
		userVo.setEmail(email);
		userVo.setFullName(name);
		userVo.setMobileNumber(contact);
		userVo.setPassword(password);
		
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");

		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

		ValidatorService validatorService = new ValidatorServiceImpl();
		try {
			con.setAutoCommit(false);
			validatorService.signupValidation(userVo,con);

			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);

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

			con.commit();

			RequestDispatcher rd = request.getRequestDispatcher("jsp/signup_success.jsp");
			request.setAttribute("username", userVo.getFullName());
			rd.forward(request, response);

			// insert into users table.

		} catch (Exception e) {
			// Validation is got failed....
			request.setAttribute("error_msg", e.getMessage());
			RequestDispatcher rd = request.getRequestDispatcher("jsp/signup.jsp");
			rd.forward(request, response);
			e.printStackTrace();
		}

	}

}
