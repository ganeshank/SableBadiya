package com.sb.integration.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sb.integration.dao.CartRepository;
import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.dao.OrderRepository;
import com.sb.integration.dao.SellerRepository;
import com.sb.integration.dao.UserRepository;
import com.sb.integration.util.Constants;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.OrderTrackingVo;
import com.sb.integration.vo.SearchRequestVo;

public class OrderRepositoryImpl implements OrderRepository {

	private static final String GET_ADDRESS_FOR_ID = "SELECT * FROM  ADDRESS WHERE ADDRESS_ID=? AND ACTIVE=1";

	private static final String ADD_ORDER_TRACKING = "INSERT INTO ORDER_TRACKING (ORDER_NUMBER, ORDER_STATUS_ID, TRACK_DATE, COMMENTS) "
			+ "VALUES(?,?,?,?)";

	private static final String GET_NEW_ORDER_COUNT = "SELECT COUNT(CART_ID) FROM CART WHERE CART_STATUS_ID=2";

	private static final String GET_NEW_ORDERS = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,"
			+ " C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, C.CART_OWNER FROM CART C WHERE C.CART_STATUS_ID=?";

	private static final String UPDATE_ORDER_STATUS = "UPDATE CART SET CART_STATUS_ID=?,MODIFIED_DATE=? WHERE CART_ID=?";

	private static final String GET_ORDER_NUMBER_FOR_ID = "SELECT ORDER_NUMBER FROM CART WHERE CART_ID=?";

	private static final String GET_NEW_ORDERS_FOR_SELLER = "SELECT C.CART_ID, C.ORDER_NUMBER, C.ORDER_DATE, CI.PRICE FROM SELLER_CART_ITEM SCI, CART_ITEM CI, CART C WHERE "
			+ "SCI.CART_ITEM_ID = CI.CART_ITEM_ID AND CI.CART_ID = C.CART_ID AND SCI.SELLER_ID=? AND C.CART_STATUS_ID in(3,10,11,12) "
			+ "AND SCI.ACTIVE=? AND SCI.STATUS=3 ORDER BY C.CART_ID";
	
	private static final String GET_ORDER_FOR_SELLER = "SELECT * FROM SELLER_CART_ITEM SCI, CART_ITEM CI, CART C WHERE "
			+ "SCI.CART_ITEM_ID = CI.CART_ITEM_ID AND CI.CART_ID = C.CART_ID AND SCI.SELLER_ID=? AND CI.CART_ID=? "
			+ "AND C.CART_STATUS_ID IN(3,10,11,12) AND SCI.STATUS=3 AND SCI.ACTIVE=1 ORDER BY C.CART_ID";
	
	private static final String GET_NEW_ORDER_COUNT_FOR_USER = "SELECT COUNT(C.CART_ID) FROM SELLER_CART_ITEM SCI, CART_ITEM CI, CART C WHERE "
			+ "SCI.CART_ITEM_ID = CI.CART_ITEM_ID AND CI.CART_ID = C.CART_ID AND SCI.SELLER_ID=? AND C.CART_STATUS_ID in(?,?) "
			+ "AND SCI.ACTIVE=? AND SCI.STATUS=3 ORDER BY C.CART_ID";
	
	private static final String GET_APPROVED_ORDERS_COUNT = "SELECT COUNT(CART_ID) FROM CART WHERE CART_STATUS_ID=?";
	
	private static final String GET_SELLER_PENDING_REJECT_ORDER = "SELECT C.CART_ID, C.CART_STATUS_ID, U.NAME, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,"
			+ " C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE FROM CART C, USERS U WHERE C.CART_OWNER = U.USER_ID AND"
			+ " C.CART_STATUS_ID IN(?,?)";
	
	private static final String GET_ORDER_TRACKING = "SELECT OT.ORDER_NUMBER, CS.DESCRIPTION, OT.TRACK_DATE FROM "
			+ "ORDER_TRACKING OT, CART_STATUS CS WHERE OT.ORDER_STATUS_ID = CS.CART_STATUS_ID AND ORDER_NUMBER = ? ORDER BY OT.TRACK_DATE";
	
	private static final String UPDATE_SELLER_CART_ITEM = "UPDATE SELLER_CART_ITEM SET ACTIVE=0 WHERE CART_ITEM_ID IN(?)";
	
	private static final String UPDATE_ORDER_TOTAL = "UPDATE CART SET TOTAL_AMOUNT=?, SUBTOTAL_AMOUNT=? WHERE CART_ID=?";
	
	private static final String GET_RECENT_ORDER_BY_SELLER = "SELECT C.CART_ID, C.ORDER_NUMBER, C.ORDER_DATE, CI.PRICE FROM SELLER_CART_ITEM SCI, CART_ITEM CI, CART C WHERE "
			+ "SCI.CART_ITEM_ID = CI.CART_ITEM_ID AND CI.CART_ID = C.CART_ID AND SCI.SELLER_ID=? AND C.CART_STATUS_ID in(?,?,?) "
			+ "AND SCI.ACTIVE=? AND SCI.STATUS=1 AND SCI.CREATED_DATE LIKE ? ORDER BY C.CART_ID";
	
	private static final String GET_ORDER_STATUS = "SELECT CART_STATUS_ID FROM CART WHERE CART_ID=?";
	
	private static final String DELETE_ADDRESS = "UPDATE ADDRESS SET ACTIVE=0 WHERE ADDRESS_ID=? AND ACTIVE=1";
	
	private static final String GET_ALL_ORDERS_FOR_USER = "SELECT C.CART_ID, C.CART_STATUS_ID, C.CART_OWNER,"
			+ "C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX, C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, C.MODIFIED_DATE "
			+ "FROM CART C WHERE C.CART_OWNER=? AND C.CART_STATUS_ID NOT IN(1) ORDER BY C.ORDER_DATE DESC LIMIT 5";
	
	private static final String UPDATE_SHIPPING_CHARGE_FOR_ORDER = "UPDATE CART SET TOTAL_AMOUNT=?,"
			+ " SHIPPING_CHARGE=? WHERE CART_ID=?";
	
	private static final String GET_DELIVERY_AREA = "SELECT DELIVERY_AREA FROM DELIVERY_AREA WHERE ACTIVE=1";
	
	private static final String GET_ORDER_ID_FOR_DATE = "SELECT CART_ID FROM CART WHERE CART_STATUS_ID NOT IN(6,7,8,9) "
			+ "AND DELIVERY_DATE=?";
	
	@Override
	public AddressVo getAddressForId(Connection con, Long addressId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ADDRESS_FOR_ID);
			pst.setLong(1, addressId);

			ResultSet rs = pst.executeQuery();

			AddressVo addressVo = null;
			if (rs.next()) {
				addressVo = new AddressVo();
				addressVo.setAddressId(addressId);
				addressVo.setContactName(rs.getString("CONTACT_NAME"));
				addressVo.setContactNumber(rs.getString("CONTACT_NUMBER"));
				addressVo.setAddressLine1(rs.getString("ADDRESS_LINE1"));
				addressVo.setAddressLine2(rs.getString("ADDRESS_LINE2"));
				addressVo.setCity(rs.getString("CITY"));
				addressVo.setState(rs.getString("STATE"));
				addressVo.setCountry(rs.getString("COUNTRY"));
				addressVo.setPinCode(rs.getString("PINCODE"));
				addressVo.setLandMark(rs.getString("LANDMARK"));
				addressVo.setEmail(rs.getString("EMAIL"));
			}

			return addressVo;
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
	public void persistOrderTracking(Connection con, String orderNumber, Long orderStatus, String comments) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(ADD_ORDER_TRACKING);
			pst.setString(1, orderNumber);
			pst.setLong(2, orderStatus);
			pst.setString(3, TimeStamp.getAsiaTimeStamp());
			pst.setString(4, comments);

			Integer rowAffectedForCartItem = pst.executeUpdate();
			if (rowAffectedForCartItem < 0) {
				System.out.println("Order Tracking is not updated successfully...");
				throw new Exception("Order Tracking is not updated successfully...");
			}
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
	public Integer getNewPlacedOrderCount(Connection con) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_NEW_ORDER_COUNT);

			ResultSet rs = pst.executeQuery();
			Integer count = 0;

			if (rs.next()) {
				count = rs.getInt(1);
			}

			return count;
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
	public List<CartDetails> getNewPlacedOrders(Connection con, Long orderStatus) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_NEW_ORDERS);
			pst.setLong(1, orderStatus);

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

				if(orderStatus.equals(Constants.ORDER_PLACED_BY_CUSTOMER_STATUS)){
					cartDetails.setCartStatusName("Order placed by customer.");
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>Process Order</a>");
				}else if(orderStatus.equals(Constants.ORDER_CONFIRMED_BY_SELLER)){
					cartDetails.setCartStatusName("Order confirmed by seller");
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>View Order</a>"
							+ "<a href='javascript:void(0);' style='padding-left: 12px;' onclick='generateBill("+ cartDetails.getCartId() +")'>Generate Bill</a>");
				}
				
				Long userId = resultSet.getLong(10);
				
				if(userId!=null && userId.equals(0l)){
					cartDetails.setOrderedBy("Guest");
				}else{
					UserRepository userRepository = new UserRepositoryImpl();
					cartDetails.setOrderedBy(userRepository.getUserDetailById(con, resultSet.getLong(10)).getFullName());
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
	public void updateOrderStatus(Connection con, Long orderStatus, Long cartId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_ORDER_STATUS);
			pst.setLong(1, orderStatus);
			pst.setString(2, TimeStamp.getAsiaTimeStamp());
			pst.setLong(3, cartId);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("Order status is not updated successfully.");
				throw new Exception();
			}

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
	public String getOrderNumberForId(Connection con, Long cartId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_NUMBER_FOR_ID);
			pst.setLong(1, cartId);

			ResultSet rs = pst.executeQuery();

			String orderNumber = null;
			if (rs.next()) {
				orderNumber = rs.getString(1);
			}

			return orderNumber;
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
	public List<CartDetails> getNewPlacedOrdersForSeller(Connection con, Long orderStatus, Integer sellerId)
			throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_NEW_ORDERS_FOR_SELLER);
			pst.setInt(1, sellerId);
			/*pst.setLong(2, orderStatus);
			pst.setLong(3, Constants.ORDER_PENDING_BY_SELLER);*/
			pst.setBoolean(2, true);

			ResultSet resultSet = pst.executeQuery();

			List<CartDetails> cartDetailsList = new ArrayList<>();
			CartDetails cartDetails = null;
			Long cartId = null;
			while (resultSet.next()) {
				if (cartId == null || (!cartId.equals(resultSet.getLong(1)))) {
					cartDetails = new CartDetails();

					cartDetails.setCartId(resultSet.getLong(1));
					cartDetails.setOrderNumber(resultSet.getString(2));
					cartDetails.setOrderDate(resultSet.getTimestamp(3));
					cartDetails.setTotalAmount(BigDecimal.ZERO);

					cartId = cartDetails.getCartId();
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(resultSet.getBigDecimal(4)));
					cartDetails.setCartStatusName("Order placed by customer.");
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>Process Order</a>");

					cartDetailsList.add(cartDetails);
				} else {
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(resultSet.getBigDecimal(4)));
				}
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
	public CartDetails getOrderForSeller(Connection con, Long cartId, Integer sellerId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_FOR_SELLER);
			pst.setInt(1, sellerId);
			pst.setLong(2, cartId);
			/*pst.setLong(3, Constants.ORDER_PROCESSED_BY_COADMIN);
			pst.setLong(4, Constants.ORDER_PENDING_BY_SELLER);*/

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
	
	@Override
	public Integer getNewOrdersCountForSeller(Connection con, Long orderStatus, Integer sellerId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_NEW_ORDER_COUNT_FOR_USER);
			pst.setInt(1, sellerId);
			pst.setLong(2, orderStatus);
			pst.setLong(3, Constants.ORDER_PENDING_BY_SELLER);
			pst.setBoolean(4, true);

			ResultSet rs = pst.executeQuery();
			Integer count = 0;

			if (rs.next()) {
				count = rs.getInt(1);
			}

			return count;
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
	public Integer getSellerApprovedOrderCount(Connection con, Long orderStatus) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_APPROVED_ORDERS_COUNT);
			pst.setLong(1, orderStatus);

			ResultSet rs = pst.executeQuery();
			Integer count = 0;

			if (rs.next()) {
				count = rs.getInt(1);
			}

			return count;
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
	public List<CartDetails> getSellerPendingRejectOrders(Connection con) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_SELLER_PENDING_REJECT_ORDER);
			pst.setLong(1, Constants.ORDER_PENDING_BY_SELLER);
			pst.setLong(2, Constants.ORDER_REJECTED_BY_SELLER);

			ResultSet resultSet = pst.executeQuery();

			List<CartDetails> cartDetailsList = new ArrayList<>();
			CartDetails cartDetails = null;
			while (resultSet.next()) {
				cartDetails = new CartDetails();
				cartDetails.setCartId(resultSet.getLong(1));
				cartDetails.setCartStatusId(resultSet.getLong(2));
				cartDetails.setOrderedBy(resultSet.getString(3));
				cartDetails.setOrderNumber(resultSet.getString(4));
				cartDetails.setSubtotalAmount(resultSet.getBigDecimal(5));
				cartDetails.setTax(resultSet.getBigDecimal(6));
				cartDetails.setShippingCharge(resultSet.getBigDecimal(7));
				cartDetails.setTotalAmount(
						resultSet.getBigDecimal(8) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(8));
				cartDetails.setTotalMsrp(
						resultSet.getBigDecimal(9) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(9));
				cartDetails.setOrderDate(resultSet.getTimestamp(10));
				cartDetails.setViewLink(
						"<a href='javascript:void(0);' onclick='viewRejectPendingOrder("+ cartDetails.getCartId() +")'>View Order</a>");
				
				if(cartDetails.getCartStatusId().equals(Constants.ORDER_PENDING_BY_SELLER)){
					cartDetails.setCartStatusName("Pending from the seller");
				}else if(cartDetails.getCartStatusId().equals(Constants.ORDER_REJECTED_BY_SELLER)){
					cartDetails.setCartStatusName("Rejected from the seller");
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
	public List<CartDetails> searchOrder(Connection con, SearchRequestVo searchRequestVo) throws Exception {
		PreparedStatement pst = null;
		try {
			
			String query = null;
			
			if(searchRequestVo.getSearchBy().equalsIgnoreCase("orderDate")){
				query = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,C.SHIPPING_CHARGE, "
						+ " C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, CS.NAME, C.CART_OWNER FROM CART C, CART_STATUS CS WHERE "
						+ " CS.CART_STATUS_ID = C.CART_STATUS_ID AND C.ORDER_DATE BETWEEN ? AND ?";
				pst = con.prepareStatement(query);
				pst.setString(1, searchRequestVo.getFromDate());
				pst.setString(2, searchRequestVo.getToDate());
				
			}else if(searchRequestVo.getSearchBy().equalsIgnoreCase("orderNumber")){
				query = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX, "
						+ "C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, CS.NAME, C.CART_OWNER FROM CART C, CART_STATUS CS "
						+ " WHERE CS.CART_STATUS_ID = C.CART_STATUS_ID "
						+ "AND C.ORDER_NUMBER LIKE ?";
				pst = con.prepareStatement(query);
				pst.setString(1, "%" + searchRequestVo.getSearchValue() + "%");
			}else if(searchRequestVo.getSearchBy().equalsIgnoreCase("phoneNumber")){
				query = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,"
						+ "C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, CS.NAME, C.CART_OWNER FROM CART C, CART_STATUS CS," 
						+ " ADDRESS A WHERE CS.CART_STATUS_ID = C.CART_STATUS_ID AND "
						+ "C.SHIPPING_ADDRESS = A.ADDRESS_ID AND A.CONTACT_NUMBER LIKE ?";
				pst = con.prepareStatement(query);
				pst.setString(1, "%" + searchRequestVo.getSearchValue() + "%");
			}else if(searchRequestVo.getSearchBy().equalsIgnoreCase("email")){
				query = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,"
						+ "C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, CS.NAME, C.CART_OWNER FROM CART C, CART_STATUS CS," 
						+ "ADDRESS A WHERE CS.CART_STATUS_ID = C.CART_STATUS_ID AND "
						+ "C.SHIPPING_ADDRESS = A.ADDRESS_ID AND A.EMAIL LIKE ?";
				pst = con.prepareStatement(query);
				pst.setString(1, "%" + searchRequestVo.getSearchValue() + "%");
			}else if(searchRequestVo.getSearchBy().equalsIgnoreCase("orderBy")){
				query = "SELECT C.CART_ID, C.CART_STATUS_ID, C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX,"
						+ "C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, CS.NAME, C.CART_OWNER FROM CART C, CART_STATUS CS," 
						+ "ADDRESS A WHERE CS.CART_STATUS_ID = C.CART_STATUS_ID AND "
						+ "C.SHIPPING_ADDRESS = A.ADDRESS_ID AND A.CONTACT_NAME LIKE ?";
				pst = con.prepareStatement(query);
				pst.setString(1, "%" + searchRequestVo.getSearchValue() + "%");
			}
			
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
				cartDetails.setCartStatusName(resultSet.getString(10));
				
				Long userId = resultSet.getLong(11);
				if(userId!=null && userId.equals(0l)){
					cartDetails.setOrderedBy("Guest");
				}else{
					UserRepository userRepository = new UserRepositoryImpl();
					cartDetails.setOrderedBy(userRepository.getUserDetailById(con, userId).getFullName());
				}
				
				cartDetails.setViewLink(
						"<a href='javascript:void(0);' onclick='viewOrderHistory("+ cartDetails.getCartId() +")'>View Order History</a>");

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
	public List<OrderTrackingVo> getOrderTrackingForOrder(Connection con, String orderNumber) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_TRACKING);
			pst.setString(1, orderNumber);

			ResultSet resultSet = pst.executeQuery();

			List<OrderTrackingVo> orderTrackList = new ArrayList<OrderTrackingVo>();
			OrderTrackingVo orderTrack = null;
			while (resultSet.next()) {
				orderTrack = new OrderTrackingVo();
				
				orderTrack.setOrderNumber(resultSet.getString(1));
				orderTrack.setOrderStatus(resultSet.getString(2));
				orderTrack.setTrackDate(resultSet.getTimestamp(3));
				
				orderTrackList.add(orderTrack);
			}

			return orderTrackList;
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
	public List<CartDetails> searchOrderForSeller(Connection con, SearchRequestVo searchRequestVo, Integer sellerId) throws Exception {
		PreparedStatement pst = null;
		try {
			
			String query = null;
			
			if(searchRequestVo.getSearchBy().equalsIgnoreCase("orderDate")){
				query = "SELECT C.CART_ID, C.ORDER_NUMBER, C.ORDER_DATE, CI.PRICE, CS.NAME FROM SELLER_CART_ITEM SCI, CART_ITEM CI, "
						+ "CART C, CART_STATUS CS WHERE CS.CART_STATUS_ID = C.CART_STATUS_ID AND SCI.CART_ITEM_ID = CI.CART_ITEM_ID "
						+ "AND CI.CART_ID = C.CART_ID AND SCI.SELLER_ID=? AND C.CART_STATUS_ID in(3,10,11,12) " 
						+ "AND SCI.ACTIVE=1 AND C.ORDER_DATE BETWEEN ? AND ? ORDER BY C.CART_ID";
				
				pst = con.prepareStatement(query);
				pst.setInt(1, sellerId);
				pst.setString(2, searchRequestVo.getFromDate());
				pst.setString(3, searchRequestVo.getToDate());
				
			}else if(searchRequestVo.getSearchBy().equalsIgnoreCase("orderNumber")){
				query = "SELECT C.CART_ID, C.ORDER_NUMBER, C.ORDER_DATE, CI.PRICE, CS.NAME FROM SELLER_CART_ITEM SCI, CART_ITEM CI, "
						+ "CART C, CART_STATUS CS WHERE CS.CART_STATUS_ID = C.CART_STATUS_ID AND SCI.CART_ITEM_ID = CI.CART_ITEM_ID "
						+ "AND CI.CART_ID = C.CART_ID AND SCI.SELLER_ID=? AND C.CART_STATUS_ID in(3,10,11,12) " 
						+ "AND SCI.ACTIVE=1 and C.ORDER_NUMBER LIKE ? ORDER BY C.CART_ID";
				
				pst = con.prepareStatement(query);
				pst.setInt(1, sellerId);
				pst.setString(2, "%" + searchRequestVo.getSearchValue() + "%");
			}
			
			ResultSet resultSet = pst.executeQuery();

			List<CartDetails> cartDetailsList = new ArrayList<>();
			CartDetails cartDetails = null;
			Long cartId = null;
			
			while (resultSet.next()) {
				if (cartId == null || (!cartId.equals(resultSet.getLong(1)))) {
					cartDetails = new CartDetails();

					cartDetails.setCartId(resultSet.getLong(1));
					cartDetails.setOrderNumber(resultSet.getString(2));
					cartDetails.setOrderDate(resultSet.getTimestamp(3));
					cartDetails.setTotalAmount(BigDecimal.ZERO);

					cartId = cartDetails.getCartId();
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(resultSet.getBigDecimal(4)));
					cartDetails.setCartStatusName("Order placed by customer.");
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>Process Order</a>");

					cartDetailsList.add(cartDetails);
				} else {
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(resultSet.getBigDecimal(4)));
				}
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
	public void reassignPendingRejectItems(Connection con, Map<Long, Long> sellerCartItemMap) throws Exception {
		
		String listOfItems = null;
		for (Map.Entry<Long, Long> entry : sellerCartItemMap.entrySet())
		{
			if(listOfItems==null){
				listOfItems = entry.getKey().toString();
			}else{
				listOfItems = listOfItems + "," + entry.getKey();
			}
		}
		
		// update the seller cart Item.
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_SELLER_CART_ITEM);
			pst.setString(1, listOfItems);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("Order status is not updated successfully.");
				throw new Exception();
			}
			
			SellerRepository sellerRepository = new SellerRepositoryImpl();
			sellerRepository.persistSellerWithCartItem(con, sellerCartItemMap);

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
	public void updateOrderTotal(Connection con, Long cartId, BigDecimal newOrderTotal) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_ORDER_TOTAL);
			pst.setBigDecimal(1, newOrderTotal);
			pst.setBigDecimal(2, newOrderTotal);
			pst.setLong(3, cartId);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("Order total is not updated successfully.");
				throw new Exception();
			}
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
	public List<CartDetails> getRecentApprovedOrdersBySeller(Connection con, Integer sellerId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_RECENT_ORDER_BY_SELLER);
			pst.setInt(1, sellerId);
			pst.setLong(2, Constants.ORDER_CONFIRMED_BY_SELLER);
			pst.setLong(3, Constants.ORDER_REJECTED_BY_SELLER);
			pst.setLong(4, Constants.ORDER_PENDING_BY_SELLER);
			pst.setBoolean(5, true);
			pst.setString(6, getTodaysDateString() + "%");

			ResultSet resultSet = pst.executeQuery();

			List<CartDetails> cartDetailsList = new ArrayList<>();
			CartDetails cartDetails = null;
			Long cartId = null;
			while (resultSet.next()) {
				if (cartId == null || (!cartId.equals(resultSet.getLong(1)))) {
					cartDetails = new CartDetails();

					cartDetails.setCartId(resultSet.getLong(1));
					cartDetails.setOrderNumber(resultSet.getString(2));
					cartDetails.setOrderDate(resultSet.getTimestamp(3));
					cartDetails.setTotalAmount(BigDecimal.ZERO);

					cartId = cartDetails.getCartId();
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(resultSet.getBigDecimal(4)));
					cartDetails.setCartStatusName("Order placed by customer.");
					cartDetails.setViewLink(
							"<a href='javascript:void(0);' onclick='viewNewOrder("+ cartDetails.getCartId() +")'>Process Order</a>");

					cartDetailsList.add(cartDetails);
				} else {
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(resultSet.getBigDecimal(4)));
				}
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
	
	private String getTodaysDateString(){
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		System.out.println(sdf.format(date));
		return sdf.format(date);
	}

	@Override
	public Integer getOrderStatus(Connection con, Long cartId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_STATUS);
			pst.setLong(1, cartId);

			ResultSet resultSet = pst.executeQuery();

			Integer orderStatus = null;
			if (resultSet.next()) {
				orderStatus = resultSet.getInt(1);
			}
			return orderStatus;
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
	public void deleteAddressForUser(Connection con, Long addressId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(DELETE_ADDRESS);
			pst.setLong(1, addressId);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("Order status is not updated successfully.");
				throw new Exception();
			}

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
	public List<CartDetails> getOrdersForUser(Connection con, Long userId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ALL_ORDERS_FOR_USER);
			pst.setLong(1, userId);

			ResultSet resultSet = pst.executeQuery();
			
			CartDetails cartDetails = null;
			List<CartDetails> cartDetailsList = new ArrayList<>();
			
			while (resultSet.next()) {
				cartDetails = new CartDetails();
				cartDetails.setCartId(resultSet.getLong(1));
				cartDetails.setCartStatusId(resultSet.getLong(2));
				cartDetails.setCartOwner(resultSet.getLong(3));
				cartDetails.setOrderNumber(resultSet.getString(4));
				cartDetails.setSubtotalAmount(resultSet.getBigDecimal(5));
				cartDetails.setTax(resultSet.getBigDecimal(6));
				cartDetails.setShippingCharge(resultSet.getBigDecimal(7));
				cartDetails.setTotalAmount(
						resultSet.getBigDecimal(8) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(8));
				cartDetails.setTotalMsrp(
						resultSet.getBigDecimal(9) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(9));
				cartDetails.setOrderDate(resultSet.getTimestamp(10));
				cartDetails.setModifiedDate(resultSet.getTimestamp(11));
				
				cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

				CartRepository cartRepository = new CartRepositoryImpl();
				cartDetails.setCartItems(cartRepository.getActiveCartItemsForCartId(con, cartDetails.getCartId(), false));
				
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
	public void updateShippingCharge(Connection con, CartDetails cartDetails) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_SHIPPING_CHARGE_FOR_ORDER);
			pst.setBigDecimal(1, cartDetails.getTotalAmount());
			pst.setBigDecimal(2, cartDetails.getShippingCharge());
			pst.setLong(3, cartDetails.getCartId());

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("Order shipping charge is not updated successfully.");
				throw new Exception();
			}

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
	public List<String> getDeliveryArea(Connection con) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_DELIVERY_AREA);

			ResultSet rs = pst.executeQuery();
			
			List<String> deliveryAreaList = new ArrayList<>();
			while(rs.next()){
				deliveryAreaList.add(rs.getString(1));
			}
			
			return deliveryAreaList;

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
	public List<Long> getOrderForDeliveryDate(Connection con, String deliveryDate) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_ID_FOR_DATE);
			pst.setString(1, deliveryDate);

			ResultSet rs = pst.executeQuery();
			
			List<Long> orderIds = new ArrayList<>();
			while(rs.next()){
				orderIds.add(rs.getLong(1));
			}
			
			return orderIds;

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
