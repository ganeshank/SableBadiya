package com.cigital.integration.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.cigital.integration.config.MailConfig;
import com.cigital.integration.config.RandomStringGeneration;
import com.cigital.integration.resources.AutomateSellerApproved;
import com.cigital.integration.service.CartService;
import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.LoadPropertiesFile;
import com.cigital.integration.util.SMSService;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.CartItem;

/**
 * Servlet implementation class OrderConfirmedController
 */
public class OrderConfirmedController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderConfirmedController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		try{
			String cartIdStr = request.getParameter("cartId");
			
			HttpSession session = request.getSession();
			CartDetails cartDetails = (CartDetails)session.getAttribute("placeOrderCart");
			cartDetails.setCartId((cartIdStr!=null && !cartIdStr.isEmpty())?Long.parseLong(cartIdStr):null);
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			con.setAutoCommit(false);
			String generatedOrderNumber = RandomStringGeneration.generateOrderNumber();
			cartDetails.setOrderNumber(generatedOrderNumber);
			
			CartService cartService = new CartServiceImpl();
			cartService.placeOrder(con, cartDetails, session);
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			String minimumOrder = prop.getProperty("order.minimum.order");
			String minimumOrderCharge = prop.getProperty("minimum.order.charge");
			
			if(cartDetails.getSubtotalAmount().compareTo(new BigDecimal(minimumOrder)) == -1){
				
				cartDetails.setShippingCharge(new BigDecimal(minimumOrderCharge));
				cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(new BigDecimal(minimumOrderCharge)));
				
				OrderService orderService = new OrderServiceImpl();
				orderService.updateShippingCharge(con, cartDetails);
			}
			
			String orderInfo = "";
			for (CartItem cartItem : cartDetails.getCartItems()) {
				orderInfo = orderInfo + cartItem.getGoodsVo().getGoodsName() + "(" + cartItem.getQuantity() +
						" " +cartItem.getUom() + "- Rs." + cartItem.getPrice() + "),";
			}
			
			String message = "Your order has been placed successfully. Your order number is "+generatedOrderNumber + ", "
					+ "you have ordered: " + orderInfo + " order total is " + cartDetails.getTotalAmount() + 
					" order will be delivered on " + cartDetails.getDeliveryOption();
			
			//System.out.println(message);
			
			// Send a mail to registered Email Id.
			if(!prop.getProperty("order.confirm.sms.off").equals("true")){
				SMSService smsService = new SMSService();
				String[] numbers = {cartDetails.getShippingAddress().getContactNumber()};
				smsService.sendMessage(numbers, message);
				
				String[] numbersOfAdmin = {prop.getProperty("admin.phone.number")};
				AddressVo shippingAddress = cartDetails.getShippingAddress();
				String messageForAdmin = message + ", FullAddress- (" + shippingAddress.getContactName() + "," 
								+ shippingAddress.getContactNumber() + "," + shippingAddress.getAddressLine1() 
								+ "," + shippingAddress.getAddressLine2() + "," + shippingAddress.getCity() + ","
								+ shippingAddress.getLandMark();
				
				smsService.sendMessage(numbersOfAdmin, messageForAdmin);
			}
			
			MailConfig mail = new MailConfig();
	        mail.sendTextMail("OrderConfirmed-"+generatedOrderNumber, message, cartDetails.getShippingAddress().getEmail());
	        mail.sendTextMail("NewOrder-"+generatedOrderNumber, message, prop.getProperty("admin.email.id"));
	        
	        Map<Long, Long> sellerCartItemMap = new HashMap<>();
	        
	        for (CartItem cartItem : cartDetails.getCartItems()) {
	        	sellerCartItemMap.put(cartItem.getCartItemId(), 1l);
			}
	        
	        AutomateSellerApproved.getAutomateSellerApproval(con, cartDetails.getCartId(), 1, sellerCartItemMap);
			
			session.setAttribute("itemCount", null);
			session.setAttribute("placeOrderCart", null);
			
			session.setAttribute("currentOrderNumber", generatedOrderNumber);
			session.setAttribute("inDeliver", cartDetails.getDeliveryOption());
			
			con.commit();
			
			response.setContentType("text/plain");
		    response.getWriter().write(cartDetails.getOrderNumber());
		    response.setStatus(200);
		    
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
