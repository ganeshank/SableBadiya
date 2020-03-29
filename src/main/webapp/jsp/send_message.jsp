<div class="container col-sm-4 col-sm-offset-4">
		<div class="form-group">
  			<label for="name">UserName:</label>
  			<input type="text" class="form-control" id="userName" placeholder="Enter Username" required>
		</div>
		<div class="form-group">
  			<label for="name">Password:</label>
  			<input type="password" class="form-control" id="password" placeholder="Enter Password" required>
		</div>
		<div class="form-group">
  			<input class="btn btn-success" type="button" value="SendMessage" onclick="validateAndSend()">
		</div>
</div>

<script>
	function validateAndSend(){
		var username = $("#userName").val();
		var password = $("#password").val();
		
		
		if(username=="" || password ==""){
			alert("Username or Password can not be empty!!");
			return false;
		}else{
			startLoading();
			$.ajax({
				url: "rest/user/senddealmessage",
				type: "POST",
				data:{username:username, password:password},
				dataType:"text",
				contentType: "application/x-www-form-urlencoded",
				success: function(response){
					stopLoading();
					$("#userName").val("");
					$("#password").val("");
					alert(response);
				},
				error: function(response){
					stopLoading();
					alert(response);
				}
			});
		}
	}
</script>