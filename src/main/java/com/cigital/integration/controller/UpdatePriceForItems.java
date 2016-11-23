package com.cigital.integration.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cigital.integration.service.GoodsService;
import com.cigital.integration.service.impl.GoodsServicesImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.vo.GoodsUpdateVo;

/**
 * Servlet implementation class UdatePriceForItems
 */
@WebServlet("/updatepriceforitems")
public class UpdatePriceForItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePriceForItems() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			Map<Long, String> selectStockMap = new HashMap<Long, String>();
			
			String[] goodsStockArray = request.getParameterValues("select-stock");
			for (String goodsStock : goodsStockArray) {
				String[] strSplit = goodsStock.split(" ");
				if(strSplit[0].equals("in") || strSplit[0].equals("out")){
					selectStockMap.put(Long.parseLong(strSplit[1]), strSplit[0]);
				}else{
					throw new Exception("Bad input of stock.");
				}
				
			}
			
			Map<Long, String> selectTodaysDealMap = new HashMap<Long, String>();
			
			String[] todaysDealArray = request.getParameterValues("todaysDeal");
			for (String todaysDeal : todaysDealArray) {
				String[] strSplit = todaysDeal.split(" ");
				if(strSplit[0].equals("false") || strSplit[0].equals("true")){
					selectTodaysDealMap.put(Long.parseLong(strSplit[1]), strSplit[0]);
				}else{
					throw new Exception("Bad input of stock.");
				}
				
			}
			
			
			Map<Long, String> priceMap = new HashMap<Long, String>();
			
			String[] goodsPriceArray = request.getParameterValues("price");
			for (String goodsPrice : goodsPriceArray) {
				String[] strSplit = goodsPrice.split(" ");
				priceMap.put(Long.parseLong(strSplit[1]), strSplit[0]);
			}
			
			Map<Long, String> msrpMap = new HashMap<Long, String>();
			
			String[] goodsMsrpArray = request.getParameterValues("msrp");
			for (String goodsMsrp : goodsMsrpArray) {
				String[] strSplit = goodsMsrp.split(" ");
				msrpMap.put(Long.parseLong(strSplit[1]), strSplit[0]);
			}
			
			
			List<GoodsUpdateVo> goodsUpdateVos = new ArrayList<>();
			GoodsUpdateVo goodsUpdateVo = null;
			for (Map.Entry<Long, String> entry : selectStockMap.entrySet()){
				
			    goodsUpdateVo = new GoodsUpdateVo();
			    goodsUpdateVo.setGoodsId(entry.getKey());
			    goodsUpdateVo.setInStock(entry.getValue().equals("in")?true:false);
			    
			    Boolean isTodaysDeal = selectTodaysDealMap.get(entry.getKey()).equals("true")?true:false;
			    goodsUpdateVo.setIsTodaysDeal(isTodaysDeal);
			    
			    goodsUpdateVo.setPrice(new BigDecimal(priceMap.get(goodsUpdateVo.getGoodsId())));
			    goodsUpdateVo.setMsrp(new BigDecimal(msrpMap.get(goodsUpdateVo.getGoodsId())));
			    
			    goodsUpdateVos.add(goodsUpdateVo);
			}
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			GoodsService goodsService = new GoodsServicesImpl();
			goodsService.updateGoodsPriceAndStock(con, goodsUpdateVos);
			
			System.out.println("Goods price n stock updated successfully.");
			
			response.sendRedirect("adminhome");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
