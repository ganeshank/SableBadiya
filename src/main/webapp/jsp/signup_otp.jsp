<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Foodcart</title>


<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/thanks.css">
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="container text-center">
<form role="form" action="signup_after_otp" method="get" id="signup_form">
	<div class="col-sm-8 col-sm-offset-2" id="tyforsignup">
		<h1>We have sent a 4 digit number on given mobile number, Please verify and signup successfully. </h1>
	
		<span style="color:red"><c:out value="${error_msg}"></c:out></span>
		<input id="otp" type="number" name="otp" min="2000" max="9999" required>
		<button  class="btn btn-primary" id="otp-submit">Submit</button>
	</div>
</form>
</div>
<jsp:include page="footer.jsp"></jsp:include>
<script type="text/javascript">

	$("#otp-submit").click(function(){
		var otp = $("#otp").val();
		
		if(isNaN(otp)){
			alert("Please enter 4 digit number only.");
		 }else{
			 document.getElementById("signup_form").submit();
		 }
		
	});
</script>

</body>
</html>