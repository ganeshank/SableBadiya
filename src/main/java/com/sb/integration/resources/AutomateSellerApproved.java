package com.sb.integration.resources;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.sb.integration.service.OrderService;
import com.sb.integration.service.SellerService;
import com.sb.integration.service.impl.OrderServiceImpl;
import com.sb.integration.service.impl.SellerServiceImpl;
import com.sb.integration.util.Constants;

public class AutomateSellerApproved {
	public static void getAutomateSellerApproval(Connection con, Long cartId, Integer fieldExecutiveId, Map<Long, Long> sellerCartItemMap)throws Exception{
		try{
			/**
			 * Order Processed by ADMIN.
			 */
			OrderService orderService = new OrderServiceImpl();
			orderService.updateOrderStatus(con, Constants.ORDER_PROCESSED_BY_COADMIN, cartId, "");
			
			SellerService sellerService = new SellerServiceImpl();
			//sellerService.persistOrderForFe(con, cartId, fieldExecutiveId);
			
			sellerService.persistSellerWithCartItem(con, sellerCartItemMap);
			
			/**
			 * Order Processed by SELLER.
			 */
			Map<Long, String> acceptRejectCartItemMap = new HashMap<>();
			Map<Long, String> rejectReasonCartItemMap = new HashMap<>();
			
			for (Map.Entry<Long, Long> entry : sellerCartItemMap.entrySet()) {
				acceptRejectCartItemMap.put(entry.getKey(), "accept");
			}
			
			sellerService.updateSellerCartItem(con, cartId, 1, acceptRejectCartItemMap, rejectReasonCartItemMap);
			
			Integer status = sellerService.getOrderStatusBasedOnSellerCartItem(con, cartId);
			// update the status of the order(CART).
			/*Long updatedStatus = null;
			if(status==1){
				updatedStatus = Constants.ORDER_PENDING_BY_SELLER;
			}else if(status.equals(2)){
				updatedStatus = Constants.ORDER_CONFIRMED_BY_SELLER;
			}else{
				updatedStatus = Constants.ORDER_REJECTED_BY_SELLER;
			}*/
			
			Long updatedStatus = Constants.ORDER_CONFIRMED_BY_SELLER;
			orderService.updateOrderStatus(con, updatedStatus, cartId,"");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
