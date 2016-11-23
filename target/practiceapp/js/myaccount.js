function myfunction() {
	document.getElementById('demo').style.display='block';
	document.getElementById('div1').style.display='none';

}

function backToView(){
	document.getElementById('demo').style.display='none';
	document.getElementById('div1').style.display='block';
}


function validation1() {
	if (document.getElementById('fullname').value == "") {
           document.getElementById('sam1').innerHTML = "Please enter your fullname!";
           document.getElementById('fullname').focus();
           return false;
        } else {
          document.getElementById('sam1').innerHTML = "";
        }

    if (document.getElementById('emailid').value == "") {
           document.getElementById('sam2').innerHTML = "Please enter registered Email Id!";
           document.getElementById('emailid').focus();
           return false;
        } else {
          document.getElementById('sam2').innerHTML = "";
        }

    if (document.getElementById('contactno').value == ""  || !(/^\d+$/.test(document.getElementById('contactno').value)) || document.getElementById('contactno').value.length>10) {
           document.getElementById('sam3').innerHTML = "Please enter only 10 digits of your mobile number!";
           document.getElementById('contactno').focus();
           return false;
        } else {
          document.getElementById('sam3').innerHTML = "";
        }        
    
    var name = document.getElementById('fullname').value;
    var email = document.getElementById('emailid').value;
    var mobile = document.getElementById('contactno').value;
    
    $.ajax({
		url: "save_personal_info",
		dataType:"json",
		data: {name:name, email:email, mobile:mobile},
		success: function(response){
			$("#name-view").text(response.fullName);
			$("#email-view").text(response.email);
			$("#mobile-view").text(response.mobileNumber);
			$("#header-user-name").text(response.fullName);
			
			backToView();
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
		}
		
	});
}

function validation3() {
	document.getElementById('dom4').innerHTML = "";
	if (document.getElementById('oldpassword').value == "") {
           document.getElementById('dom1').innerHTML = "Please enter your Old Password!";
           document.getElementById('oldpassword').focus();
           return false;
        } else {
          document.getElementById('dom1').innerHTML = "";
        }


    if (document.getElementById('newpassword').value == "") {
           document.getElementById('dom2').innerHTML = "Please enter your New Password!";
           document.getElementById('newpassword').focus();
           return false;
        } else {
          document.getElementById('dom2').innerHTML = "";
        }  



    if (document.getElementById('confirmpassword').value == "") {
           document.getElementById('dom3').innerHTML = "Retype Password!";
           document.getElementById('confirmpassword').focus();
           return false;
        }  else {
          document.getElementById('dom3').innerHTML = "";
        }   


    if (document.getElementById('newpassword').value != document.getElementById('confirmpassword').value) {
          document.getElementById('dom3').innerHTML = "Password mismatch";
             myform3.password1.focus();
             return false;
      }       
    
    var oldPassword = document.getElementById('oldpassword').value;
    var newPassword = document.getElementById('newpassword').value;
    
    startLoading();
    $.ajax({
		url: "change_password",
		dataType:"text",
		data: {oldPassword:oldPassword, newPassword:newPassword},
		success: function(response){
			$("#oldpassword").val("");
			$("#newpassword").val("");
			$("#confirmpassword").val("");
			
			if(response=="password is modified successfully."){
				document.getElementById('dom4').innerHTML = response;
			}else{
				document.getElementById('dom3').innerHTML = response;
			}
			stopLoading();
			//window.location.href="logout";
		},
		error: function(response){
			document.getElementById('dom3').innerHTML = response;
			stopLoading();
		}
		
	});
    
}

function cancel() {
	document.getElementById('cancelorder').innerHTML = "Your item is cancelled!"
}


$(".cancel-click").click(function(){
	startLoading();
	var clickId = $(this).data("cancelid");
	alert(clickId);
	
	var thisVar = $(this).parent("td");
	
	$.ajax({
		url: "fehome/cancle_order_or_item",
		data: {clickId:clickId},
		dataType:"text",
		success: function(response){
			console.log(response);
			thisVar.html("Cancelled.")
			stopLoading();
			alert("Canceled successfully!!!");
		},
		error: function(response){
			stopLoading();
		}
		
	});
	
});