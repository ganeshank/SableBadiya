package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.CartService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class TestAjaxController
 */
public class TestAjaxController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestAjaxController() {
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
			Integer itemCount = null;
			if(itemCount==null){
				UserVo userDetails = (UserVo)session.getAttribute("userDetails");
				CartService cartService = new CartServiceImpl();
				itemCount = cartService.getActiveCartItemCountForUser(con, userDetails.getUserId());
				session.setAttribute("itemCount", itemCount);
				
				System.out.println(itemCount);
				
				response.setContentType("text/plain");
		        response.getWriter().write(itemCount.toString());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
         
	}

}
