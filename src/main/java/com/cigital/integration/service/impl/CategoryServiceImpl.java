package com.cigital.integration.service.impl;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.dao.CategoryRepository;
import com.cigital.integration.dao.impl.CategoryRepositoryImpl;
import com.cigital.integration.service.CategoryService;
import com.cigital.integration.vo.CategoryVo;

public class CategoryServiceImpl implements CategoryService {

	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired) {
		CategoryRepository categoryRepository = new CategoryRepositoryImpl();
		return categoryRepository.getAllCategory(con, isGoodsRequired);
	}
}
