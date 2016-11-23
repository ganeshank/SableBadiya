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
import com.cigital.integration.vo.SearchRequestVo;

/**
 * Servlet implementation class OrderSearchController
 */
@WebServlet("/ordersearch")
public class OrderSearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderSearchController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String searchBy = request.getParameter("searchBy");
			SearchRequestVo searchRequestVo = new SearchRequestVo();
			searchRequestVo.setSearchBy(searchBy);
			
			if(searchBy.equals("orderDate")){
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				
				searchRequestVo.setFromDate(fromDate);
				searchRequestVo.setToDate(toDate);
			}else{
				String searchValue = request.getParameter("searchValue");
				searchRequestVo.setSearchValue(searchValue);
			}
			
			OrderService orderService = new OrderServiceImpl();
			String jsonCartDetails = orderService.searchOrder(con, searchRequestVo);
			
			response.setContentType("application/json");
	        response.getWriter().write(jsonCartDetails);
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
