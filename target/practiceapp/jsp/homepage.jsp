<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
	<title>SableBadiya</title>
  	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
    <!-- 
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"> -->
    <!-- <link rel="stylesheet" type="text/css" href="css/main.css"> -->
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div id="myCarousel" class="carousel slide" data-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
	    <c:forEach items="${sliderMedia}" var="media" varStatus="status">
	    	<c:choose>
			  <c:when test="${status.index=='0'}">
			   	<li data-target="#myCarousel" data-slide-to="${status.index}" class="active"></li>
			  </c:when>
			  <c:otherwise>
			    <li data-target="#myCarousel" data-slide-to="${status.index}"></li>
			  </c:otherwise>
			</c:choose>
	    </c:forEach>
   </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner" role="listbox">
    	<c:forEach items="${sliderMedia}" var="media" varStatus="status">
			<c:choose>
			  <c:when test="${status.index=='0'}">
			   	<div class="item active">
				    <a href="javascript:void(0);"><img src="${media.mediaWebpath}" alt="" width="100%"></a>
				    <div class="carousel-caption">
				    	<h3></h3>
				    	<p></p>
				    </div>      
			     </div>
			  </c:when>
			  <c:otherwise>
			    <div class="item">
				   <a href="javascript:void(0);"> <img src="${media.mediaWebpath}" alt="" width="100%"></a>
				    <div class="carousel-caption">
				    	<h3></h3>
				    	<p></p>
				    </div>      
			     </div>
			  </c:otherwise>
			</c:choose>
		</c:forEach>
    </div>

    <!-- Left and right controls -->
    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
      <span class="sr-only">Next</span>
    </a>
  </div><br>


  <div class="container-fluid">
    <!-- <div class="row">
    	<div class="col-sm-8 col-sm-offset-2 text-center">
    		<a href="deal_of_the_day" style="text-decoration: none;">
    			<h1 class="heading_for_dod">Deal Of The Day</h1>
    		</a>
    	</div>
    </div> -->
    <div class="row">
      <c:forEach items="${categories}" var="category" varStatus="status">
      	<c:choose>
		  <c:when test="${status.index==0}">
		  	<div class="col-sm-3 text-center">
		         
		        <a href="getgoods?categoryId=${category.categoryId}&isFromSearch=false" onclick="startLoading()" style="text-decoration: none;">
		        	<h4 class="heading"><c:out value="${category.categoryName}" /></h4>
		        	<img class="img-responsive" src="${category.mediaWebpath}" width="100%" height="250px">
		        </a>
      		</div>
      		<!-- <div class="col-sm-3 text-center">
		         
		        <a href="deal_of_the_day" onclick="startLoading()" style="text-decoration: none;">
		        	<h4 class="heading_for_dod">Today's Deal</h4>
		        	<img class="img-responsive" src="media/Deal-of-the-Day.jpg" width="100%" height="250px">
		        </a>
      		</div> -->
		  </c:when>
		  <c:otherwise>
		  	<div class="col-sm-3 text-center">
		         
		        <a href="getgoods?categoryId=${category.categoryId}&isFromSearch=false" onclick="startLoading()" style="text-decoration: none;">
		        	<h4 class="heading"><c:out value="${category.categoryName}" /></h4>
		        	<img class="img-responsive" src="${category.mediaWebpath}" width="100%" height="250px">
		        </a>
	      	</div>
		  </c:otherwise>
		</c:choose>
      </c:forEach>
     
      <div class="col-sm-2"></div>
    </div>
  </div>
  <!-- <div class="container-fluid">
    <div class="row">
      
      <div class="col-sm-4 col-sm-offset-2 text-center">
        <h2 class="heading">Fruits</h2> 
        <a href="fruits.html"><img src="images/fruits1.jpg" class="img-responsive" style="width:420px;height:250px;"></a>
      </div>
      <div class="col-sm-4 text-center">
        <h2 class="heading">Vegetables</h2> 
        <a href="vegetables.html"><img src="images/vegetable4.jpg" class="img-responsive" style="width:420px;height:250px;"></a>
      </div>
      <div class="col-sm-2"></div>
    </div>
  </div> -->
  
  <jsp:include page="footer.jsp"></jsp:include>
  
	<!-- <script src="js/main.js"></script> -->
</body>
</html>