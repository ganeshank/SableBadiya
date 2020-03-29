<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="container">
  		
		<h2>Search By:</h2>
		
		<form>
		    <label class="radio-inline">
		      <input type="radio" name="optradio" value="order_number" checked="checked">Order Number
		    </label>
		    <label class="radio-inline">
		      <input type="radio" name="optradio" value="phone_number" >Phone Number
		    </label>
		    <label class="radio-inline">
		      <input type="radio" name="optradio" value="email" >Email Id
		    </label>
		    <label class="radio-inline">
		      <input type="radio" name="optradio" value="order_by">Ordered By
		    </label>
		    <label class="radio-inline">
		      <input type="radio" name="optradio" value="order_date">Order Date
		    </label>
		</form>

		<div class="container" >
			<form id="order-number-div">
				<div class="form-group">
				  <div class="col-sm-4 label-class">
				  	<label for="usr" data-lebel="orderNumber" class="label-tag">Order Number :</label>
			      </div>
				  <div class="col-sm-4 col-sm-pull-2 input-div">
				  	<input type="text" class="form-control input-box" id="usr">
				  </div>
				  <div class="col-sm-4 col-sm-pull-2">
				  	<button type="button" class="btn btn-info search-click">SEARCH</button>
				  </div>
			      
			    </div>
			</form>

			<form id="phone-number-div" style="display:none;">
				<div class="form-group">
				  <div class="col-sm-4 label-class">
				  	<label for="tele" data-lebel="phoneNumber" class="label-tag">Phone Number :</label>
			      </div>
				  <div class="col-sm-4 col-sm-pull-2 input-div">
				  	<input type="tel" class="form-control input-box" id="tele">
				  </div>
				  <div class="col-sm-4 col-sm-pull-2">
				  	<button type="button" class="btn btn-info search-click">SEARCH</button>
				  </div>
			      
			    </div>
			</form>

			<form id="email-div" style="display:none;">
				<div class="form-group">
				  <div class="col-sm-4 label-class">
				  	<label for="email" data-lebel="email" class="label-tag">Email Id :</label>
			      </div>
				  <div class="col-sm-4 col-sm-pull-2 input-div">
				  	<input type="email" class="form-control input-box" id="email">
				  </div>
				  <div class="col-sm-4 col-sm-pull-2">
				  	<button type="button" class="btn btn-info search-click">SEARCH</button>
				  </div>
			      
			    </div>
			</form>

			<form id="order-by-div" style="display:none;">
				<div class="form-group">
				  <div class="col-sm-4 label-class">
				  	<label for="orderedby" data-lebel="orderBy" class="label-tag">Ordered By :</label>
			      </div>
				  <div class="col-sm-4 col-sm-pull-2 input-div">
				  	<input type="text" class="form-control input-box" id="orderedby">
				  </div>
				  <div class="col-sm-4 col-sm-pull-2">
				  	<button type="button" class="btn btn-info search-click">SEARCH</button>
				  </div>
			      
			    </div>
			</form>

			<form id="order-date-div" style="display:none;">
				<div class="form-group">
				  <div class="col-sm-3 label-class">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Order Date :</label>
			      </div>
				  <div class="col-sm-3">
				  	From Date:
				  	<input type="date" id="fromDate" name="fdate">
  					
				  </div>
				  <div class="col-sm-3">
				  	To Date:
				  	<input type="date" id="toDate" name="tdate">
  					
				  </div>
				  <div class="col-sm-3">
				  	<button type="button" class="btn btn-info search-click">SEARCH</button>
				  </div>
			      
			    </div>
			</form>
		</div>

  	</div>
  	
  	
  	<!-- <div style="display: none;" id="table-div"> -->
  		<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Order Number</th>
                <th>Total Amount</th>
                <th>Order Date</th>
                <th>User Name</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Order Number</th>
                <th>Total Amount</th>
                <th>Order Date</th>
                <th>User Name</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </tfoot>
    </table>
  	<!-- </div> -->
  	
  	<script>
  	$('input[type=radio][name=optradio]').change(function() {
        var radioClick = $(this).val();
        $(".input-box").val("");
        
        if(radioClick == "order_number"){
        	$("#order-number-div").show();
        	$("#phone-number-div").hide();
        	$("#email-div").hide();
        	$("#order-by-div").hide();
        	$("#order-date-div").hide();
        }else if(radioClick == "phone_number"){
        	$("#order-number-div").hide();
        	$("#phone-number-div").show();
        	$("#email-div").hide();
        	$("#order-by-div").hide();
        	$("#order-date-div").hide();
        }else if(radioClick == "email"){
        	$("#order-number-div").hide();
        	$("#phone-number-div").hide();
        	$("#email-div").show();
        	$("#order-by-div").hide();
        	$("#order-date-div").hide();
        }else if(radioClick == "order_by"){
        	$("#order-number-div").hide();
        	$("#phone-number-div").hide();
        	$("#email-div").hide();
        	$("#order-by-div").show();
        	$("#order-date-div").hide();
        }else if(radioClick == "order_date"){
        	$("#order-number-div").hide();
        	$("#phone-number-div").hide();
        	$("#email-div").hide();
        	$("#order-by-div").hide();
        	$("#order-date-div").show();
        }else{
        	$("#order-number-div").show();
        	$("#phone-number-div").hide();
        	$("#email-div").hide();
        	$("#order-by-div").hide();
        	$("#order-date-div").hide();
        }
        
    });
  	
  	var Table;
  	$(document).ready(function(){
  		
  		Table = $("#example").DataTable({
			data:[],
			columns: [
				{ "data": "orderNumber" },
	            { "data": "totalAmount" },
	            { "data": "orderDate" },
	            { "data": "orderedBy" },
	            { "data": "cartStatusName" },
	            { "data": "viewLink" }
			]
			});
  		
  		$("#example_wrapper").hide();
  	})
  	
  	$(".search-click").click(function(){
  		var searchBy = $(this).parent('div').siblings('.label-class').children('.label-tag').data('lebel');
  		
  		var searchValue, fromDate, toDate;
  		if(searchBy == "orderDate"){
  			fromDate = $("#fromDate").val();
  			toDate = $("#toDate").val();
  			//alert(fromDate+"....."+toDate);
  		}else{
  			searchValue = $(this).parent('div').siblings('.input-div').children('input').val();
  		} 
  		
		
		$.ajax({
			url: "ordersearch",
			type: "get",
			cache: false,
			data: {searchBy:searchBy, searchValue:searchValue, fromDate:fromDate, toDate:toDate}
			}).done(function (result) {
				$("#example_wrapper").show();
				//console.log(result);
				Table.clear().draw();
				Table.rows.add(result.data).draw();
			}).fail(function (jqXHR, textStatus, errorThrown) {
			
			});
			
  	});
  	
  	function viewOrderHistory(cartId){
  		$.ajax({
  			url: "adminhome/view_order_history",
  			data:{cartId:cartId},
  			dataType:"text",
  			success: function(response){
  				
  				$("#main-container").html(response);
  			},
  			error: function(response){
  				console.log(response.status);
  				console.log(response);
  			}
  			
  		});
  	}
  	</script>