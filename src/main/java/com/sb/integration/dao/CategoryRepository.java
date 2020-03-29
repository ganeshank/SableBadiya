package com.sb.integration.dao;

import java.sql.Connection;
import java.util.List;

import com.sb.integration.vo.CategoryVo;

public interface CategoryRepository {
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired,
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded, Boolean onlyParentRequired);
	
	public CategoryVo getCategory(Connection con, Long categoryId) throws Exception;
	
	public List<CategoryVo> getSubCategoryForParent(Connection con, Long parentCategoryId) throws Exception;
}
