package com.sb.integration.controller;

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

import org.apache.log4j.Logger;

import com.sb.integration.config.MailConfig;
import com.sb.integration.config.RandomStringGeneration;
import com.sb.integration.dao.UserRepository;
import com.sb.integration.dao.impl.UserRepositoryImpl;
import com.sb.integration.resources.AutomateSellerApproved;
import com.sb.integration.service.CartService;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.CreateOrderEmailTemplate;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.SMSService;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class OrderConfirmedController
 */
public class OrderConfirmedController extends HttpServlet {
	final static Logger logger = Logger.getLogger(OrderConfirmedController.class);
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
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			String minimumOrder = prop.getProperty("order.minimum.order");
			String minimumOrderCharge = prop.getProperty("minimum.order.charge");
			
			Properties propApplication = propertiesFile.loadProperties("/application.properties");
			BigDecimal offerInRupee = new BigDecimal(propApplication.getProperty("wallet.per.order.offer.in.rupee")) ;
			
			/*BigDecimal offer = (cartDetails.getTotalAmount().
					multiply(new BigDecimal(offerPercentage))).divide(new BigDecimal(100));*/
			
			if(cartDetails.getSubtotalAmount().compareTo(new BigDecimal(minimumOrder)) == -1){
				
				cartDetails.setShippingCharge(new BigDecimal(minimumOrderCharge));
				cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(new BigDecimal(minimumOrderCharge)));
				
				OrderService orderService = new OrderServiceImpl();
				orderService.updateShippingCharge(con, cartDetails);
			}
			
			UserRepository userRepository = new UserRepositoryImpl();
			UserVo userVo = userRepository.getUserDetailById(con, cartDetails.getCartOwner());
			
			if(userVo!=null && userVo.getWalletAmount()!=null && 
					userVo.getWalletAmount().compareTo(BigDecimal.ZERO)==1 && 
					cartDetails.getTotalAmount().compareTo(offerInRupee)==1){
				
				if(userVo.getWalletAmount().compareTo(offerInRupee) < 0){
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(userVo.getWalletAmount()));
					userVo.setWalletAmount(BigDecimal.ZERO);
					cartDetails.setWalletAmount(userVo.getWalletAmount());
				}else{
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(offerInRupee));
					userVo.setWalletAmount(userVo.getWalletAmount().subtract(offerInRupee));
					cartDetails.setWalletAmount(offerInRupee);
				}
			}else{
				cartDetails.setWalletAmount(BigDecimal.ZERO);
			}
			
			CartService cartService = new CartServiceImpl();
			cartService.placeOrder(con, cartDetails, session);
			
			//update user wallet after order
			if(userVo.getWalletAmount().compareTo(BigDecimal.ZERO)>=0){
				UserService userService = new UserServiceImpl();
				userService.updateUserWallet(con, userVo.getUserId(), userVo.getWalletAmount());
				
				UserVo userSessionDetails = (UserVo) session.getAttribute("userDetails");
				userSessionDetails.setWalletAmount(userVo.getWalletAmount());
				
				session.setAttribute("userDetails", userSessionDetails);
			}
			//userService.createUserWallet(con, userVo.getUserId(), offer.toString(), "Reference Order number: "+ generatedOrderNumber);
			
			
			String orderInfo = "";
			for (CartItem cartItem : cartDetails.getCartItems()) {
				orderInfo = orderInfo + cartItem.getGoodsVo().getGoodsName() + "(" + cartItem.getQuantity() +
						" " +cartItem.getUom() + "- Rs." + cartItem.getPrice() + "),";
			}
			
			String message = "Your order has been placed successfully. Your order number is "+generatedOrderNumber + ", "
					+ " order total is " + cartDetails.getTotalAmount() + 
					" order will be delivered on " + cartDetails.getDeliveryOption();
			
			//System.out.println(message);
			
			// Send a mail to registered Email Id.
			if(!prop.getProperty("order.confirm.sms.off").equals("true")){
				logger.debug("order.confirm.sms.off is true....now message will send to customer");
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
				logger.debug("Order confirmed message is sent successfully.");
			}
			
			message = CreateOrderEmailTemplate.getEmailContent(cartDetails);
			//UserUtil.convertHtmlStringToPdf(message);
			System.out.println("*************" + message);
			
			cartDetails.setOrderDateForEmail(TimeStamp.getAsiaTimeStamp());
			
			MailConfig mail = new MailConfig();
	        mail.sendTextMail("Your Order -"+generatedOrderNumber + " is confirmed", message, cartDetails.getShippingAddress().getEmail(), false);
	        //mail.sendTextMail("New Order -"+generatedOrderNumber, message, prop.getProperty("admin.email.id"), false);
	        
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
