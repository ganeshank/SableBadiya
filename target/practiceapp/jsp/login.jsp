<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>SableBadiya</title>
	<meta charset="utf-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<meta name="google-signin-client_id" content="1082344612925-6bfsqivi3u0vmj1i2el7qbc6chnofkn8.apps.googleusercontent.com">
  	<link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  	<script src="https://apis.google.com/js/platform.js" async defer></script>
  	<link rel="stylesheet" type="text/css" href="css/login.css?version=7">
  	<link rel="stylesheet" type="text/css" href="css/spinner.css">
</head>
<body>

<jsp:include page="loading.jsp"></jsp:include>
	<div class="container col-sm-3 col-sm-offset-8">
    <h2 style="text-align: center;">LOGIN</h2>
    
    <span style="color:red"><c:out value="${error_msg}"></c:out></span>
		<form role="form" id="login_form" action="login" method="post">
    		<div class="form-group">
      			<label for="email">Email / Mobile Number:</label>
      			<input type="input" class="form-control" name="email_contact" id="email" placeholder="Enter Email Id or Phone Number">
    		</div>
    		<div class="form-group">
      			<label for="pwd">Password:</label>
      			<input type="password" class="form-control" name="password" id="pwd" placeholder="Enter Password">
    		</div>
    		<!-- <div class="checkbox">
      			<label><input type="checkbox"> Remember me</label> 
    		</div> -->
    		<div align="center">
    		<button type="submit" class="btn btn-success" style="width: 250px;">Login</button></br/></br/>
    		<!-- <div class="g-signin2" data-width="250" data-onsuccess="onSignIn"></div> -->
    		<div style="padding: 5px;"><a href="signup" ><b>Signup</b></a></div>
    		<div style="padding: 5px;"><a href="homepage"><b>Skip</b></a></div>
  			<div><a href="forgotpassword"><b>Forgot password?</b></a></div>
    		</div>
    		<br><br>
    		
  			<!-- <a href="homepage">Continue as guest</a> -->
  			
  			
		 </form>
	</div>
<script type="text/javascript">
function onSignIn(googleUser) {
	  startLoading();
	  var profile = googleUser.getBasicProfile();
	  if(typeof profile != 'undefined' && profile!=null){
		  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
		  console.log('Name: ' + profile.getName());
		  console.log('Image URL: ' + profile.getImageUrl());
		  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
		  
		  var token_id = googleUser.getAuthResponse().id_token;
		  
		  $.ajax({
				url: "google_signin",
				data: {token_id:token_id},
				type: "post",
				success: function(response){
					location.href = "homepage";
					stopLoading();
				},
				error: function(response){
					alert("Something goes wrong, Please contact to customer support");
					stopLoading();
				}
				
		 }); 
	  }
	} 
	
</script>
</body>
</html>