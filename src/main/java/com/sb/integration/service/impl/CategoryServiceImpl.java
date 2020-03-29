package com.sb.integration.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sb.integration.dao.CategoryRepository;
import com.sb.integration.dao.impl.CategoryRepositoryImpl;
import com.sb.integration.service.CategoryService;
import com.sb.integration.vo.CategoryVo;

public class CategoryServiceImpl implements CategoryService {

	CategoryRepository categoryRepository = new CategoryRepositoryImpl();
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired, 
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded, Boolean onlyParentRequired) {
		
		return categoryRepository.getAllCategory(con, isGoodsRequired, smallImageNeeded, 
				rupeeQuantityAdded, onlyParentRequired);
	}

	@Override
	public CategoryVo getCategory(Connection con, Long categoryId) throws Exception {
		// TODO Auto-generated method stub
		return categoryRepository.getCategory(con, categoryId);
	}
	
	@Override
	public List<CategoryVo> getCategoryHierarchy(Connection con) throws Exception {
		
		List<CategoryVo> allCategory = categoryRepository.getAllCategory(con, false, false, false, false);
		
		LinkedHashMap<Long, CategoryVo> categoryMap = new LinkedHashMap<>();
		for (CategoryVo categoryVo : allCategory) {
			categoryMap.put(categoryVo.getCategoryId(), categoryVo);
		}
		
		for (Map.Entry<Long, CategoryVo> entry : categoryMap.entrySet()) {
			CategoryVo category = entry.getValue();
			if(!category.getCategoryId().equals(category.getParentCategoryId())){
				subCategorySetup(categoryMap, category);
			}
		}
		
		List<CategoryVo> categoryWithParent = new ArrayList<>();
		for (Map.Entry<Long, CategoryVo> entry : categoryMap.entrySet()) {
			CategoryVo category = entry.getValue();
			//if(category.getCategoryId().equals(category.getParentCategoryId())){
				categoryWithParent.add(category);
			//}
		}
		
		return categoryWithParent;
	}
	
	private static void subCategorySetup(LinkedHashMap<Long, CategoryVo> categoryMap, CategoryVo category){
		CategoryVo parentCategory = categoryMap.get(category.getParentCategoryId());
		List<CategoryVo> subCategorie = parentCategory.getCategoryVos();
		if(subCategorie==null){
			subCategorie = new ArrayList<>();
		}
		
		subCategorie.add(category);
		parentCategory.setCategoryVos(subCategorie);
		
		categoryMap.put(category.getParentCategoryId(), parentCategory);
	}

	@Override
	public List<CategoryVo> getSubCategoryForParent(Connection con, Long parentCategoryId) throws Exception {
		
		
		return categoryRepository.getSubCategoryForParent(con, parentCategoryId);
	}
}
