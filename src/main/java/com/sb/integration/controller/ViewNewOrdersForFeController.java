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

import com.sb.integration.service.FeService;
import com.sb.integration.service.impl.FeServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.FieldExecutiveVo;

/**
 * Servlet implementation class ViewNewOrdersForFeController
 */
@WebServlet("/viewnewordersforfe")
public class ViewNewOrdersForFeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewNewOrdersForFeController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

			HttpSession session = request.getSession();
			FieldExecutiveVo feDetails = (FieldExecutiveVo) session.getAttribute("feDetails");
			
			System.out.println(feDetails);

			FeService feService = new FeServiceImpl();
			String newOrders = feService.getNewOrdersForFe(con, feDetails.getFieldExecutiveId());

			response.setContentType("application/json");
			response.getWriter().write(newOrders);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
