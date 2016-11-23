
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
	
	<form action="orderproceedbyadmin" method="post" id="order-proceed-form">
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
  			<div class="col-sm-3">
  				<c:choose>
  					<c:when test="${empty selectedFe}">
  						<div class="col-sm-4">
			  				<p style="font-family:verdana;"><b>Choose Field Executive:</b></p>
			  			</div>
			  			<div class="col-sm-4" id="fe-select-div">
				            <select class="form-control" id="fe" name="fieldEx">
				                  <c:forEach items="${feList}" var="fe" varStatus="status">
						              <option value="${fe.fieldExecutiveId}">${fe.feName}</option>  
					              </c:forEach>
				            </select>
				        </div>
			  			<div class="col-sm-4" id="assign-fe-div">
			  				<button class="btn btn-sm btn-success assign-fe" data-cartid="${cartDetails.cartId}" style="font-family:verdana;">Assign FE</button>
			  			</div>
  					</c:when>
  					<c:otherwise>
  						<div class="col-sm-6">
			  				<p style="font-family:verdana;"><b>Field Executive: ${selectedFe.feName}</b></p>
			  			</div>
  					</c:otherwise>
  				</c:choose>
	  		</div>
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
	  			<p style="font-family:verdana;"><b>User Comment :</b> <c:out value="${cartDetails.comments}"></c:out></p>
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
	  				<p style="font-family:verdana;"><b>Seller: SB Retail</b></p>
	  				<%-- <div class="col-sm-6">
		  				<p style="font-family:verdana;"><b>Choose Seller:</b></p>
		  			</div>
		  			<div class="col-sm-6">
			            <select class="form-control seller-info" name="sellers">
			              <c:forEach items="${sellerList}" var="seller" varStatus="status">
				              <option value="${seller.sellerId} ${cartItem.cartItemId}">${seller.sellerName}</option>  
			              </c:forEach>
			            </select>
			        </div> --%>
	  			</div>
  			</div>
  			<hr style="border:1px solid #999999;">
  		</c:forEach>
  		</div>
		

		<div class="col-sm-4 col-sm-offset-8">
			<p style="font-family:verdana;"><b>Subtotal Amount:</b> Rs. <c:out value="${cartDetails.subtotalAmount}"></c:out></p>
			<p style="font-family:verdana;"><b>Shipping Charge:</b> Rs. <c:out value="${cartDetails.shippingCharge}"></c:out></p>
			<p style="font-family:verdana;"><b>Total Amount: Rs. <c:out value="${cartDetails.totalAmount}"></c:out></b></p>
			<!-- <button class="btn btn-success order-proceed" style="font-family:verdana;">Proceed</button> -->
		</div>
		<input type="hidden" value="${cartDetails.cartId}" name="cartId"/>
		</form>
		
  	<script type="text/javascript">
  		/* $(".order-proceed").click(function(){
	    	$("#order-proceed-form").submit();	
  		}); */
  		
  		$(".assign-fe").click(function(e){
  			e.preventDefault();
  			var feId = $("#fe").val();
  			var feName = $("#fe option:selected").text();
  			var cartId = $(this).data("cartid");
  			
  			startLoading();
  			console.log(feId+".."+feName+".."+cartId)
  			
  			$.ajax({
  				url: "adminhome/assign_fe",
  				data:{feId:feId, cartId:cartId},
  				dataType:"text",
  				success: function(response){
  					console.log(response);
  					$("#assign-fe-div").html("Assigned");
  					$("#fe-select-div").html(feName);
  					stopLoading();
  				},
  				error: function(response){
  					stopLoading();
  					alert("Something went wrong, Please contact to technical specialist.");
  					console.log(response.status);
  					console.log(response);
  				}
  				
  			});
  		});
  	
  	</script>