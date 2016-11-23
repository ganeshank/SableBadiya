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

import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.vo.AddressVo;

/**
 * Servlet implementation class EditAddressRedirect
 */
@WebServlet("/editaddress")
public class EditAddressRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditAddressRedirect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String addressId = request.getParameter("addressId");
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			OrderService orderService = new OrderServiceImpl();
			AddressVo addressVo = orderService.getAddressForId(con, Long.parseLong(addressId));
			
			request.setAttribute("addressForEdit", addressVo);
			request.getRequestDispatcher("jsp/new_address.jsp").forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
