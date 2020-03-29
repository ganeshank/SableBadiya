package com.sb.integration.service;

import java.sql.Connection;
import java.util.List;

import com.sb.integration.vo.MediaVo;

public interface MediaService {
	public List<MediaVo> getHomePageSliderImage(Connection con, Boolean smallMediaNeeded);
}
