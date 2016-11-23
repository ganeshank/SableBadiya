package com.cigital.integration.controller;

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

import com.cigital.integration.service.UserService;
import com.cigital.integration.service.impl.UserServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.vo.UserVo;
import com.google.gson.Gson;

/**
 * Servlet implementation class SavePersonalInfoController
 */
@WebServlet("/save_personal_info")
public class SavePersonalInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SavePersonalInfoController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String modifiedName = request.getParameter("name");
			String modifiedEmail = request.getParameter("email");
			String modifiedMobile = request.getParameter("mobile");
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			HttpSession session = request.getSession();
			UserVo user = (UserVo)session.getAttribute("userDetails");
			
			user.setFullName(modifiedName);
			user.setEmail(modifiedEmail);
			user.setMobileNumber(modifiedMobile);
			
			UserService userService = new UserServiceImpl();
			userService.updateUserPersonalInfo(con, user);
			
			session.setAttribute("userDetails", user);
			
			response.setContentType("application/json");
	        response.getWriter().write(new Gson().toJson(user));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
