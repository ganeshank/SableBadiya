package com.sb.integration.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sb.integration.config.MailConfig;
import com.sb.integration.dao.CartRepository;
import com.sb.integration.dao.OrderRepository;
import com.sb.integration.dao.impl.CartRepositoryImpl;
import com.sb.integration.dao.impl.OrderRepositoryImpl;
import com.sb.integration.service.CartService;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.SellerService;
import com.sb.integration.util.Constants;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.SMSService;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.OrderTrackingVo;
import com.sb.integration.vo.SearchRequestVo;
import com.sb.integration.vo.UserVo;
import com.google.gson.Gson;

public class OrderServiceImpl implements OrderService {
	final static Logger logger = Logger.getLogger(OrderServiceImpl.class);
	private OrderRepository orderRepository = new OrderRepositoryImpl();
	CartService cartService = new CartServiceImpl();
	SellerService sellerService = new SellerServiceImpl();
	CartRepository cartRepository = new CartRepositoryImpl();

	@Override
	public AddressVo getAddressForId(Connection con, Long addressId) throws Exception {
		try {
			return orderRepository.getAddressForId(con, addressId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public Integer getNewPlacedOrderCount(Connection con) throws Exception {
		try {
			return orderRepository.getNewPlacedOrderCount(con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public String getNewPlacedOrders(Connection con, Long orderStatus) throws Exception {
		try {
			List<CartDetails> cartDetails = orderRepository.getNewPlacedOrders(con, orderStatus);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void updateOrderStatus(Connection con, Long orderStatus, Long cartId, String comments) throws Exception {
		try {

			orderRepository.updateOrderStatus(con, orderStatus, cartId);
			String orderNumber = orderRepository.getOrderNumberForId(con, cartId);
			orderRepository.persistOrderTracking(con, orderNumber, orderStatus, comments);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public String getNewPlacedOrdersForSeller(Connection con, Long orderStatus, Integer sellerId) throws Exception {
		try {
			List<CartDetails> cartDetails = orderRepository.getNewPlacedOrdersForSeller(con, orderStatus, sellerId);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public CartDetails getOrderForSeller(Connection con, Long cartId, Integer sellerId) throws Exception {
		try {
			return orderRepository.getOrderForSeller(con, cartId, sellerId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public Integer getNewOrdersCountForSeller(Connection con, Long orderStatus, Integer sellerId)throws Exception {
		try {
			return orderRepository.getNewOrdersCountForSeller(con, orderStatus, sellerId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public Integer getSellerApprovedOrderCount(Connection con, Long orderStatus) throws Exception {
		try {
			return orderRepository.getSellerApprovedOrderCount(con, orderStatus);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public String getSellerPendingRejectOrders(Connection con) throws Exception {
		try {
			List<CartDetails> cartDetails = orderRepository.getSellerPendingRejectOrders(con);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public String searchOrder(Connection con, SearchRequestVo searchRequestVo) throws Exception {
		try {
			List<CartDetails> cartDetails = orderRepository.searchOrder(con, searchRequestVo);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public List<OrderTrackingVo> getOrderTrackingForOrder(Connection con, String orderNumber) throws Exception {
		try {

			List<OrderTrackingVo> orderTrackingVos = orderRepository.getOrderTrackingForOrder(con, orderNumber);
			return orderTrackingVos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public String searchOrderForSeller(Connection con, SearchRequestVo searchRequestVo, Integer sellerId) throws Exception {
		try {
			List<CartDetails> cartDetails = orderRepository.searchOrderForSeller(con, searchRequestVo, sellerId);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void reassignPendingRejectItems(Connection con, Map<Long, Long> sellerCartItemMap) throws Exception {
		try {
			orderRepository.reassignPendingRejectItems(con, sellerCartItemMap);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void updateOrderTotal(Connection con, Long cartId, BigDecimal newOrderTotal) throws Exception {
		try {
			orderRepository.updateOrderTotal(con, cartId, newOrderTotal);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public String getRecentApprovedOrdersBySeller(Connection con, Integer sellerId) throws Exception {
		try {
			
			List<CartDetails> cartDetails = orderRepository.getRecentApprovedOrdersBySeller(con, sellerId);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public Integer getOrderStatus(Connection con, Long cartId) throws Exception {
		try {
			
			return orderRepository.getOrderStatus(con, cartId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void persistOrderTracking(Connection con, String orderNumber, Long orderStatus, String comments)
			throws Exception {
		try {
			
			orderRepository.persistOrderTracking(con, orderNumber, orderStatus, comments);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public String getOrderNumberForId(Connection con, Long cartId) throws Exception {
		try {
			
			return orderRepository.getOrderNumberForId(con, cartId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void cancelOrderOrItem(Connection con, Long clickId, String cancelType, Long roleId) throws Exception {
		try{
			Long statusId = null;
			if(roleId.equals(3l)){
				statusId = Constants.ORDER_IS_CANCELED_BY_CUSTOMER;
			}else{
				statusId = Constants.ORDER_IS_CANCELED_AT_DELIVERY_TIME;
			}
			
			CartDetails cartDetails = null;
			if(cancelType.equals("order")){
				updateOrderStatus(con, statusId, clickId, "Order is canceled.");
				cartDetails = cartService.getOrderById(con, clickId);
			}else{

				CartItem cartItem = cartService.getCartItemForId(con, clickId, false);
				cartDetails = cartService.getOrderById(con, cartItem.getCartId());
				
				if(cartDetails.getCartItems().size()==1 && cartDetails.getCartItems().get(0).getCartItemId().equals(clickId)){
					// Cancel the order because order contains only one item. 
					updateOrderStatus(con, statusId, cartDetails.getCartId(), "Order is canceled.");
				}else{
					sellerService.InActiveSellerCartItem(con, cartItem.getCartItemId());
					cartService.inActiveCartItemById(con, cartItem.getCartItemId());
					
					BigDecimal newOrderTotal = cartDetails.getTotalAmount().subtract(cartItem.getPrice());
					updateOrderTotal(con, cartDetails.getCartId(), newOrderTotal);
					updateOrderStatus(con, Constants.ORDER_ITEM_CANCELED, cartDetails.getCartId(), "Order Item is canceled.");
				}
			}
			// send email & message for order cancellation
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			
			MailConfig mail = new MailConfig();
			String message = "Your Order #"+cartDetails.getOrderNumber() + " has been cancelled";
	        mail.sendTextMail("Order Cancelled", message, cartDetails.getShippingAddress().getEmail(), false);
	        mail.sendTextMail("Order Cancelled", message+" by customer", prop.getProperty("admin.email.id"), false);
	        
	        if(!prop.getProperty("order.confirm.sms.off").equals("true")){
				SMSService smsService = new SMSService();
				String[] numbers = {cartDetails.getShippingAddress().getContactNumber()};
				smsService.sendMessage(numbers, message);
				
				String[] numbersOfAdmin = {prop.getProperty("admin.phone.number")};
				String messageForAdmin = message+" by customer";
				
				smsService.sendMessage(numbersOfAdmin, messageForAdmin);
				logger.debug("Order confirmed message is sent successfully.");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAddressForUser(Connection con, Long addressId) throws Exception {
		try{
			orderRepository.deleteAddressForUser(con, addressId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<CartDetails> getOrdersForUser(Connection con, Long userId) throws Exception {
		try{
			List<CartDetails> orderDetails = orderRepository.getOrdersForUser(con, userId);
			Timestamp currentDate = new Timestamp(new Date().getTime());
			for (CartDetails cartDetails : orderDetails) {
				
				Timestamp orderRecentChange = cartDetails.getModifiedDate();
				if(cartDetails.getModifiedDate()!=null){
					
					long milliseconds = currentDate.getTime() - orderRecentChange.getTime();
				    int seconds = (int) milliseconds / 1000;
				 
				    // calculate hours minutes and seconds
				    int hours = seconds / 3600;
				    if(hours<=2){
				    	cartDetails.setIsRecentOrder(true);
				    }
				}
			}
			return orderDetails;
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void updateShippingCharge(Connection con, CartDetails cartDetails) throws Exception {
		try{
			orderRepository.updateShippingCharge(con, cartDetails);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public CartDetails replaceGuestCartToRegCart(Connection con, UserVo userVo, CartDetails cartDetails) throws Exception {
		try{
			if(cartDetails!=null && cartDetails.getCartItems()!=null && cartDetails.getCartItems().size()>0){
				con.setAutoCommit(false);
				// in-activate user cart.
				CartDetails userCart = cartRepository.getActiveCartForUser(con, userVo.getUserId());
				cartRepository.inActiveOrder(con, userCart.getCartId());
				
				cartDetails.setCartOwner(userVo.getUserId());
				Long cartId = cartRepository.persistCart(con, cartDetails);
				cartDetails.setCartId(cartId);
				List<CartItem> cartItems = cartDetails.getCartItems();
				for (CartItem cartItem : cartItems) {
					cartRepository.addCartItem(con, cartDetails, cartItem, false);
				}
				
				con.commit();
			}		
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
			throw new Exception(e);
		}
		return cartDetails;
	}
	
	@Override
	public List<String> getDeliveryArea(Connection con) throws Exception {
		try{
			return orderRepository.getDeliveryArea(con);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public List<CartDetails> getOrderForDeliveryDate(Connection con, String deliveryDate) throws Exception {
		try{
			List<Long> orderIds = orderRepository.getOrderForDeliveryDate(con, deliveryDate);
			CartRepository cartRepository = new CartRepositoryImpl();
			
			for (Long cartId : orderIds) {
				List<CartItem> cartItems = cartRepository.getActiveCartItemsForCartId(con, cartId, false);
				
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
