package com.sb.integration.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.GoodsUpdateVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;
import com.sb.integration.vo.SellerGoodsVo;

public class GoodsRepositoryImpl implements GoodsRepository {

	private String GET_ALL_GOODS_FOR_CATEGORY = "SELECT G.GOODS_ID, G.GOODS_NAME, G.DESCRIPTION,"
			+ " C.CATEGORY_ID, M.MEDIA_ID, M.WEBPATH, M.MEDIA_NAME,S.PRICE,S.MSRP,S.QUANTITY_PER_UOM,"
			+ "S.UOM, G.IN_STOCK, G.IN_RUPEE, G.IS_TODAYS_DEAL, G.IS_PRE_ORDER FROM  GOODS G,  MEDIA M,  CATEGORY C,  SELLER_GOODS S "
			+ "WHERE G.MEDIA_ID = M.MEDIA_ID AND G.GOODS_ID = S.GOODS_ID "
			+ "AND G.CATEGORY_ID = C.CATEGORY_ID AND C.ACTIVE=1 AND G.ACTIVE=1"
			+ " AND M.ACTIVE=1 AND C.CATEGORY_ID IN (categories) AND S.SELLER_ID=? ORDER BY G.IS_TODAYS_DEAL=1 DESC, G.IN_STOCK=1 DESC, G.GOODS_NAME";
	
	private static String GET_ALL_ACTIVE_GOODS = "SELECT G.GOODS_ID, G.GOODS_NAME, G.DESCRIPTION,"
			+ " C.CATEGORY_ID, M.MEDIA_ID, M.WEBPATH, M.MEDIA_NAME,S.PRICE,S.MSRP,S.QUANTITY_PER_UOM,"
			+ "S.UOM FROM  GOODS G,  MEDIA M,  CATEGORY C,  SELLER_GOODS S "
			+ "WHERE G.MEDIA_ID = M.MEDIA_ID AND G.GOODS_ID = S.GOODS_ID "
			+ "AND G.CATEGORY_ID = C.CATEGORY_ID AND C.ACTIVE=1 AND G.ACTIVE=1"
			+ " AND M.ACTIVE=1";

	private static String GET_ALL_QUANTITY_FOR_GOOD = "SELECT QS.* FROM  QUANTITY_STANDARD QS, "
			+ " GOODS_QUANTITY GQ,  GOODS G WHERE G.GOODS_ID = GQ.GOODS_ID "
			+ "AND GQ.QUANTITY_ID = QS.QUANTITY_ID AND GQ.GOODS_ID=? AND "
			+ "GQ.ACTIVE=1 AND QS.ACTIVE=1 AND G.ACTIVE=1";

	private static final String GET_QUANTITY_FOR_ID = "SELECT * FROM  QUANTITY_STANDARD "
			+ "WHERE QUANTITY_ID=? AND ACTIVE=1";
	
	private static final String GET_GOODS_FOR_ID = "SELECT G.GOODS_ID, G.GOODS_NAME, G.DESCRIPTION,"
			+ " G.CATEGORY_ID, M.MEDIA_ID, M.WEBPATH, M.MEDIA_NAME,S.PRICE,S.MSRP,S.QUANTITY_PER_UOM,"
			+ "S.UOM, G.IN_STOCK, G.IN_RUPEE FROM  GOODS G,  MEDIA M,  SELLER_GOODS S "
			+ "WHERE G.MEDIA_ID = M.MEDIA_ID AND G.GOODS_ID = S.GOODS_ID "
			+ " AND G.ACTIVE=1 AND M.ACTIVE=1 AND G.GOODS_ID=? AND S.SELLER_ID=?";
	
	private static String GET_ALL_GOODS_BASED_ON_NAME = "SELECT G.GOODS_ID, G.GOODS_NAME, G.DESCRIPTION,"
			+ " C.CATEGORY_ID, M.MEDIA_ID, M.WEBPATH, M.MEDIA_NAME,S.PRICE,S.MSRP,S.QUANTITY_PER_UOM,"
			+ "S.UOM, G.IN_STOCK, G.IN_RUPEE, G.IS_TODAYS_DEAL, G.IS_PRE_ORDER  FROM  GOODS G,  MEDIA M,  CATEGORY C,  SELLER_GOODS S "
			+ "WHERE G.MEDIA_ID = M.MEDIA_ID AND G.GOODS_ID = S.GOODS_ID "
			+ "AND G.CATEGORY_ID = C.CATEGORY_ID AND C.ACTIVE=1 AND G.ACTIVE=1"
			+ " AND M.ACTIVE=1 AND G.GOODS_NAME like ? ORDER BY G.IN_STOCK=0 AND G.IS_TODAYS_DEAL=1";
	
	private static final String UPDATE_GOODS_PRICE_AND_STOCK = "UPDATE GOODS G INNER JOIN SELLER_GOODS SG ON "
			+ "G.GOODS_ID = SG.GOODS_ID SET G.IN_STOCK=?, SG.PRICE=?, SG.MSRP=?, G.IS_TODAYS_DEAL=? WHERE SG.SELLER_ID = 1 AND SG.GOODS_ID = ? "
			+ "AND G.ACTIVE=1 AND SG.ACTIVE=1";
	
	private static final String GET_ALL_QUANTITY = "select quantity_id, weight, uom from quantity_standard where active=1";
	
	private static final String PERSIST_GOODS = "INSERT INTO GOODS"
			+ "(GOODS_NAME, DESCRIPTION, CATEGORY_ID, MEDIA_ID, ACTIVE, CREATED_BY, CREATED_DATE, IN_RUPEE, IN_STOCK) VALUES(?,?,?,?,?,?,?,?,?)";
	
	private static final String PERSIST_GOODS_QTY = "INSERT INTO GOODS_QUANTITY (GOODS_ID, QUANTITY_ID, ACTIVE) VALUES(?,?,?)";
	
	private static final String PERSIST_SELLER_GOODS = "INSERT INTO SELLER_GOODS(SELLER_ID, GOODS_ID, PRICE, MSRP, "
			+ "QUANTITY_PER_UOM, UOM, ACTIVE) VALUES(?,?,?,?,?,?,?)";
	
	private static final String GET_SELLER_GOODS_DETAILS = "SELECT SG.SELLER_ID, SG.GOODS_ID, SG.PRICE, SG.MSRP, G.IN_STOCK FROM "
			+ " SELLER_GOODS SG, GOODS G WHERE SG.GOODS_ID = G.GOODS_ID AND G.ACTIVE=1 AND SG.ACTIVE=1";
	
	private static final String PERSIST_SELLER_GOODS_BACKUP = "INSERT INTO seller_goods_day_wise (GOODS_ID, SELLER_ID, PRICE, MSRP,"
			+ " RECORD_DATE, ACTIVE, IN_STOCK) VALUES(?,?,?,?,?,?,?)";
	
	
	private static final String GET_DEAL_OF_THE_DAY = "SELECT G.GOODS_ID, G.GOODS_NAME, G.DESCRIPTION,"
			+ " C.CATEGORY_ID, M.MEDIA_ID, M.WEBPATH, M.MEDIA_NAME,S.PRICE,S.MSRP,S.QUANTITY_PER_UOM,"
			+ "S.UOM, G.IN_STOCK, G.IN_RUPEE, G.IS_TODAYS_DEAL, G.IS_PRE_ORDER FROM  GOODS G,  MEDIA M,  CATEGORY C,  SELLER_GOODS S "
			+ "WHERE G.MEDIA_ID = M.MEDIA_ID AND G.GOODS_ID = S.GOODS_ID "
			+ "AND G.CATEGORY_ID = C.CATEGORY_ID AND C.ACTIVE=1 AND G.ACTIVE=1"
			+ " AND M.ACTIVE=1 AND S.ACTIVE=1 AND G.IS_TODAYS_DEAL=1";

	public List<GoodsVo> getGoodsForCategory(Connection con, String categories, 
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded) {

		try {
			GET_ALL_GOODS_FOR_CATEGORY = GET_ALL_GOODS_FOR_CATEGORY.replaceAll("categories", categories);
			
			PreparedStatement pst = con.prepareStatement(GET_ALL_GOODS_FOR_CATEGORY);
			pst.setLong(1, 1); // seller id is hard-coded because for now we
								// have 1 seller.
			ResultSet rs = pst.executeQuery();

			List<GoodsVo> goodsVos = new ArrayList<GoodsVo>();
			GoodsVo goodsVo = null;
			while (rs.next()) {
				goodsVo = new GoodsVo();
				goodsVo.setGoodsId(rs.getLong(1));
				goodsVo.setGoodsName(rs.getString(2));
				goodsVo.setDescription(rs.getString(3));
				goodsVo.setCategoryId(rs.getLong(4));
				goodsVo.setMediaId(rs.getLong(5));
				goodsVo.setWebpath(rs.getString(6));
				goodsVo.setMediaName(rs.getString(7));
				goodsVo.setPrice(rs.getBigDecimal(8));
				goodsVo.setMsrp(rs.getBigDecimal(9));
				goodsVo.setSaving(goodsVo.getMsrp().subtract(goodsVo.getPrice()));
				goodsVo.setQuantity_per_annum(rs.getString(10));
				goodsVo.setUom(rs.getString(11));
				goodsVo.setInStock(rs.getBoolean(12));
				goodsVo.setInRupee(rs.getBoolean(13));
				goodsVo.setQuantityVos(getQuantityListForGoods(con, goodsVo.getGoodsId()));
				goodsVo.setIsTodaysDeal(rs.getBoolean(14));
				goodsVo.setIsPreOrder(rs.getBoolean(15));
				
				if(goodsVo.getInRupee() && rupeeQuantityAdded){
					List<QuantityVo> existingQty = goodsVo.getQuantityVos();
					List<QuantityVo> rupeeQty = UserUtil.getRuppeQuantity();
					
					existingQty.addAll(rupeeQty);
					goodsVo.setQuantityVos(existingQty);
				}
				
				if(smallImageNeeded){
					goodsVo.setWebpath(UserUtil.getMediaUrl(goodsVo.getWebpath()));
				}

				goodsVos.add(goodsVo);
			}

			return goodsVos;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<QuantityVo> getQuantityListForGoods(Connection con, Long goodsId) {

		try {
			PreparedStatement pst = con.prepareStatement(GET_ALL_QUANTITY_FOR_GOOD);
			pst.setLong(1, goodsId);
			ResultSet rs = pst.executeQuery();

			List<QuantityVo> quantityVos = new ArrayList<QuantityVo>();
			QuantityVo quantityVo = null;
			while (rs.next()) {
				quantityVo = new QuantityVo();
				quantityVo.setQuantityId(rs.getLong("QUANTITY_ID"));
				quantityVo.setWeight(rs.getString("WEIGHT"));
				quantityVo.setUom(rs.getString("UOM"));
				quantityVos.add(quantityVo);
			}

			return quantityVos;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public QuantityVo getQuantityForId(Connection con, Long quantityId)throws Exception {

		try {
			PreparedStatement pst = con.prepareStatement(GET_QUANTITY_FOR_ID);
			pst.setLong(1, quantityId);
			ResultSet rs = pst.executeQuery();

			QuantityVo quantityVo = null;
			while (rs.next()) {
				quantityVo = new QuantityVo();
				quantityVo.setQuantityId(rs.getLong("QUANTITY_ID"));
				quantityVo.setWeight(rs.getString("WEIGHT"));
				quantityVo.setUom(rs.getString("UOM"));
			}

			return quantityVo;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public GoodsVo getGoodsForId(Connection con, Long goodsId)throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_GOODS_FOR_ID);
			pst.setLong(1, goodsId);
			pst.setLong(2, 1);    // seller id is hardcoded...
			ResultSet rs = pst.executeQuery();

			GoodsVo goodsVo = null;
			while (rs.next()) {
				goodsVo = new GoodsVo();
				goodsVo.setGoodsId(rs.getLong(1));
				goodsVo.setGoodsName(rs.getString(2));
				goodsVo.setDescription(rs.getString(3));
				goodsVo.setCategoryId(rs.getLong(4));
				goodsVo.setMediaId(rs.getLong(5));
				goodsVo.setWebpath(rs.getString(6));
				goodsVo.setMediaName(rs.getString(7));
				goodsVo.setPrice(rs.getBigDecimal(8));
				goodsVo.setMsrp(rs.getBigDecimal(9));
				goodsVo.setQuantity_per_annum(rs.getString(10));
				goodsVo.setUom(rs.getString(11));
				goodsVo.setInStock(rs.getBoolean(12));
				goodsVo.setInRupee(rs.getBoolean(13));
				goodsVo.setQuantityVos(getQuantityListForGoods(con, goodsVo.getGoodsId()));
			}

			return goodsVo;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public List<GoodsVo> getAllGoods(Connection con) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_ALL_ACTIVE_GOODS);
			ResultSet rs = pst.executeQuery();

			List<GoodsVo> goodsVos = new ArrayList<GoodsVo>();
			GoodsVo goodsVo = null;
			while (rs.next()) {
				goodsVo = new GoodsVo();
				goodsVo.setGoodsId(rs.getLong(1));
				goodsVo.setGoodsName(rs.getString(2));
				goodsVo.setDescription(rs.getString(3));
				goodsVo.setCategoryId(rs.getLong(4));
				goodsVo.setMediaId(rs.getLong(5));
				goodsVo.setWebpath(rs.getString(6));
				goodsVo.setMediaName(rs.getString(7));
				goodsVo.setPrice(rs.getBigDecimal(8));
				goodsVo.setMsrp(rs.getBigDecimal(9));
				goodsVo.setSaving(goodsVo.getMsrp().subtract(goodsVo.getPrice()));
				goodsVo.setQuantity_per_annum(rs.getString(10));
				goodsVo.setUom(rs.getString(11));

				goodsVos.add(goodsVo);
			}

			return goodsVos;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<GoodsVo> getGoodsByGoodsName(Connection con, String searchValue,
			Boolean smallImageNeeded, Boolean rupeeQuantityAdded) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_ALL_GOODS_BASED_ON_NAME);
			pst.setString(1, "%" + searchValue + "%");
			
			ResultSet rs = pst.executeQuery();

			List<GoodsVo> goodsVos = new ArrayList<GoodsVo>();
			GoodsVo goodsVo = null;
			while (rs.next()) {
				goodsVo = new GoodsVo();
				goodsVo.setGoodsId(rs.getLong(1));
				goodsVo.setGoodsName(rs.getString(2));
				goodsVo.setDescription(rs.getString(3));
				goodsVo.setCategoryId(rs.getLong(4));
				goodsVo.setMediaId(rs.getLong(5));
				goodsVo.setWebpath(rs.getString(6));
				goodsVo.setMediaName(rs.getString(7));
				goodsVo.setPrice(rs.getBigDecimal(8));
				goodsVo.setMsrp(rs.getBigDecimal(9));
				goodsVo.setSaving(goodsVo.getMsrp().subtract(goodsVo.getPrice()));
				goodsVo.setQuantity_per_annum(rs.getString(10));
				goodsVo.setUom(rs.getString(11));
				goodsVo.setQuantityVos(getQuantityListForGoods(con, goodsVo.getGoodsId()));
				goodsVo.setInStock(rs.getBoolean(12));
				goodsVo.setInRupee(rs.getBoolean(13));
				goodsVo.setIsTodaysDeal(rs.getBoolean(14));
				goodsVo.setIsPreOrder(rs.getBoolean(15));
				
				if(goodsVo.getInRupee() && rupeeQuantityAdded){
					List<QuantityVo> existingQty = goodsVo.getQuantityVos();
					List<QuantityVo> rupeeQty = UserUtil.getRuppeQuantity();
					
					existingQty.addAll(rupeeQty);
					goodsVo.setQuantityVos(existingQty);
				}
				
				if(smallImageNeeded){
					goodsVo.setWebpath(UserUtil.getMediaUrl(goodsVo.getWebpath()));
				}

				goodsVos.add(goodsVo);
			}

			return goodsVos;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    throw new Exception();
		}
	}

	@Override
	public void updateGoodsPriceAndStock(Connection con, List<GoodsUpdateVo> goodsUpdateVos) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(UPDATE_GOODS_PRICE_AND_STOCK);

			for (GoodsUpdateVo goodsUpdateVo : goodsUpdateVos) {
				pst.setBoolean(1, goodsUpdateVo.getInStock());
				pst.setBigDecimal(2, goodsUpdateVo.getPrice());
				pst.setBigDecimal(3, goodsUpdateVo.getMsrp());
				pst.setBoolean(4, goodsUpdateVo.getIsTodaysDeal());
				pst.setLong(5, goodsUpdateVo.getGoodsId());
				
				pst.addBatch();
			}

			int[] rowAffectedForCartItem = pst.executeBatch();

			if (rowAffectedForCartItem.length != goodsUpdateVos.size()) {
				System.out.println("Goods price n stock is not updated successfully.");
				throw new Exception();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public List<QuantityVo> getAllQuantity(Connection con) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_ALL_QUANTITY);
			
			ResultSet rs = pst.executeQuery();

			List<QuantityVo> quantityVos = new ArrayList<QuantityVo>();
			QuantityVo quantityVo = null;
			while (rs.next()) {
				quantityVo = new QuantityVo();
				quantityVo.setQuantityId(rs.getLong(1));
				quantityVo.setWeight(rs.getString(2));
				quantityVo.setUom(rs.getString(3));
				
				quantityVos.add(quantityVo);
			}

			return quantityVos;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    throw new Exception();
		}
	}

	@Override
	public Long persistGoodsByAdmin(Connection con, GoodsVo goodsVo, Long createdBy) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PERSIST_GOODS, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, goodsVo.getGoodsName());
			pst.setString(2, goodsVo.getDescription());
			pst.setLong(3, goodsVo.getCategoryId());
			pst.setLong(4, goodsVo.getMediaId());
			pst.setBoolean(5, true);
			pst.setLong(6, createdBy);
			pst.setString(7, TimeStamp.getAsiaTimeStamp());
			pst.setBoolean(8, goodsVo.getInRupee());
			pst.setBoolean(9, goodsVo.getInStock());
			

			int rowAffectedForGoods = pst.executeUpdate();

			if (rowAffectedForGoods < 0) {
				System.out.println("Goods is not inserted successfully.");
				throw new Exception();
			}
			
			ResultSet rs = pst.getGeneratedKeys();
			Long goodsId = null;
			if (rs != null && rs.next()) {
				goodsId = rs.getLong(1);
			}
			
			return goodsId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public void persistGoodsQuantity(Connection con, String qty, Long goodsId) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PERSIST_GOODS_QTY);

			String[] qtyArray = qty.split(",");
			for(int i=0; i<qtyArray.length; i++){
				pst.setLong(1, goodsId);
				pst.setLong(2, Long.parseLong(qtyArray[i]));
				pst.setBoolean(3, true);
				
				pst.addBatch();
			}

			int[] rowAffectedForCartItem = pst.executeBatch();

			if (rowAffectedForCartItem.length != qtyArray.length) {
				System.out.println("Goods qty is not inserted successfully.");
				throw new Exception();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public void persistSellerGoods(Connection con, Long sellerId, Long goodsId, BigDecimal price, BigDecimal mrp,
			Integer qtyPerUom, String uom) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PERSIST_SELLER_GOODS);
			pst.setLong(1, sellerId);
			pst.setLong(2, goodsId);
			pst.setBigDecimal(3, price);
			pst.setBigDecimal(4, mrp);
			pst.setInt(5, qtyPerUom);
			pst.setString(6, uom);
			pst.setBoolean(7, true);

			int rowAffectedForGoods = pst.executeUpdate();

			if (rowAffectedForGoods < 0) {
				System.out.println("Goods seller is not inserted successfully.");
				throw new Exception();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
		
	}

	@Override
	public List<SellerGoodsVo> getAllSellerGoods(Connection con) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_SELLER_GOODS_DETAILS);
			
			ResultSet rs = pst.executeQuery();

			List<SellerGoodsVo> sellerGoodsVos = new ArrayList<SellerGoodsVo>();
			SellerGoodsVo sellerGoodsVo = null;
			while (rs.next()) {
				sellerGoodsVo = new SellerGoodsVo();
				sellerGoodsVo.setSellerId(rs.getLong(1));
				sellerGoodsVo.setGoodsId(rs.getLong(2));
				sellerGoodsVo.setPrice(rs.getBigDecimal(3));
				sellerGoodsVo.setMsrp(rs.getBigDecimal(4));
				sellerGoodsVo.setInStock(rs.getBoolean(5));
				
				sellerGoodsVos.add(sellerGoodsVo);
			}

			return sellerGoodsVos;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    throw new Exception();
		}
	}

	@Override
	public void backupSellerGoods(Connection con, List<SellerGoodsVo> sellerGoodsVos) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(PERSIST_SELLER_GOODS_BACKUP);
			
			for (SellerGoodsVo sellerGoodsVo : sellerGoodsVos) {
				pst.setLong(1, sellerGoodsVo.getGoodsId());
				pst.setLong(2, sellerGoodsVo.getSellerId());
				pst.setBigDecimal(3, sellerGoodsVo.getPrice());
				pst.setBigDecimal(4, sellerGoodsVo.getMsrp());
				pst.setString(5, TimeStamp.getAsiaTimeStamp());
				pst.setBoolean(6, true);
				pst.setBoolean(7, sellerGoodsVo.getInStock());
				
				pst.addBatch();
			}

			int[] rowAffectedForCartItem = pst.executeBatch();

			if (rowAffectedForCartItem.length != sellerGoodsVos.size()) {
				System.out.println("seller goods backup is not successfully taken.");
				throw new Exception();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	@Override
	public List<GoodsVo> getDealOfTheDayGoods(Connection con, Boolean smallImageNeeded, Boolean rupeeQuantityAdded) throws Exception {
		try {
			PreparedStatement pst = con.prepareStatement(GET_DEAL_OF_THE_DAY);
			ResultSet rs = pst.executeQuery();

			List<GoodsVo> goodsVos = new ArrayList<GoodsVo>();
			GoodsVo goodsVo = null;
			while (rs.next()) {
				goodsVo = new GoodsVo();
				goodsVo.setGoodsId(rs.getLong(1));
				goodsVo.setGoodsName(rs.getString(2));
				goodsVo.setDescription(rs.getString(3));
				goodsVo.setCategoryId(rs.getLong(4));
				goodsVo.setMediaId(rs.getLong(5));
				goodsVo.setWebpath(rs.getString(6));
				goodsVo.setMediaName(rs.getString(7));
				goodsVo.setPrice(rs.getBigDecimal(8));
				goodsVo.setMsrp(rs.getBigDecimal(9));
				goodsVo.setSaving(goodsVo.getMsrp().subtract(goodsVo.getPrice()));
				goodsVo.setQuantity_per_annum(rs.getString(10));
				goodsVo.setUom(rs.getString(11));
				goodsVo.setInStock(rs.getBoolean(12));
				goodsVo.setInRupee(rs.getBoolean(13));
				goodsVo.setQuantityVos(getQuantityListForGoods(con, goodsVo.getGoodsId()));
				goodsVo.setIsTodaysDeal(rs.getBoolean(14));
				goodsVo.setIsPreOrder(rs.getBoolean(15));
				
				if(goodsVo.getInRupee() && rupeeQuantityAdded){
					List<QuantityVo> existingQty = goodsVo.getQuantityVos();
					List<QuantityVo> rupeeQty = UserUtil.getRuppeQuantity();
					
					existingQty.addAll(rupeeQty);
					goodsVo.setQuantityVos(existingQty);
				}
				
				if(smallImageNeeded){
					goodsVo.setWebpath(UserUtil.getMediaUrl(goodsVo.getWebpath()));
				}
				
				goodsVos.add(goodsVo);
			}

			return goodsVos;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}
