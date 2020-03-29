<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>SableBadiya</title>
	<meta charset="utf-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	 <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
  	<!--<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script> -->
  	<link rel="stylesheet" type="text/css" href="css/cart.css">
  	
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container" style="margin-bottom: 358px;">
		<div class="text-center" id="empty-cart-div">
  			<p>There are no items in your cart.</p>
  			<a href="homepage" class="btn btn-primary btn-md">CLICK HERE FOR SHOPPING</a>
  		</div>
  		<div class="col-sm-8 table-responsive table-div">
  		<table class="table table-striped table-bordered" id="table-item">
  			<tbody>
  				<tr>
	  				<th>ITEM</th>
	  				<th>QTY</th>
	  				<th>PRICE</th>
	  				<!-- <th>DELIVERY DETAILS</th> -->
	  				<th>SUBTOTAL</th>
  				</tr>
  				<c:forEach items="${cartDetails.cartItems}" var="cartItem" varStatus="status">
  					<tr data-item="${cartItem.cartItemId}">
  					<td>
  						<img src="${cartItem.goodsVo.webpath}" class="img-responsive" style="width:145px;"><br/>
  						<center><strong>${cartItem.goodsVo.goodsName}</strong></center>
  					</td>
  					<td>
  					  <select class="form-control qty-change" data-itemid="${cartItem.cartItemId}">
  					    <c:choose>
  					    	<c:when test="${cartItem.uom== 'Rs'}">
  					    		<c:choose>
  					    			<c:when test="${cartItem.quantity == 5}">
  					    				<option value="5 Rs" selected>5 Rs</option>
  					    				<option value="10 Rs">10 Rs</option>
  					    				<option value="15 Rs">15 Rs</option>
  					    				<option value="20 Rs">20 Rs</option>
  					    				<option value="25 Rs">25 Rs</option>
  					    				<option value="30 Rs">30 Rs</option>
  					    			</c:when>
  					    			<c:when test="${cartItem.quantity == 10}">
  					    				<option value="5 Rs">5 Rs</option>
  					    				<option value="10 Rs" selected>10 Rs</option>
  					    				<option value="15 Rs">15 Rs</option>
  					    				<option value="20 Rs">20 Rs</option>
  					    				<option value="25 Rs">25 Rs</option>
  					    				<option value="30 Rs">30 Rs</option>
  					    			</c:when>
  					    			<c:when test="${cartItem.quantity == 15}">
  					    				<option value="5 Rs">5 Rs</option>
  					    				<option value="10 Rs">10 Rs</option>
  					    				<option value="15 Rs" selected>15 Rs</option>
  					    				<option value="20 Rs">20 Rs</option>
  					    				<option value="25 Rs">25 Rs</option>
  					    				<option value="30 Rs">30 Rs</option>
  					    			</c:when>
  					    			<c:when test="${cartItem.quantity == 20}">
  					    				<option value="5 Rs">5 Rs</option>
  					    				<option value="10 Rs">10 Rs</option>
  					    				<option value="15 Rs">15 Rs</option>
  					    				<option value="20 Rs" selected>20 Rs</option>
  					    				<option value="25 Rs">25 Rs</option>
  					    				<option value="30 Rs">30 Rs</option>
  					    			</c:when>
  					    			<c:when test="${cartItem.quantity == 25}">
  					    				<option value="5 Rs">5 Rs</option>
  					    				<option value="10 Rs">10 Rs</option>
  					    				<option value="15 Rs">15 Rs</option>
  					    				<option value="20 Rs">20 Rs</option>
  					    				<option value="25 Rs" selected>25 Rs</option>
  					    				<option value="30 Rs">30 Rs</option>
  					    			</c:when>
  					    			<c:when test="${cartItem.quantity == 30}">
  					    				<option value="5 Rs">5 Rs</option>
  					    				<option value="10 Rs">10 Rs</option>
  					    				<option value="15 Rs">15 Rs</option>
  					    				<option value="20 Rs">20 Rs</option>
  					    				<option value="25 Rs">25 Rs</option>
  					    				<option value="30 Rs" selected>30 Rs</option>
  					    			</c:when>
  					    		</c:choose>
  					    	</c:when>
  					    	<c:otherwise>
  					    		<c:forEach items="${cartItem.quantityVos}" var="quantity">
		  							<c:choose>
									  <c:when test="${(quantity.weight eq cartItem.quantity) && (quantity.uom eq cartItem.uom)}">
									    <option value="${quantity.quantityId}" selected>${quantity.weight} - ${quantity.uom}</option>
									  </c:when>
									  <c:otherwise>
									  	<option value="${quantity.quantityId}">${quantity.weight} - ${quantity.uom}</option>
									  </c:otherwise>
									</c:choose>
		  						</c:forEach>
  					    	</c:otherwise>
  					    </c:choose>
		              </select>
		            </td>
  					<%-- <td><input type="number" style="width:40px;" value="${cartItem.quantity}" min="1"> ${cartItem.uom}</td> --%>
  					<td><span class="price-span">Rs.${cartItem.price}</span>
  						<br>Market Price: Rs.<span class="msrp-span">${cartItem.msrp}</span>
  						<br>Savings: Rs.<span class="save-span">${cartItem.saving}</span>
  					</td>
  					<!-- <td><br>Delivery in 1 hour.</td> -->
  					<td>Rs.<span class="price-span">${cartItem.price}</span> <a href="javascript:void(0);" data-itemid="${cartItem.cartItemId}" class="delete-item"><span class="glyphicon glyphicon-remove" style="margin-left:20px;"></span></a></td>
  				</tr>
  				</c:forEach>

  			</tbody>
  		</table>
  		</div>



  		<div class="col-sm-4" id="totalamountdiv">
	  		<h1 class="text-center">Total Amount : Rs.<span class="total-amount">${cartDetails.totalAmount}</span></h1>
	  		<p class="text-center"><del>Market Price</del>: <del>Rs.<span class="total-msrp">${cartDetails.totalMsrp}</span></del></p> 
	  		<p class="text-center">You saved: Rs.<span class="total-saving">${cartDetails.totalSaving}</span></p>
	  		<p><a href="homepage" class="btn btn-info btn-block">CONTINUE SHOPPING</a></p>
	  		<p><a href="javascript:void(0);" class="btn btn-success btn-block place-order">PLACE ORDER</a></p>
	  		<p><a href="javascript:void(0);" data-cartid="${cartDetails.cartId}" class="btn btn-warning btn-block empty-cart">EMPTY CART</a></p>
  		</div>
  	</div>
  	<jsp:include page="footer.jsp"></jsp:include>
  	<script src="js/header.js"></script>
  	<script src="js/main.js"></script>
  	<script type="text/javascript">
	  	$( document ).ready(function() {
	  		totalAmount = $(".total-amount").text();
	  		if(totalAmount==0){
	  			$(".table-div").hide();
	  			$("#totalamountdiv").hide();
	  			$("#empty-cart-div").show();
	  			$( ".place-order" ).addClass( "disabled" );
	  			$( ".empty-cart" ).addClass( "disabled" );
	  		}
	  		else{
	  			$(".table").show();
	  			$("#totalamountdiv").show();
	  			$("#empty-cart-div").hide();
	  			$( ".place-order" ).removeClass( "disabled" );
	  			$( ".empty-cart" ).removeClass( "disabled" );
	  		}  
	  	});
	  	
  	</script>
</body>
</html>