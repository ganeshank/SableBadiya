package com.sb.integration.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sb.integration.config.DataSourceConfig;
import com.sb.integration.dao.CartRepository;
import com.sb.integration.dao.GoodsRepository;
import com.sb.integration.dao.OrderRepository;
import com.sb.integration.dao.UserRepository;
import com.sb.integration.dao.impl.CartRepositoryImpl;
import com.sb.integration.dao.impl.GoodsRepositoryImpl;
import com.sb.integration.dao.impl.OrderRepositoryImpl;
import com.sb.integration.dao.impl.UserRepositoryImpl;
import com.sb.integration.service.CartService;
import com.sb.integration.util.Constants;
import com.sb.integration.util.UserUtil;
import com.sb.integration.vo.CartDetails;
import com.sb.integration.vo.CartItem;
import com.sb.integration.vo.DeliveryOption;
import com.sb.integration.vo.DeliveryTypeVo;
import com.sb.integration.vo.GoodsVo;
import com.sb.integration.vo.QuantityVo;
import com.sb.integration.vo.UserVo;

public class CartServiceImpl implements CartService {

	final static Logger logger = Logger.getLogger(DataSourceConfig.class);
	
	CartRepository cartRepository = new CartRepositoryImpl();
	GoodsRepository goodsRepository = new GoodsRepositoryImpl();
	OrderRepository orderRepository = new OrderRepositoryImpl();
	UserRepository userRepository = new UserRepositoryImpl();

	public void createEmptyCart(Connection con, Long userId) throws Exception {

		try {
			cartRepository.createEmptyCart(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void addToCart(Connection con, Long goodsId, Long quantityId, Long sellerId, HttpServletRequest req, BigDecimal rupee)
			throws Exception {

		try {
			HttpSession session = req.getSession();
			Boolean isGuestUser = UserUtil.isGuestUser(session);
			if (isGuestUser == null) {
				return;
			}

			QuantityVo quantityVo = goodsRepository.getQuantityForId(con, quantityId);
			GoodsVo goodsVo = goodsRepository.getGoodsForId(con, goodsId);
			
			// Actual price for selected item
			BigDecimal priceForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getPrice(),
					goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());

			// Actual MSRP for selected item
			BigDecimal msrpForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getMsrp(),
					goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());
			
			// Register User...
			if (!isGuestUser) {
				UserVo userDetails = (UserVo) session.getAttribute("userDetails");
				
				CartDetails cartDetails = cartRepository.getActiveCartForUser(con, userDetails.getUserId());

				CartItem newCartItem = new CartItem();
				newCartItem.setCartId(cartDetails.getCartId());
				newCartItem.setGoodsId(goodsId);
				
				if(rupee.equals(BigDecimal.ZERO)){
					newCartItem.setMsrp(msrpForQty);
					newCartItem.setPrice(priceForQty);
					newCartItem.setQuantity(quantityVo.getWeight());
					newCartItem.setUom(quantityVo.getUom());
					
					cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().add(priceForQty));
					cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
					cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().add(msrpForQty));
					
				}else{
					newCartItem.setMsrp(rupee);
					newCartItem.setPrice(rupee);
					newCartItem.setQuantity(rupee.toString());
					newCartItem.setUom("Rs");
					
					cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().add(rupee));
					cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
					cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().add(rupee));
				}

				cartRepository.addCartItem(con, cartDetails, newCartItem, true);
				/* } */
			} else {
				// guest user...
				CartDetails cartDetails = (CartDetails)session.getAttribute("guestCart");
				
				CartItem cartItem = new CartItem();
				cartItem.setGoodsId(goodsId);
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
				
				cartItem.setGoodsVo(goodsVo);
				
				List<QuantityVo> quantityVos = goodsRepository.getQuantityListForGoods(con, cartItem.getGoodsId());
				cartItem.setQuantityVos(quantityVos);
				cartItem.setSaving(cartItem.getMsrp().subtract(cartItem.getPrice()));
				
				if(cartDetails==null){
					// First item need to be added.
					cartItem.setCartItemId(1l);
					
					cartDetails = new CartDetails();
					cartDetails.setCartStatusId(Constants.ACTIVE_CART_STATUS_ID.longValue());
					if(rupee.equals(BigDecimal.ZERO)){
						cartDetails.setTotalAmount(priceForQty);
						cartDetails.setSubtotalAmount(priceForQty);
						cartDetails.setTotalMsrp(msrpForQty);
					}else{
						cartDetails.setTotalAmount(rupee);
						cartDetails.setSubtotalAmount(rupee);
						cartDetails.setTotalMsrp(rupee);
					}
					
					cartDetails.setShippingCharge(BigDecimal.ZERO);
					cartDetails.setTax(BigDecimal.ZERO);
					
					List<CartItem> cartItems = new ArrayList<>();
					cartItems.add(cartItem);
					
					cartDetails.setCartItems(cartItems);
					
				}else{
					// Some item is already there, add new item.
					List<CartItem> cartItems = cartDetails.getCartItems();
					
					Integer cartItemsSize = cartItems.size();
					cartItem.setCartItemId(cartItemsSize.longValue()+1);
					
					cartItems.add(cartItem);
					
					cartDetails.setCartItems(cartItems);
					if(rupee.equals(BigDecimal.ZERO)){
						cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().add(priceForQty));
						cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
						cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().add(msrpForQty));
					}else{
						cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().add(rupee));
						cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
						cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().add(rupee));
					}
				}
				
				cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getSubtotalAmount()));
				
				session.setAttribute("guestCart", cartDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}

	}

	@Override
	public Boolean isCartItemAlreadyExist(Connection con, Long userId, Long goodsId) throws Exception {

		return cartRepository.isCartItemAlreadyExist(con, userId, goodsId);
	}

	/*private void updateCartItemObject(CartItem cartItem, BigDecimal price, BigDecimal msrp, String quantity,
			String uom) {

		cartItem.setPrice(cartItem.getPrice().add(price));
		cartItem.setMsrp(cartItem.getMsrp().add(msrp));
		String[] quantityUom = calculateQuantity(cartItem.getQuantity(), cartItem.getUom(), quantity, uom).split("/");
		cartItem.setQuantity(quantityUom[0]);
		cartItem.setUom(quantityUom[1]);
	}

	private void updateCartObject(List<CartItem> cartItems, CartDetails cartDetails) {

		BigDecimal subTotalAmount = BigDecimal.ZERO;
		BigDecimal totalMsrp = BigDecimal.ZERO;
		for (CartItem cartItem : cartItems) {
			subTotalAmount = subTotalAmount.add(cartItem.getPrice());
			totalMsrp = totalMsrp.add(cartItem.getMsrp());
		}

		cartDetails.setSubtotalAmount(subTotalAmount);
		cartDetails.setTotalAmount(subTotalAmount);
		cartDetails.setTotalMsrp(totalMsrp);

	}

	private CartItem getCartItemForUpdation(List<CartItem> cartItems, Long goodsId) {

		for (CartItem cartItem : cartItems) {
			if (cartItem.getGoodsId().equals(goodsId)) {
				return cartItem;
			}
		}
		return null;
	}*/

	public BigDecimal calculatePriceForQuantity(String unitUom, BigDecimal unitPrice, String qtyPerAnnum,
			String orderUom, String orderWieght) {

		BigDecimal priceForQty = null;
		BigDecimal oneGmPrice = null;

		if (unitUom.equalsIgnoreCase("gm")) {
			oneGmPrice = unitPrice.divide(new BigDecimal(qtyPerAnnum));
		} else if (unitUom.equalsIgnoreCase("kg")) {
			oneGmPrice = unitPrice.divide(new BigDecimal(1000));
		} else if (unitUom.equalsIgnoreCase("dz")) {
			if (orderWieght.equals("1/2")) {
				priceForQty = unitPrice.divide(new BigDecimal(2));
			} else {
				priceForQty = unitPrice.multiply(new BigDecimal(orderWieght));
			}
		}else if(unitUom.equalsIgnoreCase("pc") || unitUom.equalsIgnoreCase("bndl")
				|| unitUom.equalsIgnoreCase("thali") || unitUom.equalsIgnoreCase("plate")){
			priceForQty = unitPrice.multiply(new BigDecimal(orderWieght));
		}

		if (orderUom.equals("gm")) {
			priceForQty = oneGmPrice.multiply(new BigDecimal(orderWieght));
		} else if(orderUom.equals("kg")) {
			// It may be KG.
			if(orderWieght.contains("-")){
				orderWieght = orderWieght.substring(0, 1);
			}
			priceForQty = oneGmPrice.multiply(new BigDecimal(1000)).multiply(new BigDecimal(orderWieght));
		}

		return priceForQty;
	}

	/*private String calculateQuantity(String existingQuantity, String existingUom, String newQuantity, String newUom) {

		String qty = null;
		String uom = null;
		if (existingUom.equals("gm") && newUom.equals("gm")) {
			Integer totalQtyInGm = Integer.parseInt(existingQuantity) + Integer.parseInt(newQuantity);
			if (totalQtyInGm >= 1000) {
				qty = 1 + "." + ((totalQtyInGm - 1000) / 10);
				uom = "kg";
			} else {
				qty = totalQtyInGm.toString();
				uom = "gm";
			}
		} else if (existingUom.equals("kg") && newUom.equals("gm")) {
			Double qunatityInt = Double.parseDouble(existingQuantity) + (Double.parseDouble(newQuantity) / 1000);
			qty = qunatityInt.toString();
			uom = "kg";
		} else if (existingUom.equals("gm") && newUom.equals("kg")) {
			Double qunatityInt = Double.parseDouble(newQuantity) + (Double.parseDouble(existingQuantity) / 1000);
			qty = qunatityInt.toString();
			uom = "kg";
		} else if (existingUom.equals("dz") && newUom.equals("dz")) {
			if (existingQuantity.equals("1/2") && newUom.equals("1/2")) {
				qty = "1";
			} else if (existingQuantity.equals("1/2")) {
				qty = Integer.parseInt(newQuantity) + ".5";
			} else if (newQuantity.equals("1/2")) {
				qty = Integer.parseInt(existingQuantity) + ".5";
			}
			uom = "dz";
		}

		return qty + "/" + uom;
	}*/

	@Override
	public Integer getActiveCartItemCountForUser(Connection con, Long userId) throws Exception {

		try {
			List<CartItem> cartItems = cartRepository.getActiveCartItemsForUser(con, userId);
			if (cartItems == null) {
				return 0;
			} else {
				return cartItems.size();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public CartDetails getActiveCartForUser(Connection con, Long userId) throws Exception {

		try {
			return cartRepository.getActiveCartForUser(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public CartDetails deleteCartItem(Connection con, Long cartItemId, HttpSession session) throws Exception {

		try {
			CartDetails cartDetails = null;
			CartItem cartItem = null;
			if(UserUtil.isGuestUser(session)){
				cartDetails = (CartDetails) session.getAttribute("guestCart");
				if(cartDetails!=null && cartDetails.getCartItems()!=null && cartDetails.getCartItems().size()>0){
					List<CartItem> cartItems = cartDetails.getCartItems();
					System.out.println("Before deleting the cart item, size:::"+cartItems.size());
					BigDecimal price = BigDecimal.ZERO;
					BigDecimal msrp = BigDecimal.ZERO;
					
					ListIterator<CartItem> listIterator = cartItems.listIterator();
					while (listIterator.hasNext()) {
						CartItem ct = listIterator.next();
						if(ct.getCartItemId().equals(cartItemId)){
							price = ct.getPrice();
							msrp = ct.getMsrp();
							listIterator.remove();
							break;
						}
						
					}
					
					System.out.println("Before deleting the cart item, size:::"+cartItems.size());
					cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(price));
					cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().subtract(price));
					cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().subtract(msrp));
					cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));
					
					session.setAttribute("guestCart", cartDetails);
					return cartDetails;
				}
				else{
					return null;
				}
				
			}else{
				cartItem = cartRepository.getCartItemForId(con, cartItemId, false);
				cartDetails = cartRepository.getActiveCartForId(con, cartItem.getCartId());

				cartRepository.deleteCartItemForId(con, cartItem.getCartItemId());

				cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(cartItem.getPrice()));
				cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().subtract(cartItem.getPrice()));
				cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().subtract(cartItem.getMsrp()));
				cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

				cartRepository.updateCart(con, cartDetails);
			}
			

			return cartDetails;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public CartDetails updateQuantityCartItem(Connection con, Long quantityId, Long cartItemId, HttpSession session, Boolean isQuantityRupee) throws Exception {

		try {
			
			CartItem cartItem = null;
			CartDetails cartDetails = null;
			if(UserUtil.isGuestUser(session)){
				cartDetails = (CartDetails)session.getAttribute("guestCart");
				if(cartDetails==null)
					return null;
				
				List<CartItem> cartItems = cartDetails.getCartItems();
				
				ListIterator<CartItem> listIterator = cartItems.listIterator();
				while (listIterator.hasNext()) {
					CartItem ct = listIterator.next();
					if(ct.getCartItemId().equals(cartItemId)){
						cartItem = ct;
						listIterator.remove();
						break;
					}
				}
				
			}else{
				cartItem = cartRepository.getCartItemForId(con, cartItemId, false);
				cartDetails = cartRepository.getActiveCartForId(con, cartItem.getCartId());
			}

			BigDecimal priceForQty = null;
			BigDecimal msrpForQty = null;
			BigDecimal oldPrice = null;
			BigDecimal oldMsrp = null;
			
			
			if(!isQuantityRupee){
				QuantityVo quantityVo = goodsRepository.getQuantityForId(con, quantityId);
				// List<CartItem> cartItems = cartDetails.getCartItems();
				GoodsVo goodsVo = goodsRepository.getGoodsForId(con, cartItem.getGoodsId());

				// Actual price for selected item
				priceForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getPrice(),
						goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());

				// Actual MSRP for selected item
				msrpForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getMsrp(),
						goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());
				
				cartItem.setQuantity(quantityVo.getWeight());
				cartItem.setUom(quantityVo.getUom());
				
			}else{
				priceForQty = new BigDecimal(quantityId);
				msrpForQty = new BigDecimal(quantityId);
				
				cartItem.setQuantity(quantityId.toString());
				cartItem.setUom("Rs");
			}
			
			oldPrice = cartItem.getPrice();
			oldMsrp = cartItem.getMsrp();

			// Update cart item object with updated values.
			cartItem.setPrice(priceForQty);
			cartItem.setMsrp(msrpForQty);
			cartItem.setSaving(cartItem.getMsrp().subtract(cartItem.getPrice()));
			

			// update cart Details object with updated prices.
			Double subTotalD = cartDetails.getSubtotalAmount().doubleValue();
			Double TotaMsrplD = cartDetails.getTotalMsrp().doubleValue();
			
			Double calculatedPriceD = (priceForQty.doubleValue()) - oldPrice.doubleValue();
			Double calculatedMsrpD = (msrpForQty.doubleValue()) - oldMsrp.doubleValue();
			
			cartDetails.setSubtotalAmount(new BigDecimal(subTotalD + calculatedPriceD));
			cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
			cartDetails.setTotalMsrp(new BigDecimal(TotaMsrplD + calculatedMsrpD));
			
			cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

			// Update in DB.
			if(!UserUtil.isGuestUser(session)){
				cartRepository.updateExistingCartItem(con, cartDetails, cartItem, true);
				List<CartItem> cartItems = new ArrayList<>();
				cartItems.add(cartItem);
				
				cartDetails.setCartItems(cartItems);
			}else{
				List<CartItem> cartItemsList = cartDetails.getCartItems();
				cartItemsList.add(0, cartItem);
				cartDetails.setCartItems(cartItemsList);
				
				session.setAttribute("guestCart", cartDetails);
			}

			return cartDetails;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void emptyCart(Connection con, Long cartId) throws Exception {
		try {
			cartRepository.emptyCart(con, cartId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public Boolean isEmailValid(Connection con, String email) throws Exception {
		try {
			return cartRepository.isEmailValid(con, email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public void resetPassword(Connection con, String emailId, String resetPass) throws Exception {
		try {
			cartRepository.resetPassword(con, emailId, resetPass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public List<DeliveryTypeVo> getAllDeliveryTpyes(Connection con) throws Exception {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void placeOrder(Connection con, CartDetails cartDetails, HttpSession session) throws Exception {
		try {
			if(UserUtil.isGuestUser(session)){
				Long cartId = cartRepository.persistCart(con, cartDetails);
				cartDetails.setCartId(cartId);
				List<CartItem> cartItems = cartDetails.getCartItems();
				for (CartItem cartItem : cartItems) {
					cartRepository.addCartItem(con, cartDetails, cartItem, false);
				}
				
				session.setAttribute("guestCart", null);
				
			}else{
				cartDetails.setOrderSource(Constants.ORDER_SOURCE_TYPE_WEB);
				cartRepository.placeOrder(con, cartDetails);
				cartRepository.createEmptyCart(con, cartDetails.getCartOwner());
			}
			
			// Insert into order tracking table.
			orderRepository.persistOrderTracking(con, cartDetails.getOrderNumber(), Constants.ORDER_PLACED_BY_CUSTOMER_STATUS, "");
			
			// Insert the notification for CoAdmin.
			//Long coAdminUser = userRepository.getCoAdminUser(con);
			//Long notificationId = userRepository.getNotificationForUser(con, coAdminUser);
			
			String notificationMessage = "Order #orderNumber is successfully placed by customer, click to view the order details.";
			notificationMessage = notificationMessage.replace("orderNumber", cartDetails.getOrderNumber());
			
			//userRepository.addNotification(con, notificationId, notificationMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public CartDetails getOrderById(Connection con, Long cartId) throws Exception {
		try{
			return cartRepository.getOrderForId(con, cartId);
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public CartItem getCartItemForId(Connection con, Long cartItemId, Boolean isRequiredGoodsQuantity)
			throws Exception {
		try{
			return cartRepository.getCartItemForId(con, cartItemId, false);
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void inActiveCartItemById(Connection con, Long cartItemId) throws Exception {
		try{
			
			cartRepository.inActiveCartItemById(con, cartItemId);
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<DeliveryOption> getDeliveryOption(Connection con, String deliveryIds)
			throws Exception {
		try{
			
			return cartRepository.getDeliveryOption(con, deliveryIds);
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Boolean isCartEmpty(Connection con, Long userId) {
		try{
			
			return cartRepository.isCartEmpty(con, userId);
		}catch(Exception e){
			return true;
		}
	}
	
	@Override
	public void refreshCart(Connection con, Long userId){
		try {
			System.out.println("Refresh cart enter-----------------------------");
			con.setAutoCommit(false);
			
			List<CartItem> cartItems = cartRepository.getActiveCartItemsForUser(con, userId);
			
			System.out.println("CartItem Size----"+cartItems.size());
			CartDetails cartDetails = new CartDetails();
			cartDetails.setCartOwner(userId);
			
			BigDecimal subtotalPrice = BigDecimal.ZERO;
			BigDecimal subtotalMsrp = BigDecimal.ZERO;
			for (CartItem cartItem : cartItems) {
				System.out.println("cartItemId::::::::::"+cartItem.getCartItemId());
				GoodsVo goodsVo = goodsRepository.getGoodsForId(con, cartItem.getGoodsId());
				System.out.println("goodsVo.getInStock()----"+goodsVo.getInStock());
				cartDetails.setCartId(cartItem.getCartId());
				
				if(!goodsVo.getInStock()){
					// deactivate the cart item
					cartRepository.deleteCartItemForId(con, cartItem.getCartItemId());
				}else{
					// refresh the price(Update the price of the cart item)
					BigDecimal priceForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getPrice(),
							goodsVo.getQuantity_per_annum(), cartItem.getUom(), cartItem.getQuantity());

					// Actual MSRP for selected item
					BigDecimal msrpForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getMsrp(),
							goodsVo.getQuantity_per_annum(), cartItem.getUom(), cartItem.getQuantity());
					
					subtotalPrice = subtotalPrice.add(priceForQty);
					subtotalMsrp = subtotalMsrp.add(msrpForQty);
					
					// do not update the price for those item which is not changed
					if (priceForQty.compareTo(cartItem.getPrice()) != 0){
						cartItem.setPrice(priceForQty);
						cartItem.setMsrp(msrpForQty);
						
						cartRepository.updateExistingCartItem(con, cartDetails, cartItem, false);
					}
					System.out.println("Update existing cart item:::::::"+cartItem.getCartItemId());
				}
			}
			
			if(!(subtotalMsrp.compareTo(BigDecimal.ZERO)==0) && !(subtotalPrice.compareTo(BigDecimal.ZERO)==0)){
				// update cart also.
				cartDetails.setSubtotalAmount(subtotalPrice);
				cartDetails.setTax(BigDecimal.ZERO);
				cartDetails.setShippingCharge(BigDecimal.ZERO);
				cartDetails.setTotalAmount(subtotalPrice);
				cartDetails.setTotalMsrp(subtotalMsrp);
				
				cartRepository.updateCart(con, cartDetails);
			}
			
			con.commit();
			System.out.println("Refresh cart is done......");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error while refreshing the user cart:::"+ e1.getMessage());
				logger.error("Error while refreshing the user cart:::"+ e1.getMessage());
				e1.printStackTrace();
			}
			System.out.println("Error while refreshing the user cart:::"+ e.getMessage());
			logger.error("Error while refreshing the user cart:::"+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public CartDetails addToCartIndependent(Connection con, Long goodsId, Long quantityId, Long sellerId, BigDecimal rupee,
			Long userId) throws Exception {

		try {
			
			if(userId==null){
				return null;
			}
			
			con.setAutoCommit(false);
			QuantityVo quantityVo = goodsRepository.getQuantityForId(con, quantityId);
			GoodsVo goodsVo = goodsRepository.getGoodsForId(con, goodsId);
			
			// Actual price for selected item
			BigDecimal priceForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getPrice(),
					goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());

			// Actual MSRP for selected item
			BigDecimal msrpForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getMsrp(),
					goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());
			
			// Register User...
			CartDetails cartDetails = cartRepository.getActiveCartForUser(con, userId);

			CartItem newCartItem = new CartItem();
			newCartItem.setCartId(cartDetails.getCartId());
			newCartItem.setGoodsId(goodsId);
			
			if(rupee.equals(BigDecimal.ZERO)){
				newCartItem.setMsrp(msrpForQty);
				newCartItem.setPrice(priceForQty);
				newCartItem.setQuantity(quantityVo.getWeight());
				newCartItem.setUom(quantityVo.getUom());
				
				cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().add(priceForQty));
				cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
				cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().add(msrpForQty));
				
			}else{
				newCartItem.setMsrp(rupee);
				newCartItem.setPrice(rupee);
				newCartItem.setQuantity(rupee.toString());
				newCartItem.setUom("Rs");
				
				cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().add(rupee));
				cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
				cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().add(rupee));
			}

			cartRepository.addCartItem(con, cartDetails, newCartItem, true);
				
			List<CartItem> cartItemList = null;
			if(cartDetails.getCartItems()==null || cartDetails.getCartItems().size()==0){
				cartItemList = new ArrayList<>();
			}else{
				cartItemList = cartDetails.getCartItems();
			}
			cartItemList.add(newCartItem);
			cartDetails.setCartItems(cartItemList);
			
			con.commit();
			return cartDetails;
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public CartDetails updateCartItemForApp(Connection con, Long quantityId, 
			Long userId, Long cartItemId, Boolean isQuantityRupee)
			throws Exception {
		try {
			
			CartItem cartItem = cartRepository.getCartItemForId(con, cartItemId, false);
			CartDetails cartDetails = cartRepository.getActiveCartForId(con, cartItem.getCartId());
		

			BigDecimal priceForQty = null;
			BigDecimal msrpForQty = null;
			BigDecimal oldPrice = null;
			BigDecimal oldMsrp = null;
			
			
			if(!isQuantityRupee){
				QuantityVo quantityVo = goodsRepository.getQuantityForId(con, quantityId);
				// List<CartItem> cartItems = cartDetails.getCartItems();
				GoodsVo goodsVo = goodsRepository.getGoodsForId(con, cartItem.getGoodsId());

				// Actual price for selected item
				priceForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getPrice(),
						goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());

				// Actual MSRP for selected item
				msrpForQty = calculatePriceForQuantity(goodsVo.getUom(), goodsVo.getMsrp(),
						goodsVo.getQuantity_per_annum(), quantityVo.getUom(), quantityVo.getWeight());
				
				cartItem.setQuantity(quantityVo.getWeight());
				cartItem.setUom(quantityVo.getUom());
				
			}else{
				priceForQty = new BigDecimal(quantityId);
				msrpForQty = new BigDecimal(quantityId);
				
				cartItem.setQuantity(quantityId.toString());
				cartItem.setUom("Rs");
			}
			
			oldPrice = cartItem.getPrice();
			oldMsrp = cartItem.getMsrp();

			// Update cart item object with updated values.
			cartItem.setPrice(priceForQty);
			cartItem.setMsrp(msrpForQty);
			cartItem.setSaving(cartItem.getMsrp().subtract(cartItem.getPrice()));
			

			// update cart Details object with updated prices.
			Double subTotalD = cartDetails.getSubtotalAmount().doubleValue();
			Double TotaMsrplD = cartDetails.getTotalMsrp().doubleValue();
			
			Double calculatedPriceD = (priceForQty.doubleValue()) - oldPrice.doubleValue();
			Double calculatedMsrpD = (msrpForQty.doubleValue()) - oldMsrp.doubleValue();
			
			cartDetails.setSubtotalAmount(new BigDecimal(subTotalD + calculatedPriceD));
			cartDetails.setTotalAmount(cartDetails.getSubtotalAmount());
			cartDetails.setTotalMsrp(new BigDecimal(TotaMsrplD + calculatedMsrpD));
			
			cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

			// Update in DB.
			cartRepository.updateExistingCartItem(con, cartDetails, cartItem, true);
			
			return cartRepository.getActiveCartForUser(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	@Override
	public CartDetails deleteCartItemForApp(Connection con, Long cartItemId) throws Exception {
		CartDetails cartDetails = null;
		CartItem cartItem = null;
		try{
			cartItem = cartRepository.getCartItemForId(con, cartItemId, false);
			cartDetails = cartRepository.getActiveCartForId(con, cartItem.getCartId());

			cartRepository.deleteCartItemForId(con, cartItem.getCartItemId());

			cartDetails.setTotalAmount(cartDetails.getTotalAmount().subtract(cartItem.getPrice()));
			cartDetails.setSubtotalAmount(cartDetails.getSubtotalAmount().subtract(cartItem.getPrice()));
			cartDetails.setTotalMsrp(cartDetails.getTotalMsrp().subtract(cartItem.getMsrp()));
			cartDetails.setTotalSaving(cartDetails.getTotalMsrp().subtract(cartDetails.getTotalAmount()));

			cartRepository.updateCart(con, cartDetails);
			
			return getActiveCartForUser(con, cartDetails.getCartOwner());
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	@Override
	public void updateShippingAddress(Connection con, Long addressId, Long cartId) throws Exception {
		try{
			cartRepository.updateShippingAddress(con, addressId, cartId);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception();
		}
	}
}
