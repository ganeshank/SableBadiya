package com.sb.integration.service.impl;

import java.sql.Connection;
import java.util.List;

import com.sb.integration.dao.MediaRepository;
import com.sb.integration.dao.impl.MediaRepositoryImpl;
import com.sb.integration.service.MediaService;
import com.sb.integration.vo.MediaVo;

public class MediaServiceImpl implements MediaService {

	public List<MediaVo> getHomePageSliderImage(Connection con, Boolean smallMediaNeeded) {
		MediaRepository mediaRepository = new MediaRepositoryImpl();
		return mediaRepository.getHomepageSlider(con, smallMediaNeeded);
	}
	
}
