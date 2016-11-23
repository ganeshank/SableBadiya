package com.cigital.integration.dao;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.CartItem;
import com.cigital.integration.vo.DeliveryTypeVo;

public interface CartRepository {

	public Long createEmptyCart(Connection con, Long userId) throws Exception;

	public Boolean isCartItemAlreadyExist(Connection con, Long userId, Long goodsId) throws Exception;

	public List<CartItem> getActiveCartItemsForUser(Connection con, Long userId) throws Exception;

	public CartDetails getActiveCartForUser(Connection con, Long userId) throws Exception;

	public void updateExistingCartItem(Connection con, CartDetails cartDetails, CartItem cartItem) throws Exception;

	public Boolean updateCart(Connection con, CartDetails cartDetails) throws Exception;

	public void addCartItem(Connection con, CartDetails cartDetails, CartItem cartItem, Boolean isCartUpdateNeeded)
			throws Exception;

	public CartItem getCartItemForId(Connection con, Long cartItemId, Boolean isRequiredGoodsQuantity) throws Exception;

	public CartDetails getActiveCartForId(Connection con, Long cartId) throws Exception;

	public CartDetails deleteCartItemForId(Connection con, Long cartItemId) throws Exception;

	public void emptyCart(Connection con, Long cartId) throws Exception;

	public Boolean isEmailValid(Connection con, String email) throws Exception;

	public void resetPassword(Connection con, String emailId, String resetPass) throws Exception;

	public List<DeliveryTypeVo> getAllDeliveryTpyes(Connection con) throws Exception;

	public void placeOrder(Connection con, CartDetails cartDetails) throws Exception;

	public Long persistCart(Connection con, CartDetails cartDetails) throws Exception;
	
	public CartDetails getOrderForId(Connection con, Long cartId)throws Exception;
	
	public List<CartItem> getActiveCartItemsForCartId(Connection con, Long cartId, Boolean isSellerInfoRequired) throws Exception;
	
	public void inActiveCartItemById(Connection con, Long cartItemId)throws Exception;
	
}
