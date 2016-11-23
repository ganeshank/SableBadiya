package com.cigital.integration.dao;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.CategoryVo;

public interface CategoryRepository {
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired);
}
