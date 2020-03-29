<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Thankyou for signing up!</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/thanks.css">

</head>
<jsp:include page="header.jsp"></jsp:include>
<div class="container text-center">
	<div class="col-sm-8 col-sm-offset-2" id="tyforsignup">
		<h1>Successfully Received.</h1>
		<p>We got your queries, we will get back to you very soon.</p>
		<a href="login" class="btn btn-info btn-md">Login</a> <a
			href="homepage" class="btn btn-warning btn-md">Home Page</a>
	</div>

</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>