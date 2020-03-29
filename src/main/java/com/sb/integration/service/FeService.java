package com.sb.integration.service;

import java.sql.Connection;

import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.FieldExecutiveVo;

public interface FeService {
	public FieldExecutiveVo getFeByUserId(Connection con, Long userId) throws Exception;
	
	public String getNewOrdersForFe(Connection con, Integer feId)throws Exception;
	
	public CartDetails getOrderForFe(Connection con, Long cartId, Integer feId)throws Exception;
}
