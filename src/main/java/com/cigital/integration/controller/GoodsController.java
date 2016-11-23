package com.cigital.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cigital.integration.service.GoodsService;
import com.cigital.integration.service.impl.GoodsServicesImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.vo.GoodsVo;
import com.google.gson.Gson;

/**
 * Servlet implementation class GoodsController
 */
public class GoodsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			String pathInfo = request.getRequestURI().substring(request.getContextPath().length());
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String isFromSearch = request.getParameter("isFromSearch");
			
			GoodsService goodsService = new GoodsServicesImpl();
			List<GoodsVo> goodsVos = null;
			
			if(pathInfo.contains("deal_of_the_day")){
				goodsVos = goodsService.getDealOfTheDayGoods(con);
			}else{
				if(isFromSearch.equals("true")){
					
					String searchVal = request.getParameter("searchVal");
					if(searchVal.contains("("))
						searchVal = searchVal.substring(0,searchVal.indexOf("("));
					
					goodsVos = goodsService.getGoodsByGoodsName(con, searchVal);
				}else{
					
					String selectedCategoryId = request.getParameter("categoryId");
					goodsVos = goodsService.getGoodsForCategory(con, Long.parseLong(selectedCategoryId));
					System.out.println(new Gson().toJson(goodsVos));
				}
			}
			
			request.setAttribute("goods", goodsVos);
			RequestDispatcher rs = request.getRequestDispatcher("jsp/goods.jsp");
			rs.forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
