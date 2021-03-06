package com.sb.integration.service;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import com.sb.integration.vo.GoodsUpdateVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;

public interface GoodsService {
	public List<GoodsVo> getGoodsForCategory(Connection con, String categories, Boolean smallImageNeeded, Boolean rupeeQuantityAdded);
	
	public List<GoodsVo> getAllGoods(Connection con)throws Exception;
	
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue, Boolean smallImageNeeded, Boolean rupeeQuantityAdded) throws Exception;
	
	public void updateGoodsPriceAndStock(Connection con, List<GoodsUpdateVo> goodsUpdateVos)throws Exception;
	
	public List<QuantityVo> getAllQuantity(Connection con)throws Exception;
	
	public void addItemByAdmin(Connection con, String goodsName, String category, String seller, String price, 
			String mrp, String qty, String fileName, Long createdBy, Boolean allowRupee, Boolean inStock, Properties prop)throws Exception;
	
	public void sellerGoodsBackupDayWise(Connection con)throws Exception;
	
	public List<GoodsVo> getDealOfTheDayGoods(Connection con, Boolean smallImageNeeded, Boolean rupeeQuantityAdded)throws Exception;
}
