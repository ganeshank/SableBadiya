package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.config.MailConfig;
import com.sb.integration.service.CartService;
import com.sb.integration.service.FeService;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.FeServiceImpl;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.SMSService;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class FeHomeController
 */
@WebServlet({"/fehome","/fehome/*"})
public class FeHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeHomeController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private static final String HOME_DEFAULT_VIEW = "/fehome";
    private static final String NEW_ORDERS_FOR_FE = "/new_orders_for_fe";
    private static final String VIEW_NEW_ORDERS = "/view_new_order_fe";
    private static final String ORDER_STATUS_FOR_UPDATE = "/get_order_status_for_update";
    private static final String UPDATE_ORDER_STATUS = "/update_order_status";
    private static final String CANCEL_ORDER_OR_ITEM = "/cancle_order_or_item";
    
    private static final String DISPATCH_FE_DEFAULT_VIEW = "/jsp/fe_home.jsp";
    private static final String DISPATCH_NEW_ORDERS_FOR_FE = "/jsp/newordersforfe.jsp";
    private static final String DISPATCH_VIEW_NEW_ORDERS = "/jsp/view_new_order_fe.jsp";
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equalsIgnoreCase(HOME_DEFAULT_VIEW)) {

			request.getRequestDispatcher(DISPATCH_FE_DEFAULT_VIEW).forward(request, response);
		}else if(pathInfo.equalsIgnoreCase(NEW_ORDERS_FOR_FE)){
			
			request.getRequestDispatcher(DISPATCH_NEW_ORDERS_FOR_FE).forward(request, response);
		}else if(pathInfo.equalsIgnoreCase(VIEW_NEW_ORDERS)){

			dispatchToViewOrderForFe(request, response);
		}else if(pathInfo.equalsIgnoreCase(ORDER_STATUS_FOR_UPDATE)){

			getOrderStatus(request, response);
		}else if(pathInfo.equalsIgnoreCase(UPDATE_ORDER_STATUS)){

			updateOrderStatus(request, response);
		}else if(pathInfo.equalsIgnoreCase(CANCEL_ORDER_OR_ITEM)){

			cancelOrderOrItem(request, response);
		}
	}
	
	private void dispatchToViewOrderForFe(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String cartId = request.getParameter("cartId");
			
			HttpSession session = request.getSession();
			FieldExecutiveVo feDetails = (FieldExecutiveVo) session.getAttribute("feDetails");
			
			System.out.println(feDetails);
			System.out.println(cartId);
			// Fetch order details
			FeService feService = new FeServiceImpl();
			CartDetails cartDetails = feService.getOrderForFe(con, Long.parseLong(cartId), feDetails.getFieldExecutiveId());
			
			request.setAttribute("cartDetails", cartDetails);
			
			// For making the count of new orders be sync.
			session.setAttribute("isFirstCall", null);
			session.setAttribute("pageName", "View Order");
			
			RequestDispatcher rd = request.getRequestDispatcher(DISPATCH_VIEW_NEW_ORDERS);
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void getOrderStatus(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String cartId = request.getParameter("cartId");
			
			OrderService orderService = new OrderServiceImpl();
			Integer orderStatus = orderService.getOrderStatus(con, Long.parseLong(cartId));
			
			response.setContentType("text/plain");
		    response.getWriter().write(orderStatus.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String cartId = request.getParameter("cartId");
			
			OrderService orderService = new OrderServiceImpl();
			Integer orderStatus = orderService.getOrderStatus(con, Long.parseLong(cartId));
			
			String rejectReasonIf = "";
			if(orderStatus.equals(11)){
				orderStatus = 5;
			}else if(orderStatus.equals(5)){
				orderStatus = 6;
			}else{
				throw new Exception("Something went wrong please contact to customer support.");
			}
			
			orderService.updateOrderStatus(con, orderStatus.longValue(), Long.parseLong(cartId), rejectReasonIf);
			
			if(orderStatus.equals(6)){
				CartService cartService = new CartServiceImpl();
				CartDetails orderDetails = cartService.getOrderById(con, Long.parseLong(cartId));
				// Send the mail & msg to customer.
				String message = "Order "+orderDetails.getOrderNumber() +" has been delivered successfully, please do not reply.";
				// Send a mail to registered Email Id.
				
				LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
				Properties prop = propertiesFile.loadProperties("/cart.properties");
				
				if(!prop.getProperty("order.confirm.sms.off").equals("true")){
					SMSService smsService = new SMSService();
					String[] numbers = {orderDetails.getShippingAddress().getContactNumber()};
					smsService.sendMessage(numbers, message);
				}
				
				MailConfig mail = new MailConfig();
		        mail.sendTextMail("OrderDelivered-"+orderDetails.getOrderNumber(), message, orderDetails.getShippingAddress().getEmail(),true);
		        
			}
			
			response.setContentType("text/plain");
		    response.getWriter().write("Order status is updated successfully.");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void cancelOrderOrItem(HttpServletRequest request, HttpServletResponse response){
		Connection con = null;
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			con.setAutoCommit(false);
			
			String clickId = request.getParameter("clickId");
			String[] requestValArray = clickId.split("-");
			
			HttpSession session = request.getSession();
			UserVo userDetails = (UserVo)session.getAttribute("userDetails");
			
			OrderService orderService = new OrderServiceImpl();
			if(requestValArray[0].equals("item") || requestValArray[0].equals("order")){
				
				orderService.cancelOrderOrItem(con, Long.parseLong(requestValArray[1]), requestValArray[0], userDetails.getUserRole().getRoleId());
			}else{
				throw new Exception("Something went wrong, Please contact to customer support.");
			}
			
			con.commit();
			response.setContentType("text/plain");
		    response.getWriter().write("Order or item is cancelled successfully.");
			
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
		}
	}
}
