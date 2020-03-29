<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	  	<title>SableBadiya</title>
	  	<meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
	   <!--  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
       -->
      
	    <link rel="stylesheet" type="text/css" href="css/checkout.css">
  </head>
<body>
<jsp:include page="header.jsp"></jsp:include><br/><br/>
	<div class="container" id="panelcontainer" style="margin-bottom: 97px;">
  
  
  <div class="panel-group" id="accordion">
    <div class="panel panel-default">
      <div class="panel-heading">
        <a data-toggle="collapse" style="cursor:auto" id="delivery-address" data-parent="#accordion" href="#collapse1">
          <h4 class="panel-title">
           Delivery Address
          </h4>
        </a>
      </div>

      
      <div id="collapse1" class="panel-collapse collapse in">
        <div class="panel-body">
        <c:choose>
		  <c:when test="${empty cartForOrder.addresses}">
		    <span>No Address found</span>
		  </c:when>
		  <c:otherwise>
		  	<c:forEach items="${cartForOrder.addresses}" var="address" varStatus="status">
	        	<div class="col-sm-3" id="borderforaddressbox">
		            <h4>${address.contactName}</h4>
		            <hr>
		            <address>${address.addressLine1}<br>
		            ${address.pinCode}, ${address.state}, ${address.city}.
		            </address>
		            <hr>
		            <p>${address.contactNumber}</p>
		            <hr>
		            <p data-addressid="${address.addressId}"><a href="javascript:void(0);" class="btn btn-info col-sm-5 edit-click">EDIT <span class="glyphicon glyphicon-edit"></span></a> 
		            <a href="javascript:void(0);" class="btn btn-danger col-sm-5 col-sm-offset-2 delete-address">DELETE <span class="glyphicon glyphicon-trash"></span></a></p><br><br>
		            <p data-addressid="${address.addressId}"><a href="javascript:void(0);" class="btn btn-success btn-block deliver-here">DELIVER HERE</a></p>
	            </div>
        	</c:forEach>
		  </c:otherwise>
		</c:choose>
        
        </div>
        <div class="panel-footer">
            <p class="text-center"><a href="newaddress?order=true" class="btn btn-warning"><span class="glyphicon glyphicon-plus-sign"></span> ADD NEW ADDRESS</a></p>
        </div>
      </div>
    </div>
    <div class="panel panel-default" id="order-summary-div">
      <div class="panel-heading">
        <a data-toggle="collapse" id="order-summary" data-parent="#accordion" href="#collapse2">
        <h4 class="panel-title">
          Order Summary
        </h4>
        </a>
      </div>
      <div id="collapse2" class="panel-collapse collapse">
        <div class="panel-body">
          <table class="table table-striped table-bordered">
            <tbody>
              <tr>
                <th>ITEM</th>
                <th>QTY</th>
                <th>PRICE</th>
                <th>SUBTOTAL</th>
              </tr>
              <c:forEach items="${cartForOrder.cartItems}" var="cartItem" varStatus="status">
              <tr data-item="${cartItem.cartItemId}">
  				<td><img src="${cartItem.goodsVo.webpath}" class="img-responsive" style="width:145px;"></td>
                <td><input type="text" style="width:50px;" value="${cartItem.quantity}" readonly="readonly"> ${cartItem.uom}</td>
                <td><span class="price-span">Rs.${cartItem.price}</span>
  						<br>Market Price: Rs.<span class="msrp-span">${cartItem.msrp}</span>
  						<br>Savings: Rs.<span class="save-span">${cartItem.saving}</span>
  					</td>
               
                <td>Rs.${cartItem.price}</td>
              </tr>
              </c:forEach>
            </tbody>
          </table>
          <button type="button" class="btn btn-warning proceed-order-summary" style="float:right;">PROCEED</button>
        </div>
      </div>
    </div>
    
    <div class="panel panel-default">
      <div class="panel-heading">
        <a data-toggle="collapse" id="delivery-option" data-parent="#accordion" href="#collapse3">
        <h4 class="panel-title">
          Delivery Options
        </h4>
        </a>
      </div>
      <div id="collapse3" class="panel-collapse collapse">
        <div class="panel-body">
          
            <form role="form">
            	<c:choose>
            		<c:when test="${cartForOrder.subtotalAmount < 150}">
            			<div class="col-sm-12">
			              <p style="color: red;">*Free Standard Delivery if total order amount is Rs. 150 or above</p>
			            </div>
            		</c:when>
            	</c:choose>
              <!-- <div class="col-sm-4">
                <div class="radio">
                  <label><input type="radio" id="exp-delivery" name="optradio" value="Deliver in 1 Hour">Deliver in 1 Hour</label>
                   <p style="color: red;display: none;" id="standard-del-msg">*This option is applicable from 8AM to 8PM.</p>
                </div>
              </div> -->
              <div class="col-sm-4">
                <div class="radio">
                 

                </div>
                <div class="form-group">
                    <label>Select Delivery option</label>
                    <%-- <select class="form-control" id="sel1">
	                    <c:forEach items="${cartForOrder.deliveryOptions}" var="deliver" varStatus="status">
	                    	<option value="${deliver.deliveryId}">${deliver.name}</option>
	                    </c:forEach>
                    </select> --%>
                 
                    
                    <!-- Modal -->
					  <select class="form-control" id="sel1">
						 <c:forEach var="type" items="${cartForOrder.deliverySlotMap}">
						 	<optgroup label="${type.key}">
						    <option value="${type.key}">${type.value}</option>
						  </optgroup>
						</c:forEach>
					</select>
                </div>
              </div>
              
            </form> 

            <div class="row">
              <div class="col-sm-8">
                <form role="form">
                  <div class="form-group">
                    <label for="comment">Comment:</label>
                    <textarea class="form-control" maxlength="200" rows="6" id="comment" placeholder="Enter comments (for e.g. I want my order after 6 PM.)"></textarea>
                  </div>
                </form>
              </div>
              <div class="col-sm-4" style="margin-top:30px;">
                
                  <div class="col-sm-6">
                    <p class="priceof">Sub Total:</p>
                  </div>
                  <div class="col-sm-6">
                    <p class="priceof">Rs.${cartForOrder.subtotalAmount}</p>
                  </div>
                  <div class="col-sm-6">
                    <p class="priceof">Delivery charges:</p>
                  </div>
                  <div class="col-sm-6">
                  	<c:choose>
	            		<c:when test="${cartForOrder.subtotalAmount < 150}">
							<p class="priceof">Rs.30</p>	            		
	            		</c:when>
	            		<c:otherwise>
	            			<p class="priceof">Rs.${cartForOrder.shippingCharge}</p>
	            		</c:otherwise>
            		</c:choose>
                  </div>
                  <div class="col-sm-6">
                    <p><strong>Total Amount:</strong></p>
                  </div>
                  <div class="col-sm-6">
                    <c:choose>
	            		<c:when test="${cartForOrder.subtotalAmount < 150}">
							<p class="priceof">Rs.${cartForOrder.subtotalAmount+30}</p>	            		
	            		</c:when>
	            		<c:otherwise>
	            			<p class="priceof"><strong>Rs.${cartForOrder.totalAmount}</strong></p>
	            		</c:otherwise>
            		</c:choose>
                  </div>
                
                <button type="button" class="btn btn-warning btn-block proceed-delivery-option" >SAVE AND CONTINUE</button>
              </div>
            </div> 
          
          
        </div>
      </div>
    </div>
    
    
    
    <div class="panel panel-default">
      <div class="panel-heading">
        <a data-toggle="collapse" id="payment-method" data-parent="#accordion" href="#collapse4">
        <h4 class="panel-title">
          Payment Method
        </h4>
        </a>
      </div>
      <div id="collapse4" class="panel-collapse collapse">
        <div class="panel-body">
          <p class="text-center">Pay by using Cash-on-Delivery</p>
          <c:choose>
           		<c:when test="${cartForOrder.subtotalAmount < 150}">
           			<c:choose>
           				<c:when test="${cartForOrder.isUserWalletMoneyAvailable}">
           					<c:choose>
           						<c:when test="${cartForOrder.remainingWalletAmount < cartForOrder.offerPrice }">
           							Used your wallet amount:<input type="checkbox" name="walletAmount" value="${cartForOrder.remainingWalletAmount}" checked disabled> ${cartForOrder.remainingWalletAmount}<br/>
           							<h3 class="text-center">Amount Payable : Rs.${cartForOrder.totalAmount+30-cartForOrder.remainingWalletAmount}</h3>
           						</c:when>
           						<c:otherwise>
           							Used your wallet amount:<input type="checkbox" name="walletAmount" value="${cartForOrder.offerPrice}" checked disabled> ${cartForOrder.offerPrice}<br/>
           							<h3 class="text-center">Amount Payable : Rs.${cartForOrder.totalAmount+30-cartForOrder.offerPrice}</h3>
           						</c:otherwise>
           					</c:choose>
           					
           				</c:when>
           				<c:otherwise>
           					<h3 class="text-center">Amount Payable : Rs.${cartForOrder.totalAmount+30}</h3>
           				</c:otherwise>
           			</c:choose>
           		</c:when>
           		<c:otherwise>
           			<c:choose>
           				<c:when test="${cartForOrder.isUserWalletMoneyAvailable}">
           					<c:choose>
           						<c:when test="${cartForOrder.remainingWalletAmount < cartForOrder.offerPrice }">
           							Used your wallet amount:<input type="checkbox" name="walletAmount" value="${cartForOrder.remainingWalletAmount}" checked disabled> ${cartForOrder.remainingWalletAmount}<br/>
           						<h3 class="text-center">Amount Payable : Rs.${cartForOrder.totalAmount-cartForOrder.remainingWalletAmount}</h3>
           						</c:when>
           						<c:otherwise>
           							Used your wallet amount:<input type="checkbox" name="walletAmount" value="${cartForOrder.offerPrice}" checked disabled> ${cartForOrder.offerPrice}<br/>
           						<h3 class="text-center">Amount Payable : Rs.${cartForOrder.totalAmount-cartForOrder.offerPrice}</h3>
           						</c:otherwise>
           					</c:choose>
           					
           				</c:when>
           				<c:otherwise>
           					<h3 class="text-center">Amount Payable : Rs.${cartForOrder.totalAmount}</h3>
           				</c:otherwise>
           			</c:choose>
           			
           		</c:otherwise>
           	</c:choose>
          <p class="text-center" data-cartid="${cartForOrder.cartId}"><a href="#" class="btn btn-success btn-lg order-confirm">CONFIRM AND PLACE ORDER</a></p>
        </div>
      </div>
    </div>
  </div> 
</div>
	<jsp:include page="footer.jsp"></jsp:include>
	
	<script type="text/javascript">
	  	$( document ).ready(function() {
	  		
	  		/* var msg = '${sessionScope.addAddressMessage}';
	  		if(typeof(msg) !== "undefined" && msg !== null && msg !==""){
	  			alert(msg);
	  		} */
	  		
	  		$("#order-summary").removeAttr("data-toggle");
	  		$("#delivery-option").removeAttr("data-toggle");
	  		$("#payment-method").removeAttr("data-toggle");
	  		/* document.getElementById('order-summary').style.pointerEvents = 'none';
	  		document.getElementById('delivery-option').style.pointerEvents = 'none';
	  		document.getElementById('payment-method').style.pointerEvents = 'none'; */
	  		
	  		/* var isStandardDeliveryEnable = '${cartForOrder.isStandardDeliveryEnable}';
	  		if(isStandardDeliveryEnable=="false"){
	  			$("#exp-delivery").attr('disabled', true);
	  			$('#tmrw-delivery').prop('checked', true);
	  			$("#standard-del-msg").show();
	  		}else{
	  			$('#exp-delivery').prop('checked', true);
		  		$('#sel1').prop('disabled', 'disabled');
	  		} */
	  		
	  		
	  	

	  	});
	  	
	  	$(".delete-address").click(function(){
	  		var r = confirm("Do you want to delete the address?");
	  	    if (r == true) {
	  	    	var addressId = $(this).parent('p').data("addressid");
		  		$.ajax({
					url: "deleteaddress",
					data: {addressId:addressId},
					dataType:"text",
					success: function(response){
						window.location.href = "placeorder";
					},
					error: function(response){
					}
					
				});
	  	    } 
	  	});
	  	
	  	$(".edit-click").click(function(){
  	    	var addressId = $(this).parent('p').data("addressid");
  	    	window.location.href="editaddress?addressId="+addressId;
	  	})
	  	
  	</script>
  	<script src="js/place-order.js"></script>
</body>
</html>