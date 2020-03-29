package com.sb.integration.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sb.integration.util.GlobalSearchVo;
import com.sb.integration.util.UserUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class GlobalSearchController
 */
@WebServlet("/globalsearch")
public class GlobalSearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GlobalSearchController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("application/json");
         try {
        	 String searchValue = request.getParameter("term");
        	 
        	 ServletContext ctx = request.getServletContext();
        	 String goodsJson = (String)ctx.getAttribute("goodsJson");
        	 List<GlobalSearchVo> resultList = UserUtil.getSearchResults(goodsJson, searchValue);
        	 
        	 response.setCharacterEncoding("UTF-8");
        	 
        	 String searchList = new Gson().toJson(resultList);
             response.getWriter().write(searchList);
             
         } catch (Exception e) {
                 System.err.println(e.getMessage());
         }
	}

}
