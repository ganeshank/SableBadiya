package com.cigital.integration.service.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cigital.integration.dao.FeRepository;
import com.cigital.integration.dao.impl.FeRepositoryImpl;
import com.cigital.integration.service.FeService;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.FieldExecutiveVo;
import com.google.gson.Gson;

public class FeServiceImpl implements FeService {
	
	FeRepository feRepository = new FeRepositoryImpl();
	
	@Override
	public FieldExecutiveVo getFeByUserId(Connection con, Long userId) throws Exception {
		
		return feRepository.getFeByUserId(con, userId);
	}

	@Override
	public String getNewOrdersForFe(Connection con, Integer feId) throws Exception {
		try {
			List<CartDetails> cartDetails = feRepository.getNewOrdersForFe(con, feId);
			Map<String, Object> map = new HashMap<>();
			map.put("data", cartDetails);

			Gson json = new Gson();
			return json.toJson(map);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public CartDetails getOrderForFe(Connection con, Long cartId, Integer feId) throws Exception {
		try {
			return feRepository.getOrderForFe(con, cartId, feId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

}
