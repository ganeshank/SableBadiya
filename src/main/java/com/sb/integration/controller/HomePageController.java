package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.CartService;
import com.sb.integration.service.CategoryService;
import com.sb.integration.service.MediaService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.CategoryServiceImpl;
import com.sb.integration.service.impl.MediaServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.CategoryVo;
import com.sb.integration.vo.MediaVo;
import com.sb.integration.vo.UserVo;

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
		List<CategoryVo> categories = categoryRepository.getAllCategory(con, false, false, false, true);
		System.out.println(categories);
		request.setAttribute("categories", categories);
		
		// getting Homepage Slider Images.....
		MediaService mediaService = new MediaServiceImpl();
		List<MediaVo> mediaVos = mediaService.getHomePageSliderImage(con, false);
		System.out.println(mediaVos);
		request.setAttribute("sliderMedia", mediaVos);
		
		// Refresh User Cart
		
		CartService cartService = new CartServiceImpl();
		HttpSession session = request.getSession();
		UserVo userDetails = (UserVo)session.getAttribute("userDetails");
		System.out.println("UserDetails:::::::::::"+userDetails);
		
		if(userDetails!=null && !cartService.isCartEmpty(con, userDetails.getUserId())){
			cartService.refreshCart(con, userDetails.getUserId());
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("jsp/homepage.jsp");
		rd.forward(request, response);
	}

}
