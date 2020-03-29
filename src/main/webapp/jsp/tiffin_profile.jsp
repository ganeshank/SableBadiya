<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
	<title>Foodcart</title>
  	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <!-- 
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"> -->
    <!-- <link rel="stylesheet" type="text/css" href="css/main.css"> -->
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container-fluid" style="margin-top: 50px;">
		<div class="panel panel-success">
		  <!-- Default panel contents -->
		  <div class="panel-heading">Today's ordered tiffin</div>
		  <div class="panel-body">
		    	<div class="panel panel-info">
				  <!-- Default panel contents -->
				  <div class="panel-heading">Lunch</div>
				  <div class="panel-body">
				    	<div class="media">
						  <div class="media-left media-middle">
						    
						  </div>
						  <div class="media-body">
						    <h4 class="media-heading">Rice, 3-Roti, Dal fry, 1-Sabji, Salad, Papad/Aachar</h4>
						  </div>
						</div>
				  </div>
				</div>
				<div class="panel panel-warning">
				  <!-- Default panel contents -->
				  <div class="panel-heading">Dinner</div>
				  <div class="panel-body">
				    	<div class="media">
						  <div class="media-left media-middle">
						    
						  </div>
						  <div class="media-body">
						    <h4 class="media-heading">Rice, 3-Roti, Dal fry, 1-Sabji, Salad, Papad/Aachar</h4>
						  </div>
						</div>
				  </div>
				</div>
		  </div>
		</div>
		
		
		<div class="panel panel-danger">
		  <!-- Default panel contents -->
		  <div class="panel-heading">Order for tomorrow</div>
		  <div class="panel-body">
		    	<div class="panel panel-info">
				  <!-- Default panel contents -->
				  <div class="panel-heading">Lunch</div>
				  <div class="panel-body">
				    	<div class="media">
						  <div class="media-left media-middle">
						    
						  </div>
						  <div class="media-body">
						    <div class="row">
							  <div class="col-lg-12">
							    <div class="input-group">
							      <span class="input-group-addon">
							        <input type="radio" aria-label="...">
							      </span>
							      <input type="text" class="form-control" aria-label="..." readonly value="Jeera rice, 4-Roti, Dal fry, 2-Sabji, Salad, Papad, Pickle, 1-Sweet">
							    </div><!-- /input-group -->
							  </div><!-- /.col-lg-6 -->
							</div>
							<div class="row">
							  <div class="col-lg-12">
							    <div class="input-group">
							      <span class="input-group-addon">
							        <input type="radio" aria-label="...">
							      </span>
							      <input type="text" class="form-control" aria-label="..." readonly value="Jeera rice, 4-Roti, Dal fry, 2-Sabji, Salad, Papad, Pickle, 1-Sweet">
							    </div><!-- /input-group -->
							  </div><!-- /.col-lg-6 -->
							</div>
							<div class="row">
							  <div class="col-lg-12">
							    <div class="input-group">
							      <span class="input-group-addon">
							        <input type="radio" aria-label="...">
							      </span>
							      <input type="text" class="form-control" aria-label="..." readonly value="Jeera rice, 4-Roti, Dal fry, 2-Sabji, Salad, Papad, Pickle, 1-Sweet">
							    </div><!-- /input-group -->
							  </div><!-- /.col-lg-6 -->
							</div>
							<div class="row">
							  <div class="col-lg-12">
							    <div class="input-group">
							      <span class="input-group-addon">
							        <input type="radio" aria-label="...">
							      </span>
							      <input type="text" class="form-control" aria-label="..." readonly value="Jeera rice, 4-Roti, Dal fry, 2-Sabji, Salad, Papad, Pickle, 1-Sweet">
							    </div><!-- /input-group -->
							  </div><!-- /.col-lg-6 -->
							</div>
							
						  </div>
						</div>
				  </div>
				</div>
				<div class="panel panel-warning">
				  <!-- Default panel contents -->
				  <div class="panel-heading">Dinner</div>
				  <div class="panel-body">
				    	<div class="media">
						  <div class="media-left media-middle">
						    
						  </div>
						  <div class="media-body">
						    <h4 class="media-heading">Rice, 3-Roti, Dal fry, 1-Sabji, Salad, Papad/Aachar</h4>
						  </div>
						</div>
				  </div>
				</div>
		  </div>
		</div>
	
	</div>

	 <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>