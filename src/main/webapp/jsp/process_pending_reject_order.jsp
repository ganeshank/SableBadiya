<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
	
	<form id="order-proceed-form" action="processpendingrejectorder" method="post">
  		<div class="container" style="margin-top:20px;">
  			<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Order Number:</b> <c:out value="${cartDetails.orderNumber}"></c:out></p>
	  		</div>
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Ordered By :</b> ${cartDetails.orderedBy}</p>
	  		</div>

	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Ordered Date:</b> <c:out value="${cartDetails.orderDate}"></c:out></p>
	  		</div>
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Contact Name :</b> <c:out value="${cartDetails.shippingAddress.contactName}"></c:out></p>
	  		</div>

	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Contact Email:</b> <c:out value="${cartDetails.shippingAddress.email}"></c:out></p>
	  		</div>
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Contact Number :</b> <c:out value="${cartDetails.shippingAddress.contactNumber}"></c:out></p>
	  		</div>
  		</div>
  		

  		<div class="container well" style="margin-top:0px;padding-bottom:0px;">
  			<div class="col-sm-4">
	  			<div class="col-sm-6">
	  				<p style="font-family:verdana;"><b>Choose Delivery Boy:</b></p>
	  			</div>
	  			<div class="col-sm-6">
		            <select class="form-control" id="fe" name="fieldEx">
		                  <c:forEach items="${feList}" var="fe" varStatus="status">
				              <option value="${fe.fieldExecutiveId}">${fe.feName}</option>  
			              </c:forEach>
		            </select>
		        </div>
	  			
	  		</div>
	  		<div class="col-sm-4">
	  			<div class="col-sm-6">
	  				<p style="font-family:verdana;"><b>Delivery Address:</b></p>
	  			</div>
	  			<div class="col-sm-6">
	  				<address>
	  					<c:out value="${cartDetails.shippingAddress.addressLine1}"></c:out><br>
			            <c:out value="${cartDetails.shippingAddress.addressLine2}"></c:out><br>
			            District - <c:out value="${cartDetails.shippingAddress.city}"></c:out>,<c:out value="${cartDetails.shippingAddress.state}"></c:out>.
	  				</address>
		            

		        </div>
	  			
	  		</div>

	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Delivery Option :</b> <c:out value="${cartDetails.deliveryOption}"></c:out></p>
	  		</div>
  		</div>

  		<div class="container">
  		<c:forEach items="${cartDetails.cartItems}" var="cartItem" varStatus="status">
  			<div class="row">
	  			<div class="col-sm-4">
	  				<img src="${cartItem.goodsVo.webpath}" class="img-responsive" style="width:145px;">
	  			</div>
	  			<div class="col-sm-4">
	  				<p style="font-family:verdana;"><b>${cartItem.goodsVo.goodsName}</b></p>
	  				<p style="font-family:verdana;"><b>Qty:</b> ${cartItem.quantity} ${cartItem.uom}</p>
	  				<p style="font-family:verdana;"><b>Price:</b> Rs.${cartItem.price}</p>
	  			</div>
	  			<div class="col-sm-4">
	  				<c:choose>
					  <c:when test="${cartItem.sellerVo.cartItemStatus eq 2}">
					  	<!-- Order dispatched from the seller -->
					  	<p style="font-family:verdana;"><b>Current Status: Rejected by ${cartItem.sellerVo.sellerName}</b></p>
					  	<div class="col-sm-6">
			  				<p style="font-family:verdana;"><b>Choose New Seller:</b></p>
			  			</div>
			  			<div class="col-sm-6">
				            <select class="form-control seller-info-reject" name="sellers-for-reject">
				              <option value="select">Select Seller</option>
				              <c:forEach items="${sellerList}" var="seller" varStatus="status">
				              	<c:choose>
					  				<c:when test="${seller.sellerId ne cartItem.sellerVo.sellerId}">
					  					<option value="${seller.sellerId} ${cartItem.cartItemId}">${seller.sellerName}</option>  
					  				</c:when>
					  			</c:choose>
					              
				              </c:forEach>
				            </select>
				            <p class="message-class" style="color:red; display: none;">Please select the seller</p>
				        </div>
					  </c:when>
					  <c:when test="${cartItem.sellerVo.cartItemStatus eq 1}">
					  	<p style="font-family:verdana;"><b>Current Status: Approved by ${cartItem.sellerVo.sellerName}</b></p>
					  </c:when>
					  <c:otherwise>  
					    <div>
							<p style="font-family:verdana;"><b>Current Status: Pending by ${cartItem.sellerVo.sellerName}</b></p>
						  	<p><input type="checkbox" name="seller-reassign" class="cancel-seller" value="reassign"> Cancel & Reassign to another seller.</p>
						  	<select class="form-control seller-info-pending" name="sellers-for-pending" style="display: none;">
						  		  <option value="select">Select Seller</option>
					              <c:forEach items="${sellerList}" var="seller" varStatus="status">
					              	<c:choose>
						  				<c:when test="${seller.sellerId ne cartItem.sellerVo.sellerId}">
						  					<option value="${seller.sellerId} ${cartItem.cartItemId}">${seller.sellerName}</option>  
						  				</c:when>
						  			</c:choose>
						              
					              </c:forEach>
				            </select>
				            <p class="message-class" style="color:red; display: none;">Please select the seller</p>					    
					    </div>
					  </c:otherwise>
					</c:choose>
	  			
	  			</div>
  			</div>
  			<hr style="border:1px solid #999999;">
  		</c:forEach>
  		</div>
		

		<div class="col-sm-4 col-sm-offset-8">
			<p style="font-family:verdana;"><b>Subtotal Amount:</b> Rs. <c:out value="${cartDetails.subtotalAmount}"></c:out></p>
			<p style="font-family:verdana;"><b>Shipping Charge:</b> Rs. <c:out value="${cartDetails.shippingCharge}"></c:out></p>
			<p style="font-family:verdana;"><b>Total Amount: Rs. <c:out value="${cartDetails.totalAmount}"></c:out></b></p>
			<button class="btn btn-success order-proceed" style="font-family:verdana;">Proceed</button>
		</div>
		<input type="hidden" value="${cartDetails.cartId}" name="cartId"/>
		</form>
		
  	<script type="text/javascript">
  		$(".order-proceed").click(function(){
	    	//	
	    	
	    	if(validateSellerPendingItems() && validateSellerRejectItems()){
	    		$("#order-proceed-form").submit();
	    	}else{
	    		return false;	
	    	}
	    	
  		});
  		
  		$(".cancel-seller").change(function(){
  			console.log($(this).parent().siblings("select").show())
  			if($(this).is(':checked'))
  				$(this).parent().siblings("select").show();  // checked
  			else
  				$(this).parent().siblings("select").hide();
  		});
  		
  		function validateSellerPendingItems(){
  			var validateSuccess = true;
  			$(".seller-info-pending").each(function() {
	    		var isChecked = $(this).parent("div").children("p").find(".cancel-seller").prop("checked");
	    		if(isChecked == true && $(this).val()=="select"){
	    			$(this).parent("div").find(".message-class").show();
	    			validateSuccess = false;
	    			return false;
	    		}else{
	    			$(this).parent("div").find(".message-class").hide();
	    		}
			});
  			
  			return validateSuccess;
  		}
  		
  		function validateSellerRejectItems(){
  			var validateSuccess = true;
  			$(".seller-info-reject").each(function() {
	    		if($(this).val()=="select"){
	    			$(this).parent("div").find(".message-class").show();
	    			validateSuccess = false;
	    			return false;
	    		}else{
	    			$(this).parent("div").find(".message-class").hide();
	    		}
			});
  			
  			return validateSuccess;
  		}
  	
  	</script>