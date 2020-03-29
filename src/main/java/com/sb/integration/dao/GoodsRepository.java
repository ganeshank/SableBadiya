package com.sb.integration.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import com.sb.integration.vo.GoodsUpdateVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;
import com.sb.integration.vo.SellerGoodsVo;

public interface GoodsRepository {

	public List<GoodsVo> getGoodsForCategory(Connection con, String categories, Boolean smallImageNeeded, Boolean rupeeQuantityAdded);

	public List<QuantityVo> getQuantityListForGoods(Connection con, Long goodsId);
	
	public QuantityVo getQuantityForId(Connection con, Long quantityId)throws Exception;

	public GoodsVo getGoodsForId(Connection con, Long goodsId)throws Exception;
	
	public List<GoodsVo> getAllGoods(Connection con)throws Exception;
	
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue, Boolean smallImageNeeded, Boolean rupeeQuantityAdded)throws Exception;
	
	public void updateGoodsPriceAndStock(Connection con, List<GoodsUpdateVo> goodsUpdateVos)throws Exception;
	
	public List<QuantityVo> getAllQuantity(Connection con)throws Exception;
	
	public Long persistGoodsByAdmin(Connection con, GoodsVo goodsVo, Long createdBy)throws Exception;
	
	public void persistGoodsQuantity(Connection con, String qty, Long goodsId)throws Exception;
	
	public void persistSellerGoods(Connection con, Long sellerId, Long goodsId, 
			BigDecimal price, BigDecimal mrp, Integer qtyPerUom, String uom)throws Exception;
	
	public List<SellerGoodsVo> getAllSellerGoods(Connection con)throws Exception;
	
	public void backupSellerGoods(Connection con, List<SellerGoodsVo> sellerGoodsVos)throws Exception;
	
	public List<GoodsVo> getDealOfTheDayGoods(Connection con, Boolean smallImageNeeded, Boolean rupeeQuantityAdded)throws Exception;
}
