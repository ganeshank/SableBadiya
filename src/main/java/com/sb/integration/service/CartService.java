package com.sb.integration.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.DeliveryOption;
import com.sb.integration.vo.DeliveryTypeVo;

public interface CartService {

	public void createEmptyCart(Connection con, Long userId) throws Exception;

	public void addToCart(Connection con, Long goodsId, Long quantityId, Long sellerId, HttpServletRequest req, BigDecimal rupee)
			throws Exception;

	public Boolean isCartItemAlreadyExist(Connection con, Long userId, Long goodsId) throws Exception;

	public Integer getActiveCartItemCountForUser(Connection con, Long userId) throws Exception;

	public CartDetails getActiveCartForUser(Connection con, Long userId) throws Exception;

	public CartDetails deleteCartItem(Connection con, Long cartItemId, HttpSession session) throws Exception;

	public CartDetails updateQuantityCartItem(Connection con, Long quantityId, Long cartItemId, HttpSession session, Boolean isQuantityRupee) throws Exception;

	public void emptyCart(Connection con, Long cartId) throws Exception;

	public Boolean isEmailValid(Connection con, String email) throws Exception;

	public void resetPassword(Connection con, String emailId, String resetPass) throws Exception;
	
	public List<DeliveryTypeVo> getAllDeliveryTpyes(Connection con)throws Exception;
	
	public void placeOrder(Connection con, CartDetails cartDetails, HttpSession session)throws Exception;
	
	public CartDetails getOrderById(Connection con, Long cartId)throws Exception;
	
	public CartItem getCartItemForId(Connection con, Long cartItemId, Boolean isRequiredGoodsQuantity) throws Exception;
	
	public void inActiveCartItemById(Connection con, Long cartItemId)throws Exception;
	
	public List<DeliveryOption> getDeliveryOption(Connection con, String deliveryIds)throws Exception;
	
	public Boolean isCartEmpty(Connection con, Long userId);
	
	public void refreshCart(Connection con, Long userId);
	
	public CartDetails addToCartIndependent(Connection con, Long goodsId, Long quantityId, 
			Long sellerId, BigDecimal rupee, Long userId)throws Exception;
	
	public CartDetails updateCartItemForApp(Connection con, Long quantityId, Long userId, Long cartItemId, Boolean isQuantityRupee) throws Exception;
	
	public CartDetails deleteCartItemForApp(Connection con, Long cartItemId) throws Exception;
	
	public void updateShippingAddress(Connection con, Long addressId, Long cartId) throws Exception;

}
