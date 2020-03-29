package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.dao.impl.GoodsRepositoryImpl;
import com.sb.integration.service.CategoryService;
import com.sb.integration.service.impl.CategoryServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.CategoryVo;
import com.sb.integration.vo.GoodsVo;

/**
 * Servlet implementation class GoodsDetailsController
 */
@WebServlet("/goods_details")
public class GoodsDetailsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsDetailsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String goodsId = request.getParameter("goodsId");
		
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			GoodsRepository goodsRepository = new GoodsRepositoryImpl();
			GoodsVo goodsVo = goodsRepository.getGoodsForId(con, Long.parseLong(goodsId));
			
			List<GoodsVo> goodsForCategory = goodsRepository.getGoodsForCategory(con, goodsVo.getCategoryId().toString(), 
					false, false);
			
			Integer randomNum = ThreadLocalRandom.current().nextInt(0, goodsForCategory.size());
			
			Integer firstIndex=randomNum, lastIndex=randomNum+4;
			
			if(randomNum + 4 > goodsForCategory.size()){
				firstIndex = goodsForCategory.size() - 4;
				lastIndex = goodsForCategory.size();
			}
			List<GoodsVo> subListOfGoods = goodsForCategory.subList(firstIndex, lastIndex);
			
			CategoryService categoryService = new CategoryServiceImpl();
			List<CategoryVo> allCategory = categoryService.getAllCategory(con, false, false, false, false);
			
			UserUtil.getLinkForCategory(allCategory, goodsVo.getCategoryId());
			
			
			RequestDispatcher rd = request.getRequestDispatcher("jsp/goods_details.jsp");
			request.setAttribute("goodsVo", goodsVo);
			request.setAttribute("goodsForCategory", subListOfGoods);
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
