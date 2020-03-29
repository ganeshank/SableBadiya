<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
	<head>
	  	<title>SableBadiya</title>
	  	<meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
	    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
	    <link rel="stylesheet" type="text/css" href="css/changepassword.css">
  </head>
  <body>

 <jsp:include page="header.jsp"></jsp:include><br/>

  <div class="container" style="margin-bottom: 90px;">
    <h3>My Account Details</h3>
    <div class="panel-group" id="accordion">
      <div class="panel panel-success">
        <div class="panel-heading">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse1">
            <h4 class="panel-title">
              My Profile Details <span class="glyphicon glyphicon-menu-down"style="float:right;" ></span>
            </h4>  
          </a>
          
        </div>
        <div id="collapse1" class="panel-collapse collapse in">
          <div class="panel-body">
            <div id="div1">
              <h4 id="name-view">${userDetails.fullName}</h4> <small onclick="myfunction()" style="cursor:pointer;">(Edit)</small>
              <P id="email-view">${userDetails.email}</P>
              <p id="mobile-view">${userDetails.mobileNumber}</p>
            </div>

            <div id="demo" style="display:none;">
              
               <div class="form-group">
                  <label for="fullname" class="col-sm-2 control-label">Full Name:</label>
                  <div class="col-sm-4">
                    <input type="text" class="form-control" id="fullname" name="fullname" value="${userDetails.fullName}" placeholder="Enter your full name">
                    <p id="sam1" style="color:red;"></p>
                  </div>
                </div>
               <div class="form-group">
                  <label for="emailid" class="col-sm-2 control-label">Email Id:</label>
                  <div class="col-sm-4">
                    <input type="email" class="form-control" id="emailid" name="emailid" value="${userDetails.email}" placeholder="Enter your Email Id">
                    <p id="sam2" style="color:red;"></p>
                  </div>
                </div>
               <div class="form-group">
                  <label for="contactno" class="col-sm-2 control-label">Mobile Number:</label>
                  <div class="col-sm-4">
                    <input type="tel" class="form-control" id="contactno" value="${userDetails.mobileNumber}" title="Enter valid 10 digit mobile number" pattern=".{10,}" placeholder="Enter only 10 digits of your mobile number">
                    <p id="sam3" style="color:red;"></p>
                    

                    <input type="button" class="btn btn-warning" onclick="validation1();" value="SAVE">
                    <input type="button" class="btn btn-danger" onclick="backToView();" value="CANCEL">
                  </div>
                </div>
              
            </div>

            
            
          </div>
        </div>
      </div>


    <%-- <div class="panel panel-success">
      <div class="panel-heading">
        <a data-toggle="collapse" data-parent="#accordion" href="#collapse3">
          <h4 class="panel-title">
            Address Book <span class="glyphicon glyphicon-menu-down"style="float:right;" ></span>
          </h4>  
        </a>
        
      </div>
      <div id="collapse3" class="panel-collapse collapse">
        <div class="panel-body">
          <a href="deliveryaddress.html" type="button" class="btn btn-default" style="float:right;">ADD NEW</a>
          <br/>
          <c:choose>
		  <c:when test="${empty addressVos}">
		    <span>No Address found</span>
		  </c:when>
		  <c:otherwise>
		  	<c:forEach items="${addressVos}" var="addressVo" varStatus="status">
		  		<div class="col-sm-3">
		            <p class="text-justify">${addressVo.contactName}<br>
		                ${addressVo.addressLine1}, ${addressVo.addressLine2}<br>
		                ${addressVo.contactNumber}<br> 
		                ${addressVo.city}, ${addressVo.state} - ${addressVo.pinCode}<br>
		            </p>
		            <p>${addressVo.contactNumber}</p>
		            <p>${addressVo.email}</p>
		            <button type="button" class="btn btn-warning">DELIVER HERE</button>
		          </div>
		  	</c:forEach>
	  	</c:otherwise>
	  	</c:choose>
        </div>
      </div>
    </div> --%>


    <div class="panel panel-success">
      <div class="panel-heading">
        <a data-toggle="collapse" data-parent="#accordion" href="#collapse4">
          <h4 class="panel-title">
            Change Password <span class="glyphicon glyphicon-menu-down"style="float:right;" ></span>
          </h4>
        </a>
        
      </div>
      <div id="collapse4" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="form-group">
              <label for="oldpassword" class="col-sm-2 col-sm-offset-3 control-label">Old Password:</label>
              <div class="col-sm-4">
                <input type="password" class="form-control" id="oldpassword" name="oldpassword" placeholder="Enter Old Password">
                <p id="dom1" style="color:red;"></p>
              </div>
            </div>

            <div class="form-group">
              <label for="newpassword" class="col-sm-2 col-sm-offset-3 control-label">New Password:</label>
              <div class="col-sm-4">
                <input type="password" class="form-control" id="newpassword" name="password" maxlength="20" placeholder="Enter New password" pattern=".{6,}" title="Minimum password length should be of 6 characters and maximum length can be upto 20 characters.">
                <p id="dom2" style="color:red;"></p>
              </div>
            </div>

            <div class="form-group">
              <label for="confirmpassword" class="col-sm-2 col-sm-offset-3 control-label">Confirm Password:</label>
              <div class="col-sm-4">
                <input type="password" class="form-control" id="confirmpassword" name="password1" placeholder="Retype New Password" maxlength="20" pattern=".{6,}" title="Minimum password length should be of 6 characters and maximum length can be upto 20 characters.">
                <p id="dom3" style="color:red;"></p>
                <p id="dom4" style="color:green;"></p>
                <input type="button" class="btn btn-success" onclick="validation3()" style="margin-top:10px;" value="UPDATE">
              </div>
            </div>
        </div>
      </div>
    </div>
    
    <div class="panel panel-success">
      <div class="panel-heading">
        <a data-toggle="collapse" data-parent="#accordion" href="#collapse7">
          <h4 class="panel-title">
            My Wallet <span class="glyphicon glyphicon-menu-down"style="float:right;" ></span>
          </h4>
        </a>
        
      </div>
      <div id="collapse7" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="form-group">
              <label for="myWallet" class="col-sm-2 col-sm-offset-3 control-label">Wallet Amount:</label>
              <div class="col-sm-4">
                <input type="text" class="form-control" value="${userDetails.walletAmount} Rs" id="myWallet" name="myWallet" readonly>
              </div>
            </div>
        </div>
      </div>
    </div>



    <div class="panel panel-success">
      <div class="panel-heading">
        <a data-toggle="collapse" data-parent="#accordion" href="#collapse5">
          <h4 class="panel-title">
            My Orders <span class="glyphicon glyphicon-menu-down"style="float:right;" ></span>
          </h4>
        </a>
        
      </div>
      <div id="collapse5" class="panel-collapse collapse">


    <div class="panel-group" id="accordion1" style="margin-top:15px;">
    
    <c:forEach items="${cartDetails}" var="order" varStatus="status">
      <div class="panel panel-info">
        <div class="panel-heading">
          
          <a data-parent="#accordion1" href="javascript:void(0);" style="text-decoration:none;">
            <h6 class="panel-title">
            ${order.orderNumber} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Onion 1kg &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
            		<%-- <c:choose>
            			<c:when test="${order.cartStatusId == 7 || order.cartStatusId == 8}">
	                		<td>Order is cancelled</td>
	                	</c:when>
	                	<c:when test="${order.isRecentOrder}">
	                		<td><button class="btn btn-danger cancel-click" data-cancelid="order-${order.cartId}">CANCEL ORDER</button></td>
	                	</c:when>
	                </c:choose> --%>
	         <span class="glyphicon glyphicon-menu-down"style="float:right;" ></span> 
            </h6>
          </a>
        </div>

        <div id="collapsea" class="panel-collapse collapse in">
          <div class="panel-body">
            <%-- <c:choose>
            	<c:when test="${order.cartStatusId != 7 || order.cartStatusId != 8}"> --%>
            		<table class="table">
			            <c:forEach items="${order.cartItems}" var="cartItem" varStatus="status">
			              <tr>
			                <td><img src="${cartItem.goodsVo.webpath}" style="width:120px;height:80px;"></td>
			                <td style="font-family:verdana;margin-top:40px;">Onion<br>Qty = ${cartItem.quantity}${cartItem.uom}</td>
			                <td style="font-family:verdana;">Rs. ${cartItem.price}</td>
			                <%-- <c:choose>
			                	<c:when test="${cartItem.isCancelled || order.cartStatusId == 7 || order.cartStatusId == 8}">
			                		<td>Item is cancelled.</td>
			                	</c:when>
			                	<c:when test="${order.isRecentOrder}">
			                		<td><button class="btn btn-danger cancel-click" data-cancelid="item-${cartItem.cartItemId}">CANCEL ITEM</button></td>
			                	</c:when>
			                </c:choose> --%>
			              </tr>
		               </c:forEach>
		              <tr>
		                <td style="font-family:verdana;">Order Date: ${order.orderDate}</td>
		                <td style="font-family:verdana;">Delivered On: ${order.orderDate}</td>
		                <td style="font-family:verdana;">Order Total: <strong>Rs. ${order.totalAmount}</strong></td>
		                <c:choose>
            				<c:when test="${order.cartStatusId == 7 || order.cartStatusId == 8}">	
            					<td><p style="color:red;">Status: Canceled</p></td>
            				</c:when>
            				<c:when test="${order.cartStatusId == 6 || order.cartStatusId == 9}">	
            					<td><p style="color:red;">Status: Delivered</p></td>
            				</c:when>
            				<c:otherwise>
            					<td><button class="btn btn-danger cancel-click" data-cancelid="order-${order.cartId}">Cancel Order</button></td>
            				</c:otherwise>
		                </c:choose>
		              </tr>
		            </table>
            	<%-- </c:when>
            </c:choose> --%>
          </div>
        </div>    
          </div>
</c:forEach>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
      
  <script src="js/myaccount.js"></script>
  </body>
</html>