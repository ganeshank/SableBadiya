package com.sb.integration.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sb.integration.util.CreateOrderEmailTemplate;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.CartDetails;

/**
 * Servlet implementation class AddDeliveryOption
 */
public class AddDeliveryOption extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDeliveryOption() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String deliveryOption = request.getParameter("deliveryOption");
		String deliveryDate = request.getParameter("deliveryDate");
		String comment = request.getParameter("comment");
		
		HttpSession session = request.getSession();
		CartDetails cartDetails = (CartDetails) session.getAttribute("placeOrderCart");
		
		cartDetails.setDeliveryOption(deliveryOption);
		cartDetails.setDeliveryDate(deliveryDate);
		cartDetails.setComments(comment);
		
		session.setAttribute("placeOrderCart", cartDetails);
		
		response.setContentType("text/plain");
	    response.getWriter().write("success");
	    response.setStatus(200);
	}

}
