package com.sb.integration.rest.api;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sb.integration.config.MailConfig;
import com.sb.integration.config.RandomStringGeneration;
import com.sb.integration.dao.CartRepository;
import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.dao.UserRepository;
import com.sb.integration.dao.impl.CartRepositoryImpl;
import com.sb.integration.dao.impl.GoodsRepositoryImpl;
import com.sb.integration.dao.impl.UserRepositoryImpl;
import com.sb.integration.resources.AutomateSellerApproved;
import com.sb.integration.service.CartService;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.CreateOrderEmailTemplate;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.SMSService;
import com.sb.integration.util.TimeStamp;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.AddressVo;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;
import com.sb.integration.vo.UserVo;
import com.google.gson.Gson;

@Path("/cart")
public class CartResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/addtocart")
	public Response addToCart(@FormParam("goods_id") String goodsId, 
			@FormParam("quantity_id") String quantityId,
			@FormParam("seller_id") String sellerId, @FormParam("rupee") String rupee, 
			@FormParam("userId") Long userId, @Context HttpServletRequest request){
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.addToCartIndependent(con, Long.parseLong(goodsId), Long.parseLong(quantityId), 
					Long.parseLong(sellerId), new BigDecimal(rupee), userId);
			
			return Response.ok().entity(new Gson().toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/view_cart")
	public Response viewCart(@FormParam("user_id") Long userId, 
			@Context HttpServletRequest request){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartDetails cartDetails = null;
				
			CartService cartService = new CartServiceImpl();
			cartDetails = cartService.getActiveCartForUser(con, userId);
			
			if(cartDetails!=null){
				List<CartItem> cartItems = cartDetails.getCartItems();
				
				if(cartItems != null){
					for (CartItem cartItem : cartItems) {
						if(cartItem.getGoodsVo().getInRupee()){
							List<QuantityVo> existingQty = cartItem.getQuantityVos();
							List<QuantityVo> rupeeQty = UserUtil.getRuppeQuantity();
							
							existingQty.addAll(rupeeQty);
							cartItem.setQuantityVos(existingQty);
						}
					}
				}
				
				
				return Response.ok().entity(new Gson().toJson(cartDetails))
						.header("Access-Control-Allow-Origin", "*").
						header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			}else{
				return Response.ok().entity("CartDetails object is null").build();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cart_count")
	public Response getCartCount(@FormParam("user_id") Long userId, 
			@Context HttpServletRequest request){
		try{
			System.out.println("*****************get_user_address********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			Integer itemCount = cartService.getActiveCartItemCountForUser(con, userId);
			
			
			if(itemCount!=null){
				return Response.ok().entity(new Gson().toJson(itemCount.toString()))
						.header("Access-Control-Allow-Origin", "*").
						header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			}else{
				return Response.ok().entity("not able to fetch itemCount").build();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/change_quantity")
	public Response changeQuantity(@FormParam("user_id") Long userId, 
			@FormParam("cart_item_id") Long cartItemId, @FormParam("is_rupee") Boolean isRupee, 
			@FormParam("quantity_id") Long quantityId, @Context HttpServletRequest request){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.updateCartItemForApp(con, quantityId, userId, cartItemId, isRupee);
			
			return Response.ok().entity(new Gson().toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete_cart_item")
	public Response deleteCartItem(@FormParam("cart_item_id") Long cartItemId, @Context HttpServletRequest request){
		try{
			System.out.println("*****************delete_cart_item********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.deleteCartItemForApp(con, cartItemId);
			
			return Response.ok().entity(new Gson().toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_user_address")
	public Response getUserAddresses(@FormParam("user_id") Long userId, @Context HttpServletRequest request){
		try{
			System.out.println("*****************get_user_address********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			UserService userService = new UserServiceImpl();
			List<AddressVo> addressVos = userService.getAddressesForUser(con, userId);
			
			UserVo userDetails = new UserVo();
			userDetails.setAddressList(addressVos);
			
			return Response.ok().entity(new Gson().toJson(userDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add_user_address")
	public Response addUserAddress(@FormParam("user_id") Long userId,
			@FormParam("address") String address, @Context HttpServletRequest request){
		try{
			System.out.println("*****************get_user_address********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			Gson gson = new Gson();
			AddressVo addressVo = gson.fromJson(address, AddressVo.class);
			
			UserService userService = new UserServiceImpl();
			userService.addAddressForUser(con, addressVo, userId, false);
			
			return Response.ok().entity(new Gson().toJson("Address added successfully!!"))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_user_address_for_id")
	public Response getUserAddressForId(@FormParam("address_id") Long addressId, 
			@Context HttpServletRequest request){
		try{
			System.out.println("*****************get_user_address********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			OrderService orderService = new OrderServiceImpl();
			AddressVo addressVo = orderService.getAddressForId(con, addressId);
			
			return Response.ok().entity(new Gson().toJson(addressVo))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update_address")
	public Response updateAddress(@FormParam("address") String address, 
			@Context HttpServletRequest request){
		try{
			System.out.println("*****************update_user_address********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			Gson gson = new Gson();
			AddressVo addressVo = gson.fromJson(address, AddressVo.class);
			
			UserService userService = new UserServiceImpl();
			userService.updateAddress(con, addressVo);
			
			return Response.ok().entity("Updated successfully")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_slots")
	public Response getDeliveryOptionList(@Context HttpServletRequest request){
		try{
			System.out.println("*****************update_user_address********************");
			String time = TimeStamp.getAsiaOnlyTime();
			String[] timeArray = time.split(" ");
			
			int hour = Integer.parseInt(timeArray[0]);
			String day = TimeStamp.getAsiaDay();
			
			String deliveryIds = "";
			
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
			
			System.out.println("deliveryIds******************"+ deliveryIds);
			
			//List<DeliveryOption> deliveryOptions = cartService.getDeliveryOption(con, deliveryIds);
			CartDetails cartDetails = new CartDetails();
			//cartDetails.setDeliveryOptions(deliveryOptions);
			
			cartDetails.setDeliverySlotMap(deliverySlotMap);
			
			return Response.ok().entity(new Gson().toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/place_order")
	public Response placeOrder(@Context HttpServletRequest request,
			@FormParam("cart_details") String cartDetailsStr){
		try{
			System.out.println("*****************Place Order********************");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			Gson gson = new Gson();
			CartDetails cartDetails = gson.fromJson(cartDetailsStr, CartDetails.class);
			
			con.setAutoCommit(false);
			String generatedOrderNumber = RandomStringGeneration.generateOrderNumber();
			cartDetails.setOrderNumber(generatedOrderNumber);
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			String minimumOrder = prop.getProperty("order.minimum.order");
			String minimumOrderCharge = prop.getProperty("minimum.order.charge");
			
			Properties propApplication = propertiesFile.loadProperties("/application.properties");
			BigDecimal offerInRupee = new BigDecimal(propApplication.getProperty("wallet.per.order.offer.in.rupee")) ;
			
			if(cartDetails.getSubtotalAmount().compareTo(new BigDecimal(minimumOrder)) == -1){
				
				cartDetails.setShippingCharge(new BigDecimal(minimumOrderCharge));
				cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(new BigDecimal(minimumOrderCharge)));
				
				OrderService orderService = new OrderServiceImpl();
				orderService.updateShippingCharge(con, cartDetails);
			}
			
			UserRepository userRepository = new UserRepositoryImpl();
			UserVo userVo = userRepository.getUserDetailById(con, cartDetails.getCartOwner());
			
			if(userVo!=null && userVo.getWalletAmount()!=null && 
					userVo.getWalletAmount().compareTo(BigDecimal.ZERO)==1 && 
					cartDetails.getTotalAmount().compareTo(offerInRupee)==1){
				
				if(userVo.getWalletAmount().compareTo(offerInRupee) < 0){
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(userVo.getWalletAmount()));
					userVo.setWalletAmount(BigDecimal.ZERO);
					cartDetails.setWalletAmount(userVo.getWalletAmount());
				}else{
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(offerInRupee));
					userVo.setWalletAmount(userVo.getWalletAmount().subtract(offerInRupee));
					cartDetails.setWalletAmount(offerInRupee);
				}
			}else{
				cartDetails.setWalletAmount(BigDecimal.ZERO);
			}
			
			CartRepository cartRepository = new CartRepositoryImpl();
			cartDetails.setOrderSource(Constants.ORDER_SOURCE_TYPE_ANDROID);
			cartRepository.placeOrder(con, cartDetails);
			cartRepository.createEmptyCart(con, cartDetails.getCartOwner());
			
			//update user wallet after order
			if(userVo.getWalletAmount().compareTo(BigDecimal.ZERO)>=0){
				UserService userService = new UserServiceImpl();
				userService.updateUserWallet(con, userVo.getUserId(), userVo.getWalletAmount());
			}
		
			cartDetails.setRemainingWalletAmount(userVo.getWalletAmount());
			String orderInfo = "";
			for (CartItem cartItem : cartDetails.getCartItems()) {
				orderInfo = orderInfo + cartItem.getGoodsVo().getGoodsName() + "(" + cartItem.getQuantity() +
						" " +cartItem.getUom() + "- Rs." + cartItem.getPrice() + "),";
			}
			
			String message = "Your order has been placed successfully. Your order number is "+generatedOrderNumber + ", "
					+ " order total is " + cartDetails.getTotalAmount() + 
					" order will be delivered on " + cartDetails.getDeliveryOption();
			
			//System.out.println(message);
			
			// Send a mail to registered Email Id.
			if(!prop.getProperty("order.confirm.sms.off").equals("true")){
				//logger.debug("order.confirm.sms.off is true....now message will send to customer");
				SMSService smsService = new SMSService();
				String[] numbers = {cartDetails.getShippingAddress().getContactNumber()};
				smsService.sendMessage(numbers, message);
				
				String[] numbersOfAdmin = {prop.getProperty("admin.phone.number")};
				AddressVo shippingAddress = cartDetails.getShippingAddress();
				String messageForAdmin = message + ", FullAddress- (" + shippingAddress.getContactName() + "," 
								+ shippingAddress.getContactNumber() + "," + shippingAddress.getAddressLine1() 
								+ "," + shippingAddress.getAddressLine2() + "," + shippingAddress.getCity() + ","
								+ shippingAddress.getLandMark();
				
				smsService.sendMessage(numbersOfAdmin, messageForAdmin);
				//logger.debug("Order confirmed message is sent successfully.");
			}
			
			message = CreateOrderEmailTemplate.getEmailContent(cartDetails);
			System.out.println("*************" + message);
			
			cartDetails.setOrderDateForEmail(TimeStamp.getAsiaTimeStamp());
			
			MailConfig mail = new MailConfig();
	        mail.sendTextMail("Your Order -"+generatedOrderNumber + " is confirmed", message, cartDetails.getShippingAddress().getEmail(), false);
	        mail.sendTextMail("New Order -"+generatedOrderNumber, message, prop.getProperty("admin.email.id"), false);
	        
	        Map<Long, Long> sellerCartItemMap = new HashMap<>();
	        
	        for (CartItem cartItem : cartDetails.getCartItems()) {
	        	sellerCartItemMap.put(cartItem.getCartItemId(), 1l);
			}
	        
	        AutomateSellerApproved.getAutomateSellerApproval(con, cartDetails.getCartId(), 1, sellerCartItemMap);
	        con.commit();
			
			return Response.ok().entity(new Gson().toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update_address_for_order")
	public Response updateAddressForOrder(@Context HttpServletRequest request,
			@FormParam("address_id") Long addressId, @FormParam("cart_id") Long cartId){
		
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			cartService.updateShippingAddress(con, addressId, cartId);
			
			return Response.ok().entity("Address updated successfully")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cancel_order")
	public Response cancelOrder(@Context HttpServletRequest request,
			@FormParam("cart_id") Long cartId){
		
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			OrderService orderService = new OrderServiceImpl();
			orderService.updateOrderStatus(con, 8l, cartId, "Canceled by customer");
			
			return Response.ok().entity("Order canceled successfully")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/send_otp_while_signup")
	public Response sendOtpWhileSignup(@Context HttpServletRequest request,
			@FormParam("mobile_number") String mobile_number){
		
		try{
			SMSService smsService = new SMSService();
			smsService.sendMessage(new String[]{mobile_number}, "");
			
			return Response.ok().entity("Message has sent.")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/total_amount_setup")
	public Response totalAmountSetup(@Context HttpServletRequest request,
			@FormParam("cartDetails") String cartDetailsStr){
		
		try{
			Gson gson = new Gson();
			CartDetails cartDetails = gson.fromJson(cartDetailsStr, CartDetails.class);
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			UserRepository userRepository = new UserRepositoryImpl();
			UserVo userDetails = userRepository.getUserDetailById(con, cartDetails.getCartOwner());
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties propApplication = propertiesFile.loadProperties("/application.properties");
			BigDecimal offerInRupee = new BigDecimal(propApplication.getProperty("wallet.per.order.offer.in.rupee")) ;
			
			if(userDetails.getWalletAmount()!=null && 
					userDetails.getWalletAmount().compareTo(BigDecimal.ZERO)==1 && 
					cartDetails.getTotalAmount().compareTo(offerInRupee)==1){
				cartDetails.setIsUserWalletMoneyAvailable(true);
				cartDetails.setWalletOfferAmount(offerInRupee);
				cartDetails.setRemainingWalletAmount(userDetails.getWalletAmount().subtract(offerInRupee));
			}else{
				cartDetails.setIsUserWalletMoneyAvailable(false);
				cartDetails.setWalletOfferAmount(BigDecimal.ZERO);
			}
			
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			String minimumOrder = prop.getProperty("order.minimum.order");
			String minimumOrderCharge = prop.getProperty("minimum.order.charge");
			
			cartDetails.setOrderMinimumAmount(new BigDecimal(minimumOrder));
			cartDetails.setMinimumOrderShippingCharge(new BigDecimal(minimumOrderCharge));
			cartDetails.setOfferPrice(offerInRupee);
			cartDetails.setRemainingWalletAmount(userDetails.getWalletAmount());
			
			return Response.ok().entity(gson.toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addtocartforguest")
	public Response addToCartForGuest(@Context HttpServletRequest request,
			@FormParam("goods_id") Long goodsId, @FormParam("in_rupee") String inRupee,
			@FormParam("quantity_id") Long quantityId){
		
		try{
			GoodsRepository goodsRepository = new GoodsRepositoryImpl();
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			QuantityVo quantityVo = goodsRepository.getQuantityForId(con, quantityId);
			GoodsVo goodsVo = goodsRepository.getGoodsForId(con, goodsId);
			
			CartServiceImpl cartServiceImpl = new CartServiceImpl();
			
			BigDecimal priceForQty = cartServiceImpl.calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getPrice(),
					goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());

			// Actual MSRP for selected item
			BigDecimal msrpForQty = cartServiceImpl.calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getMsrp(),
					goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());
			
			BigDecimal rupee = new BigDecimal(inRupee);
			CartItem cartItem = new CartItem();
			cartItem.setGoodsId(goodsId);
			cartItem.setGoodsVo(goodsVo);
			if(rupee.equals(BigDecimal.ZERO)){
				cartItem.setMsrp(msrpForQty);
				cartItem.setPrice(priceForQty);
				cartItem.setQuantity(quantityVo.getWeight());
				cartItem.setUom(quantityVo.getUom());
			}else{
				cartItem.setMsrp(rupee);
				cartItem.setPrice(rupee);
				cartItem.setQuantity(rupee.toString());
				cartItem.setUom("Rs");
			}
			List<QuantityVo> quantityVos = goodsRepository.getQuantityListForGoods(con, cartItem.getGoodsId());
			cartItem.setQuantityVos(quantityVos);
			
			Gson gson = new Gson();
			return Response.ok().entity(gson.toJson(cartItem))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/replaceguestcarttousercart")
	public Response replaceGuestCartToUserCart(@Context HttpServletRequest request,
			@FormParam("cart_details") String cartDetailsStr, @FormParam("user_details") String userDetailsStr){
		
		try{
			Gson gson = new Gson();
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			UserVo userDetails = gson.fromJson(userDetailsStr, UserVo.class);
			CartDetails cartDetails = gson.fromJson(cartDetailsStr, CartDetails.class);
			
			OrderService orderService = new OrderServiceImpl();
			cartDetails = orderService.replaceGuestCartToRegCart(con, userDetails, cartDetails);
			
			
			return Response.ok().entity(gson.toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception occured").build();
		}
		
	}
	
}
