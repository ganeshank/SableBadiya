<jsp:include page="loading.jsp"></jsp:include>
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
                     <li class="active">
                     	<a href="javascript:void(0);" class="btn tab-click" data-tab="new_orders" id="mycart"><span class="glyphicon glyphicon-shopping-cart"></span>New Orders<span class="badge new-order-count"></span></a>
                     </li>
      				<!-- <li>
      					<a href="javascript:void(0);" class="btn tab-click" data-tab="seller_pending_rejected_orders"><span class="glyphicon glyphicon-shopping-cart"></span> Pending/Rejected Orders <span class="badge seller-pending-reject-count"></span></a>
      				</li> -->
      				<li>
      					<a href="javascript:void(0);" class="btn tab-click" data-tab="check_order_status"><span class="glyphicon glyphicon-shopping-cart"></span> Check Order Status</a>
      				</li>
                    <li>
                        <a href="javascript:void(0);" class="btn tab-click" data-tab="update_price">Update Price</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" class="btn tab-click" data-tab="add_goods">Add Goods</a>
                    </li>
                     <!-- <li>
                        <a href="javascript:void(0);" class="btn tab-click" data-tab="offline_order">Offline Order</a>
                     </li> -->
                     <!-- <li>
                        <a href="javascript:void(0);" class="btn tab-click" data-tab="send_dotd_message">Send Message</a>
                    </li> -->
                    <li>
                        <a href="javascript:void(0);" class="btn tab-click" data-tab="order_filter">Order Filter</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);">Hi ${sessionScope.userDetails.userName}</a>
                    </li>
                    <li>
                        <a href="logout">Logout</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>

<script>
$(".tab-click").click(function(){
	var tabName = $(this).data("tab");
	//alert(tabName);
	if(tabName == "new_orders"){
		loadContainer("adminhome/neworders_view");
	}/* else if(tabName == "seller_approved_orders"){
		loadContainer("adminhome/sellerapproved_view");
	} */else if(tabName == "seller_pending_rejected_orders"){
		loadContainer("adminhome/seller_pending_reject_order");
	}else if(tabName == "check_order_status"){
		loadContainer("adminhome/order_status_check_page");
	}else if(tabName == "update_price"){
		loadContainer("adminhome/update_price");
	}else if(tabName == "add_goods"){
		loadContainer("adminhome/add_goods");
	}else if(tabName == "offline_order"){
		loadContainer("adminhome/offline_order");
	}else if(tabName == "send_dotd_message"){
		loadContainer("adminhome/send_message");
	}else if(tabName == "order_filter"){
		loadContainer("adminhome/order_filter");
	}
	
	
});
</script>