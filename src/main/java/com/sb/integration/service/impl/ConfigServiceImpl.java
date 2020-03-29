package com.sb.integration.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.sb.integration.dao.ConfigRepository;
import com.sb.integration.dao.impl.ConfigRepositoryImpl;
import com.sb.integration.service.ConfigService;

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
