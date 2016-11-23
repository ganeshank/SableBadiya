package com.cigital.integration.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class CartDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long cartId;
	private Long cartStatusId;
	private Long cartOwner;
	private String orderedBy;
	private String orderNumber;
	private Timestamp orderDate;
	private BigDecimal subtotalAmount;
	private BigDecimal tax;
	private BigDecimal shippingCharge;
	private BigDecimal totalAmount;
	private BigDecimal totalMsrp;
	private BigDecimal totalSaving;
	
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	
	private AddressVo shippingAddress;
	private String deliveryOption;
	private String comments;

	private String cartStatusName;
	private List<CartItem> cartItems;
	// this field is used for placeorder page to show users all existing
	// addresses
	private List<AddressVo> addresses;
	private String viewLink;
	
	private Boolean isRecentOrder=false;
	private Boolean isStandardDeliveryEnable;
	private Boolean isDeliveryOptionTomorrow;

	public Long getCartId() {

		return cartId;
	}

	public void setCartId(Long cartId) {

		this.cartId = cartId;
	}

	public Long getCartStatusId() {

		return cartStatusId;
	}

	public void setCartStatusId(Long cartStatusId) {

		this.cartStatusId = cartStatusId;
	}

	public Long getCartOwner() {

		return cartOwner;
	}

	public void setCartOwner(Long cartOwner) {

		this.cartOwner = cartOwner;
	}

	public String getOrderNumber() {

		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {

		this.orderNumber = orderNumber;
	}

	public Timestamp getOrderDate() {

		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {

		this.orderDate = orderDate;
	}

	public BigDecimal getSubtotalAmount() {

		return subtotalAmount;
	}

	public void setSubtotalAmount(BigDecimal subtotalAmount) {

		this.subtotalAmount = subtotalAmount;
	}

	public BigDecimal getTax() {

		return tax;
	}

	public void setTax(BigDecimal tax) {

		this.tax = tax;
	}

	public BigDecimal getShippingCharge() {

		return shippingCharge;
	}

	public void setShippingCharge(BigDecimal shippingCharge) {

		this.shippingCharge = shippingCharge;
	}

	public BigDecimal getTotalAmount() {

		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {

		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalMsrp() {

		return totalMsrp;
	}

	public void setTotalMsrp(BigDecimal totalMsrp) {

		this.totalMsrp = totalMsrp;
	}

	public BigDecimal getTotalSaving() {

		return totalSaving;
	}

	public void setTotalSaving(BigDecimal totalSaving) {

		this.totalSaving = totalSaving;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<CartItem> getCartItems() {

		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {

		this.cartItems = cartItems;
	}

	public List<AddressVo> getAddresses() {

		return addresses;
	}

	public void setAddresses(List<AddressVo> addresses) {

		this.addresses = addresses;
	}

	
	public AddressVo getShippingAddress() {
	
		return shippingAddress;
	}

	
	public void setShippingAddress(AddressVo shippingAddress) {
	
		this.shippingAddress = shippingAddress;
	}

	
	
	public String getDeliveryOption() {
	
		return deliveryOption;
	}

	
	public void setDeliveryOption(String deliveryOption) {
	
		this.deliveryOption = deliveryOption;
	}

	
	public String getComments() {
	
		return comments;
	}

	
	public void setComments(String comments) {
	
		this.comments = comments;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public String getCartStatusName() {
		return cartStatusName;
	}

	public void setCartStatusName(String cartStatusName) {
		this.cartStatusName = cartStatusName;
	}

	public String getViewLink() {
		return viewLink;
	}

	public void setViewLink(String viewLink) {
		this.viewLink = viewLink;
	}

	public Boolean getIsRecentOrder() {
		return isRecentOrder;
	}

	public void setIsRecentOrder(Boolean isRecentOrder) {
		this.isRecentOrder = isRecentOrder;
	}

	public Boolean getIsStandardDeliveryEnable() {
		return isStandardDeliveryEnable;
	}

	public void setIsStandardDeliveryEnable(Boolean isStandardDeliveryEnable) {
		this.isStandardDeliveryEnable = isStandardDeliveryEnable;
	}

	public Boolean getIsDeliveryOptionTomorrow() {
		return isDeliveryOptionTomorrow;
	}

	public void setIsDeliveryOptionTomorrow(Boolean isDeliveryOptionTomorrow) {
		this.isDeliveryOptionTomorrow = isDeliveryOptionTomorrow;
	}

	@Override
	public String toString() {

		return "CartDetails [cartId=" + cartId + ", cartStatusId=" + cartStatusId + ", cartOwner=" + cartOwner
				+ ", orderNumber=" + orderNumber + ", orderDate=" + orderDate + ", subtotalAmount=" + subtotalAmount
				+ ", tax=" + tax + ", shippingCharge=" + shippingCharge + ", totalAmount=" + totalAmount + "]";
	}
}
