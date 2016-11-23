
package com.cigital.integration.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.cigital.integration.dao.GoodsRepository;
import com.cigital.integration.dao.impl.GoodsRepositoryImpl;
import com.cigital.integration.service.CartService;
import com.cigital.integration.service.CategoryService;
import com.cigital.integration.service.GoodsService;
import com.cigital.integration.service.OrderService;
import com.cigital.integration.service.SellerService;
import com.cigital.integration.service.impl.CartServiceImpl;
import com.cigital.integration.service.impl.CategoryServiceImpl;
import com.cigital.integration.service.impl.GoodsServicesImpl;
import com.cigital.integration.service.impl.OrderServiceImpl;
import com.cigital.integration.service.impl.SellerServiceImpl;
import com.cigital.integration.util.Constants;
import com.cigital.integration.util.DataSourceUtil;
import com.cigital.integration.util.LoadPropertiesFile;
import com.cigital.integration.util.SMSService;
import com.cigital.integration.vo.CartDetails;
import com.cigital.integration.vo.CartItem;
import com.cigital.integration.vo.CategoryVo;
import com.cigital.integration.vo.FieldExecutiveVo;
import com.cigital.integration.vo.OrderTrackingVo;
import com.cigital.integration.vo.QuantityVo;
import com.cigital.integration.vo.SellerVo;
import com.cigital.integration.vo.UserVo;

/**
 * Servlet implementation class AdminHomeController
 */

public class AdminHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = Logger.getLogger(AdminHomeController.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminHomeController() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
    private static final String HOME_DEFAULT_VIEW = "/adminhome";
    private static final String NEW_ORDERS_VIEW = "/neworders_view";
    private static final String SELLER_APPROVED_VIEW = "/sellerapproved_view";
    private static final String NEW_ORDER_VIEW = "/view_new_order_admin";
    private static final String SELLER_PENDING_REJECTION_ORDER = "/seller_pending_reject_order";
    private static final String ORDER_STATUS_PAGE = "/order_status_check_page";
    private static final String SEARCH_ORDER = "/search_order";
    private static final String VIEW_ORDER_HISTORY = "/view_order_history";
    private static final String PROCESS_PENDING_REJECTION_ORDER = "/process_pending_reject_order";
    private static final String CANCEL_ORDER_ITEM_ORDER = "/cancle_order_item";
    private static final String UPDATE_PRICE = "/update_price";
    private static final String ASSIGN_FE_BY_ADMIN = "/assign_fe";
    private static final String ADD_GOODS = "/add_goods";
    private static final String OFFLINE_ORDER = "/offline_order";
    private static final String OFFLINE_ORDER_ADD_GOODS = "/offline_order_add_goods";
    private static final String OFFLINE_ORDER_ITEM_DELETE = "/offline_order_item_delete";
    
    
    
    private static final String DISPATCH_ADMIN_DEFAULT_VIEW = "/jsp/admin_home.jsp";
    private static final String DISPATCH_NEW_ORDERS = "/jsp/neworders.jsp";
    private static final String DISPATCH_SELLER_APPROVED_ORDERS = "/jsp/seller-approved-order.jsp";
    private static final String DISPATCH_VIEW_ORDER = "/jsp/view_order_by_admin.jsp";
    private static final String DISPATCH_SELLER_PENDING_REJECTION_ORDER = "/jsp/seller_pending_reject_order.jsp";
    private static final String DISPATCH_ORDER_STATUS_PAGE = "/jsp/order_status.jsp";
    private static final String DISPATCH_SEARCH_ORDER = "/jsp/order_status_search.jsp";
    private static final String DISPATCH_VIEW_ORDER_HISTORY = "/jsp/view_order_history.jsp";
    private static final String DISPATCH_PROCESS_PENDING_REJECTION_ORDER = "/jsp/process_pending_reject_order.jsp";
    private static final String DISPATCH_UPDATE_PRICE = "/jsp/update_price.jsp";
    private static final String DISPATCH_ADD_GOODS = "/jsp/add_goods.jsp";
    private static final String DISPATCH_OFFLINE_ORDER = "/jsp/offline_order.jsp";
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String pathInfo = request.getPathInfo();
			System.out.println(pathInfo);
			
			if (pathInfo == null || pathInfo.equalsIgnoreCase(HOME_DEFAULT_VIEW)) {

				HttpSession session = request.getSession();
				session.setAttribute("offline_order", null);
				request.getRequestDispatcher(DISPATCH_ADMIN_DEFAULT_VIEW).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(NEW_ORDERS_VIEW)){
				
				request.getRequestDispatcher(DISPATCH_NEW_ORDERS).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(SELLER_APPROVED_VIEW)){
				
				request.getRequestDispatcher(DISPATCH_SELLER_APPROVED_ORDERS).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(NEW_ORDER_VIEW)){
				
				dispatchViewOrder(request, response, true);
			}else if(pathInfo.equalsIgnoreCase(SELLER_PENDING_REJECTION_ORDER)){
				
				request.getRequestDispatcher(DISPATCH_SELLER_PENDING_REJECTION_ORDER).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(ORDER_STATUS_PAGE)){
				
				request.getRequestDispatcher(DISPATCH_ORDER_STATUS_PAGE).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(SEARCH_ORDER)){
				
				request.getRequestDispatcher(DISPATCH_SEARCH_ORDER).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(VIEW_ORDER_HISTORY)){
				
				dispatchToOrderHistory(request, response);
			}else if(pathInfo.equalsIgnoreCase(PROCESS_PENDING_REJECTION_ORDER)){
				
				dispatchViewOrder(request, response, false);
			}else if(pathInfo.equalsIgnoreCase(CANCEL_ORDER_ITEM_ORDER)){
				
				ajaxCallForCancleItemOrder(request, response);
			}else if(pathInfo.equalsIgnoreCase(UPDATE_PRICE)){
				
				updatePriceRedirect(request, response);
			}else if(pathInfo.equalsIgnoreCase(ASSIGN_FE_BY_ADMIN)){
				
				ajaxCallForAssignFe(request, response);
			}else if(pathInfo.equalsIgnoreCase(ADD_GOODS)){
				
				ServletContext ctx = request.getServletContext();
				DataSource ds = (DataSource) ctx.getAttribute("dataSource");
				Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
				
				GoodsService goodsService = new GoodsServicesImpl();
				request.setAttribute("quantityVos", goodsService.getAllQuantity(con));
				request.getRequestDispatcher(DISPATCH_ADD_GOODS).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(OFFLINE_ORDER)){
				
				ServletContext ctx = request.getServletContext();
				DataSource ds = (DataSource) ctx.getAttribute("dataSource");
				Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
				
				GoodsService goodsService = new GoodsServicesImpl();
				request.setAttribute("quantities", goodsService.getAllQuantity(con));
				request.getRequestDispatcher(DISPATCH_OFFLINE_ORDER).forward(request, response);
			}else if(pathInfo.equalsIgnoreCase(OFFLINE_ORDER_ADD_GOODS)){
				
				ajaxCallForAddGoodOffline(request, response);
			}else if(pathInfo.equalsIgnoreCase(OFFLINE_ORDER_ITEM_DELETE)){
				
				ajaxCallForDeleteItem(request, response);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
				
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(req)) {
            // if not, we stop here
            PrintWriter writer = resp.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }
 
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);
 
        // constructs the directory path to store upload file
        // this path is relative to application's directory
        LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
        Properties prop = null;
		try {
			prop = propertiesFile.loadProperties("/application.properties");
		} catch (Exception e2) {
			logger.error("Error::", e2);
			e2.printStackTrace();
		}
		
        String uploadPath = prop.getProperty("file.upload.path");
         
        String goodsName = null;
        String category = null;
		String seller = null;
		String price = null;
		String mrp = null;
		String qty = null;
		String fileName = null;
		Boolean allowRupee = null;
		Boolean inStock = null;
		
		FileItem fileItemForUpload = null;
 
        try {
            // parses the request's content to extract file data
           // @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(req);
 
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                    	fileItemForUpload = item;
                    	fileName = new File(fileItemForUpload.getName()).getName();
                    }else{
                    	String fieldname = item.getFieldName();
                    	if(fieldname.equalsIgnoreCase("goods_name")){
                    		goodsName = item.getString();
                    	}else if(fieldname.equalsIgnoreCase("category")){
                    		category = item.getString();
                    	}else if(fieldname.equalsIgnoreCase("qty")){
                    		if(qty==null)
                    			qty = item.getString();
                    		else
                    			qty = qty + "," + item.getString();
                    	}else if(fieldname.equalsIgnoreCase("seller")){
                    		seller = item.getString();
                    	}else if(fieldname.equalsIgnoreCase("price")){
                    		price = item.getString();
                    	}else if(fieldname.equalsIgnoreCase("mrp")){
                    		mrp = item.getString();
                    	}else if(fieldname.equalsIgnoreCase("allow_rupee")){
                    		allowRupee = Boolean.parseBoolean(item.getString());
                    	}else if(fieldname.equalsIgnoreCase("stock")){
                    		inStock = Boolean.parseBoolean(item.getString());
                    	}
                    }
                }
            }
        } catch (Exception ex) {
        	logger.error("Error::", ex);
            req.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }
        
        ServletContext ctx = req.getServletContext();
		DataSource ds = (DataSource) ctx.getAttribute("dataSource");
		Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
		
		try{
			con.setAutoCommit(false);
			
			HttpSession session =  req.getSession();
			UserVo userDetails = (UserVo) session.getAttribute("userDetails");
			
			GoodsService goodsService = new GoodsServicesImpl();
	        goodsService.addItemByAdmin(con, goodsName, category, seller, price, mrp, qty, 
	        		fileName, userDetails.getUserId(),allowRupee, inStock, prop);
	        
	        if(fileItemForUpload==null){
	        	throw new Exception();
	        }
	        
	        if(category.equalsIgnoreCase("veg")){
	        	uploadPath = uploadPath + File.separator + "vegetables";
	        }else{
	        	uploadPath = uploadPath + File.separator + "fruits";
	        }
	        
	        // creates the directory if it does not exist
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }
	        
            String filePath = uploadPath + File.separator + fileName;
            File storeFile = new File(filePath);

            // saves the file on disk
            fileItemForUpload.write(storeFile);
	        
	        con.commit();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.error("Error::", e);
			e.printStackTrace();
		}finally {
			try {
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		resp.sendRedirect("adminhome");
	}
	
	
	
	private void dispatchViewOrder(HttpServletRequest request, HttpServletResponse response, Boolean isForNewOrder){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String cartId = request.getParameter("cartId");
			
			// Fetch order details
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.getOrderById(con, Long.parseLong(cartId));
			
			// Fetch all sellers details
			SellerService sellerService = new SellerServiceImpl();
			List<SellerVo> sellerList = sellerService.getAllSeller(con);
			
			FieldExecutiveVo selectedFe = sellerService.getFieldExecutiveForOrder(con, Long.parseLong(cartId));
			
			// Fetch DeliveryBoy details.
			List<FieldExecutiveVo> feList = sellerService.getAllFieldExecutive(con);
			
			request.setAttribute("cartDetails", cartDetails);
			request.setAttribute("sellerList", sellerList);
			request.setAttribute("feList", feList);
			request.setAttribute("selectedFe", selectedFe);
			
			// For making the count of new orders be sync.
			HttpSession session = request.getSession();
			session.setAttribute("isFirstCall", null);
			session.setAttribute("pageName", "View Order");
			
			RequestDispatcher rd = null;
			if(isForNewOrder){
				rd = request.getRequestDispatcher(DISPATCH_VIEW_ORDER);
			}else{
				rd = request.getRequestDispatcher(DISPATCH_PROCESS_PENDING_REJECTION_ORDER);
			}
				
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void dispatchToOrderHistory(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			Long cartId = Long.parseLong(request.getParameter("cartId"));
			
			// Fetch order details
			CartService cartService = new CartServiceImpl();
			CartDetails cartDetails = cartService.getOrderById(con, cartId);
			
			OrderService orderService = new OrderServiceImpl();
			List<OrderTrackingVo> orderTrackList = orderService.getOrderTrackingForOrder(con, cartDetails.getOrderNumber());
			
			FieldExecutiveVo fieldExecutiveVo = null;
			if(!cartDetails.getCartStatusId().equals(2l)){
				SellerService sellerService = new SellerServiceImpl();
				fieldExecutiveVo = sellerService.getFieldExecutiveForOrder(con, cartDetails.getCartId());
				request.setAttribute("fe", fieldExecutiveVo);
			}
			
			request.setAttribute("cartDetails", cartDetails);
			request.setAttribute("orderTrackList", orderTrackList);
			
			RequestDispatcher rd = request.getRequestDispatcher(DISPATCH_VIEW_ORDER_HISTORY);
			rd.forward(request, response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void ajaxCallForCancleItemOrder(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
			
			CartService cartService = new CartServiceImpl();
			CartItem cartItem = cartService.getCartItemForId(con, cartItemId, false);
			
			cartService.inActiveCartItemById(con, cartItemId);
			
			// TODO: Need to inactive the seller cart items also.
			/*SellerService sellerService = new SellerServiceImpl();
			sellerService.InActiveSellerCartItem(con, cartItemId);*/
			
			CartDetails cartDetails = cartService.getOrderById(con, cartItem.getCartId());
			BigDecimal newOrderTotal = cartDetails.getTotalAmount().subtract(cartItem.getPrice());
			
			OrderService orderService = new OrderServiceImpl();
			orderService.updateOrderTotal(con, cartItem.getCartId(), newOrderTotal);
			
			String comment = "CartItem #"+cartItemId +" is canceled";
			orderService.updateOrderStatus(con, Constants.ORDER_ITEM_IS_CANCEL, cartItem.getCartId(), comment);
			
			response.setContentType("application/json");
	        response.getWriter().write(newOrderTotal.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void updatePriceRedirect(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			CategoryService categoryService = new CategoryServiceImpl();
			List<CategoryVo> categoryVos = categoryService.getAllCategory(con, true);
			
			request.setAttribute("categories", categoryVos);
			
			request.getRequestDispatcher(DISPATCH_UPDATE_PRICE).forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void ajaxCallForAssignFe(HttpServletRequest request, HttpServletResponse response){
		try{
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String feId = request.getParameter("feId");
			Long cartId = Long.parseLong(request.getParameter("cartId"));
			
			SellerService sellerService = new SellerServiceImpl();
			sellerService.persistOrderForFe(con, cartId, Integer.parseInt(feId));
			
			LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
			Properties prop = propertiesFile.loadProperties("/cart.properties");
			
			if(!prop.getProperty("order.confirm.sms.off").equals("true")){
				
				CartService cartService = new CartServiceImpl();
				CartDetails orderDetails = cartService.getOrderById(con, cartId);
				
				FieldExecutiveVo fieldExecutiveVo = sellerService.getFieldExecutiveForOrder(con, cartId);
				
				String message = "New Order "+ orderDetails.getOrderNumber() +" is assigned to you. Please check sablebadiya.com";
				SMSService smsService = new SMSService();
				String[] numbers = {fieldExecutiveVo.getFeContact()};
				smsService.sendMessage(numbers, message);
			}
			
			response.setContentType("text/plain");
	        response.getWriter().write("Successfully assigned FE.");
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void ajaxCallForAddGoodOffline(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			CartDetails cartDetails = (CartDetails) session.getAttribute("offline_order");
			
			if(cartDetails==null){
				BigDecimal zeroPrice = new BigDecimal("0");
				cartDetails = new CartDetails();
				cartDetails.setCartItems(new ArrayList<CartItem>());
				
				cartDetails.setTotalAmount(zeroPrice);
				cartDetails.setSubtotalAmount(zeroPrice);
				cartDetails.setShippingCharge(zeroPrice);
				cartDetails.setTotalMsrp(zeroPrice);
			}
			
			ServletContext ctx = request.getServletContext();
			DataSource ds = (DataSource) ctx.getAttribute("dataSource");
			Connection con = DataSourceUtil.getConnectionThruDataSource(ds);
			
			String goodsId = request.getParameter("goodsId");
			
			BigDecimal price = new BigDecimal(request.getParameter("price"));
			
			GoodsRepository goodsRepository = new GoodsRepositoryImpl();
			QuantityVo quantityVo = new QuantityVo();
			
			if (request.getParameter("qtyId").contains("Rs")) {
				String[] quantityStrArray = request.getParameter("qtyId").split(" ");
				quantityVo.setWeight(quantityStrArray[0]);
				quantityVo.setUom(quantityStrArray[1]);
			}else{
				Long qtyId = Long.parseLong(request.getParameter("qtyId"));
				quantityVo = goodsRepository.getQuantityForId(con, qtyId);
			}
			
			
			CartItem cartItem = new CartItem();
			cartItem.setGoodsId(Long.parseLong(goodsId));
			cartItem.setQuantity(quantityVo.getWeight());
			cartItem.setUom(quantityVo.getUom());
			cartItem.setPrice(price);
			cartItem.setMsrp(price);
			
			cartDetails.getCartItems().add(cartItem);
			
			cartDetails.setTotalAmount(cartDetails.getTotalAmount().add(price));
			cartDetails.setSubtotalAmount(cartDetails.getTotalAmount());
			cartDetails.setTotalMsrp(cartDetails.getTotalAmount());
			
			System.out.println("After Adding:::"+cartDetails.getCartItems().size());
			session.setAttribute("offline_order", cartDetails);
			
			response.setContentType("text/plain");
	        response.getWriter().write("Item is added successfully.");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void ajaxCallForDeleteItem(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			CartDetails cartDetails = (CartDetails) session.getAttribute("offline_order");
			
			Long goodsId = Long.parseLong(request.getParameter("goodsId"));
			
			List<CartItem> cartItems = cartDetails.getCartItems();
			BigDecimal itemPrice = BigDecimal.ZERO;
			
			Iterator<CartItem> itCartItem = cartItems.iterator();
			while (itCartItem.hasNext()) {
				CartItem cartItem = itCartItem.next();
				if(cartItem.getGoodsId().equals(goodsId)){
					itemPrice = cartItem.getPrice();
					itCartItem.remove();
					break;
				}
			}
			cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(itemPrice));
			cartDetails.setSubtotalAmount(cartDetails.getTotalAmount());
			cartDetails.setTotalMsrp(cartDetails.getTotalAmount());
			
			System.out.println("After Delete:::"+cartDetails.getCartItems().size());
			session.setAttribute("offline_order", cartDetails);
			
			response.setContentType("text/plain");
	        response.getWriter().write("Item is removed successfully.");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
