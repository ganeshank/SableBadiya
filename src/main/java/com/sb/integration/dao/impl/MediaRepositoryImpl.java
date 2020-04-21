package com.sb.integration.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sb.integration.dao.MediaRepository;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.MediaVo;

public class MediaRepositoryImpl implements MediaRepository {
	
	private static final String GET_SLIDER_IMAGE_FOR_HOMEPAGE = "SELECT MEDIA_ID, MEDIA_NAME, WEBPATH,"
			+ " MEDIA_TYPE_ID FROM media WHERE ACTIVE=1 AND MEDIA_TYPE_ID=2";
	
	private static final String PERSIST_MEDIA = "INSERT INTO media"
			+ "(MEDIA_NAME, WEBPATH, MEDIA_TYPE_ID, ACTIVE, CREATED_BY, CREATED_DATE) VALUES(?,?,?,?,?,?)";

	public List<MediaVo> getHomepageSlider(Connection con, Boolean smallMediaNeeded) {
		try {
			PreparedStatement pst = con.prepareStatement(GET_SLIDER_IMAGE_FOR_HOMEPAGE);
			ResultSet rs = pst.executeQuery();
			
			List<MediaVo> mediaVos = new ArrayList<MediaVo>();
			MediaVo media = null;
			while(rs.next()){
				media = new MediaVo();
				media.setMediaId(rs.getLong(1));
				media.setMediaName(rs.getString(2));
				media.setMediaWebpath(rs.getString(3));
				media.setMediaTypeId(rs.getInt(4));
				
				if(smallMediaNeeded){
					media.setMediaWebpath(UserUtil.getMediaUrlForCategory(media.getMediaWebpath()));
				}
				
				mediaVos.add(media);
			}
			
			return mediaVos;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Long persistMedia(Connection con, String mediaName, String webpath, Integer mediaType, Long createdBy) throws Exception {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(PERSIST_MEDIA, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, mediaName);
			pst.setString(2, webpath);
			pst.setInt(3, mediaType);
			pst.setBoolean(4, true);
			pst.setLong(5, createdBy);
			pst.setString(6, TimeStamp.getAsiaTimeStamp());

			Integer rowAffectedForCart = pst.executeUpdate();
			
			ResultSet rs = pst.getGeneratedKeys();
			Long mediaId = null;
			if (rs != null && rs.next()) {
				mediaId = rs.getLong(1);
			}
			
			if (rowAffectedForCart < 0) {
				System.out.println("Media is inserted successfully...");
				throw new Exception("Media is inserted successfully...");
			}
			
			return mediaId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Media is not inserted successfully..." + e.getMessage());
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

}
