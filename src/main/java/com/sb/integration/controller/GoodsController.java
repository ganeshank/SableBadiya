package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.CategoryService;
import com.sb.integration.service.GoodsService;
import com.sb.integration.service.impl.CategoryServiceImpl;
import com.sb.integration.service.impl.GoodsServicesImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.SendOtp;
import com.sb.integration.vo.CategoryVo;
import com.sb.integration.vo.GoodsVo;
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
			
			CategoryService categoryService = new CategoryServiceImpl();
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String isFromSearch = request.getParameter("isFromSearch");
			
			GoodsService goodsService = new GoodsServicesImpl();
			List<GoodsVo> goodsVos = null;
			
			String selectedCategoryId = null;
			
			List<CategoryVo> immediateChild = new ArrayList<>();
			
			if(pathInfo.contains("deal_of_the_day")){
				immediateChild = categoryService.getAllCategory(con, false, false, false, true);
				goodsVos = goodsService.getDealOfTheDayGoods(con, false, false);
			}else{
				if(isFromSearch.equals("true")){
					immediateChild = categoryService.getAllCategory(con, false, false, false, true);
					String searchVal = request.getParameter("searchVal");
					if(searchVal.contains("("))
						searchVal = searchVal.substring(0,searchVal.indexOf("("));
					
					goodsVos = goodsService.getGoodsByGoodsName(con, searchVal, false, false);
				}else{
					selectedCategoryId = request.getParameter("categoryId");
					immediateChild = categoryService.getSubCategoryForParent(con, Long.parseLong(selectedCategoryId));
					
					if(immediateChild==null || immediateChild.size()==0){
						immediateChild = categoryService.getAllCategory(con, false, false, false, true);
					}
					
					List<CategoryVo> subCategoryVo = categoryService.getCategoryHierarchy(con);
					
					String categories = null;
					if(subCategoryVo!=null && subCategoryVo.size()>0){
						for (CategoryVo categoryVo : subCategoryVo) {
							if(categoryVo.getCategoryId().equals(Long.parseLong(selectedCategoryId))){
								categories = getLeastCategories(categoryVo, categories);
							}
						}
					}
					goodsVos = goodsService.getGoodsForCategory(con, categories, false, false);
					System.out.println(new Gson().toJson(goodsVos));
				}
			}
			
			request.setAttribute("goods", goodsVos);
			request.setAttribute("categoryWithSubcategory", immediateChild);
			request.setAttribute("selectedCategoryId", selectedCategoryId);
			RequestDispatcher rs = request.getRequestDispatcher("jsp/goods.jsp");
			rs.forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String getLeastCategories(CategoryVo categoryVo, String categories){
		List<CategoryVo> subCategory = categoryVo.getCategoryVos();
		
		if(subCategory!=null && subCategory.size()>0){
			for (CategoryVo categoryVo2 : subCategory) {
				categories = getLeastCategories(categoryVo2, categories);
			}
		}else{
			if(categories==null){
				categories = categoryVo.getCategoryId().toString();
			}else{
				categories = categories + "," + categoryVo.getCategoryId();
			}
		}
		
		return categories;
	}
}
