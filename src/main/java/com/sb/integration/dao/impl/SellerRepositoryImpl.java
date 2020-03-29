package com.sb.integration.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sb.integration.dao.CartRepository;
import com.sb.integration.dao.SellerRepository;
import com.sb.integration.util.Constants;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.SellerVo;

public class SellerRepositoryImpl implements SellerRepository {

	private static final String GET_ALL_SELLER = "SELECT * FROM SELLER WHERE ACTIVE=1";

	private static final String GET_ALL_FIELD_EXECUTIVE = "SELECT * FROM FIELD_EXECUTIVE WHERE ACTIVE=1";

	private static final String PERSIST_ORDER_FOR_FE = "INSERT INTO ORDER_FOR_FE (CART_ID, FIELD_EXECUTIVE_ID, ACTIVE) VALUES(?,?,?)";

	private static final String PERSIST_SELLER_WITH_ITEM = "INSERT INTO SELLER_CART_ITEM (SELLER_ID, CART_ITEM_ID,STATUS, ACTIVE,CREATED_DATE) VALUES(?,?,?,?,?)";

	private static final String GET_SELLER_BY_USER_ID = "SELECT * FROM SELLER WHERE USER_ID=? AND ACTIVE=1";

	public static final String GET_FE_FOR_ORDER = "SELECT FE.* FROM ORDER_FOR_FE OFE, FIELD_EXECUTIVE FE WHERE "
			+ "OFE.FIELD_EXECUTIVE_ID = FE.FIELD_EXECUTIVE_ID AND OFE.ACTIVE=1 AND FE.ACTIVE=1 AND CART_ID=?";
	
	public static final String GET_ORDER_STATUS_BASED_ON_SELLER = "SELECT SCI.STATUS FROM SELLER_CART_ITEM SCI, CART_ITEM CI, "
			+ "CART C WHERE SCI.CART_ITEM_ID = CI.CART_ITEM_ID AND CI.CART_ID = C.CART_ID AND CI.ACTIVE=1 AND SCI.ACTIVE=1 "
			+ "AND C.CART_ID = ?";

	public static String UPDATE_SELLER_CART_ITEM = "UPDATE SELLER_CART_ITEM SET STATUS=?, REJECT_REASON=? WHERE SELLER_ID=? AND CART_ITEM_ID=? AND ACTIVE=1";

	public static final String GET_SELLER_FOR_CART_ITEM = "SELECT S.*, SCI.STATUS, SCI.REJECT_REASON FROM CART_ITEM CI, "
			+ "SELLER_CART_ITEM SCI, SELLER S WHERE CI.CART_ITEM_ID = SCI.CART_ITEM_ID AND SCI.SELLER_ID = S.SELLER_ID "
			+ "AND CI.ACTIVE=1 AND CI.CART_ITEM_ID=? AND SCI.ACTIVE=1";
	
	private static final String INACTIVE_FE_FOR_ORDER = "UPDATE ORDER_FOR_FE SET ACTIVE=0 WHERE CART_ID=?";
	
	private static final String INACTIVE_SELLER_CART_ITEM = "UPDATE SELLER_CART_ITEM SCI INNER JOIN CART_ITEM CI ON "
			+ "SCI.CART_ITEM_ID = CI.CART_ITEM_ID SET SCI.ACTIVE=0 WHERE SCI.ACTIVE=1 AND CI.ACTIVE=1 AND SCI.ACTIVE AND SCI.CART_ITEM_ID=?";
	
	@Override
	public List<SellerVo> getAllSeller(Connection con) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ALL_SELLER);
			ResultSet rs = pst.executeQuery();

			SellerVo sellerVo = null;
			List<SellerVo> sellerList = new ArrayList<SellerVo>();
			while (rs.next()) {
				sellerVo = new SellerVo();
				sellerVo.setSellerId(rs.getInt(1));
				sellerVo.setSellerName(rs.getString(2));
				sellerVo.setSellerContact(rs.getString(3));
				sellerVo.setAddressLine1(rs.getString(4));
				sellerVo.setAddressLine2(rs.getString(5));
				sellerVo.setCity(rs.getString(6));
				sellerVo.setState(rs.getString(7));
				sellerVo.setCountry(rs.getString(8));
				sellerVo.setUserId(rs.getLong(10));

				sellerList.add(sellerVo);
			}

			return sellerList;
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
	public List<FieldExecutiveVo> getAllFieldExecutive(Connection con) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ALL_FIELD_EXECUTIVE);
			ResultSet rs = pst.executeQuery();

			FieldExecutiveVo feVo = null;
			List<FieldExecutiveVo> feList = new ArrayList<FieldExecutiveVo>();
			while (rs.next()) {
				feVo = new FieldExecutiveVo();
				feVo.setFieldExecutiveId(rs.getInt(1));
				feVo.setFeName(rs.getString(2));
				feVo.setFeContact(rs.getString(3));
				feVo.setAddressLine1(rs.getString(4));
				feVo.setAddressLine2(rs.getString(5));
				feVo.setCity(rs.getString(6));
				feVo.setState(rs.getString(7));
				feVo.setUserId(rs.getLong(9));

				feList.add(feVo);
			}

			return feList;
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
	public void persistOrderForFe(Connection con, Long cartId, Integer feId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PERSIST_ORDER_FOR_FE);
			pst.setLong(1, cartId);
			pst.setInt(2, feId);
			pst.setBoolean(3, true);

			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("Order for fe is not persisted successfully.");
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
	public void persistSellerWithCartItem(Connection con, Map<Long, Long> sellerCartItemMap) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PERSIST_SELLER_WITH_ITEM);

			for (Map.Entry<Long, Long> entry : sellerCartItemMap.entrySet()) {
				Long key = entry.getKey();
				Long value = entry.getValue();

				pst.setLong(1, value);
				pst.setLong(2, key);
				pst.setInt(3, Constants.SELLER_CART_ITEM_STATUS_WAIT);
				pst.setBoolean(4, true);
				pst.setString(5, TimeStamp.getAsiaTimeStamp());

				pst.addBatch();
			}

			int[] rowAffectedForCartItem = pst.executeBatch();

			if (rowAffectedForCartItem.length != sellerCartItemMap.size()) {
				System.out.println("Seller with cart item is not persisted successfully.");
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
	public SellerVo getSellerByUserId(Connection con, Long userId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_SELLER_BY_USER_ID);
			pst.setLong(1, userId);
			ResultSet rs = pst.executeQuery();

			SellerVo sellerVo = null;
			while (rs.next()) {
				sellerVo = new SellerVo();
				sellerVo.setSellerId(rs.getInt(1));
				sellerVo.setSellerName(rs.getString(2));
				sellerVo.setSellerContact(rs.getString(3));
				sellerVo.setAddressLine1(rs.getString(4));
				sellerVo.setAddressLine2(rs.getString(5));
				sellerVo.setCity(rs.getString(6));
				sellerVo.setState(rs.getString(7));
				sellerVo.setCountry(rs.getString(8));
				sellerVo.setUserId(rs.getLong(10));
			}

			return sellerVo;
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
	public FieldExecutiveVo getFieldExecutiveForOrder(Connection con, Long cartId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_FE_FOR_ORDER);
			pst.setLong(1, cartId);
			ResultSet rs = pst.executeQuery();

			FieldExecutiveVo feVo = null;
			while (rs.next()) {
				feVo = new FieldExecutiveVo();
				feVo.setFieldExecutiveId(rs.getInt(1));
				feVo.setFeName(rs.getString(2));
				feVo.setFeContact(rs.getString(3));
				feVo.setAddressLine1(rs.getString(4));
				feVo.setAddressLine2(rs.getString(5));
				feVo.setCity(rs.getString(6));
				feVo.setState(rs.getString(7));
				feVo.setUserId(rs.getLong(9));
			}

			return feVo;
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
	public void updateSellerCartItem(Connection con, Long cartId, Integer sellerId, Map<Long, String> acceptRejectCartItemMap,
			Map<Long, String> rejectReasonCartItemMap) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_SELLER_CART_ITEM);

			for (Map.Entry<Long, String> entry : acceptRejectCartItemMap.entrySet()) {
				Long key = entry.getKey();
				String value = entry.getValue();
				
				if(value.equals("accept")){
					pst.setLong(1, 1l);
					pst.setString(2, "No Reason");
				}else{
					pst.setLong(1, 2l);
					pst.setString(2, rejectReasonCartItemMap.get(key));
				}
				
				pst.setInt(3, sellerId);
				pst.setLong(4, key);
				pst.addBatch();
			}

			int[] rowAffectedForCartItem = pst.executeBatch();

			if (rowAffectedForCartItem.length != acceptRejectCartItemMap.size()) {
				System.out.println("Seller with cart item is not updated successfully.");
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
	public Integer getOrderStatusBasedOnSellerCartItem(Connection con, Long cartId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_ORDER_STATUS_BASED_ON_SELLER);
			pst.setLong(1, cartId);
			ResultSet rs = pst.executeQuery();

			Integer status = 0;
			while (rs.next()) {
				Integer sellerCartItemStatus = rs.getInt(1);
				if(sellerCartItemStatus.equals(3)){
					// 1 says that all cart item does not get response from seller
					status = 1;
				}else if(sellerCartItemStatus.equals(1) && !status.equals(1)){
					// 2 says that item is approved from the seller.
					status = 2;
					continue;
				}else if(sellerCartItemStatus.equals(2)){
					// 3 says that item is rejected from the seller.
					status = 3;
					break;
				}
			}

			return status;
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
	public SellerVo getSellerForCartItem(Connection con, Long cartItemId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_SELLER_FOR_CART_ITEM);
			pst.setLong(1, cartItemId);
			ResultSet rs = pst.executeQuery();

			SellerVo sellerVo = null;
			while (rs.next()) {
				sellerVo = new SellerVo();
				sellerVo.setSellerId(rs.getInt(1));
				sellerVo.setSellerName(rs.getString(2));
				sellerVo.setSellerContact(rs.getString(3));
				sellerVo.setAddressLine1(rs.getString(4));
				sellerVo.setAddressLine2(rs.getString(5));
				sellerVo.setCity(rs.getString(6));
				sellerVo.setState(rs.getString(7));
				sellerVo.setCountry(rs.getString(8));
				sellerVo.setUserId(rs.getLong(10));
				
				sellerVo.setCartItemStatus(rs.getInt(11));
				sellerVo.setRejectReason(rs.getString(12));
			}

			return sellerVo;
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
	public void inActiveFeForOrder(Connection con, Long cartId) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(INACTIVE_FE_FOR_ORDER);
			pst.setLong(1, cartId);

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
	public void InActiveSellerCartItem(Connection con, Long cartItemId) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(INACTIVE_SELLER_CART_ITEM);
			pst.setLong(1, cartItemId);
			
			Integer rowAffectedForCartItem = pst.executeUpdate();

			if (rowAffectedForCartItem < 0) {
				System.out.println("seller cart item status is not updated successfully.");
				throw new Exception();
			}else{
				CartRepository cartRepository = new CartRepositoryImpl();
				cartRepository.inActiveCartItemById(con, cartItemId);
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
}
