package com.cigital.integration.service;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import com.cigital.integration.vo.GoodsUpdateVo;
import com.cigital.integration.vo.GoodsVo;
import com.cigital.integration.vo.QuantityVo;

public interface GoodsService {
	public List<GoodsVo> getGoodsForCategory(Connection con, Long categoryId);
	
	public String getAllGoods(Connection con)throws Exception;
	
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue) throws Exception;
	
	public void updateGoodsPriceAndStock(Connection con, List<GoodsUpdateVo> goodsUpdateVos)throws Exception;
	
	public List<QuantityVo> getAllQuantity(Connection con)throws Exception;
	
	public void addItemByAdmin(Connection con, String goodsName, String category, String seller, String price, 
			String mrp, String qty, String fileName, Long createdBy, Boolean allowRupee, Boolean inStock, Properties prop)throws Exception;
	
	public void sellerGoodsBackupDayWise(Connection con)throws Exception;
	
	public List<GoodsVo> getDealOfTheDayGoods(Connection con)throws Exception;
}
