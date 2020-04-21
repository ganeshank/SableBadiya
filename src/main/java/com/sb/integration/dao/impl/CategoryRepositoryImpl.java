package com.sb.integration.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sb.integration.dao.CategoryRepository;
import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.CategoryVo;

public class CategoryRepositoryImpl implements CategoryRepository{

	private final static String GET_ALL_PARENT_CATEGORY = "SELECT c.CATEGORY_ID, c.CATEGORY_NAME, c.CATEGORY_DESC,"
			+ " c.MEDIA_ID, m.MEDIA_NAME, m.WEBPATH, m.MEDIA_TYPE_ID, ch.PARENT_CATEGORY_ID FROM category_hierarchy ch INNER JOIN"
			+ " category c ON c.CATEGORY_ID = ch.CATEGORY_ID INNER JOIN media m ON c.MEDIA_ID = m.MEDIA_ID"
			+ " WHERE ch.CATEGORY_ID=ch.PARENT_CATEGORY_ID AND c.category_id=c.top_category AND c.ACTIVE=1 AND m.ACTIVE=1";
	
	private final static String GET_ALL_CATGEORY = "SELECT c.CATEGORY_ID, c.CATEGORY_NAME, c.CATEGORY_DESC,"
			+ " c.MEDIA_ID, m.MEDIA_NAME, m.WEBPATH, m.MEDIA_TYPE_ID, ch.PARENT_CATEGORY_ID FROM category_hierarchy ch INNER JOIN"
			+ " category c ON c.CATEGORY_ID = ch.CATEGORY_ID INNER JOIN MEDIA m ON c.MEDIA_ID = m.MEDIA_ID"
			+ " WHERE c.ACTIVE=1 AND m.ACTIVE=1";
	
	private final static String GET_CATEGORY_FOR_ID = "SELECT CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESC,"
			+ " MEDIA_ID, CATEGORY_MEDIA_FOLDER FROM category WHERE CATEGORY_ID=?";
	
	private final static String GET_SUBCATEGORY_FOR_PARENT = "SELECT C.CATEGORY_ID, C.CATEGORY_NAME, C.CATEGORY_DESC, C.MEDIA_ID FROM category C INNER JOIN category_hierarchy CH "
			+ "ON C.CATEGORY_ID = CH.CATEGORY_ID WHERE CH.PARENT_CATEGORY_ID=? AND "
			+ "CH.PARENT_CATEGORY_ID <> C.CATEGORY_ID";
	
	
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired, Boolean smallImageNeeded, 
			Boolean rupeeQuantityAdded, Boolean onlyParentRequired) {
		
		try {
			PreparedStatement pst = null;
			
			if(onlyParentRequired){
				pst = con.prepareStatement(GET_ALL_PARENT_CATEGORY);
			}else{
				pst = con.prepareStatement(GET_ALL_CATGEORY);
			}
			
			ResultSet rs = pst.executeQuery();
			
			List<CategoryVo> categories = new ArrayList<CategoryVo>();
			CategoryVo category = null;
			while(rs.next()){
				category = new CategoryVo();
				category.setCategoryId(rs.getLong(1));
				category.setCategoryName(rs.getString(2));
				category.setCategoryDesc(rs.getString(3));
				category.setMediaId(rs.getLong(4));
				category.setMediaName(rs.getString(5));
				category.setMediaWebpath(rs.getString(6));
				category.setMediaTypeId(rs.getInt(7));
				category.setParentCategoryId(rs.getLong(8));
				
				if(isGoodsRequired){
					GoodsRepository goodsRepository = new GoodsRepositoryImpl();
					category.setGoods(goodsRepository.getGoodsForCategory(con, category.getCategoryId().toString(),smallImageNeeded, rupeeQuantityAdded));
				}
				
				if(smallImageNeeded){
					category.setMediaWebpath(UserUtil.getMediaUrlForCategory(category.getMediaWebpath()));
				}
				
				categories.add(category);
			}
			
			return categories;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public CategoryVo getCategory(Connection con, Long categoryId) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_CATEGORY_FOR_ID);
			pst.setLong(1, categoryId);
			ResultSet rs = pst.executeQuery();
			
			CategoryVo category = null;
			if(rs.next()){
				category = new CategoryVo();
				category.setCategoryId(rs.getLong(1));
				category.setCategoryName(rs.getString(2));
				category.setCategoryDesc(rs.getString(3));
				category.setMediaId(rs.getLong(4));
				category.setCategoryMediaForder(rs.getString(5));
			}
			
			return category;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<CategoryVo> getSubCategoryForParent(Connection con, Long parentCategoryId) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_SUBCATEGORY_FOR_PARENT);
			pst.setLong(1, parentCategoryId);
			
			ResultSet rs = pst.executeQuery();
			
			List<CategoryVo> categories = new ArrayList<CategoryVo>();
			CategoryVo category = null;
			while(rs.next()){
				category = new CategoryVo();
				category.setCategoryId(rs.getLong(1));
				category.setCategoryName(rs.getString(2));
				category.setCategoryDesc(rs.getString(3));
				category.setMediaId(rs.getLong(4));
				
				categories.add(category);
			}
			
			return categories;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
	}

}
