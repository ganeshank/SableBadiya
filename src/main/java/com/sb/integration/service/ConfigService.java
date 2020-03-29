package com.sb.integration.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ConfigService {
	public Map<Integer, List<String>> getRestrictedUrls(Connection con)throws Exception;
}
