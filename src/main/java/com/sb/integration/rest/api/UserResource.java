package com.sb.integration.rest.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.sb.integration.config.MailConfig;
import com.sb.integration.config.RandomStringGeneration;
import com.sb.integration.service.CartService;
import com.sb.integration.service.CategoryService;
import com.sb.integration.service.GoodsService;
import com.sb.integration.service.MediaService;
import com.sb.integration.service.OrderService;
import com.sb.integration.service.SecurityService;
import com.sb.integration.service.UserService;
import com.sb.integration.service.ValidatorService;
import com.sb.integration.service.impl.CartServiceImpl;
import com.sb.integration.service.impl.CategoryServiceImpl;
import com.sb.integration.service.impl.GoodsServicesImpl;
import com.sb.integration.service.impl.MediaServiceImpl;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SecurityServiceImpl;
import com.sb.integration.service.impl.UserServiceImpl;
import com.sb.integration.service.impl.ValidatorServiceImpl;
import com.sb.integration.util.Constants;
import com.sb.integration.util.DataSourceUtil;
import com.sb.integration.util.LoadPropertiesFile;
import com.sb.integration.util.SMSService;
import com.sb.integration.util.SendOtp;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CategoryVo;
import com.sb.integration.vo.CouponVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.MediaVo;
import com.sb.integration.vo.RoleVo;
import com.sb.integration.vo.UserVo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;

@Path("/user")
public class UserResource {
	final static Logger logger = Logger.getLogger(UserResource.class);
	
	ValidatorService validatorService = new ValidatorServiceImpl();
	private static final String CLIENT_ID = "1082344612925-6bfsqivi3u0vmj1i2el7qbc6chnofkn8.apps.googleusercontent.com";
	private static final String ACCESS_TOKEN_ID="124982921380075|fWJ3AX_gVU9-Rkl5aQchCG3SB_g";
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response isLoginSuccess(@FormParam("username") String userName, 
			@FormParam("password") String password, @Context HttpServletRequest request){
		System.out.println("UserName::::" + userName + ",   password::::::" + password);
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			System.out.println("Session---"+ request.getSession().getId());
			
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);

			UserService userService = new UserServiceImpl();
			UserVo userDetails = userService.checkLoginCredential(userName, encryptedPassword, con);
			
			String userDetailsJson = new Gson().toJson(userDetails);
			if(userDetails!=null){
				//String val = new Gson().toJson(new ArrayList<>().add("User is logged in successfully!"));
				return Response.ok().entity(userDetailsJson)
						.header("Access-Control-Allow-Origin", "*").
						header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			}else{
				return Response.status(401).entity("User is not authorized!").build();
			}
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@GET
	@Path("/apply_coupon")
	public Response applyCoupon(@QueryParam("coupon_number")String couponNumber, @Context HttpServletRequest request){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			UserService userService = new UserServiceImpl();
			CouponVo couponVo = userService.isCouponValid(con, couponNumber);
			
			if(couponVo!=null){
				HttpSession session = request.getSession();
				CartDetails cartDetails = (CartDetails) session.getAttribute("placeOrderCart");
				if(cartDetails==null){
					return Response.status(500).entity("Something went wrong, Please contact to customer care").build();
				}else{
					BigDecimal totalAmount = cartDetails.getTotalAmount();
					BigDecimal discountPrice = UserUtil.calculateOfferPrice(totalAmount, couponVo.getOfferPercentage());
					
					cartDetails.setOfferPrice(totalAmount.subtract(discountPrice));
					cartDetails.setCouponId(couponVo.getCouponId());
					session.setAttribute("placeOrderCart", cartDetails);
					return Response.status(200).entity("Coupon is valid!").build();
				}
			}else{
				return Response.status(401).entity("Coupon is invalid!").build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/signup")
	public Response registerNewUser(@FormParam("fullName") String fullName, 
			@FormParam("email") String email, @FormParam("contact") String contactNumber,
			@FormParam("password") String password, @Context HttpServletRequest request){
		
		Connection con = null;
		try{
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/application.properties");
			String walletInitialAmount = prop.getProperty("wallet.initial.amount");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setFullName(fullName);
			userVo.setMobileNumber(contactNumber);
			userVo.setPassword(password);
			userVo.setWalletAmount(new BigDecimal(walletInitialAmount));
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			con.setAutoCommit(false);
			validatorService.signupValidation(userVo,con);

			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);

			userVo.setPassword(encryptedPassword);

			// If validation is getting successful then create new record in the
			// table called USERS.
			UserService userService = new UserServiceImpl();
			userService.registerNewUser(userVo, con);

			// Then create an empty cart for the new registered user.
			CartService cartService = new CartServiceImpl();
			cartService.createEmptyCart(con, userVo.getUserId());
			
			// Assign the role to new user.
			userService.createUserRole(con, userVo.getUserId(), Constants.REG_USER_ROLE_ID);

			con.commit();
			
			return Response.ok().entity("New user is registered successfully!")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			

		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getcategory")
	public Response getCategory(@Context HttpServletRequest request,
			@QueryParam("category_id")String categoryId){
		
		Connection con = null;
		try{
			CategoryService categoryService = new CategoryServiceImpl();
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			
			System.out.println("Session---"+ request.getSession().getId());
			
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			// getting Categories....
			CategoryService categoryRepository = new CategoryServiceImpl();
			List<CategoryVo> categories = null;
			
			if(!categoryId.equals("0")){
				categories = categoryService.getSubCategoryForParent(con, Long.parseLong(categoryId));
				
				if(categories==null || categories.size()==0){
					categories = categoryService.getAllCategory(con, false, true, false, true);
				}
			}				
			else{
				categories = categoryRepository.getAllCategory(con, false, true, false, true);
			}
			
			// getting HomePage Slider Images....
			MediaService mediaService = new MediaServiceImpl();
			List<MediaVo> mediaVos = mediaService.getHomePageSliderImage(con, true);
			
			List<Object> responseObject = new ArrayList<>();
			responseObject.add(categories);
			responseObject.add(mediaVos);
			
			System.out.println(categories);
			
			return Response.ok().entity(new Gson().toJson(responseObject))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getitembycategory")
	public Response getItemsByCategory(@Context HttpServletRequest request, 
			@QueryParam("category_id")String categoryId, @QueryParam("deal_of_the_day")Boolean dealOfTheDay,
			@QueryParam("is_from_search")Boolean isFromSearch, @QueryParam("search_val")String searchValue){
		
		Connection con = null;
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			GoodsService goodsService = new GoodsServicesImpl();
			List<GoodsVo> goodsVos = null;
			
			CategoryService categoryService = new CategoryServiceImpl();
			
			if(dealOfTheDay){
				
				goodsVos = goodsService.getDealOfTheDayGoods(con, true, true);
			}else{
				if(isFromSearch){
					if(searchValue.contains("("))
						searchValue = searchValue.substring(0,searchValue.indexOf("("));
					
					goodsVos = goodsService.getGoodsByGoodsName(con, searchValue, true, true);
				}else{
					
					List<CategoryVo> subCategoryVo = categoryService.getCategoryHierarchy(con);
					
					String categories = null;
					if(subCategoryVo!=null && subCategoryVo.size()>0){
						for (CategoryVo categoryVo : subCategoryVo) {
							if(categoryVo.getCategoryId().equals(Long.parseLong(categoryId))){
								categories = getLeastCategories(categoryVo, categories);
							}
						}
					}
					
					goodsVos = goodsService.getGoodsForCategory(con, categories, true, true);
					System.out.println(new Gson().toJson(goodsVos));
				}
			}
			
			CategoryVo categoryVo = new CategoryVo();
			categoryVo.setGoods(goodsVos);
			
			return Response.ok().entity(new Gson().toJson(categoryVo))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();

		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/senddealmessage")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response sendDealMessage(@Context HttpServletRequest request, @FormParam("username") String userName, 
			@FormParam("password") String password){
		Connection con = null;
		try{
			
			SMSService smsService = new SMSService();
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);
			logger.debug("sendDealMessage::::: UserName::" + userName + ", encryptedPassword:::" + encryptedPassword);

			UserService userService = new UserServiceImpl();
			UserVo userDetails = userService.checkLoginCredential(userName, encryptedPassword, con);
			
			logger.debug("UserDetails::::::"+ userDetails);
			
			if(userDetails == null){
				throw new Exception("Login Credential is wrong.");
			}else{
				RoleVo roleVo = userService.getRoleForUser(con, userDetails.getUserId());
				logger.debug("roleVo::::::" + roleVo);
				if(!roleVo.getRoleId().equals(1l)){
					logger.debug("User is not correct, Please put admin credentials");
					throw new Exception("Login Credential is wrong.");
				}
			}
			
			String message = UserUtil.messageForDealOfTheDay(con);
			logger.debug("message:::::::" + message);
			
			ArrayList<String> customerNumber = userService.getDOTDCustomerNumber(con);
			String[] numbers = customerNumber.toArray(new String[customerNumber.size()]);
			logger.debug("numbers:::::::" + numbers);

			smsService.sendMessage(numbers, message);
			logger.debug("Message sent successfully!!!!!");
			
			return Response.ok().entity(new Gson().toJson("Message Sent Successfully!"))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error while sending message:::" + e.getMessage());
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_all_goods")
	public Response getAllGoods(@Context HttpServletRequest request){
		
		Connection con = null;
		try{
			System.out.println("&&&&&&&&&&&&&&&&& Get All Goods &&&&&&&&&&&&&&&&");
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			GoodsService goodsService = new GoodsServicesImpl();
			List<GoodsVo> goodsList = goodsService.getAllGoods(con);
			
			CategoryVo categoryVo = new CategoryVo();
			categoryVo.setGoods(goodsList);
			
			return Response.ok().entity(new Gson().toJson(categoryVo))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();

		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getmyorders")
	public Response getMyOrders(@Context HttpServletRequest request, @FormParam("user_id") Long userId){
		
		Connection con = null;
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			
			System.out.println("Session---"+ request.getSession().getId());
			
			con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			OrderService orderService = new OrderServiceImpl();
			List<CartDetails> cartDetails = orderService.getOrdersForUser(con, userId);
			
			return Response.ok().entity(new Gson().toJson(cartDetails))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			

		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/signin_google")
	public Response googleSignIn(@Context HttpServletRequest request, @FormParam("token_id") String tokenId){
		
		try {
			
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
		    .setAudience(Collections.singletonList(CLIENT_ID))
		    // Or, if multiple clients access the backend:
		    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
		    .build();
	
			// (Receive idTokenString by HTTPS POST)
		
			GoogleIdToken idToken=null;
		
			idToken = verifier.verify(tokenId);
			
			if (idToken != null) {
			  Payload payload = idToken.getPayload();
		
			  // Print user identifier
			  String userId = payload.getSubject();
			  System.out.println("User ID: " + userId);
		
			  // Get profile information from payload
			  String email = payload.getEmail();
			  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			  String name = (String) payload.get("name");
			  /*String pictureUrl = (String) payload.get("picture");
			  String locale = (String) payload.get("locale");
			  String familyName = (String) payload.get("family_name");
			  String givenName = (String) payload.get("given_name");*/
			  
			  String userIdForPassword = payload.getSubject();
			  
			  System.out.println(email+"....."+emailVerified+"...."+name);
			  
			  UserVo userVo = new UserVo();
			  userVo.setFullName(name);
			  userVo.setUserName(name);
			  userVo.setEmail(email);
			  userVo.setPassword(userIdForPassword);
			  
			  ServletContext ctx = request.getServletContext();
			  DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			  Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			  
			  UserService userService = new UserServiceImpl();
			  UserVo userDetails = userService.otherSignUpAndSignIn(userVo, con, 2);
			  
			  return Response.ok().entity(new Gson().toJson(userDetails))
						.header("Access-Control-Allow-Origin", "*").
						header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			  
		
			} else {
			  System.out.println("Invalid ID token.");
			  throw new Exception("Invalid ID token.");
			}
			
			
		} catch (Exception e) {
			System.out.println("Some exception come at gmail login::::::"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/signin_facebook")
	public Response facebookSignIn(@Context HttpServletRequest request, @FormParam("access_token_id") String accessTokenId){
		try{
			
			
			URIBuilder builder = new URIBuilder("https://graph.facebook.com/debug_token?")
		            .addParameter("input_token", accessTokenId)
		            .addParameter("access_token", ACCESS_TOKEN_ID);

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet post = new HttpGet(builder.build());

            org.apache.http.HttpResponse response = client.execute(post);
            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            
            System.out.println("&&&&&&&&&&&&&--------"+ result.toString());
			
            return Response.ok().entity("success")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/signup")
	public Response userSignup(@Context HttpServletRequest request, @FormParam("email") String email,
			@FormParam("name") String name, @FormParam("contact") String contact, @FormParam("password") String password,
			@FormParam("otp") String randomNumber){
		Connection con = null;
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/application.properties");
			String walletInitialAmount = prop.getProperty("wallet.initial.amount");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setFullName(name);
			userVo.setMobileNumber(contact);
			userVo.setPassword(password);
			userVo.setWalletAmount(new BigDecimal(walletInitialAmount));
			
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			validatorService.signupValidation(userVo,con);
			
			String otpMessage = "Welcome to SableBadiya.com, Your otp is " + randomNumber + " ." ;
			SendOtp sendOtp = new SendOtp();
			sendOtp.sendSms(otpMessage, contact);
			/*SMSService smsService = new SMSService();
			smsService.sendMessage(new String[]{contact}, otpMessage);*/

			/*SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);

			userVo.setPassword(encryptedPassword);

			// If validation is getting successful then create new record in the
			// table called USERS.
			UserService userService = new UserServiceImpl();
			userService.registerNewUser(userVo, con);

			// Then create an empty cart for the new registered user.
			CartService cartService = new CartServiceImpl();
			cartService.createEmptyCart(con, userVo.getUserId());
			
			// Assign the role to new user.
			userService.createUserRole(con, userVo.getUserId(), Constants.REG_USER_ROLE_ID);
			
			// create user_wallet	
			userService.createUserWallet(con, userVo.getUserId(),
					walletInitialAmount, "Wallet created 1st time at user registration userId:" 
							+ userVo.getUserId().toString());

			con.commit();
			System.out.println("User created successfully");*/
			
			return Response.ok().entity("Validation is successfull & send otp message")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			

		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Error : "+e.getMessage()).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update_user_profile")
	public Response updateUserProfile(@Context HttpServletRequest request, @FormParam("email") String email,
			@FormParam("name") String name, @FormParam("contact") String contact, 
			@FormParam("user_id") Long userId){
		Connection con = null;
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setFullName(name);
			userVo.setMobileNumber(contact);
			userVo.setUserId(userId);
			
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			UserService userService = new UserServiceImpl();
			userService.updateUserPersonalInfo(con, userVo);

			con.commit();
			System.out.println("User profile updated successfully");
			
			return Response.ok().entity("User profile updated successfully")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			

		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Error : "+e.getMessage()).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/change_password")
	public Response changePassword(@Context HttpServletRequest request, @FormParam("old_password") String oldPass,
			@FormParam("new_password") String newPass, 
			@FormParam("email") String email){
		Connection con = null;
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			
			con = ds.getConnection();
			
			
			CartService cartService = new CartServiceImpl();
			// Reset the password for following users.
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedOldPassword = securityService.getEncryptedPassword(oldPass);
			
			UserService userService = new UserServiceImpl();
			UserVo user = userService.checkLoginCredential(email, encryptedOldPassword, con);
			
			if (user != null) {
				con.setAutoCommit(false);
				String encryptedNewPassword = securityService.getEncryptedPassword(newPass);
				cartService.resetPassword(con, email, encryptedNewPassword);
				
				// Send an email for change password.
				/*MailConfig mailConfig = new MailConfig();
				mailConfig.sendTextMail(subject, body, userDetails.getEmail());*/
				
				con.commit();
				
				return Response.ok().entity("User password updated successfully")
						.header("Access-Control-Allow-Origin", "*").
						header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			}else{
				throw new Exception("old password is not correct");
			}
		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Error : "+e.getMessage()).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/signup_after_otp_matched")
	public Response signUpAfterOtp(@Context HttpServletRequest request, @FormParam("email") String email,
			@FormParam("name") String name, @FormParam("contact") String contact, @FormParam("password") String password){
		
		Connection con = null;
		try{
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource)ctx.getAttribute("dataSource");
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/application.properties");
			String walletInitialAmount = prop.getProperty("wallet.initial.amount");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setFullName(name);
			userVo.setMobileNumber(contact);
			userVo.setPassword(password);
			userVo.setWalletAmount(new BigDecimal(walletInitialAmount));
			
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			SecurityService securityService = new SecurityServiceImpl();
			String encryptedPassword = securityService.getEncryptedPassword(password);

			userVo.setPassword(encryptedPassword);

			// If validation is getting successful then create new record in the
			// table called USERS.
			UserService userService = new UserServiceImpl();
			userService.registerNewUser(userVo, con);

			// Then create an empty cart for the new registered user.
			CartService cartService = new CartServiceImpl();
			cartService.createEmptyCart(con, userVo.getUserId());
			
			// Assign the role to new user.
			userService.createUserRole(con, userVo.getUserId(), Constants.REG_USER_ROLE_ID);
			
			// create user_wallet	
			userService.createUserWallet(con, userVo.getUserId(),
					walletInitialAmount, "Wallet created 1st time at user registration userId:" 
							+ userVo.getUserId().toString());

			con.commit();
			System.out.println("User created successfully");
			
			
			return Response.ok().entity("User created successfully")
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			

		}catch(Exception e){
			e.printStackTrace();
			if(con != null){
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return Response.status(500).entity("Error : "+e.getMessage()).build();
		}
		
	}
	
	@GET
	@Path("/email_validation")
	public Response emailValidation(@QueryParam("email")String email, @Context HttpServletRequest request){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CartService cartService = new CartServiceImpl();
			Boolean isEmailValid = cartService.isEmailValid(con, email);
			
			if(isEmailValid){
				// Reset the password for following users.
				SecurityService securityService = new SecurityServiceImpl();
				String randomPass = RandomStringGeneration.getRandomString(15);
				String encryptedPassword = securityService.getEncryptedPassword(randomPass);
				
				System.out.println("random pass---" + randomPass);
				
				cartService.resetPassword(con, email, encryptedPassword);
				
				String subject = "Password is changed";
				String body = "Congratulations your password is reset successfully, Your new password is now : " + randomPass + ".\n";
				
				body = body + "You can change your password by Login-> Home-> MyAccount-> Password Change.";
				// Send an email for change password.
				MailConfig mailConfig = new MailConfig();
				mailConfig.sendTextMail(subject, body, email, true);
				
				return Response.ok().entity("Email is valid")
						.header("Access-Control-Allow-Origin", "*").
						header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
			}else{
				throw new Exception("Given email id is invalid");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delivery_area")
	public Response getDeliveryArea(@Context HttpServletRequest request){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			OrderService orderService = new OrderServiceImpl();
			List<String> deliveryArea = orderService.getDeliveryArea(con);
			
			return Response.ok().entity(new Gson().toJson(deliveryArea))
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user_wallet_amount")
	public Response getUserWalletAmount(@Context HttpServletRequest request, @FormParam("user_id") Long userId){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			UserService userService = new UserServiceImpl();
			UserVo userVo = userService.getUserDetailById(con, userId);
			
			String walletAmount = "0";
			if(userVo!=null){
				walletAmount = userVo.getWalletAmount().toString();
			}
			
			return Response.ok().entity(walletAmount)
					.header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Exception :::"+e.getMessage()).build();
		}
		
	}
	
	private String getLeastCategories(CategoryVo categoryVo, String categories){
		List<CategoryVo> subCategory = categoryVo.getCategoryVos();
		
		if(subCategory!=null && subCategory.size()>0){
			for (CategoryVo categoryVo2 : subCategory) {
				categories = getLeastCategories(categoryVo2, categories);
			}
		}else{
			if(categories==null){
				categories = categoryVo.getCategoryId().toString();
			}else{
				categories = categories + "," + categoryVo.getCategoryId();
			}
		}
		
		return categories;
	}
}
