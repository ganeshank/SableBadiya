package com.cigital.integration.dao;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.MediaVo;

public interface MediaRepository {
	public List<MediaVo> getHomepageSlider(Connection con);
	public Long persistMedia(Connection con, String mediaName, String webpath, Integer mediaType, Long createdBy)throws Exception;
}
