<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
	
	<form action="orderproceedbyseller" method="post" id="order-proceed-form">
  		<div class="container well" style="margin-top:20px;margin-bottom:20px;">
  			<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Order Number:</b> ${cartDetails.orderNumber}</p>
	  		</div>
	  		

	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Ordered Date:</b> ${cartDetails.orderDate}</p>
	  		</div>

	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Field Executive:</b> ${feVo.feName}(${feVo.feContact})</p>
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
	  			<c:choose>
					  <c:when test="${cartItem.sellerVo.cartItemStatus eq 3}">
					  	<div class="col-sm-4">
			  				<div class="col-sm-6">
				  				<p style="font-family:verdana;"><b>Action:</b></p>
				  			</div>
				  			<div class="col-sm-6 select-div">
					            <select class="form-control seller-action" name="accept-reject">
					              <option value="select 0">Select</option>
					              <option value="accept ${cartItem.cartItemId}">Accept</option>
					              <option value="reject ${cartItem.cartItemId}">Reject</option>
					            </select>
					        </div><br><br>
		
					        <div class="col-sm-6">
				  				<p style="font-family:verdana;"><b>Reason for Rejection:</b></p>
				  			</div>
				  			<div class="col-sm-6 reject-reason">
					            <select class="form-control reject-select" name="reject-reason" disabled>
					              <option value="select 0">Select</option>
					              <option value="stocknotavailable ${cartItem.cartItemId}">Item not Available</option>
					            </select>
					        </div>
			  			</div>
					  </c:when>
					  <c:when test="${cartItem.sellerVo.cartItemStatus eq 2}">
					  	<p style="font-family:verdana;"><b>Order is already Rejected by you.</b></p>
					  </c:when>
					  <c:when test="${cartItem.sellerVo.cartItemStatus eq 1}">
					  	<p style="font-family:verdana;"><b>Order is already Accepted by you.</b></p>
					  </c:when>
				</c:choose>
	  			
  			</div>
  			<hr style="border:1px solid #999999;">
  		</c:forEach>
  		</div>
		

		<div class="col-sm-4 col-sm-offset-8">
			<p style="font-family:verdana;"><b>Total Amount: Rs. <c:out value="${cartDetails.totalAmount}"></c:out></b></p>
			<button class="btn btn-success order-proceed" style="font-family:verdana;">Proceed</button>
		</div>
		<input type="hidden" value="${cartDetails.cartId}" name="cartId"/>
		</form>
  	
  	<script type="text/javascript">
  		$(".order-proceed").click(function(){
	    	$("#order-proceed-form").submit();	
  		});
  		
  		
  		$("div.select-div").delegate( ".seller-action", "change", function() {
  			var optionSelected = $(this).val();
  			
  			if(optionSelected.indexOf("reject")!= -1){
  				$(this).parent('.select-div').siblings('.reject-reason').children('.reject-select').prop('disabled', false);
  			}else if(optionSelected.indexOf("accept") != -1){
  				$(this).parent('.select-div').siblings('.reject-reason').children('.reject-select').prop('disabled', 'disabled');
  			} else{
  				$(this).parent('.select-div').siblings('.reject-reason').children('.reject-select').prop('disabled', 'disabled');
  			}
  		});
  	
  	</script>