<!DOCTYPE html>
<html>
	<head>
	  	<title>SableBadiya</title>
	  	<meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
	    <!-- <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"> -->
	    <link rel="stylesheet" type="text/css" href="css/contactus.css">
  </head>
  <body>

  <jsp:include page="header.jsp"></jsp:include>
  
<div class="container">
  	<div class="col-sm-6 col-sm-offset-3" id="submitcxrequest">
  	<h3>Submit your queries.</h3>
  	<hr>
  	<p>If you have a question about our service or have an issue to report, You can directly contact with us <span class="glyphicon glyphicon-earphone"></span> +91-9584069665</p>
  	<p>Or you can send a request below and we will get back to you as soon as possible.</p>

  	<form class="form-horizontal" id="myform" name="myform" action="sendquery" method="post">
  		<div class="form-group">
	  		<label for="subject" class="col-sm-4 control-label">Full Name<sup style="color:red;">*</sup></label>
	  		<div class="col-sm-8">
	    		<input type="text" class="form-control" id="name" placeholder="Enter your name" name="name">
          <p id="name-p" style="color:red;"></p>
	    	</div>
	  	</div>
	  	<div class="form-group">
	  		<label for="subject" class="col-sm-4 control-label">Your Email<sup style="color:red;">*</sup></label>
	  		<div class="col-sm-8">
	    		<input type="text" class="form-control" id="email" placeholder="Enter email" name="email">
          <p id="email-p" style="color:red;"></p>
	    	</div>
	  	</div>
	  	<div class="form-group">
	  		<label for="subject" class="col-sm-4 control-label">Contact Number<sup style="color:red;">*</sup></label>
	  		<div class="col-sm-8">
	    		<input type="text" class="form-control" id="contact" placeholder="Enter contact number" name="contact">
          <p id="contact-p" style="color:red;"></p>
	    	</div>
	  	</div>
	  	<div class="form-group">
	  		<label for="subject" class="col-sm-4 control-label">Subject<sup style="color:red;">*</sup></label>
	  		<div class="col-sm-8">
	    		<input type="text" class="form-control" id="subject" placeholder="Enter subject" name="subject">
          <p id="roy1" style="color:red;"></p>
	    	</div>
	  	</div>
	  	<div class="form-group">
		    <label for="message" class="col-sm-4 control-label">Message<sup style="color:red;">*</sup></label>
		    <div class="col-sm-8">
		      <textarea class="form-control" rows="3" placeholder="Type your message here" id="message" name="message"></textarea>
          <p id="roy2" style="color:red;"></p>
		      <input type="button" class="btn btn-success btn-sm sendandcancelbutton" onclick="formsubmit();" value="SEND">
		      <input type="reset" class="btn btn-danger btn-sm sendandcancelbutton" value="CANCEL">
		    </div>
	  	</div>

  		
  	</form>

  </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
    	
  <script>
  function formsubmit() {

	  if (document.getElementById('name').value == "") {
	      document.getElementById('name-p').innerHTML = "Please enter full name";
	      myform.subject.focus();
	      return false;
	    } else {
	      document.getElementById('name-p').innerHTML = "";
	    }
	  
	  if (document.getElementById('email').value == "") {
	      document.getElementById('email-p').innerHTML = "Please enter Email name";
	      myform.subject.focus();
	      return false;
	    } else {
	      document.getElementById('email-p').innerHTML = "";
	    }
	  
	  if (document.getElementById('contact').value == "") {
	      document.getElementById('contact-p').innerHTML = "Please enter Contact name";
	      myform.subject.focus();
	      return false;
	    } else {
	      document.getElementById('contact-p').innerHTML = "";
	    }
	  
    if (document.getElementById('subject').value == "") {
      document.getElementById('roy1').innerHTML = "Please enter subject name";
      myform.subject.focus();
      return false;
    } else {
      document.getElementById('roy1').innerHTML = "";
    }

    if (document.getElementById('message').value == "") {
      document.getElementById('roy2').innerHTML = "Please enter your message";
      myform.message.focus();
      return false;
    } else {
      document.getElementById('roy2').innerHTML = "";
    }
    
    $("#myform").submit();
    
  }
  </script>
  </body>
</html>