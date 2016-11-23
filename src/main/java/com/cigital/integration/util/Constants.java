package com.cigital.integration.util;


public class Constants {
	public final static Integer ACTIVE_CART_STATUS_ID = 1;
	public final static Integer RESPONSE_OK = 200;
	public final static Integer RESPONSE_FORBIDDEN = 403;
	
	public final static Integer ADMIN_USER_ROLE_ID = 1;
	public final static Integer SELLER_USER_ROLE_ID = 2;
	public final static Integer REG_USER_ROLE_ID = 3;
	public final static Integer COADMIN_USER_ROLE_ID = 4;
	public final static Integer SALES_USER_ROLE_ID = 5;
	public final static Integer GUEST_USER_ROLE_ID = 6;
	
	public final static Long ORDER_PLACED_BY_CUSTOMER_STATUS = 2l;
	public final static Long ORDER_PROCESSED_BY_COADMIN= 3l;
	public final static Long ORDER_PENDING_BY_SELLER = 10l;
	public final static Long ORDER_CONFIRMED_BY_SELLER = 11l;
	public final static Long ORDER_REJECTED_BY_SELLER = 12l;
	public final static Long SELLER_IS_CHANGED_FOR_ORDER_ITEM = 13l;
	public final static Long ORDER_ITEM_IS_CANCEL = 14l;
	public final static Long ORDER_IS_CANCELED_AT_DELIVERY_TIME = 7l;
	public final static Long ORDER_IS_CANCELED_BY_CUSTOMER = 8l;
	public final static Long ORDER_ITEM_CANCELED = 14l;
	
	
	public final static Integer SELLER_CART_ITEM_STATUS_WAIT = 3;
}
