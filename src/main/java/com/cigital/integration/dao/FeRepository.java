package com.cigital.integration.dao;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.FieldExecutiveVo;

public interface FeRepository {
	public FieldExecutiveVo getFeByUserId(Connection con, Long userId) throws Exception;
	
	public List<CartDetails> getNewOrdersForFe(Connection con, Integer feId)throws Exception;
	
	public CartDetails getOrderForFe(Connection con, Long cartId, Integer feId)throws Exception;
}
