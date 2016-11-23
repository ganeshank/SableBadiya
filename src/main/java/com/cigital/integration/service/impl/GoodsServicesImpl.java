package com.cigital.integration.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cigital.integration.dao.GoodsRepository;
import com.cigital.integration.dao.MediaRepository;
import com.cigital.integration.dao.impl.GoodsRepositoryImpl;
import com.cigital.integration.dao.impl.MediaRepositoryImpl;
import com.cigital.integration.service.GoodsService;
import com.cigital.integration.vo.GoodsUpdateVo;
import com.cigital.integration.vo.GoodsVo;
import com.cigital.integration.vo.QuantityVo;
import com.cigital.integration.vo.SellerGoodsVo;
import com.google.gson.Gson;

public class GoodsServicesImpl implements GoodsService {
	
	final static Logger logger = Logger.getLogger(GoodsServicesImpl.class);
	
	GoodsRepository goodsRepository = new GoodsRepositoryImpl();
	
	public List<GoodsVo> getGoodsForCategory(Connection con, Long categoryId) {
		
		return goodsRepository.getGoodsForCategory(con, categoryId);
	}

	@Override
	public String getAllGoods(Connection con) throws Exception {
		
		List<GoodsVo> goodsList = goodsRepository.getAllGoods(con);
		return new Gson().toJson(goodsList);
	}

	@Override
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue)throws Exception {
		// TODO Auto-generated method stub
		return goodsRepository.getGoodsByGoodsName(con, searchValue);
	}

	@Override
	public void updateGoodsPriceAndStock(Connection con, List<GoodsUpdateVo> goodsUpdateVos) throws Exception {
		try{
			goodsRepository.updateGoodsPriceAndStock(con, goodsUpdateVos);
		}catch(Exception e){
			throw new Exception(e);
		}
	}

	@Override
	public List<QuantityVo> getAllQuantity(Connection con) throws Exception {
		// TODO Auto-generated method stub
		try{
			return goodsRepository.getAllQuantity(con);
		}catch(Exception e){
			throw new Exception(e);
		}
	}

	@Override
	public void addItemByAdmin(Connection con, String goodsName, String category, String seller, String price,
			String mrp, String qty, String fileName, Long createdBy, Boolean allowRupee, Boolean inStock, Properties prop) throws Exception {
		try{
			String mediaCategory = null;
			if(category.equalsIgnoreCase("veg")){
				mediaCategory = "vegetables";
			}else{
				mediaCategory = "fruits";
			}
			
			String webpath = prop.getProperty("application.base.path") + "/media/" + mediaCategory + "/" + fileName;
			MediaRepository mediaRepository = new MediaRepositoryImpl();
			Long mediaId = mediaRepository.persistMedia(con, goodsName, webpath, 3, createdBy);
			
			GoodsVo goodsVo = new GoodsVo();
			goodsVo.setGoodsName(goodsName);
			goodsVo.setDescription(goodsName);
			goodsVo.setCategoryId(category.equalsIgnoreCase("veg")?2l:1l);
			goodsVo.setMediaId(mediaId);
			goodsVo.setInRupee(allowRupee);
			goodsVo.setInStock(inStock);
			
			Long goodsId = goodsRepository.persistGoodsByAdmin(con, goodsVo, createdBy);
			
			goodsRepository.persistGoodsQuantity(con, qty, goodsId);
			
			Integer quantityPerUom = 1;
			String uom = null;
			
			QuantityVo quantityVo = goodsRepository.getQuantityForId(con, Long.parseLong(qty.split(",")[0]));
			if(quantityVo.getUom().equalsIgnoreCase("gm")){
				uom = "kg";
			}else{
				uom = quantityVo.getUom();
			}
			
			goodsRepository.persistSellerGoods(con, Long.parseLong(seller), goodsId, new BigDecimal(price), 
					new BigDecimal(mrp), quantityPerUom, uom);
			
		}catch(Exception e){
			throw new Exception(e);
		}
	}

	@Override
	public void sellerGoodsBackupDayWise(Connection con) throws Exception {
		try{
			List<SellerGoodsVo> sellerGoodsVos = goodsRepository.getAllSellerGoods(con);
			goodsRepository.backupSellerGoods(con, sellerGoodsVos);
			
		}catch(Exception e){
			logger.error("Error occured::"+e);
			throw new Exception(e);
		}
	}

	@Override
	public List<GoodsVo> getDealOfTheDayGoods(Connection con) throws Exception {
		// TODO Auto-generated method stub
		try{
			return goodsRepository.getDealOfTheDayGoods(con);
			
		}catch(Exception e){
			logger.error("Error occured::"+e);
			throw new Exception(e);
		}
	}

}
