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

import com.cigital.integration.service.CategoryService;
import com.cigital.integration.service.MediaService;
import com.cigital.integration.service.impl.CategoryServiceImpl;
import com.cigital.integration.service.impl.MediaServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.vo.CategoryVo;
import com.cigital.integration.vo.MediaVo;

/**
 * Servlet implementation class HomePageController
 */
public class HomePageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomePageController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource)ctx.getAttribute("dataSource");
		
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
		
		// getting Categories....
		CategoryService categoryRepository = new CategoryServiceImpl();
		List<CategoryVo> categories = categoryRepository.getAllCategory(con, false);
		System.out.println(categories);
		request.setAttribute("categories", categories);
		
		// getting Homepage Slider Images.....
		MediaService mediaService = new MediaServiceImpl();
		List<MediaVo> mediaVos = mediaService.getHomePageSliderImage(con);
		System.out.println(mediaVos);
		request.setAttribute("sliderMedia", mediaVos);
		
		
		RequestDispatcher rd = request.getRequestDispatcher("jsp/homepage.jsp");
		rd.forward(request, response);
	}

}
