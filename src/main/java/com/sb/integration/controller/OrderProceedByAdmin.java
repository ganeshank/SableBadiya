package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;

/**
 * Servlet implementation class OrderProceedByAdmin
 */
@WebServlet("/orderproceedbyadmin")
public class OrderProceedByAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderProceedByAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cartId = request.getParameter("cartId");
		String fieldExecutiveId = request.getParameter("fieldEx");
		
		System.out.println(cartId+".........."+fieldExecutiveId);
		Map<Long, Long> sellerCartItemMap = new HashMap<Long, Long>();
		
		String[] lines = request.getParameterValues("sellers");
		for (String string : lines) {
			String[] strSplit = string.split(" ");
			sellerCartItemMap.put(Long.parseLong(strSplit[1]), Long.parseLong(strSplit[0]));
		}
		Connection con = null;
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			con.setAutoCommit(false);
			
			OrderService orderService = new OrderServiceImpl();
			orderService.updateOrderStatus(con, Constants.ORDER_PROCESSED_BY_COADMIN, Long.parseLong(cartId), "");
			
			SellerService sellerService = new SellerServiceImpl();
			sellerService.persistOrderForFe(con, Long.parseLong(cartId), Integer.parseInt(fieldExecutiveId));
			
			sellerService.persistSellerWithCartItem(con, sellerCartItemMap);
			con.commit();
			
			response.sendRedirect("jsp/order_proceed_by_admin_successfully.jsp");
		}catch(Exception e){
			if(con!=null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
