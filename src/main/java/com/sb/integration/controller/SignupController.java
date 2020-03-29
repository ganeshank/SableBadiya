package com.sb.integration.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sb.integration.service.CartService;
import com.sb.integration.service.SecurityService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.ValidatorService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.SecurityServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.service.impl.ValidatorServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.SendOtp;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class SignupController
 */
public class SignupController extends HttpServlet {
	
	final static Logger logger = Logger.getLogger(SignupController.class);

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
		
		logger.debug("Signup process start.....");
		logger.debug("name....."+name);
		logger.debug("email....."+email);
		logger.debug("contact....."+contact);
		logger.debug("password....."+password);
		
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");

		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

		ValidatorService validatorService = new ValidatorServiceImpl();
		try {
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/application.properties");
			String walletInitialAmount = prop.getProperty("wallet.initial.amount");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setFullName(name);
			userVo.setMobileNumber(contact);
			userVo.setPassword(password);
			userVo.setWalletAmount(new BigDecimal(walletInitialAmount));
			
			con.setAutoCommit(false);
			validatorService.signupValidation(userVo,con);
			
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
			con.commit();
			
			/*HttpSession session = request.getSession();
			session.setAttribute("signp_data", userVo);
			
			String randomNumber = randomFourDigitNumber();
			session.setAttribute("signp_otp_random", randomNumber);
			
			String otpMessage = "Signup otp number is " + randomNumber ;
			
			SendOtp sendOtp = new SendOtp();
			sendOtp.sendSms(otpMessage, contact);
			System.out.println(otpMessage);*/
			
			/*RequestDispatcher rd = request.getRequestDispatcher("jsp/signup_otp.jsp");
			rd.forward(request, response);*/
			
			RequestDispatcher rd = request.getRequestDispatcher("jsp/signup_success.jsp");
			request.setAttribute("username", userVo.getFullName());
			rd.forward(request, response);

		} catch (Exception e) {
			// Validation is got failed....
			logger.error("error while creating a new account::::::"+e.getMessage());
			request.setAttribute("error_msg", e.getMessage());
			RequestDispatcher rd = request.getRequestDispatcher("jsp/signup.jsp");
			rd.forward(request, response);
			e.printStackTrace();
		}

	}
	
	private String randomFourDigitNumber(){
		Integer randomNum = ThreadLocalRandom.current().nextInt(2000, 10000);
		return randomNum+"";
	}

}
