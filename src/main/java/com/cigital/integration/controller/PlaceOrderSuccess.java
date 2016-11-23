package com.cigital.integration.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PlaceOrderSuccess
 */
public class PlaceOrderSuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlaceOrderSuccess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String orderNumber = (String)session.getAttribute("currentOrderNumber");
		String inDeliver = (String)session.getAttribute("inDeliver");
		
		session.removeAttribute("currentOrderNumber");
		session.removeAttribute("inDeliver");
		
		System.out.println("OrderNumber::::::::"+orderNumber);
		
		RequestDispatcher rd = request.getRequestDispatcher("jsp/place_order_success.jsp");
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("inDeliver", inDeliver);
		rd.forward(request, response);
	}

}
