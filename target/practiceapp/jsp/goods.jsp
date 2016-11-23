<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
      <title>Fresh Goods</title>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
       <link rel="shortcut icon" type="image/x-icon" href="media/logo-icon.ico" />
      <!--<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"> -->
      <link rel="stylesheet" type="text/css" href="css/fruits.css">
      <link rel="stylesheet" type="text/css" href="css/notifIt.css">
      
  </head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br/>
	
	 <div class="container" id="Fruits" style="margin-bottom: 423px;">
	 <c:choose>
	 	<c:when test="${empty goods}">
	 		<div class="text-center" id="empty-cart-div">
	  			<p>There are no items found for your search.</p>
	  			<a href="homepage" class="btn btn-primary btn-md">CLICK HERE FOR HOMEPAGE</a>
	  		</div>
	 	</c:when>
	 	<c:otherwise>
	 		<c:forEach items="${goods}" var="good" varStatus="status">
				 <c:choose>
				 	<c:when test="${good.inStock}">
				 		<div class="col-sm-4 text-center fruitsbox">
				 			<c:choose>
				 				<c:when test="${good.isTodaysDeal}">
				 					<p class="bg-info"><strong>Deal Of The Day</strong></p>
				 				</c:when>
				 				<c:otherwise>
				 					<p class="bg-info" style="visibility: hidden;">Deal Of The Day</p>
				 				</c:otherwise>
				 			</c:choose>
				          <!-- <a href="javascript:void(0);" class="thumbnail img-href"> -->
				            <img src="${good.webpath}" class="goods-img" style="width:250px;height:150px;">
				            <!-- </a> -->
				            <h3>${good.goodsName}</h3>
				            <div class="row">
			            	     <c:choose>
			 						<c:when test="${good.inRupee}">
			            				<div class="col-sm-6 col-xs-6">
							      			<input type="radio" name="qty-option-${good.goodsName}" value="qty-wise" class="qty-option-radio" checked>Quantity Wise
				            			</div>
				            			<div class="col-sm-6 col-xs-6">
							      			<input type="radio" name="qty-option-${good.goodsName}" value="rupee-wise" class="qty-option-radio">Rupee Wise
				            			</div>
				            		</c:when>
				            		<c:otherwise>
				            			<div class="col-sm-6 col-xs-6" style="visibility: hidden;">
							      			<input type="radio" name="qty-option-demo" value="demo" class="deom-class">Quantity Wise
				            			</div>
				            			<div class="col-sm-6 col-xs-6" style="visibility: hidden;">
							      			<input type="radio" name="qty-option-demo" value="demo" class="deom-class">Rupee Wise
				            			</div>
				            		</c:otherwise>
				            	</c:choose>
				            </div>
				            <div class="row qty-class">
					            <div class="col-sm-6 col-xs-6">
					              <p class="price text-left"><strong>Quantity:</strong></p>
					            </div>
					            <div class="col-sm-6 col-xs-6 qty-inner-div">
					              <select class="form-control qty-select">
					              <c:forEach items="${good.quantityVos}" var="qty">
					              	<option value="${qty.quantityId}">${qty.weight} - ${qty.uom}</option>
					              </c:forEach>
					            </select>
					            </div>
				            </div>
				            <div class="row rupee-class" style="display: none;">
					            <div class="col-sm-6 col-xs-6">
					              <p class="price text-left"><strong>Rupee:</strong></p>
					            </div>
					            <div class="col-sm-6 col-xs-6 qty-inner-div-rupee">
					              <select class="form-control qty-select-rupee">
					              <%-- <c:forEach items="${good.quantityVos}" var="qty"> --%>
					              	<option value="5">5 Rs</option>
					              	<option value="10">10 Rs</option>
					              	<option value="15">15 Rs</option>
					              	<option value="20">20 Rs</option>
					              	<option value="25">25 Rs</option>
					              	<option value="30">30 Rs</option>
					              <%-- </c:forEach> --%>
					            </select>
					            </div>
				            </div>
				            <div class="row">
				            <div class="col-sm-6 col-xs-6">
				              <p class="price text-left"><del><strong>Market Price:</strong></del></p>
				            </div>
				            <div class="col-sm-6 col-xs-6">
				              <p class="price"><del><strong>Rs.${good.msrp}/${good.uom}</strong></del></p>
				            </div>
				            </div>
				
				            <div class="row">
				             <div class="col-sm-6 col-xs-6">
				               <p class="price text-left"><strong>Your Price :</strong></p>
				             </div>
				             <div class="col-sm-6 col-xs-6">
				               <p class="price"><strong>Rs.${good.price}/${good.uom}</strong></p>
				             </div>
				             </div>
				
				             <div class="row">
				             <div class="col-sm-6 col-xs-6">
				               <p class="price text-left"><strong>Savings :</strong></p>
				             </div>
				             <div class="col-sm-6 col-xs-6">
				               <p class="price"><strong>Rs.${good.saving}/${good.uom}</strong></p>
				             </div>
				             </div>
				            <p data-goods="${good.goodsId}" data-goodsName="${good.goodsName}" data-seller="1"><button class="btn-warning btn-block addtocartandbuynow" type="button">Add to cart</button></p>
			        	</div>
				 	</c:when>
				 	<c:otherwise>
				 		<div class="col-sm-4 text-center fruitsbox">
				 			<c:choose>
				 				<c:when test="${good.isTodaysDeal}">
				 					<p class="bg-info"><strong>Deal Of The Day</strong></p>
				 				</c:when>
				 				<c:otherwise>
				 					<p class="bg-info" style="visibility: hidden;">Deal Of The Day</p>
				 				</c:otherwise>
				 			</c:choose>
				 			
				          <!-- <a href="javascript:void(0);" class="thumbnail img-href"> -->
				            <img src="${good.webpath}" class="goods-img" style="width:250px;height:150px;">
				            <!-- </a> -->
				            <h3>${good.goodsName}</h3>
				            <div class="row">
				                <c:choose>
				            	<c:when test="${good.inRupee}">
			            				<div class="col-sm-6 col-xs-6">
							      			<input type="radio" name="qty-option-${good.goodsName}" value="qty-wise" class="qty-option-radio" checked>Quantity Wise
				            			</div>
				            			<div class="col-sm-6 col-xs-6">
							      			<input type="radio" name="qty-option-${good.goodsName}" value="rupee-wise" class="qty-option-radio">Rupee Wise
				            			</div>
				            		</c:when>
				            		<c:otherwise>
				            			<div class="col-sm-6 col-xs-6" style="visibility: hidden;">
							      			<input type="radio" name="qty-option-demo" value="demo" class="deom-class">Quantity Wise
				            			</div>
				            			<div class="col-sm-6 col-xs-6" style="visibility: hidden;">
							      			<input type="radio" name="qty-option-demo" value="demo" class="deom-class">Rupee Wise
				            			</div>
				            		</c:otherwise>
				            		</c:choose>
				            </div>
				            <div class="row qty-class">
					            <div class="col-sm-6 col-xs-6">
					              <p class="price text-left"><strong>Quantity:</strong></p>
					            </div>
					            <div class="col-sm-6 col-xs-6 qty-inner-div">
					              <select class="form-control qty-select" disabled>
					              <c:forEach items="${good.quantityVos}" var="qty">
					              	<option value="${qty.quantityId}">${qty.weight} - ${qty.uom}</option>
					              </c:forEach>
					            </select>
					            </div>
				            </div>
				            <div class="row rupee-class" style="display: none;">
					            <div class="col-sm-6 col-xs-6">
					              <p class="price text-left"><strong>Rupee:</strong></p>
					            </div>
					            <div class="col-sm-6 col-xs-6 qty-inner-div-rupee">
					              <select class="form-control qty-select-rupee" disabled>
					              <%-- <c:forEach items="${good.quantityVos}" var="qty"> --%>
					              	<option value="5">5 Rs</option>
					              	<option value="10">10 Rs</option>
					              	<option value="15">15 Rs</option>
					              	<option value="20">20 Rs</option>
					              	<option value="25">25 Rs</option>
					              	<option value="30">30 Rs</option>
					              <%-- </c:forEach> --%>
					            </select>
					            </div>
				            </div>
				            <div class="row">
				            <div class="col-sm-6 col-xs-6">
				              <p class="price text-left"><del><strong>Market Price:</strong></del></p>
				            </div>
				            <div class="col-sm-6 col-xs-6">
				              <p class="price"><del><strong>Rs.${good.msrp}/${good.uom}</strong></del></p>
				            </div>
				            </div>
				
				            <div class="row">
				             <div class="col-sm-6 col-xs-6">
				               <p class="price text-left"><strong>Your Price :</strong></p>
				             </div>
				             <div class="col-sm-6 col-xs-6">
				               <p class="price"><strong>Rs.${good.price}/${good.uom}</strong></p>
				             </div>
				             </div>
				
				             <div class="row">
				             <div class="col-sm-6 col-xs-6">
				               <p class="price text-left"><strong>Savings :</strong></p>
				             </div>
				             <div class="col-sm-6 col-xs-6">
				               <p class="price"><strong>Rs.${good.saving}/${good.uom}</strong></p>
				             </div>
				             </div>
				            <p data-goods="${good.goodsId}" data-goodsName="${good.goodsName}" data-seller="1"><button style="background-color: #ccc;" class="btn-warning btn-block addtocartandbuynow" type="button" disabled>Out Of Stock</button></p>
			        	</div>
				 	</c:otherwise>
				 </c:choose>
	 		</c:forEach>
	 	
	 	</c:otherwise>
	 </c:choose>
    </div>
    <script src="js/main.js"></script>
      <script src="js/notifIt.min.js"></script>
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>