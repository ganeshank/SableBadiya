package com.cigital.integration.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.integration.dao.CategoryRepository;
import com.cigital.integration.dao.GoodsRepository;
import com.cigital.integration.vo.CategoryVo;

public class CategoryRepositoryImpl implements CategoryRepository{

	private final static String GET_ALL_CATEGORY = "SELECT C.CATEGORY_ID, C.CATEGORY_NAME, C.CATEGORY_DESC,"
			+ " C.MEDIA_ID, M.MEDIA_NAME, M.WEBPATH, M.MEDIA_TYPE_ID FROM  CATEGORY C,  MEDIA M"
			+ " WHERE C.MEDIA_ID  = M.MEDIA_ID AND C.ACTIVE=1 AND M.ACTIVE=1";
	
	
	public List<CategoryVo> getAllCategory(Connection con, Boolean isGoodsRequired) {
		
		try {
			PreparedStatement pst = con.prepareStatement(GET_ALL_CATEGORY);
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
				
				if(isGoodsRequired){
					GoodsRepository goodsRepository = new GoodsRepositoryImpl();
					category.setGoods(goodsRepository.getGoodsForCategory(con, category.getCategoryId()));
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

}
