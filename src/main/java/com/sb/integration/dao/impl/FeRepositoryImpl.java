package com.sb.integration.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sb.integration.dao.FeRepository;
import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.dao.SellerRepository;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.FieldExecutiveVo;

public class FeRepositoryImpl implements FeRepository {

	private static final String GET_FE_BY_USER_ID = "SELECT * FROM field_executive WHERE USER_ID=? AND ACTIVE=1";
	
	private static final String GET_NEW_ORDERS_FOR_FE = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,"
					+ " C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE FROM cart C, order_for_fe OFE "
					+ " WHERE C.CART_ID = OFE.CART_ID AND OFE.FIELD_EXECUTIVE_ID=? AND C.CART_STATUS_ID IN(11,5,14) AND OFE.ACTIVE=1";
	
	private static final String GET_ORDER_FOR_FE = "SELECT * FROM seller_cart_item SCI, cart_item CI, cart C, order_for_fe FE WHERE "
			+ " SCI.CART_ITEM_ID = CI.CART_ITEM_ID AND CI.CART_ID = C.CART_ID AND C.CART_ID=? AND CI.ACTIVE=1 "
			+ " AND C.CART_ID = FE.CART_ID AND FE.FIELD_EXECUTIVE_ID=? AND FE.ACTIVE=1 AND C.CART_STATUS_ID IN(11,5,14) "
			+ " AND SCI.STATUS=1 AND SCI.ACTIVE=1 ORDER BY C.CART_ID";
	
	@Override
	public FieldExecutiveVo getFeByUserId(Connection con, Long userId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_FE_BY_USER_ID);
			pst.setLong(1, userId);
			ResultSet rs = pst.executeQuery();

			FieldExecutiveVo fieldExecutiveVo = null;
			while (rs.next()) {
				fieldExecutiveVo = new FieldExecutiveVo();
				fieldExecutiveVo.setFieldExecutiveId(rs.getInt(1));
				fieldExecutiveVo.setFeName(rs.getString(2));
				fieldExecutiveVo.setFeContact(rs.getString(3));
				fieldExecutiveVo.setAddressLine1(rs.getString(4));
				fieldExecutiveVo.setAddressLine2(rs.getString(5));
				fieldExecutiveVo.setCity(rs.getString(6));
				fieldExecutiveVo.setState(rs.getString(7));
				fieldExecutiveVo.setUserId(userId);
			}

			return fieldExecutiveVo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public List<CartDetails> getNewOrdersForFe(Connection con, Integer feId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_NEW_ORDERS_FOR_FE);
			pst.setInt(1, feId);
			

			ResultSet resultSet = pst.executeQuery();

			List<CartDetails> cartDetailsList = new ArrayList<>();
			CartDetails cartDetails = null;
			while (resultSet.next()) {
				cartDetails = new CartDetails();
				cartDetails.setCartId(resultSet.getLong(1));
				cartDetails.setCartStatusId(resultSet.getLong(2));
				//cartDetails.setOrderedBy(resultSet.getString(3));
				cartDetails.setOrderNumber(resultSet.getString(3));
				cartDetails.setSubtotalAmount(resultSet.getBigDecimal(4));
				cartDetails.setTax(resultSet.getBigDecimal(5));
				cartDetails.setShippingCharge(resultSet.getBigDecimal(6));
				cartDetails.setTotalAmount(
						resultSet.getBigDecimal(7) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(7));
				cartDetails.setTotalMsrp(
						resultSet.getBigDecimal(8) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(8));
				cartDetails.setOrderDate(resultSet.getTimestamp(9));
				if(cartDetails.getCartStatusId().equals(11l))
					cartDetails.setCartStatusName("Order approved by sellers.");
				else
					cartDetails.setCartStatusName("Order dispatched from the sellers.");
				
				if(cartDetails.getCartStatusId().equals(14l)){
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>ViewOrder</a> ");
				}else{
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>ViewOrder</a> "
									+ "<a href='javascript:void(0);' onclick='updateOrder("+ cartDetails.getCartId() +")'>UpdateOrder</a>");
				}
				
				
				cartDetailsList.add(cartDetails);
			}

			return cartDetailsList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public CartDetails getOrderForFe(Connection con, Long cartId, Integer feId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_FOR_FE);
			pst.setLong(1, cartId);
			pst.setInt(2, feId);

			ResultSet resultSet = pst.executeQuery();

			CartDetails cartDetails = null;
			List<CartItem> cartItems = new ArrayList<>();
			while (resultSet.next()) {
				if(cartDetails == null){
					cartDetails = new CartDetails();
					cartDetails.setCartId(resultSet.getLong("CART_ID"));
					cartDetails.setCartStatusId(resultSet.getLong("CART_STATUS_ID"));
					
					cartDetails.setOrderNumber(resultSet.getString("ORDER_NUMBER"));
					cartDetails.setTotalAmount(
							resultSet.getBigDecimal("TOTAL_AMOUNT") == null ? BigDecimal.ZERO : resultSet.getBigDecimal("TOTAL_AMOUNT"));
					cartDetails.setOrderDate(resultSet.getTimestamp("ORDER_DATE"));
					cartDetails.setDeliveryOption(resultSet.getString(19));
					cartDetails.setCartItems(cartItems);
					
					OrderService orderService = new OrderServiceImpl();
					AddressVo orderShippingAddress = orderService.getAddressForId(con, Long.parseLong(resultSet.getString("SHIPPING_ADDRESS")));
					cartDetails.setShippingAddress(orderShippingAddress);
					
					cartDetails.setDeliveryOption(resultSet.getString("DELIVERY_OPTION"));
					cartDetails.setSubtotalAmount(resultSet.getBigDecimal("SUBTOTAL_AMOUNT") == null ? BigDecimal.ZERO : resultSet.getBigDecimal("SUBTOTAL_AMOUNT"));
					cartDetails.setShippingCharge(resultSet.getBigDecimal("SHIPPING_CHARGE") == null ? BigDecimal.ZERO : resultSet.getBigDecimal("SHIPPING_CHARGE"));
				}
				
				CartItem cartItem = new CartItem();
				cartItem.setCartItemId(resultSet.getLong("CART_ITEM_ID"));
				cartItem.setQuantity(resultSet.getString("QUANTITY"));
				cartItem.setPrice(resultSet.getBigDecimal("PRICE"));
				cartItem.setUom(resultSet.getString("UOM"));
				
				GoodsRepository goodsRepository = new GoodsRepositoryImpl();
				cartItem.setGoodsVo(goodsRepository.getGoodsForId(con, resultSet.getLong("GOODS_ID")));
				
				SellerRepository sellerRepository = new SellerRepositoryImpl();
				cartItem.setSellerVo(sellerRepository.getSellerForCartItem(con, cartItem.getCartItemId()));
				
				cartItems.add(cartItem);
			}

			return cartDetails;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

}
