<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- Navigation -->
    <nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand" href="#page-top">SABLEBADIYA.${sessionScope.userDetails.userRole.roleName}</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="hidden">
                        <a href="#page-top"></a>
                    </li>
                    <li class="page-scroll">
                        
                    </li>
                    <li class="active"><a href="javascript:void(0);" class="btn tab-click" data-tab="new_orders" id="mycart"><span class="glyphicon glyphicon-shopping-cart"></span> New Orders <span class="badge new-order-count"></span></a></li>
      <li><a href="javascript:void(0);" class="btn tab-click"  data-tab="check_orders"><span class="glyphicon glyphicon-shopping-cart"></span> Check Orders(Date Wise)</a></li>
      <li><a href="javascript:void(0);" class="btn tab-click"  data-tab="recent_approved_orders"><span class="glyphicon glyphicon-shopping-cart"></span> Recent Approved Orders</a></li>
                    <li class="page-scroll">
                        <a href="javascript:void(0);">Hi ${sessionScope.userDetails.userName}</a>
                    </li>
                    <li class="page-scroll">
                        <a href="logout">Logout</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
    
   <%--  <nav class="navbar navbar-default" style="padding-top: 52px;">
  <div class="container-fluid">
    <div class="navbar-header">
      
    </div>
    <ul class="nav navbar-nav navbar-left">
    <li><a href="javascript:void(0);">${sessionScope.pageName}</a></li>
    </ul>
    <ul class="nav navbar-nav navbar-right">
      
      <li class="active"><a href="javascript:void(0);" class="btn tab-click" data-tab="new_orders" id="mycart"><span class="glyphicon glyphicon-shopping-cart"></span> New Orders <span class="badge new-order-count"></span></a></li>
      <li><a href="javascript:void(0);" class="btn tab-click"  data-tab="check_orders"><span class="glyphicon glyphicon-shopping-cart"></span> Check Orders(Date Wise)</a></li>
      <li><a href="javascript:void(0);" class="btn tab-click"  data-tab="recent_approved_orders"><span class="glyphicon glyphicon-shopping-cart"></span> Recent Approved Orders</a></li>
      <li><a href="#">Page 3</a></li>
    </ul>
  </div>
</nav> --%>

<script>
$(".tab-click").click(function(){
	var tabName = $(this).data("tab");
	//alert(tabName);
	if(tabName == "new_orders"){
		loadContainer("sellerhome/new_orders_for_seller");
	}else if(tabName == "check_orders"){
		loadContainer("sellerhome/order_status_check_page");
	}else if(tabName == "recent_approved_orders"){
		loadContainer("sellerhome/recent_approved_orders");
	}
	
});
</script>