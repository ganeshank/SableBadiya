package com.sb.integration.service;

import java.sql.Connection;
import java.util.List;
import com.sb.integration.vo.CategoryVo;

public interface CategoryService {
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired,
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded, Boolean onlyParentRequired);
	
	public CategoryVo getCategory(Connection con, Long categoryId) throws Exception;
	
	public List<CategoryVo> getCategoryHierarchy(Connection con) throws Exception;
	
	public List<CategoryVo> getSubCategoryForParent(Connection con, Long parentCategoryId) throws Exception;
}
