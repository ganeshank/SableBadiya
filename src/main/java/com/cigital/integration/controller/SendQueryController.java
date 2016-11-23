package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cigital.integration.service.UserService;
import com.cigital.integration.service.impl.UserServiceImpl;
import com.cigital.integration.util.DataSourceUtil;

/**
 * Servlet implementation class SendQueryController
 */
@WebServlet("/sendquery")
public class SendQueryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendQueryController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String fullName = request.getParameter("name");
			String email = request.getParameter("email");
			String contact = request.getParameter("contact");
			String subject = request.getParameter("subject");
			String message = request.getParameter("message");
			
			/*String completeMessage = "Full Name: "+ fullName + "\n";
			completeMessage = completeMessage + "Email: "+ email + "\n";
			completeMessage = completeMessage + "Contact Number: "+ contact + "\n";
			completeMessage = completeMessage + "Subject: "+ subject + "\n";
			completeMessage = completeMessage + "Message: "+ message + "\n";
			
			MailConfig mailConfig = new MailConfig();
			mailConfig.sendTextMail("Customer Query", completeMessage, "cs@sablebadiya.com");*/
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			UserService userService = new UserServiceImpl();
			userService.saveCustomerQuery(con, fullName, email, contact, subject, message);
			
			request.getRequestDispatcher("jsp/send_query_success.jsp").forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
