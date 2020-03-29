package com.sb.integration.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.FieldExecutiveVo;
import com.sb.integration.vo.SellerVo;

/**
 * Servlet implementation class SellerHomeController
 */
@WebServlet({"/sellerhome","/sellerhome/*"})
public class SellerHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SellerHomeController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private static final String HOME_DEFAULT_VIEW = "/sellerhome";
    private static final String NEW_ORDERS_FOR_SELLER = "/new_orders_for_seller";
    private static final String ORDER_STATUS_CHECK_PAGE = "/order_status_check_page";
    
    private static final String NEW_ORDER_VIEW = "/view_new_order_seller";
    private static final String RECENT_APPROVED_ORDERS = "/recent_approved_orders";
    
    private static final String DISPATCH_SELLER_DEFAULT_VIEW = "/jsp/seller_home.jsp";
    private static final String DISPATCH_NEW_ORDERS_FOR_SELLER = "/jsp/newordersforseller.jsp";
    private static final String DISPATCH_ORDER_STATUS_CHECK_PAGE = "/jsp/order_status_seller.jsp";
    private static final String DISPATCH_NEW_ORDER_VIEW = "/jsp/view_order_by_seller.jsp";
    private static final String DISPATCH_RECENT_APPROVED_ORDERS = "/jsp/recent_approved_order_for_seller.jsp";
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String pathInfo = request.getPathInfo();
			System.out.println(pathInfo);
			
			if (pathInfo == null || pathInfo.equalsIgnoreCase(HOME_DEFAULT_VIEW)) {

				request.getRequestDispatcher(DISPATCH_SELLER_DEFAULT_VIEW).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(NEW_ORDERS_FOR_SELLER)){
				
				request.getRequestDispatcher(DISPATCH_NEW_ORDERS_FOR_SELLER).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(NEW_ORDER_VIEW)){
				
				dispatchViewOrder(request, response);
			}else if(pathInfo.equalsIgnoreCase(ORDER_STATUS_CHECK_PAGE)){
				
				request.getRequestDispatcher(DISPATCH_ORDER_STATUS_CHECK_PAGE).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(RECENT_APPROVED_ORDERS)){
				
				request.getRequestDispatcher(DISPATCH_RECENT_APPROVED_ORDERS).forward(request, response);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void dispatchViewOrder(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String cartId = request.getParameter("cartId");
			
			HttpSession session = request.getSession();
			SellerVo sellerDetails = (SellerVo) session.getAttribute("sellerDetails");
			
			System.out.println(sellerDetails);
			System.out.println(cartId);
			// Fetch order details
			OrderService orderService = new OrderServiceImpl();
			CartDetails cartDetails = orderService.getOrderForSeller(con, Long.parseLong(cartId), sellerDetails.getSellerId());
			
			// Fetch DeliveryBoy details.
			SellerService sellerService = new SellerServiceImpl();
			FieldExecutiveVo feVo = sellerService.getFieldExecutiveForOrder(con, Long.parseLong(cartId));
			
			request.setAttribute("cartDetails", cartDetails);
			request.setAttribute("feVo", feVo);
			
			// For making the count of new orders be sync.
			session.setAttribute("isFirstCall", null);
			session.setAttribute("pageName", "View Order");
			
			RequestDispatcher rd = request.getRequestDispatcher(DISPATCH_NEW_ORDER_VIEW);
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
