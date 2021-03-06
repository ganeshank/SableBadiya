package com.sb.integration.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.SellerVo;

public interface SellerService {
	public List<SellerVo> getAllSeller(Connection con) throws Exception;

	public List<FieldExecutiveVo> getAllFieldExecutive(Connection con) throws Exception;

	public void persistOrderForFe(Connection con, Long cartId, Integer feId) throws Exception;

	public void persistSellerWithCartItem(Connection con, Map<Long, Long> sellerCartItemMap) throws Exception;

	public SellerVo getSellerByUserId(Connection con, Long userId) throws Exception;

	public FieldExecutiveVo getFieldExecutiveForOrder(Connection con, Long cartId) throws Exception;

	public void updateSellerCartItem(Connection con, Long cartId, Integer sellerId, Map<Long, String> acceptRejectCartItemMap,
			Map<Long, String> rejectReasonCartItemMap) throws Exception;
	
	public Integer getOrderStatusBasedOnSellerCartItem(Connection con, Long cartId)throws Exception;
	
	public void inActiveFeForOrder(Connection con, Long cartId) throws Exception;
	
	public void InActiveSellerCartItem(Connection con, Long cartItemId)throws Exception;
	
}
