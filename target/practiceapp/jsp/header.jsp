<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

	<link rel="shortcut icon" type="image/x-icon" href="media/sb_logo_ico.ico" />
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.0/themes/base/jquery-ui.css">
    <meta name="google-signin-client_id" content="1082344612925-6bfsqivi3u0vmj1i2el7qbc6chnofkn8.apps.googleusercontent.com">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="css/main.css?version=2">
    <link rel="stylesheet" type="text/css" href="css/spinner.css">
    
<style>
	#ui-id-1{
	 z-index: 99999;
	}
</style>
<jsp:include page="loading.jsp"></jsp:include>
<header class="navbar-fixed-top">
      <div id="smallheader" class="container-fluid">
        <div class="row"> 
         
        <!-- Navbar -->
        <nav class="navbar">
          <div class="container-fluid">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="homepage"><img src="media/sb_logo_new_2.gif" alt="brand logo" style="width:90px;margin-top:-11px;" class="hidden-xs"><img src="media/small_logo.jpg" class="visible-xs" style="width:40px;height:38px;margin-top:-7px;margin-left:14px;"></a>
              <div class="visible-xs text-right" id="tom">
                <a href="viewcart" class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-shopping-cart"></span><span class="badge item-count">0</span></a>
              </div>
            </div>
            <div class="collapse navbar-collapse" id="myNavbar">
              <ul class="nav navbar-nav navbar-right">
              	<li class="active"><a href="homepage">Home</a></li>
                <li><a href="aboutus">About Us</a></li>
                <li><a href="contactus">Contact Us</a></li>
                <li><a href="deal_of_the_day">Deal Of The Day</a></li>
                <c:choose>
				  <c:when test="${not empty sessionScope.userDetails.userId}">
				   	<li><a href="#" id="header-user-name">Hi ${sessionScope.userDetails.userName}</a></li>
				   	<li> <a href="javascript:void(0);"><span class="glyphicon glyphicon-earphone"></span> +91-9584069665</a> </li>
				   	<li><a href="myaccount">MyAccount</a></li>
			         <li> <a href="javascript:void(0);" onclick="logout();">Logout</a> </li>
				  </c:when>
				  <c:otherwise>
				     <li> <a href="javascript:void(0);"><span class="glyphicon glyphicon-earphone"></span> +91-9584069665</a> </li>
				     <li> <a href="signup">Sign Up</a> </li>
			         <li> <a href="login">Login</a> </li> 
				  </c:otherwise>
			  </c:choose>
              </ul>
            </div>
          </div>
        </nav> 
          
        </div>
      </div>
      <div id="bigheader" class="container-fluid">
        
        <div class="col-sm-8 col-sm-offset-2 col-xs-12">
          <div class="input-group">
            <input type="text" placeholder="What are you looking for?" id="search" name="search" class="search form-control">
            <span class="input-group-btn">
            <button  type="button" class="btn btn-warning btn-md global-search">Search</button>
            <a href="viewcart" class="btn btn-primary btn-md hidden-xs" id="mycart"><span class="glyphicon glyphicon-shopping-cart"></span> CART <span class="badge item-count">${sessionScope.itemCount}</span></a>
            </span>
          </div>
        </div>
        </div>        
        
        
  
        
    </header><br/><br/><br/><br/>
  <script type="text/javascript">
  $(document).ready(function() {
	  
	      $(function() {
           	  $("#search").autocomplete({     
              source : function(request, response) {
	              $.ajax({
	                      url : "globalsearch",
	                      type : "GET",
	                      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	                      data : {
	                              term : request.term
	                      },
	                      dataType : "json",
	                      success : function(data) {
	                    	  response($.map(data, function (item) {
									
                    	            return {
                    	                label: item.value,
                    	                value: item.value,
                    	                id: item.key
                    	            };
	                    	  }));
	                      }
	              });
	      	  },
		      select: function( event, ui ) {
		    	  //console.log(ui.item);
		    	  redirectToGoodsBasedOnSearchValue(ui.item.label);
		      }
			});
		});
	      
	      //alert("Our services are not available on 31st October & 01st November. Sorry for your inconvenient");
	      onLoad();
	   
  });
  
  
  function redirectToGoodsBasedOnSearchValue(searchVal){
	  location.href = "getgoods?searchVal="+searchVal+"&isFromSearch=true";
  }
  
  $(".global-search").click(function(){
  	  var searchVal = $(".search").val();
  	  redirectToGoodsBasedOnSearchValue(searchVal);
  });
  
$('#search').keypress(function (e) {
  var key = e.which;
  if(key == 13)  // the enter key code
   {
	  $(".global-search").click();
      return false;  
   }
 });
 
 function logout(){
	 signOut();
	 document.location.href = "http://sablebadiya.com/practiceapp/logout";
 }
 
 function onLoad() {
	 gapi.load('client:auth2', function() {
	       gapi.auth2.init();
	     });
   }
 
 function signOut() {
     var auth2 = gapi.auth2.getAuthInstance();
     auth2.signOut().then(function () {
       console.log('User signed out.');
     });
   }

   
 
  </script>
  <script src="js/header.js"></script>
  <script src="js/main.js"></script>
</body>
</html>