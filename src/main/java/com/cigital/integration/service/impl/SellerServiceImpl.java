package com.cigital.integration.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.cigital.integration.dao.SellerRepository;
import com.cigital.integration.dao.impl.SellerRepositoryImpl;
import com.cigital.integration.service.SellerService;
import com.cigital.integration.vo.FieldExecutiveVo;
import com.cigital.integration.vo.SellerVo;

public class SellerServiceImpl implements SellerService {

	private SellerRepository sellerRepository = new SellerRepositoryImpl();

	@Override
	public List<SellerVo> getAllSeller(Connection con) throws Exception {
		try {
			return sellerRepository.getAllSeller(con);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public List<FieldExecutiveVo> getAllFieldExecutive(Connection con) throws Exception {
		try {
			return sellerRepository.getAllFieldExecutive(con);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public void persistOrderForFe(Connection con, Long cartId, Integer feId) throws Exception {
		// TODO Auto-generated method stub
		try {
			sellerRepository.persistOrderForFe(con, cartId, feId);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public void persistSellerWithCartItem(Connection con, Map<Long, Long> sellerCartItemMap) throws Exception {
		try {
			sellerRepository.persistSellerWithCartItem(con, sellerCartItemMap);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public SellerVo getSellerByUserId(Connection con, Long userId) throws Exception {
		try {
			return sellerRepository.getSellerByUserId(con, userId);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public FieldExecutiveVo getFieldExecutiveForOrder(Connection con, Long cartId) throws Exception {
		try {
			return sellerRepository.getFieldExecutiveForOrder(con, cartId);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public void updateSellerCartItem(Connection con, Long cartId, Integer sellerId,
			Map<Long, String> acceptRejectCartItemMap, Map<Long, String> rejectReasonCartItemMap) throws Exception {
		try {
			sellerRepository.updateSellerCartItem(con, cartId, sellerId, acceptRejectCartItemMap, rejectReasonCartItemMap);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}
	
	@Override
	public Integer getOrderStatusBasedOnSellerCartItem(Connection con, Long cartId) throws Exception {
		try {
			return sellerRepository.getOrderStatusBasedOnSellerCartItem(con, cartId);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public void inActiveFeForOrder(Connection con, Long cartId) throws Exception {
		try {
			sellerRepository.inActiveFeForOrder(con, cartId);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}

	@Override
	public void InActiveSellerCartItem(Connection con, Long cartItemId) throws Exception {
		try {
			sellerRepository.InActiveSellerCartItem(con, cartItemId);
		} catch (Exception e) {
			System.out.println("Failed to get all seller details.");
			throw new Exception(e);
		}
	}
	
}
