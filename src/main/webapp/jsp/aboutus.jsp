<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>foodcart</title>

<link rel="stylesheet" type="text/css" href="css/aboutus.css">
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br/>
	<div class="container" id="contforh3andp">
    <h3>What is Foodcart</h3>
    <p class="text-justify"> </p><hr>

    <h3>Why should I use Foodcart</h3>
    <p class="text-justify"></p>
    <hr>

    <h3>How do I order?</h3>
      <ol class="list-group">
        <li class="list-group-item">Browse foodcart for products or use the search feature</li>
        <li class="list-group-item">Add item to your Shopping Basket</li>
         <li class="list-group-item">Pay via COD (Cash-on-Delivery) or Credit/Debit card</li>
        <li class="list-group-item">Your products will be home-delivered as per your order.</li>
      </ol>
    <hr>

    <h3>Where do we operate</h3>
    <p class="text-justify">We currently offer our services in mahasamund city.</p>
    <hr>
  </div>
  <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>