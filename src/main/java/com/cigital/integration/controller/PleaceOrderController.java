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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.cigital.integration.service.CartService;
import com.cigital.integration.service.UserService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.service.impl.UserServiceImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.TimeStamp;
import com.cigital.integration.util.UserUtil;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class PleaceOrderController
 */
public class PleaceOrderController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PleaceOrderController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);

			HttpSession session = request.getSession();
			CartDetails cartDetails = null;
			
			/*LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			String minimumOrder = prop.getProperty("order.minimum.order");
			String minimumOrderCharge = prop.getProperty("minimum.order.charge");*/
			
			if(UserUtil.isGuestUser(session)){
				cartDetails = (CartDetails)session.getAttribute("guestCart");
				
				/*if(cartDetails.getSubtotalAmount().compareTo(new BigDecimal(minimumOrder)) == -1){
					cartDetails.setShippingCharge(new BigDecimal(minimumOrderCharge));
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(new BigDecimal(minimumOrderCharge)));
				}else{
					cartDetails.setShippingCharge(new BigDecimal(0));
					cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
				}
				session.setAttribute("guestCart", cartDetails);*/
				
			}else{
				UserVo userDetails = (UserVo) session.getAttribute("userDetails");
				UserService userService = new UserServiceImpl();
				List<AddressVo> addressVos = userService.getAddressesForUser(con, userDetails.getUserId());

				CartService cartService = new CartServiceImpl();
				cartDetails = cartService.getActiveCartForUser(con, userDetails.getUserId());
				
				/*if(cartDetails.getSubtotalAmount().compareTo(new BigDecimal(minimumOrder)) == -1){
					cartDetails.setShippingCharge(new BigDecimal(minimumOrderCharge));
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(new BigDecimal(minimumOrderCharge)));
				}else{
					cartDetails.setShippingCharge(new BigDecimal(0));
					cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
				}
				OrderService orderService = new OrderServiceImpl();
				orderService.updateShippingCharge(con, cartDetails);*/
				
				cartDetails.setAddresses(addressVos);
			}
			
			cartDetails.setIsStandardDeliveryEnable(isStandardDeliveryEnabled());
			if(!cartDetails.getIsStandardDeliveryEnable()){
				cartDetails.setIsDeliveryOptionTomorrow(isDeliveryOptionTomorrow());
			}else{
				cartDetails.setIsDeliveryOptionTomorrow(true);
			}
			/*List<DeliveryTypeVo> deliveryTypeVos = cartService.getAllDeliveryTpyes(con);
			cartDetails.setDeliveryType(deliveryTypeVos);*/

			RequestDispatcher rd = request.getRequestDispatcher("jsp/place-order.jsp");
			request.setAttribute("cartForOrder", cartDetails);
			rd.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Boolean isStandardDeliveryEnabled(){
		String time = TimeStamp.getAsiaOnlyTime();
		String[] timeArray = time.split(" ");
		
		int hour = Integer.parseInt(timeArray[0]);
		
		if(timeArray[1].equalsIgnoreCase("PM") && (hour>=20 && hour<24)){
			return false;
		}else if(timeArray[1].equalsIgnoreCase("AM") && (hour>=0 && hour<8)){
			return false;
		}else{
			return true;
		}
	}
	
	private static Boolean isDeliveryOptionTomorrow(){
		String time = TimeStamp.getAsiaOnlyTime();
		String[] timeArray = time.split(" ");
		
		int hour = Integer.parseInt(timeArray[0]);
		
		if(timeArray[1].equalsIgnoreCase("PM") && (hour>=20 && hour<24)){
			return true;
		}else{
			return false;
		}
		
	}
}
