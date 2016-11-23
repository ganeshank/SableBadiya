package com.cigital.integration.service;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.CategoryVo;

public interface CategoryService {
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired);
}
