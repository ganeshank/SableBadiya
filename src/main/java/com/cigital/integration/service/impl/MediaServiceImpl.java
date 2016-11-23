package com.cigital.integration.service.impl;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.dao.MediaRepository;
import com.cigital.integration.dao.impl.MediaRepositoryImpl;
import com.cigital.integration.service.MediaService;
import com.cigital.integration.vo.MediaVo;

public class MediaServiceImpl implements MediaService {

	public List<MediaVo> getHomePageSliderImage(Connection con) {
		MediaRepository mediaRepository = new MediaRepositoryImpl();
		return mediaRepository.getHomepageSlider(con);
	}
	
}
