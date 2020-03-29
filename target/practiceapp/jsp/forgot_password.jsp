<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SableBadiya</title>
<link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br/><br/>
	<div class="container" style="margin-bottom: 260px;">
    <div class="col-sm-6 col-sm-offset-3">
      <h3 id="headingforgotpassword">Forgot Password?</h3>
      <form role="form" action="forgotpass">
        <div class="form-group">
            <label for="email">Enter Email:</label>
            <input type="email" name="email" class="form-control" id="email" placeholder="Enter email id">
            <p id="forgot-pass-error"></p>
        </div>
        <button type="submit" class="btn btn-danger">Send</button>
      </form> 
    </div>   

  </div>
  <jsp:include page="footer.jsp"></jsp:include>
  <script type="text/javascript">
  $( document ).ready(function() {
	  	var msg = '${errorMsg}';
		if(typeof(msg) !== "undefined" && msg !== null && msg !==""){
			console.log(msg);
			$("#forgot-pass-error").text(msg);
		}
	});
  
  </script>
</body>
</html>