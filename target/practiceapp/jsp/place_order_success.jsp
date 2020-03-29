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
      <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
      <link rel="stylesheet" type="text/css" href="css/ordercomplete.css">
  </head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="container">
    <div class="col-sm-6 col-sm-offset-3" id="borderofbox">
      <h3>Congratulations!</h3>
      <p>Your order has been placed successfully.</p>
      <p>Thank you for your purchase.</p>
      <p>Your order Id is #<strong><c:out value="${orderNumber}"></c:out></strong> and your order will be 
      delivered <c:out value="${inDeliver}"></c:out></p>
      <p>You will receive an order confirmation email and message with details of your order.</p>

    </div>
  </div>
	
	
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>