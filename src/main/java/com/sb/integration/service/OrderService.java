package com.sb.integration.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.OrderTrackingVo;
import com.sb.integration.vo.SearchRequestVo;
import com.sb.integration.vo.UserVo;

public interface OrderService {
	public AddressVo getAddressForId(Connection con, Long addressId) throws Exception;

	public Integer getNewPlacedOrderCount(Connection con) throws Exception;

	public String getNewPlacedOrders(Connection con, Long orderStatus) throws Exception;

	public void updateOrderStatus(Connection con, Long orderStatus, Long cartId, String comments) throws Exception;
	
	public String getNewPlacedOrdersForSeller(Connection con, Long orderStatus, Integer sellerId) throws Exception;
	
	public CartDetails getOrderForSeller(Connection con, Long cartId, Integer sellerId)throws Exception;
	
	public Integer getNewOrdersCountForSeller(Connection con, Long orderStatus, Integer sellerId)throws Exception;
	
	public Integer getSellerApprovedOrderCount(Connection con, Long orderStatus)throws Exception;
	
	public String getSellerPendingRejectOrders(Connection con) throws Exception;
	
	public String searchOrder(Connection con, SearchRequestVo searchRequestVo)throws Exception;
	
	public List<OrderTrackingVo> getOrderTrackingForOrder(Connection con, String orderNumber)throws Exception;
	
	public String searchOrderForSeller(Connection con, SearchRequestVo searchRequestVo, Integer sellerId)throws Exception;
	
	public void reassignPendingRejectItems(Connection con, Map<Long, Long> sellerCartItemMap)throws Exception;
	
	public void updateOrderTotal(Connection con, Long cartId, BigDecimal newOrderTotal)throws Exception;
	
	public String getRecentApprovedOrdersBySeller(Connection con, Integer sellerId) throws Exception;
	
	public Integer getOrderStatus(Connection con, Long cartId)throws Exception;
	
	public void persistOrderTracking(Connection con, String orderNumber, Long orderStatus, String comments) throws Exception;
	
	public String getOrderNumberForId(Connection con, Long cartId)throws Exception;
	
	public void cancelOrderOrItem(Connection con, Long clickId, String cancelType, Long roleId)throws Exception;
	
	public void deleteAddressForUser(Connection con, Long addressId)throws Exception;
	
	public List<CartDetails> getOrdersForUser(Connection con, Long userId)throws Exception;
	
	public void updateShippingCharge(Connection con, CartDetails cartDetails)throws Exception;
	
	public CartDetails replaceGuestCartToRegCart(Connection con, UserVo userVo, CartDetails cartDetails)throws Exception;
	
	public List<String> getDeliveryArea(Connection con) throws Exception;
	
	public List<CartDetails> getOrderForDeliveryDate(Connection con, String deliveryDate)throws Exception;
	
}
