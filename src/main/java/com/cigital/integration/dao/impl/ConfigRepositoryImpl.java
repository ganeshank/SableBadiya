package com.cigital.integration.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cigital.integration.dao.ConfigRepository;

public class ConfigRepositoryImpl implements ConfigRepository {
	
	public static final String GET_RESTRICTED_URL_WITH_ROLES = "select * from url_restriction where active=1 order by role_id";

	@Override
	public Map<Integer, List<String>> getRestrictedUrls(Connection con)throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(GET_RESTRICTED_URL_WITH_ROLES);
			ResultSet resultSet = pst.executeQuery();

			Map<Integer, List<String>> restrictedUrlMap = new HashMap<>();
			List<String> urlList = null;
			Integer roleChangeFlag = 0;
			while (resultSet.next()) {
				Integer roleId = resultSet.getInt("ROLE_ID");
				String url = resultSet.getString("URL");
				
				if(restrictedUrlMap.size()==0 || !roleChangeFlag.equals(roleId)){
					roleChangeFlag = roleId;
					urlList = new ArrayList<>();
					urlList.add(url);
					restrictedUrlMap.put(roleId, urlList);
				}else{
					urlList.add(url);
				}
			}

			return restrictedUrlMap;
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
