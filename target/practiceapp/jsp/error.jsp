<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SableBadiya</title>
<link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<div class="col-sm-6 col-sm-offset-3">
			<p id="para1">You don't have a permission for that URL.</p>
			<a href="login" class="btn btn-danger">Login</a>
		</div>
	</div>
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>