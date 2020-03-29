package com.sb.integration.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class TestController
 */
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String jsonString = "[{\"label\" : \"Critical\",\"value\" : \"3\"},"
				+ "{\"label\" : \"Minimum\",\"value\" : \"2\"},"
				+ "{\"label\" : \"Low\",\"value\" : \"5\"},"
				+ "{\"label\" : \"Medium\",\"value\" : \"3\"},"
				+ "{\"label\" : \"High\",\"value\" : \"1\"}]";
		
		
		
		System.out.println("hello to all..........."+jsonString);
		response.setContentType("application/json");
	    response.getWriter().write(jsonString);
	    response.setStatus(200);
	}

}
