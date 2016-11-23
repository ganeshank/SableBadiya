package com.cigital.integration.service;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.MediaVo;

public interface MediaService {
	public List<MediaVo> getHomePageSliderImage(Connection con);
}
