package com.sb.integration.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.SellerVo;

/**
 * Servlet implementation class NewOrdersCountForSeller
 */
@WebServlet("/getneworderscountforseller")
public class NewOrdersCountForSeller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewOrdersCountForSeller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
		
		
		HttpSession session = request.getSession();
		Boolean isFirstCall = (Boolean) session.getAttribute("isFirstCall");
		PrintWriter writer = null;
		try {
			SellerVo sellerDetails = (SellerVo)session.getAttribute("sellerDetails");
			OrderService orderService = new OrderServiceImpl();
			int newOrderCount = orderService.getNewOrdersCountForSeller(con, Constants.ORDER_PROCESSED_BY_COADMIN, sellerDetails.getSellerId());

			response.setContentType("text/event-stream");
			response.setCharacterEncoding("UTF-8");

			writer = response.getWriter();

			writer.write("data: " + newOrderCount + "\n\n");

			if(isFirstCall!=null){
				Thread.sleep(60000);
			}
			else{
				session.setAttribute("isFirstCall", false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

}
