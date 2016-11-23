package com.cigital.integration.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.GoodsUpdateVo;
import com.cigital.integration.vo.GoodsVo;
import com.cigital.integration.vo.QuantityVo;
import com.cigital.integration.vo.SellerGoodsVo;

public interface GoodsRepository {

	public List<GoodsVo> getGoodsForCategory(Connection con, Long categoryId);

	public List<QuantityVo> getQuantityListForGoods(Connection con, Long goodsId);
	
	public QuantityVo getQuantityForId(Connection con, Long quantityId)throws Exception;

	public GoodsVo getGoodsForId(Connection con, Long goodsId)throws Exception;
	
	public List<GoodsVo> getAllGoods(Connection con)throws Exception;
	
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue)throws Exception;
	
	public void updateGoodsPriceAndStock(Connection con, List<GoodsUpdateVo> goodsUpdateVos)throws Exception;
	
	public List<QuantityVo> getAllQuantity(Connection con)throws Exception;
	
	public Long persistGoodsByAdmin(Connection con, GoodsVo goodsVo, Long createdBy)throws Exception;
	
	public void persistGoodsQuantity(Connection con, String qty, Long goodsId)throws Exception;
	
	public void persistSellerGoods(Connection con, Long sellerId, Long goodsId, 
			BigDecimal price, BigDecimal mrp, Integer qtyPerUom, String uom)throws Exception;
	
	public List<SellerGoodsVo> getAllSellerGoods(Connection con)throws Exception;
	
	public void backupSellerGoods(Connection con, List<SellerGoodsVo> sellerGoodsVos)throws Exception;
	
	public List<GoodsVo> getDealOfTheDayGoods(Connection con)throws Exception;
}
