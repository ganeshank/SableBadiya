<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

		<form role="form" action="" method="post" id="offline_order_form">
		<div class="container">
			<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Customer Name: </b> 
	  				<input type="text" class="form-control" id="customer_name" name="customer_name" placeholder="Enter customer name" required>
	  			</p>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Mobile Number: </b> 
	  				<input type="tel" class="form-control" id="mobile" title="Enter valid 10 digit mobile number" name="mobile_number" pattern=".{10,}" 
	  				placeholder="Enter only 10 digits of your mobile number" required>
	  			</p>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Customer Address: </b> 
	  				<textarea class="form-control" rows="3" id="address"></textarea>
	  			</p>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Email: </b> 
	  				<input type="text" class="form-control" id="email" name="email" placeholder="Enter Email here" required>
	  			</p>
	  		</div>
	  		<div class="col-sm-4">
	  			<p style="font-family:verdana;"><b>Order Date:</b> 
	  				<input type="date" id="order_date" name="order_date">
	  			</p>
	  		</div>
		</div>
	       <div class="container well" style="margin-top:0px;padding-bottom:0px;">
	       <div class="row">
	       		<div class="col-sm-4">
	  			<div class="col-sm-3">
	  				<p style="font-family:verdana;"><b>Goods:</b></p>
	  			</div>
	  			<div class="col-sm-9">
		            <input type="text" class="form-control" id="goods_id" name="goods" placeholder="Enter goods name" required>
		            <!-- <input type="hidden" id="goods_id"> -->
		        </div>
 			</div>
  			
	  		<div class="col-sm-4">
	  			<div class="col-sm-3">
	  				<p style="font-family:verdana;"><b>Quantity:</b></p>
	  			</div>
	  			<div class="col-sm-9">
	  				<select id="qty-goods" class="form-control">
	  					<option value="select">Select Quantity</option>
	  					<c:forEach items="${quantities}" var="quantity" varStatus="status">
	  						<option value="${quantity.quantityId}">${quantity.weight} ${quantity.uom}</option>
	  					</c:forEach>
	  				</select>
		        </div>
	  		</div>
	  		
	  		<div class="col-sm-4">
	  			<div class="col-sm-3">
	  				<p style="font-family:verdana;"><b>Price:</b></p>
	  			</div>
	  			<div class="col-sm-9">
		            <input type="text" class="form-control" id="price" name="price" placeholder="Enter price for goods" required>
		        </div>
 			</div>
	       </div>
		  	
 			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
	 			<div class="col-sm-4">
		  			<input class="btn btn-success" id="addGoods" type="button" value="Add Goods">
	 			</div>
	  		
 			</div>
 			
	  		</div>
	  		
	  		<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
		        <thead>
		            <tr>
		                <th>Goods Name</th>
		                <th>Quantity</th>
		                <th>Price</th>
		                <th>Action</th>
		            </tr>
		        </thead>
		    </table>
	  		
		 </form>
		 
		 
	
	
	<script>
		function validate(){
			$("#additem_form").submit();
		}
		
		$(document).ready(function() {
		    var table = $('#example').DataTable({
		    	"paging":   false,
		        "ordering": false,
		        "info":     false
		    });
		    
		    $(function() {
	           	  $("#goods_id").autocomplete({     
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
		                              response(data);
		                      }
		              });
		      	  },
			      select: function( event, ui ) {
			    	  $("#goods_id")(ui.item.value);
			      }
				});
			});
		    
		    $('#addGoods').on( 'click', function () {
		    	var goodsId = $("#goods_id").val();
		    	var qtyId = $("#qty-goods").val();
		    	var price = $("#price").val();
		    	
		    	$.ajax({
		    		url: "adminhome/offline_order_add_goods",
		    		data:{goodsId:goodsId, qtyId:qtyId, price:price},
		    		dataType:"text",
		    		success: function(response){
		    			table.row.add( [
   				            $("#goods").val(),
   				            $("#qty-goods").text(),
   				            price,
   				            "<input class='btn' type='button' value='Delete'>"
   				        ] ).draw( false );
		    			
		    		},
		    		error: function(response){
		    			console.log(response.status);
		    			console.log(response);
		    		}
		    		
		    	});
		 
		    } );
		 
		} );
		
	
	</script>