<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Foodcart</title>

    <!-- Bootstrap Core CSS -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.0/themes/base/jquery-ui.css">

    <!-- Custom CSS -->
    <link href="css/portfolio-item.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>
<jsp:include page="header.jsp"></jsp:include><br/>
    <!-- Page Content -->
    <div class="container">

        <!-- Portfolio Item Heading -->
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">${goodsVo.goodsName}
                    <!-- <small>Item Subheading</small> -->
                </h1>
            </div>
        </div>
        <!-- /.row -->

        <!-- Portfolio Item Row -->
        <div class="row">

            <div class="col-md-8">
                <img class="img-responsive" src="${goodsVo.webpath}" alt="">
            </div>

            <div class="col-md-4">
                <h3>${goodsVo.goodsName}</h3>
	            <div class="col-sm-6 col-xs-6">
	              <p class="price text-left"><strong>Quantity:</strong></p>
	            </div>
	            <div class="col-sm-6 col-xs-6 qty-inner-div">
	            	<div class="row">
		              <select class="form-control qty-select">
			              <c:forEach items="${goodsVo.quantityVos}" var="qty">
			              	<option value="${qty.quantityId}">${qty.weight} - ${qty.uom}</option>
			              </c:forEach>
	            	  </select>
            	    </div>
            	  
            	  	
	            </div>
	            
	            <div class="col-sm-6 col-xs-6">
	               <p class="price text-left"><strong>Price:</strong></p>
	            </div>
	            
	            <div class="col-sm-6 col-xs-6">
	             <p class="price"><strong>Rs.${goodsVo.price}/${goodsVo.uom}</strong></p>
	            </div>
	            
	            <div class="col-sm-6 col-xs-6">
	               <p class="price text-left"><del><strong>Market Price:</strong></del></p>
	            </div>
	            
	            <div class="col-sm-6 col-xs-6">
	             <p class="price"><del><strong>Rs.${goodsVo.msrp}/${goodsVo.uom}</strong></del></p>
	            </div>
	            
	            <div class="col-sm-6 col-xs-6">
	               <p class="price text-left"><strong>Saving Price:</strong></p>
	            </div>
	            
	            <div class="col-sm-6 col-xs-6">
	             <p class="price"><strong>Rs.${goodsVo.saving}/${goodsVo.uom}</strong></p>
	            </div>
            </div>

        </div>
        <!-- /.row -->

        <!-- Related Projects Row -->
        <div class="row">

            <div class="col-lg-12">
                <h3 class="page-header">You may also buy</h3>
            </div>
            
            <c:forEach items="${goodsForCategory}" var="goods">
            	<div class="col-sm-3 col-xs-6">
	                <a href="goods_details?goodsId=${goods.goodsId}">
	                    <img class="img-responsive portfolio-item" src="${goods.webpath }" alt="" style="width:250px;height:150px;">
	                </a>
	                <h5 align="center">${goods.goodsName}</h5>
            	</div>
            </c:forEach>
        </div>

    </div>
    <!-- /.container -->

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<jsp:include page="footer.jsp"></jsp:include>
</body>

</html>
