package com.sb.integration.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.dao.MediaRepository;
import com.sb.integration.dao.impl.GoodsRepositoryImpl;
import com.sb.integration.dao.impl.MediaRepositoryImpl;
import com.sb.integration.service.CategoryService;
import com.sb.integration.service.GoodsService;
import com.sb.integration.vo.CategoryVo;
import com.sb.integration.vo.GoodsUpdateVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;
import com.sb.integration.vo.SellerGoodsVo;

public class GoodsServicesImpl implements GoodsService {
	
	final static Logger logger = Logger.getLogger(GoodsServicesImpl.class);
	
	GoodsRepository goodsRepository = new GoodsRepositoryImpl();
	
	public List<GoodsVo> getGoodsForCategory(Connection con, String categories, 
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded) {
		
		return goodsRepository.getGoodsForCategory(con, categories, smallImageNeeded, rupeeQuantityAdded);
	}

	@Override
	public List<GoodsVo> getAllGoods(Connection con) throws Exception {
		
		List<GoodsVo> goodsList = goodsRepository.getAllGoods(con);
		return goodsList;
	}

	@Override
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue,
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded)throws Exception {
		// TODO Auto-generated method stub
		return goodsRepository.getGoodsByGoodsName(con, searchValue, smallImageNeeded, rupeeQuantityAdded);
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
			CategoryService categoryService = new CategoryServiceImpl();
			CategoryVo categoryVo = categoryService.getCategory(con, Long.parseLong(category));
			
			String webpath = prop.getProperty("application.base.path") + "/media/" + categoryVo.getCategoryMediaForder() + "/" + fileName;
			MediaRepository mediaRepository = new MediaRepositoryImpl();
			Long mediaId = mediaRepository.persistMedia(con, goodsName, webpath, 3, createdBy);
			
			GoodsVo goodsVo = new GoodsVo();
			goodsVo.setGoodsName(goodsName);
			goodsVo.setDescription(goodsName);
			goodsVo.setCategoryId(Long.parseLong(category));
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
	public List<GoodsVo> getDealOfTheDayGoods(Connection con, Boolean smallImageNeeded, Boolean rupeeQuantityAdded) throws Exception {
		// TODO Auto-generated method stub
		try{
			return goodsRepository.getDealOfTheDayGoods(con, smallImageNeeded, rupeeQuantityAdded);
			
		}catch(Exception e){
			logger.error("Error occured::"+e);
			throw new Exception(e);
		}
	}

}
