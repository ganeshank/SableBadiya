<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
	<title>SableBadiya</title>
	<meta charset="utf-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  	<link rel="stylesheet" type="text/css" href="css/signup.css?version=2">
</head>
<body>
	<div class="container col-sm-4 col-sm-offset-4">
    <h1>Sign Up here!</h1>
    	<span style="color:red"><c:out value="${error_msg}"></c:out></span>
		<form role="form" action="signup" method="post" id="signup_form">
    		<div class="form-group">
      			<label for="name">Name:</label>
      			<input type="text" class="form-control" id="name" name="name" placeholder="Enter full name" required>
    		</div>
        <div class="form-group">
            <label for="email">Email Id:</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="Enter email id" required>
        </div>
        <div class="form-group">
            <label for="contactno">Mobile Number:</label>
            <input type="tel" class="form-control" id="contactno" title="Enter valid 10 digit mobile number" name="contactnumber" pattern=".{10,}" placeholder="Enter only 10 digits of your mobile number" required>
        </div>
    		<div class="form-group">
      			<label for="pwd">Password:</label>
      			<input type="password" class="form-control" id="pwd" name="password" maxlength="20" placeholder="Enter password" pattern=".{6,}" title="Minimum password length should be of 6 characters and maximum length can be upto 20 characters." required>
    		</div>
        <div class="form-group">
            <label for="confirm password">Confirm Password:</label>
            <input type="password" class="form-control" id="cpwd" name="password" maxlength="20" placeholder="Enter password" required>
        </div>
        
    		
    		
    		<input class="btn btn-success" type="button" value="Signup" onclick="validate()">
    		<input class="btn btn-danger" type="button" value="Back to home" onclick="back()">
    		<br><br>
  			
		 </form>
	</div>
	<script type="text/javascript">
		function validate(){
			var pass = document.getElementById("pwd").value;
			var confirmPass = document.getElementById("cpwd").value;
			var name = document.getElementById("name").value;
			var email = document.getElementById("email").value;
			var mobile = document.getElementById("contactno").value;
			
			var phonenoRegx = /^\d{10}$/;
			
			if (!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email))){
				alert('Email is not valid');
			   return false;
			}else if(!(mobile.match(phonenoRegx))){
				alert('Mobile number is not valid');
				   return false;
			}else if(pass!=confirmPass){
				alert('password and confirm password is not matched, Please try again!!!');
			} 
			else{
				document.getElementById("signup_form").submit();
			}
		}
		
		function back(){
			window.location.href = "homepage";
		}
		
	</script>

</body>
</html>