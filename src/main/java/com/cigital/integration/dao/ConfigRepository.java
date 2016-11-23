package com.cigital.integration.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ConfigRepository {
	public Map<Integer, List<String>> getRestrictedUrls(Connection con)throws Exception;
}
