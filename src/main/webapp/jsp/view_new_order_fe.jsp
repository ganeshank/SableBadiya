
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
	
	<!-- <form action="orderproceedbyadmin" method="post" id="order-proceed-form"> -->
  		<div class="container" style="margin-top:20px;">
  			<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Order Number:</b> <c:out value="${cartDetails.orderNumber}"></c:out></p>
	  		</div>
	  		<%-- <div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Ordered By :</b> ${cartDetails.orderedBy}</p>
	  		</div> --%>

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
  			<%-- <c:choose>
				  <c:when test="${(cartDetails.cartStatusId ne 2)}">
				  	<div class="col-sm-3">
			  			<div class="col-sm-6">
			  				<p style="font-family:verdana;"><b>Delivery Boy:</b></p>
			  			</div>
			  			<div class="col-sm-6">
				            <c:out value="${fe.feName}"></c:out>
				        </div>
	  				</div>
				  </c:when>
				</c:choose> --%>
  			
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
	  		
	  		<c:choose>
			  <c:when test="${(cartDetails.cartStatusId ne 11)}">
			  	<div class="col-sm-4">
		  			<button class="btn btn-info btn-md cancel-click" value="order-${cartDetails.cartId}">Cancel Order</button>
		  		</div>
			  </c:when>
			</c:choose>
	  		
  		</div>

  		<div class="container">
  		<c:forEach items="${cartDetails.cartItems}" var="cartItem" varStatus="status">
  			<div class="row" data-item="${cartItem.cartItemId}">
	  			<div class="col-sm-4">
	  				<img src="${cartItem.goodsVo.webpath}" class="img-responsive" style="width:145px;">
	  			</div>
	  			<div class="col-sm-4">
	  				<p style="font-family:verdana;"><b>${cartItem.goodsVo.goodsName}</b></p>
	  				<p style="font-family:verdana;"><b>Qty:</b> ${cartItem.quantity} ${cartItem.uom}</p>
	  				<p style="font-family:verdana;"><b>Price:</b> Rs.${cartItem.price}</p>
	  			</div>
	  			<div class="col-sm-4">
		  			Seller Name: ${cartItem.sellerVo.sellerName}<br/>
		  			<c:choose>
					  <c:when test="${(cartDetails.cartStatusId ne 11)}">
			  				<button class="btn btn-info btn-md cancel-click" value="item-${cartItem.cartItemId}">Cancel Item</button>
					  </c:when>
					</c:choose>
				</div>
	  			
  			</div>
  			<hr style="border:1px solid #999999;">
  		</c:forEach>
  		</div>
		

		<div class="col-sm-4 col-sm-offset-8">
			<p style="font-family:verdana;"><b>Subtotal Amount:</b> Rs. <span class="order-total"><c:out value="${cartDetails.subtotalAmount}"></c:out></span></p>
			<p style="font-family:verdana;"><b>Shipping Charge:</b> Rs. <c:out value="${cartDetails.shippingCharge}"></c:out></p>
			<p style="font-family:verdana;"><b>Total Amount: Rs. <span class="order-total"><c:out value="${cartDetails.totalAmount}"></c:out></span></b></p>
			<!-- <button class="btn btn-success order-proceed" style="font-family:verdana;">Proceed</button> -->
		</div>
		<input type="hidden" value="${cartDetails.cartId}" name="cartId"/>
		<!-- </form> -->
		
		<button type="button" class="btn btn-info btn-sm order-update-button" data-toggle="modal" data-target="#myModal" style="display: none;"></button>
	  			
	  			<!-- Modal -->
				  <div class="modal fade" id="myModal" role="dialog" style="display: none;">
				    <div class="modal-dialog modal-lg">
				      <div class="modal-content">
				        <div class="modal-header">
				          <button type="button" class="close" data-dismiss="modal">&times;</button>
				          <h4 class="modal-title">Order Update</h4>
				        </div>
				        <div class="modal-body">
				        	
				        </div>
				        <div class="modal-footer">
				          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				        </div>
				      </div>
				    </div>
				  </div>
		
  	<script type="text/javascript">
  		$(".order-proceed").click(function(){
	    	$("#order-proceed-form").submit();	
  		});
  		
  		$(".cancel-click").click(function(){
  			var r = confirm("Are you sure, want to cancel?");
  		    if (r == true) {
  		    	var clickId = $(this).val();
  		    	startLoading();
  		    	
  		    	$.ajax({
	  		  		url: "fehome/cancle_order_or_item",
	  		  		data:{clickId:clickId},
	  		  		dataType:"text",
	  		  		success: function(response){
	  		  			stopLoading();
	  		  			alert("Canceled successfully!!!");
	  		  		    loadContainer("fehome/new_orders_for_fe");
	  		  		},
	  		  		error: function(response){
	  		  			stopLoading();
	  		  			alert("Something went wrong, Please contact to technical specialist.");
	  		  			console.log(response.status);
	  		  			console.log(response);
	  		  		}
  		  		});
  		    	
  		    } else {
  		        txt = "You pressed Cancel!";
  		    }
  		});
  	
  	</script>