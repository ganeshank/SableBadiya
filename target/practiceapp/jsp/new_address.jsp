<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
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
        <h3 id="deliveryaddress">Delivery Address</h3>
        <form role="form" action="addaddress" id="address-form" method="post">
          <div class="form-group">
            <label for="fullname">Full Name<sup style="color:red;">*</sup> :</label>
            <input type="text" value="${addressForEdit.contactName}" class="form-control" name="full-name" id="fullname" placeholder="Enter Full Name" required>
            <p id="sam1" style="color:red;"></p>
          </div>
          <div class="form-group">
            <label for="contactnumber">Contact Number<sup style="color:red;">*</sup> :</label>
            <input type="tel" pattern=".{10,}" value="${addressForEdit.contactNumber}" class="form-control" name="contact-number" id="contactnumber" placeholder="Enter Contact Number" required>
            <p id="sam2" style="color:red;"></p>
          </div>
          
          <div class="form-group">
            <label for="email">Email<sup style="color:red;">*</sup> :</label>
            <input type="text" class="form-control" value="${addressForEdit.email}" name="email" id="email" placeholder="Enter Email" required>
            <p id="email-error" style="color:red;"></p>
          </div>
          
          <div class="form-group">
            <label for="addressline1">Address<sup style="color:red;">*</sup> :</label>
            <input type="text" class="form-control" value="${addressForEdit.addressLine1}" name="address1" id="addressline1" placeholder="Enter Address here" required>
            <p id="sam3" style="color:red;"></p>
          </div>

		  <div class="form-group">
            <label for="landmark">Landmark :</label>
            <input type="text" name="landmark" value="${addressForEdit.landMark}" class="form-control" id="landmark" placeholder="Enter Landmark ">
          </div>
          <%-- <div class="form-group">
            <label for="addressline2">Address Line 2 :</label>
            <input type="text" class="form-control" value="${addressForEdit.addressLine2}" name="address2" id="addressline2" placeholder="Enter Address here">
          </div> --%>

          <div class="form-group">
            <label for="city">City<sup style="color:red;">*</sup> :</label>
            <select class="form-control" id="city" name="city">
              <option value="select">Select Area</option>
              <option value="SMRITI_NAGAR">SMRITI NAGAR</option>
              <option value="JUNWANI">JUNWANI</option>
              <option value="NEHRU_NAGAR">NEHRU NAGAR</option>
              <option value="SUPELA">SUPELA</option>
            </select>
            <p id="city-error" style="color:red;"></p>
          </div>
          <div class="form-group">
            <label for="state">State :</label>
            <select class="form-control" id="state" name="state" disabled>
              <option value="Chhattisgarh">Chhattisgarh</option>
            </select>
          </div>

          <div class="form-group">
            <label for="pincode">PIN code :</label>
            <input type="text" name="pincode" value="${addressForEdit.pinCode}" class="form-control" id="pincode" placeholder="Enter six digit PIN code" required>
          </div>

          <input type="hidden" value="${isFromOrder}" name="isfromorder"/>
          <input type="hidden" value="${addressForEdit.addressId}" name="addressIdForEdit"/>
          <input type="hidden" name="button-click" id="button-click"/>
          
          <div class="form-group">
          	<button type="button" class="btn btn-success save-address btn-md" data-click="new" id="add-button">Add</button> 
          </div>
          <div class="form-group">
          	<button type="button" class="btn btn-success btn-md save-address" data-click="edit" id="edit-button">Update</button>
          </div>
          <div class="form-group">
          	<button type="button" class="btn btn-danger btn-md" id="back-click">Back</button>
          </div>
        </form>

      </div>
      
      
    </div>
    <jsp:include page="footer.jsp"></jsp:include>
    <script type="text/javascript">
    	$(document).ready(function(){
    		var addressForEdit = '${addressForEdit}';
    		
    		if(typeof(addressForEdit) !== "undefined" && addressForEdit !== null && addressForEdit !==""){
	  			$("#add-button").css("display", "none");
	  			$("#edit-button").css("display", "block");
	  		}else{
	  			$("#add-button").css("display", "block");
	  			$("#edit-button").css("display", "none");
	  		}
    	});
    
    	$(".save-address").click(function(){
    		if (document.getElementById('fullname').value == "") {
    		      document.getElementById('sam1').innerHTML = "Please enter your full name!";
    		      document.getElementById('fullname').focus();
    		      return false;
    		    } else {
    		      document.getElementById('sam1').innerHTML = "";
    		    }

    		    if (document.getElementById('contactnumber').value == "") {
    		           document.getElementById('sam2').innerHTML = "Please enter only 10 digits of your mobile number";
    		           document.getElementById('contactnumber').focus();
    		           return false;
    		        } else {
   		        	  var phoneno = /^\d{10}$/;  
   		        	  if(document.getElementById('contactnumber').value.match(phoneno)){
   		        		  document.getElementById('sam2').innerHTML = "";
   		        	  }else{
   		        		  document.getElementById('sam2').innerHTML = "Please enter only 10 digits of your mobile number";
   		        		document.getElementById('contactnumber').focus();
     		           return false;
   		        	  }
    		     	}
    		    
    		    if (document.getElementById('email').value == "") {
 		           document.getElementById('email-error').innerHTML = "Please enter your email id";
 		           document.getElementById('email').focus();
 		           return false;
 		        } else {
		        	  var phoneno = /\S+@\S+\.\S+/;  
		        	  if(document.getElementById('email').value.match(phoneno)){
		        		  document.getElementById('email-error').innerHTML = "";
		        	  }else{
		        		  document.getElementById('email-error').innerHTML = "Please enter correct email format like 'example@abc.com'";
		        		  document.getElementById('email').focus();
	 		              return false;
		        	  }
 		     	}

    		    if (document.getElementById('addressline1').value == "") {
    		      document.getElementById('sam3').innerHTML = "Please enter your address!";
    		      document.getElementById('addressline1').focus();
    		      return false;
    		    } else {
    		      document.getElementById('sam3').innerHTML = "";
    		    } 
				
				if(document.getElementById('city').value == "select"){
					document.getElementById('city-error').innerHTML = "Select city option!";
					document.getElementById('city').focus();
	    		    return false;
				}else{
					document.getElementById('city-error').innerHTML = "";
				}
				
				var buttonClick = $(this).data("click");
				$("#button-click").val(buttonClick);
    		   
				$("#address-form").submit();				
    	});
    	
    	$("#back-click").click(function(){
    		window.location.href="placeorder";
    	});
    	
    </script>
</body>
</html>