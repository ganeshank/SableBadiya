package com.cigital.integration.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cigital.integration.dao.CartRepository;
import com.cigital.integration.dao.GoodsRepository;
import com.cigital.integration.dao.SellerRepository;
import com.cigital.integration.dao.UserRepository;
import com.cigital.integration.util.Constants;
import com.cigital.integration.util.TimeStamp;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.CartItem;
import com.cigital.integration.vo.DeliveryTypeVo;
import com.cigital.integration.vo.GoodsVo;
import com.cigital.integration.vo.QuantityVo;

public class CartRepositoryImpl implements CartRepository {
	
	private SellerRepository sellerRepository = new SellerRepositoryImpl();

	private static final String CREATE_CART = "INSERT INTO  CART (CART_STATUS_ID,"
			+ " CART_OWNER,SUBTOTAL_AMOUNT,TAX,SHIPPING_CHARGE,TOTAL_AMOUNT,TOTAL_MSRP,CREATED_BY,"
			+ " CREATED_DATE,ACTIVE, ORDER_NUMBER, ORDER_DATE, SHIPPING_ADDRESS, COMMENTS, DELIVERY_OPTION) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String IS_GOODS_ALREADY_EXIST_INTO_CART = "SELECT C.CART_ID FROM  CART C, "
			+ " CART_ITEM CI WHERE C.CART_ID = CI.CART_ID AND C.CART_OWNER=? AND CI.GOODS_ID=? AND "
			+ "C.CART_STATUS_ID=1 AND C.ACTIVE=1 AND CI.ACTIVE=1";

	private static final String GET_CART_ITEMS_FOR_USER = "SELECT CI.* FROM  CART C,  CART_ITEM CI "
			+ "WHERE C.CART_ID = CI.CART_ID AND C.CART_OWNER=? AND C.CART_STATUS_ID=1 "
			+ "AND C.ACTIVE=1 AND CI.ACTIVE=1";

	private static final String GET_CART_ITEMS_FOR_ID = "SELECT CI.* FROM  CART_ITEM CI "
			+ "WHERE CI.CART_ITEM_ID=? AND CI.ACTIVE=1";

	private static final String GET_CART_FOR_USER = "SELECT C.CART_ID, C.CART_STATUS_ID, C.CART_OWNER,"
			+ "C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX, C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP "
			+ " FROM  CART C WHERE C.CART_OWNER=? AND C.CART_STATUS_ID=1 AND C.ACTIVE=1 ";

	private static final String GET_CART_FOR_ID = "SELECT C.CART_ID, C.CART_STATUS_ID, C.CART_OWNER,"
			+ "C.ORDER_NUMBER, C.SUBTOTAL_AMOUNT, C.TAX, C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP "
			+ " FROM  CART C WHERE C.CART_ID=? AND C.CART_STATUS_ID=1 AND C.ACTIVE=1 ";

	private static final String UPDATE_CART_FOR_USER = "UPDATE  CART SET SUBTOTAL_AMOUNT=?, TAX=?, SHIPPING_CHARGE=?,"
			+ " TOTAL_AMOUNT=?, TOTAL_MSRP=? WHERE CART_ID=?";

	private static final String UPDATE_CART_ITEM_FOR_USER = "UPDATE  CART_ITEM SET QUANTITY=?, UOM=?, PRICE=?, MSRP_PRICE=?,"
			+ " MODIFIED_BY=?, MODIFIED_DATE=? WHERE CART_ID=? AND CART_ITEM_ID=?";

	private static final String INSERT_CART_ITEM_FOR_USER = "INSERT INTO  CART_ITEM (CART_ID, QUANTITY, UOM, PRICE, MSRP_PRICE, GOODS_ID, "
			+ "CREATED_BY, CREATED_DATE, ACTIVE) VALUES(?,?,?,?,?,?,?,?,1)";

	private static final String UPDATE_CART_ITEM_ON_DELETE = "UPDATE  CART_ITEM SET ACTIVE=0 WHERE CART_ITEM_ID=?";

	private static final String EMPTY_CART = "UPDATE  CART_ITEM SET ACTIVE=0 WHERE CART_ID=? AND ACTIVE=1";

	private static final String IS_EMAIL_VALID = "SELECT USER_ID FROM  USERS WHERE EMAIL=?";

	private static final String RESET_PASSWORD = "UPDATE  USERS SET PASSWORD=? WHERE EMAIL=?";

	private static final String GET_ALL_DELIVERY_TYPE = "SELECT * FROM  DELIVERY_TYPE WHERE ACTIVE=1";

	private static final String PLACE_ORDER = "UPDATE  CART SET ORDER_NUMBER=?, ORDER_DATE=?, MODIFIED_BY=?,"
			+ "MODIFIED_DATE=?, SHIPPING_ADDRESS=?, DELIVERY_OPTION=?, COMMENTS=?, CART_STATUS_ID=?, ACTIVE=0 WHERE CART_ID=?";
	
	private static final String GET_ORDER_FOR_ID = "SELECT C.CART_ID, C.CART_STATUS_ID, A.CONTACT_NAME, A.CONTACT_NUMBER,"
			+ " A.EMAIL,A.ADDRESS_LINE1, A.ADDRESS_LINE2, A.CITY, A.STATE, A.LANDMARK, C.ORDER_NUMBER,"
			+ " C.SUBTOTAL_AMOUNT, C.TAX,C.SHIPPING_CHARGE, C.TOTAL_AMOUNT, C.TOTAL_MSRP, C.ORDER_DATE, C.DELIVERY_OPTION, "
			+ " C.CART_OWNER, C.COMMENTS FROM CART C,"
			+ " ADDRESS A WHERE C.SHIPPING_ADDRESS = A.ADDRESS_ID AND C.CART_ID=?";
	
	private static final String GET_CART_ITEMS_FOR_CART = "SELECT CI.* FROM CART_ITEM CI WHERE CI.ACTIVE=1 AND CI.CART_ID=?";
	
	private static final String INACTIVE_CART_ITEM_FOR_ID = "UPDATE CART_ITEM SET ACTIVE=0, IS_CANCELLED=1 WHERE CART_ITEM_ID=?";

	public Long createEmptyCart(Connection con, Long userId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(CREATE_CART, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, Constants.ACTIVE_CART_STATUS_ID);
			pst.setLong(2, userId);
			pst.setDouble(3, 0);
			pst.setDouble(4, 0);
			pst.setDouble(5, 0);
			pst.setDouble(6, 0);
			pst.setDouble(7, 0);
			pst.setLong(8, userId);
			/*pst.setDate(9, new Date(new java.util.Date().getTime()));*/
			pst.setString(9, TimeStamp.getAsiaTimeStamp());
			pst.setBoolean(10, true);
			pst.setNull(11, java.sql.Types.VARCHAR);
			pst.setNull(12, java.sql.Types.DATE);
			pst.setNull(13, java.sql.Types.BIGINT);
			pst.setNull(14, java.sql.Types.VARCHAR);
			pst.setNull(15, java.sql.Types.VARCHAR);

			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			Long cartId = null;
			if (rs != null && rs.next()) {
				cartId = rs.getLong(1);
			}

			return cartId;

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
	public Boolean isCartItemAlreadyExist(Connection con, Long userId, Long goodsId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(IS_GOODS_ALREADY_EXIST_INTO_CART);
			pst.setLong(1, userId);
			pst.setLong(2, goodsId);

			ResultSet resultSet = pst.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
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
	public List<CartItem> getActiveCartItemsForUser(Connection con, Long userId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_CART_ITEMS_FOR_USER);
			pst.setLong(1, userId);

			ResultSet resultSet = pst.executeQuery();

			List<CartItem> cartItems = new ArrayList<>();
			CartItem cartItem = null;
			while (resultSet.next()) {
				cartItem = new CartItem();
				cartItem.setCartItemId(resultSet.getLong("CART_ITEM_ID"));
				cartItem.setCartId(resultSet.getLong("CART_ID"));
				cartItem.setQuantity(resultSet.getString("QUANTITY"));
				cartItem.setUom(resultSet.getString("UOM"));
				cartItem.setPrice(resultSet.getBigDecimal("PRICE"));
				cartItem.setMsrp(resultSet.getBigDecimal("MSRP_PRICE"));
				cartItem.setGoodsId(resultSet.getLong("GOODS_ID"));
				cartItem.setSaving(cartItem.getMsrp().subtract(cartItem.getPrice()));

				GoodsRepository goodsRepository = new GoodsRepositoryImpl();
				GoodsVo goodsVo = goodsRepository.getGoodsForId(con, cartItem.getGoodsId());
				List<QuantityVo> quantityVos = goodsRepository.getQuantityListForGoods(con, cartItem.getGoodsId());

				cartItem.setGoodsVo(goodsVo);
				cartItem.setQuantityVos(quantityVos);

				cartItems.add(cartItem);
			}

			return cartItems;
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
	public CartDetails getActiveCartForUser(Connection con, Long userId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_CART_FOR_USER);
			pst.setLong(1, userId);

			ResultSet resultSet = pst.executeQuery();

			CartDetails cartDetails = null;
			if (resultSet.next()) {
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
				cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

				cartDetails.setCartItems(getActiveCartItemsForUser(con, userId));
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
	public void updateExistingCartItem(Connection con, CartDetails cartDetails, CartItem cartItem) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_CART_ITEM_FOR_USER);
			pst.setString(1, cartItem.getQuantity());
			pst.setString(2, cartItem.getUom());
			pst.setBigDecimal(3, cartItem.getPrice());
			pst.setBigDecimal(4, cartItem.getMsrp());
			pst.setLong(5, cartDetails.getCartOwner());
			/*pst.setDate(6, UserUtil.getCurrentDateTime());*/
			pst.setString(6, TimeStamp.getAsiaTimeStamp());
			pst.setLong(7, cartItem.getCartId());
			pst.setLong(8, cartItem.getCartItemId());

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem > 0) {
				updateCart(con, cartDetails);
			} else {
				System.out.println("Cart Item is not updated successfully...");
				throw new Exception();
			}

		} catch (Exception e) {
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
	public Boolean updateCart(Connection con, CartDetails cartDetails) throws Exception {

		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(UPDATE_CART_FOR_USER);
			pst.setBigDecimal(1, cartDetails.getSubtotalAmount());
			pst.setBigDecimal(2, cartDetails.getTax());
			pst.setBigDecimal(3, cartDetails.getShippingCharge());
			pst.setBigDecimal(4, cartDetails.getTotalAmount());
			pst.setBigDecimal(5, cartDetails.getTotalMsrp());
			pst.setLong(6, cartDetails.getCartId());

			Integer rowAffectedForCart = pst.executeUpdate();
			if (rowAffectedForCart < 0) {
				System.out.println("Cart is not updated successfully...");
				throw new Exception("Cart is not updated successfully...");
			}

			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Cart is not updated successfully..." + e.getMessage());
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public void addCartItem(Connection con, CartDetails cartDetails, CartItem cartItem, Boolean isCartUpdateNeeded) throws Exception {

		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(INSERT_CART_ITEM_FOR_USER, Statement.RETURN_GENERATED_KEYS);
			pst.setLong(1, cartDetails.getCartId());
			pst.setString(2, cartItem.getQuantity());
			pst.setString(3, cartItem.getUom());
			pst.setBigDecimal(4, cartItem.getPrice());
			pst.setBigDecimal(5, cartItem.getMsrp());
			pst.setLong(6, cartItem.getGoodsId());
			if(cartDetails.getCartOwner()!=null)
				pst.setLong(7, cartDetails.getCartOwner());
			else
				pst.setNull(7, java.sql.Types.BIGINT);
			//pst.setDate(8, new Date(new java.util.Date().getTime()));
			pst.setString(8, TimeStamp.getAsiaTimeStamp());

			Integer rowAffectedForCartItem = pst.executeUpdate();
			if (rowAffectedForCartItem < 0) {
				System.out.println("Cart is not updated successfully...");
				throw new Exception("Cart is not updated successfully...");
			}
			
			ResultSet rs = pst.getGeneratedKeys();
			if (rs != null && rs.next()) {
				cartItem.setCartItemId(rs.getLong(1));
			}

			if(isCartUpdateNeeded)
				updateCart(con, cartDetails);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Cart is not updated successfully..." + e.getMessage());
		} finally {
			if (pst != null) {
				pst.close();
			}
		}

	}

	@Override
	public CartItem getCartItemForId(Connection con, Long cartItemId, Boolean isRequiredGoodsQuantity)
			throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_CART_ITEMS_FOR_ID);
			pst.setLong(1, cartItemId);

			ResultSet resultSet = pst.executeQuery();

			CartItem cartItem = null;
			if (resultSet.next()) {
				cartItem = new CartItem();
				cartItem.setCartItemId(resultSet.getLong("CART_ITEM_ID"));
				cartItem.setCartId(resultSet.getLong("CART_ID"));
				cartItem.setQuantity(resultSet.getString("QUANTITY"));
				cartItem.setUom(resultSet.getString("UOM"));
				cartItem.setPrice(resultSet.getBigDecimal("PRICE"));
				cartItem.setMsrp(resultSet.getBigDecimal("MSRP_PRICE"));
				cartItem.setGoodsId(resultSet.getLong("GOODS_ID"));
				cartItem.setSaving(cartItem.getMsrp().subtract(cartItem.getPrice()));

				if (isRequiredGoodsQuantity) {
					GoodsRepository goodsRepository = new GoodsRepositoryImpl();
					GoodsVo goodsVo = goodsRepository.getGoodsForId(con, cartItem.getGoodsId());
					List<QuantityVo> quantityVos = goodsRepository.getQuantityListForGoods(con, cartItem.getGoodsId());

					cartItem.setGoodsVo(goodsVo);
					cartItem.setQuantityVos(quantityVos);

				}
			}

			return cartItem;
		} catch (Exception e) {
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
	public CartDetails getActiveCartForId(Connection con, Long cartId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_CART_FOR_ID);
			pst.setLong(1, cartId);

			ResultSet resultSet = pst.executeQuery();

			CartDetails cartDetails = null;
			if (resultSet.next()) {
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
				cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

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
	public CartDetails deleteCartItemForId(Connection con, Long cartItemId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_CART_ITEM_ON_DELETE);
			pst.setLong(1, cartItemId);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem > 0) {
				System.out.println("Deleted successfullly...");
			} else {
				System.out.println("Cart Item is not deleted successfully...");
				throw new Exception();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
		return null;
	}

	@Override
	public void emptyCart(Connection con, Long cartId) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(EMPTY_CART);
			pst.setLong(1, cartId);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem > 0) {
				System.out.println("Deleted successfullly...");
				CartDetails cartDetails = new CartDetails();
				cartDetails.setCartId(cartId);
				cartDetails.setSubtotalAmount(BigDecimal.ZERO);
				cartDetails.setTax(BigDecimal.ZERO);
				cartDetails.setTotalAmount(BigDecimal.ZERO);
				cartDetails.setTotalMsrp(BigDecimal.ZERO);
				cartDetails.setShippingCharge(BigDecimal.ZERO);

				updateCart(con, cartDetails);
			} else {
				System.out.println("Cart Items is not deleted successfully...");
				throw new Exception();
			}

		} catch (Exception e) {
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
	public Boolean isEmailValid(Connection con, String email) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(IS_EMAIL_VALID);
			pst.setString(1, email);

			ResultSet resultSet = pst.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
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
	public void resetPassword(Connection con, String emailId, String resetPass) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(RESET_PASSWORD);
			pst.setString(1, resetPass);
			pst.setString(2, emailId);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem > 0) {
				System.out.println("Reset password is successfullly done...");
			} else {
				System.out.println("Reset password is not successfullly done...");
				throw new Exception();
			}

		} catch (Exception e) {
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
	public List<DeliveryTypeVo> getAllDeliveryTpyes(Connection con) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ALL_DELIVERY_TYPE);

			ResultSet rs = pst.executeQuery();

			List<DeliveryTypeVo> deliveryTypeVos = new ArrayList<>();
			while (rs.next()) {
				DeliveryTypeVo deliveryTypeVo = new DeliveryTypeVo();
				deliveryTypeVo.setDeliveryTypeId(rs.getLong("DELIVERY_TYPE_ID"));
				deliveryTypeVo.setDeliveryName(rs.getString("DELIVERY_NAME"));
				deliveryTypeVo.setDeliveryDesc(rs.getString("DELIVERY_DESC"));

				deliveryTypeVos.add(deliveryTypeVo);
			}

			return deliveryTypeVos;

		} catch (Exception e) {
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
	public void placeOrder(Connection con, CartDetails cartDetails) throws Exception {

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PLACE_ORDER);
			pst.setString(1, cartDetails.getOrderNumber());
			pst.setString(2, TimeStamp.getAsiaTimeStamp());
			/*pst.setDate(2, new Date(new java.util.Date().getTime()));*/
			pst.setLong(3, cartDetails.getCartOwner());
			//pst.setDate(4, new Date(new java.util.Date().getTime()));
			pst.setString(4, TimeStamp.getAsiaTimeStamp());
			pst.setLong(5, cartDetails.getShippingAddress().getAddressId());
			pst.setString(6, cartDetails.getDeliveryOption());
			pst.setString(7, cartDetails.getComments());
			pst.setLong(8, Constants.ORDER_PLACED_BY_CUSTOMER_STATUS);
			pst.setLong(9, cartDetails.getCartId());

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem > 0) {
				System.out.println("order placed successfullly...");
			} else {
				System.out.println("order is not placed successfully...");
				throw new Exception();
			}

		} catch (Exception e) {
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
	public Long persistCart(Connection con, CartDetails cartDetails) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(CREATE_CART, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, Constants.ACTIVE_CART_STATUS_ID);
			if(cartDetails.getCartOwner()==null)
				pst.setNull(2, java.sql.Types.BIGINT);
			else
				pst.setLong(2, cartDetails.getCartOwner());
			
			pst.setBigDecimal(3, cartDetails.getSubtotalAmount());
			pst.setBigDecimal(4, cartDetails.getTax());
			pst.setBigDecimal(5, cartDetails.getShippingCharge());
			pst.setBigDecimal(6, cartDetails.getTotalAmount());
			pst.setBigDecimal(7, cartDetails.getTotalMsrp());
			if(cartDetails.getCartOwner()==null)
				pst.setNull(8, java.sql.Types.BIGINT);
			else
				pst.setLong(8, cartDetails.getCartOwner());
			/*pst.setDate(9, new Date(new java.util.Date().getTime()));*/
			pst.setString(9, TimeStamp.getAsiaTimeStamp());
			pst.setBoolean(10, true);
			pst.setString(11, cartDetails.getOrderNumber());
			/*pst.setDate(12, new Date(new java.util.Date().getTime()));*/
			pst.setString(12, TimeStamp.getAsiaTimeStamp());
			pst.setLong(13, cartDetails.getShippingAddress().getAddressId());
			pst.setString(14, cartDetails.getComments());
			pst.setString(15, cartDetails.getDeliveryOption());

			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			Long cartId = null;
			if (rs != null && rs.next()) {
				cartId = rs.getLong(1);
			}

			return cartId;

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
	public CartDetails getOrderForId(Connection con, Long cartId) throws Exception {
		
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_FOR_ID);
			pst.setLong(1, cartId);

			ResultSet resultSet = pst.executeQuery();

			CartDetails cartDetails = null;
			if (resultSet.next()) {
				cartDetails = new CartDetails();
				cartDetails.setCartId(resultSet.getLong(1));
				cartDetails.setCartStatusId(resultSet.getLong(2));
				//cartDetails.setOrderedBy(resultSet.getString(3));
				
				AddressVo address = new AddressVo();
				address.setContactName(resultSet.getString(3));
				address.setContactNumber(resultSet.getString(4));
				address.setEmail(resultSet.getString(5));
				address.setAddressLine1(resultSet.getString(6));
				address.setAddressLine2(resultSet.getString(7));
				address.setCity(resultSet.getString(8));
				address.setState(resultSet.getString(9));
				address.setLandMark(resultSet.getString(10));
				
				cartDetails.setShippingAddress(address);
				cartDetails.setOrderNumber(resultSet.getString(11));
				cartDetails.setSubtotalAmount(resultSet.getBigDecimal(12));
				cartDetails.setTax(resultSet.getBigDecimal(13));
				cartDetails.setShippingCharge(resultSet.getBigDecimal(14));
				cartDetails.setTotalAmount(
						resultSet.getBigDecimal(15) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(15));
				cartDetails.setTotalMsrp(
						resultSet.getBigDecimal(16) == null ? BigDecimal.ZERO : resultSet.getBigDecimal(16));
				cartDetails.setOrderDate(resultSet.getTimestamp(17));
				cartDetails.setDeliveryOption(resultSet.getString(18));
				cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));
				
				Boolean isSellerInfoRequired = false;
				if(!cartDetails.getCartStatusId().equals(2l))
					isSellerInfoRequired = true;
				
				cartDetails.setCartItems(getActiveCartItemsForCartId(con, cartId, isSellerInfoRequired));
				
				Long userId = resultSet.getLong(19);
				if(userId!=null && userId.equals(0l)){
					cartDetails.setOrderedBy("Guest");
				}else{
					UserRepository userRepository = new UserRepositoryImpl();
					cartDetails.setOrderedBy(userRepository.getUserDetailById(con, userId).getFullName());
				}
				
				cartDetails.setComments(resultSet.getString(20));
				
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
	public List<CartItem> getActiveCartItemsForCartId(Connection con, Long cartId, Boolean isSellerInfoRequired) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_CART_ITEMS_FOR_CART);
			pst.setLong(1, cartId);

			ResultSet resultSet = pst.executeQuery();

			List<CartItem> cartItems = new ArrayList<>();
			CartItem cartItem = null;
			while (resultSet.next()) {
				cartItem = new CartItem();
				cartItem.setCartItemId(resultSet.getLong("CART_ITEM_ID"));
				cartItem.setCartId(resultSet.getLong("CART_ID"));
				cartItem.setQuantity(resultSet.getString("QUANTITY"));
				cartItem.setUom(resultSet.getString("UOM"));
				cartItem.setPrice(resultSet.getBigDecimal("PRICE"));
				cartItem.setMsrp(resultSet.getBigDecimal("MSRP_PRICE"));
				cartItem.setGoodsId(resultSet.getLong("GOODS_ID"));
				cartItem.setSaving(cartItem.getMsrp().subtract(cartItem.getPrice()));
				cartItem.setIsCancelled(resultSet.getBoolean("IS_CANCELLED"));

				GoodsRepository goodsRepository = new GoodsRepositoryImpl();
				GoodsVo goodsVo = goodsRepository.getGoodsForId(con, cartItem.getGoodsId());
				List<QuantityVo> quantityVos = goodsRepository.getQuantityListForGoods(con, cartItem.getGoodsId());

				cartItem.setGoodsVo(goodsVo);
				cartItem.setQuantityVos(quantityVos);
				
				if(isSellerInfoRequired){
					cartItem.setSellerVo(sellerRepository.getSellerForCartItem(con, cartItem.getCartItemId()));
				}

				cartItems.add(cartItem);
			}

			return cartItems;
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
	public void inActiveCartItemById(Connection con, Long cartItemId) throws Exception {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(INACTIVE_CART_ITEM_FOR_ID);
			pst.setLong(1, cartItemId);

			Integer rowAffectedForCart = pst.executeUpdate();
			if (rowAffectedForCart < 0) {
				System.out.println("Cart Item is not updated successfully...");
				throw new Exception("Cart Item is not updated successfully...");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Cart Item is not updated successfully..." + e.getMessage());
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}
}
