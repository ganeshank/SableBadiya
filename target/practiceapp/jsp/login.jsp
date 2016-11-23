<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Login Page</title>
	<meta charset="utf-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<link rel="shortcut icon" type="image/x-icon" href="media/logo-icon.ico" />
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  	<link rel="stylesheet" type="text/css" href="css/login.css?version=6">
</head>
<body>

	<div class="container col-sm-3 col-sm-offset-8">
    <h2>LOGIN</h2>
    <span style="color:red"><c:out value="${error_msg}"></c:out></span>
		<form role="form" id="login_form" action="login" method="post">
    		<div class="form-group">
      			<label for="email">Email / Mobile Number:</label>
      			<input type="input" class="form-control" name="email_contact" id="email" placeholder="Enter EmailId or Contact">
    		</div>
    		<div class="form-group">
      			<label for="pwd">Password:</label>
      			<input type="password" class="form-control" name="password" id="pwd" placeholder="Enter Password">
    		</div>
    		<!-- <div class="checkbox">
      			<label><input type="checkbox"> Remember me</label> 
    		</div> -->
    		<button type="submit" class="btn btn-success">Login</button>
    		<a href="signup" class="btn btn-danger">Signup</a><br><br>
  			<a href="forgotpassword">Forgot password?</a><br/>
  			<a href="homepage">Continue as guest</a>
		 </form>
	</div>

</body>
</html>