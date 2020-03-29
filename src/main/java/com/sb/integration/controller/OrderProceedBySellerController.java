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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.SellerVo;

/**
 * Servlet implementation class OrderProceedBySellerController
 */
@WebServlet("/orderproceedbyseller")
public class OrderProceedBySellerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderProceedBySellerController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		try{
			String cartId = request.getParameter("cartId");
			Map<Long, String> acceptRejectCartItemMap = new HashMap<Long, String>();
			
			String[] lines = request.getParameterValues("accept-reject");
			for (String string : lines) {
				String[] strSplit = string.split(" ");
				acceptRejectCartItemMap.put(Long.parseLong(strSplit[1]), strSplit[0]);
			}
			
			Map<Long, String> rejectReasonCartItemMap = new HashMap<Long, String>();
			
			String[] rejectReasonArray = request.getParameterValues("reject-reason");
			if(rejectReasonArray!=null){
				for (String rejectReasonStr : rejectReasonArray) {
					String[] strSplit = rejectReasonStr.split(" ");
					rejectReasonCartItemMap.put(Long.parseLong(strSplit[1]), strSplit[0]);
				}
			}
			
			HttpSession session = request.getSession();
			SellerVo sellerDetails = (SellerVo)session.getAttribute("sellerDetails");
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			con.setAutoCommit(false);
			
			SellerService sellerService = new SellerServiceImpl();
			sellerService.updateSellerCartItem(con, Long.parseLong(cartId), sellerDetails.getSellerId(), acceptRejectCartItemMap, rejectReasonCartItemMap);
			
			Integer status = sellerService.getOrderStatusBasedOnSellerCartItem(con, Long.parseLong(cartId));
			// update the status of the order(CART).
			Long updatedStatus = null;
			if(status==1){
				updatedStatus = Constants.ORDER_PENDING_BY_SELLER;
			}else if(status.equals(2)){
				updatedStatus = Constants.ORDER_CONFIRMED_BY_SELLER;
			}else{
				updatedStatus = Constants.ORDER_REJECTED_BY_SELLER;
			}
			
			OrderService orderService = new OrderServiceImpl();
			orderService.updateOrderStatus(con, updatedStatus, Long.parseLong(cartId),"");
			
			con.commit();
			response.sendRedirect("jsp/order_proceed_by_seller_successfully.jsp");

		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
