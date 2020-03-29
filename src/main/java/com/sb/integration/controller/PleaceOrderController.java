package com.sb.integration.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sb.integration.service.CartService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.CreateOrderEmailTemplate;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.UserVo;

/**
 * Servlet implementation class PleaceOrderController
 */
public class PleaceOrderController extends HttpServlet {

	final static Logger logger = Logger.getLogger(PleaceOrderController.class);
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
			
			if(UserUtil.isGuestUser(session)){
				//cartDetails = (CartDetails)session.getAttribute("guestCart");
				
				session.setAttribute("place_order_redirect", "true");
				RequestDispatcher rd = request.getRequestDispatcher("jsp/login.jsp");
				rd.forward(request, response);
				
			}else{
				UserVo userDetails = (UserVo) session.getAttribute("userDetails");
				UserService userService = new UserServiceImpl();
				List<AddressVo> addressVos = userService.getAddressesForUser(con, userDetails.getUserId());

				CartService cartService = new CartServiceImpl();
				cartDetails = cartService.getActiveCartForUser(con, userDetails.getUserId());
				
				cartDetails.setAddresses(addressVos);
				
				String day = TimeStamp.getAsiaDay();
				
				String time = TimeStamp.getAsiaOnlyTime();
				String[] timeArray = time.split(" ");
				
				int hour = Integer.parseInt(timeArray[0]);
				
				LinkedHashMap<String, String> deliverySlotMap = new LinkedHashMap<>();
				if(day.equalsIgnoreCase("MON")){
					deliverySlotMap.put(TimeStamp.getAddupDate(1), "Tuesday 4 PM to 7 PM");
				}else if((day.equalsIgnoreCase("SUN") && hour>=12)){
					deliverySlotMap.put(TimeStamp.getAddupDate(2), "Tuesday 4 PM to 7 PM");
				}else{
					if(timeArray[1].equalsIgnoreCase("PM") && (hour>=12 && hour<=23)){
						deliverySlotMap.put(TimeStamp.getTomorrowsDate(), "Tomorrow 4 PM to 7 PM");
					}else if(timeArray[1].equalsIgnoreCase("AM") && (hour>=0 && hour<=11)){
						deliverySlotMap.put(TimeStamp.getTodaysDate(), "Today 4 PM to 7 PM");
						deliverySlotMap.put(TimeStamp.getTomorrowsDate(), "Tomorrow 4 PM to 7 PM");
					}
				}
				
				cartDetails.setDeliverySlotMap(deliverySlotMap);
				
				cartDetails.setIsStandardDeliveryEnable(isStandardDeliveryEnabled());
				if(!cartDetails.getIsStandardDeliveryEnable()){
					cartDetails.setIsDeliveryOptionTomorrow(isDeliveryOptionTomorrow());
				}else{
					cartDetails.setIsDeliveryOptionTomorrow(true);
				}
				
				LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
				Properties propApplication = propertiesFile.loadProperties("/application.properties");
				BigDecimal offerInRupee = new BigDecimal(propApplication.getProperty("wallet.per.order.offer.in.rupee")) ;
				
				if(userDetails.getWalletAmount()!=null && 
						userDetails.getWalletAmount().compareTo(BigDecimal.ZERO)==1 && 
						cartDetails.getTotalAmount().compareTo(offerInRupee)==1){
					cartDetails.setIsUserWalletMoneyAvailable(true);
				}else{
					cartDetails.setIsUserWalletMoneyAvailable(false);
				}
				
				cartDetails.setOfferPrice(offerInRupee);
				cartDetails.setRemainingWalletAmount(userDetails.getWalletAmount());

				RequestDispatcher rd = request.getRequestDispatcher("jsp/place-order.jsp");
				request.setAttribute("cartForOrder", cartDetails);
				rd.forward(request, response);
			}
			
			
			

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
