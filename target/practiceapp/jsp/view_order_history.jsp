<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
	
	<!-- <form action="orderproceedbyadmin" method="post" id="order-proceed-form"> -->
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
  			<c:choose>
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
				</c:choose>
  			
	  		<div class="col-sm-3">
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

	  		<div class="col-sm-3">
	  			<p style="font-family:verdana;"><b>Delivery Option :</b> <c:out value="${cartDetails.deliveryOption}"></c:out></p>
	  		</div>
	  		
	  		<div class="col-sm-3">
	  			<button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#myModal">Order Tracking</button>
	  			
	  			<!-- Modal -->
				  <div class="modal fade" id="myModal" role="dialog">
				    <div class="modal-dialog modal-lg">
				      <div class="modal-content">
				        <div class="modal-header">
				          <button type="button" class="close" data-dismiss="modal">&times;</button>
				          <h4 class="modal-title">Order Tracking</h4>
				        </div>
				        <div class="modal-body">
				        <table class="table table-striped table-bordered table-hover table-condensed">
					        <thead>
						      <tr>
						        <th>Order Number</th>
						        <th>Order Status</th>
						        <th>Track Date</th>
						      </tr>
						    </thead>
						    <tbody>
					        <c:forEach items="${orderTrackList}" var="orderTrack" varStatus="ot">
					          	<tr>
							        <td>${orderTrack.orderNumber}</td>
							        <td>${orderTrack.orderStatus}</td>
							        <td>${orderTrack.trackDate}</td>
						        </tr>
					        </c:forEach>
						    </tbody>
					  </table>
				        </div>
				        <div class="modal-footer">
				          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				        </div>
				      </div>
				    </div>
				  </div>
				</div>
	  		</div>
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
	  			<p style="display: none; color:red" class="canceled-item">Item is canceled successfully.</p> 
	  			<c:choose>
				  <c:when test="${cartDetails.cartStatusId eq 5}">
				  	<!-- Order dispatched from the seller -->
				  	<button type="button" class="btn btn-warning cancel-order">Courier Returns</button>
				  </c:when>
				  <c:when test="${cartDetails.cartStatusId eq 6}">
				  	<!-- Order dispatched from the seller -->
				  	<button type="button" class="btn btn-warning">Return & Replace</button>&nbsp;&nbsp;
				  	<button type="button" class="btn btn-danger">Return & Refund</button>
				  </c:when>
				  <c:when test="${cartDetails.cartStatusId eq 7}">
				  	Order is already cancelled.
				  </c:when>
				  <c:otherwise>
				  	<button type="button" class="btn btn-primary cancel-order">Cancel Order</button>
				  </c:otherwise>
				</c:choose>
	  			<%-- <div class="col-sm-4">
	  				<div class="col-sm-6">
		  				<p style="font-family:verdana;"><b>Choose Seller:</b></p>
		  			</div>
		  			<div class="col-sm-6">
			            <select class="form-control seller-info" name="sellers">
			              <c:forEach items="${sellerList}" var="seller" varStatus="status">
				              <option value="${seller.sellerId} ${cartItem.cartItemId}">${seller.sellerName}</option>  
			              </c:forEach>
			            </select>
			        </div>
	  			</div> --%>	
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
		
  	<script type="text/javascript">
  		$(".order-proceed").click(function(){
	    	$("#order-proceed-form").submit();	
  		});
  		
  		$(".cancel-order").click(function(){
  			var r = confirm("Are you sure, want to cancel the Item.");
  		    if (r == true) {
  		    	var cartItemId = $(this).parent("div.row").data("item");
  		    	alert(cartItemId);
  		    	var thisPointer = $(this);
  		    	
  		    	$.ajax({
	  		  		url: "adminhome/cancle_order_item",
	  		  		data:{cartItemId:cartItemId},
	  		  		dataType:"text",
	  		  		success: function(response){
	  		  			$(".order-total").text(response);
	  		  			
	  		  			thisPointer.parent("div.row").children(".canceled-item").show();
	  		  			thisPointer.hide();
	  		  		},
	  		  		error: function(response){
	  		  			console.log(response.status);
	  		  			console.log(response);
	  		  		}
  		  		});
  		    	
  		    } else {
  		        txt = "You pressed Cancel!";
  		    }
  		});
  	
  	</script>