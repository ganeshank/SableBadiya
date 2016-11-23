package com.cigital.integration.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.cigital.integration.dao.ConfigRepository;
import com.cigital.integration.dao.impl.ConfigRepositoryImpl;
import com.cigital.integration.service.ConfigService;

public class ConfigServiceImpl implements ConfigService {

	@Override
	public Map<Integer, List<String>> getRestrictedUrls(Connection con)throws Exception {
		try {
			ConfigRepository configRepository = new ConfigRepositoryImpl();
			return configRepository.getRestrictedUrls(con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

}
