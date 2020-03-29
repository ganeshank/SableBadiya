package com.sb.integration.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.OrderTrackingVo;
import com.sb.integration.vo.SearchRequestVo;

public interface OrderRepository {
	public AddressVo getAddressForId(Connection con, Long addressId) throws Exception;

	public void persistOrderTracking(Connection con, String orderNumber, Long orderStatus, String comments) throws Exception;

	public Integer getNewPlacedOrderCount(Connection con) throws Exception;

	public List<CartDetails> getNewPlacedOrders(Connection con, Long orderStatus) throws Exception;

	public void updateOrderStatus(Connection con, Long orderStatus, Long cartId) throws Exception;
	
	public String getOrderNumberForId(Connection con, Long cartId)throws Exception;
	
	public List<CartDetails> getNewPlacedOrdersForSeller(Connection con, Long orderStatus, Integer sellerId) throws Exception;
	
	public CartDetails getOrderForSeller(Connection con, Long cartId, Integer sellerId)throws Exception;
	
	public Integer getNewOrdersCountForSeller(Connection con, Long orderStatus, Integer sellerId)throws Exception;
	
	public Integer getSellerApprovedOrderCount(Connection con, Long orderStatus)throws Exception;
	
	public List<CartDetails> getSellerPendingRejectOrders(Connection con) throws Exception;
	
	public List<CartDetails> searchOrder(Connection con, SearchRequestVo searchRequestVo)throws Exception;
	
	public List<OrderTrackingVo> getOrderTrackingForOrder(Connection con, String orderNumber)throws Exception;
	
	public List<CartDetails> searchOrderForSeller(Connection con, SearchRequestVo searchRequestVo, Integer sellerId)throws Exception;
	
	public void reassignPendingRejectItems(Connection con, Map<Long, Long> sellerCartItemMap)throws Exception;
	
	public void updateOrderTotal(Connection con, Long cartId, BigDecimal newOrderTotal)throws Exception;
	
	public List<CartDetails> getRecentApprovedOrdersBySeller(Connection con, Integer sellerId) throws Exception;
	
	public Integer getOrderStatus(Connection con, Long cartId)throws Exception;
	
	public void deleteAddressForUser(Connection con, Long addressId)throws Exception;
	
	public List<CartDetails> getOrdersForUser(Connection con, Long userId) throws Exception;
	
	public void updateShippingCharge(Connection con, CartDetails cartDetails)throws Exception;
	
	public List<String> getDeliveryArea(Connection con) throws Exception;
	
	public List<Long> getOrderForDeliveryDate(Connection con, String deliveryDate)throws Exception;
}
