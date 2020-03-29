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
 * Servlet implementation class ProcessPendingRejectOrderController
 */
@WebServlet("/processpendingrejectorder")
public class ProcessPendingRejectOrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessPendingRejectOrderController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<Long, Long> sellerCartItemMap = new HashMap<Long, Long>();
		Long cartId = Long.parseLong(request.getParameter("cartId"));
		String fieldExecutiveId = request.getParameter("fieldEx");
		
		String[] rejectSellerArray = request.getParameterValues("sellers-for-reject");
		if(rejectSellerArray!=null){
			for (String string : rejectSellerArray) {
				if(!string.equalsIgnoreCase("select")){
					String[] strSplit = string.split(" ");
					sellerCartItemMap.put(Long.parseLong(strSplit[1]), Long.parseLong(strSplit[0]));
				}
			}
		}
		
		String[] pendingSellerArray = request.getParameterValues("sellers-for-pending");
		if(pendingSellerArray!=null){
			for (String string : pendingSellerArray) {
				if(!string.equalsIgnoreCase("select")){
					String[] strSplit = string.split(" ");
					sellerCartItemMap.put(Long.parseLong(strSplit[1]), Long.parseLong(strSplit[0]));
				}
			}
		}
		
		Connection con = null;
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			con.setAutoCommit(false);
			
			OrderService orderService = new OrderServiceImpl();
			orderService.updateOrderStatus(con, Constants.SELLER_IS_CHANGED_FOR_ORDER_ITEM, cartId, "");
			orderService.updateOrderStatus(con, Constants.ORDER_PENDING_BY_SELLER, cartId, "");
			
			SellerService sellerService = new SellerServiceImpl();
			sellerService.inActiveFeForOrder(con, cartId);
			sellerService.persistOrderForFe(con, cartId, Integer.parseInt(fieldExecutiveId));
			
			orderService.reassignPendingRejectItems(con, sellerCartItemMap);
			//sellerService.persistSellerWithCartItem(con, sellerCartItemMap);
			
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
