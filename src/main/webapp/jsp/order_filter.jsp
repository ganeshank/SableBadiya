<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="container">
  		
		<h2>Search Goods:</h2>
		
		<div class="container" >
			<form id="order-date-div">
				<div class="form-group">
				  <div class="col-sm-2 label-class">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Goods Name</label>
			      </div>
				  <div class="col-sm-2">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Unit Price</label>
				  </div>
				  <div class="col-sm-2">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag">UOM</label>
				  </div>
				  <div class="col-sm-2">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag qty">Quantity</label>
				  </div>
				  <div class="col-sm-2">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Total</label>
				  </div>
				  <div class="col-sm-2">
				  	<label for="orderdate" data-lebel="orderDate" class="label-tag">Action</label>
				  </div>
			      
			    </div>
			    
			    <div class="form-group">
				  <div class="col-sm-2 label-class">
				  	<input type="text" class="form-control" name="goods_name" id="goods_name">
				  	<input type="hidden" class="form-control" name="goods_id" id="goods_id">
			      </div>
				  <div class="col-sm-2">
				  	<input type="text" class="form-control" name="unit_price" id="unit_price">
				  </div>
				   <div class="col-sm-2">
				  	<input type="text" class="form-control" name="uom" id="uom">
				  </div>
				  <div class="col-sm-2">
				  	<input type="text" class="form-control" name="qty" id="quantity">
				  </div>
				  <div class="col-sm-2">
				  	<input type="text" class="form-control" name="total" id="total">
				  </div>
				  <div class="col-sm-2">
				  	<button type="button" name="goods_name" class="btn btn-info add-item">Add</button>
				  </div>
			      
			    </div>
			    <br/><br/>
			    <div class="form-group">
			    	<div class="col-sm-6">
			    		<label for="orderdate" data-lebel="orderDate" class="label-tag">
			    		Total Amount: <span id="totalAmount">Rs. 0</span></label>
			    	</div>
			    	
			    	<div class="col-sm-6">
			    		<button type="button" class="btn btn-info order-proceed">Order Proceed</button>
			    	</div>
			    </div>
			</form>
			
			
		</div>
		<br/><br/>
		
		<table id="example" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Name</th>
                <th>Unit Price</th>
                <th>Quantity</th>
                <th>Total</th>
                <th>Action</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Name</th>
                <th>Unit Price</th>
                <th>Quantity</th>
                <th>Total</th>
                <th>Action</th>
            </tr>
        </tfoot>
    </table>

  	</div>
  	
  	
  	
  	
  	<script type="text/javascript">
  $(document).ready(function() {
	  
	      $(function() {
           	  $("#goods_name").autocomplete({     
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
                    	                id: item.key,
                    	                price: item.unitPrice,
                    	                qtyPerUom: item.qtyPerUom,
                    	                uom: item.uom
                    	            };
	                    	  }));
	                      }
	              });
	      	  },
		      select: function( event, ui ) {
		    	  console.log(ui);
		    	  $("#unit_price").val(ui.item.price + '/' + ui.item.uom);
		    	  $("#quantity").val(ui.item.qtyPerUom);
		    	  $("#total").val(ui.item.price);
		    	  $("#uom").val(ui.item.uom);
		    	  $("#goods_id").val(ui.item.id);
		    	 // redirectToGoodsBasedOnSearchValue(ui.item.label);
		      }
			});
		});
	      
	      
	      var t = $('#example').DataTable();
	      var counter = 1;
	      
	      $("#quantity").change(function(){
	    	
	    	  var unitPrice = parseFloat($("#unit_price").val());
	    	  var qty = parseFloat($("#quantity").val());
	    	  
	    	  var price = unitPrice * qty;
	    	  
	    	  console.log(price);
	    	  
	    	  $("#total").val(price);
	    	  
	      });
	      
	      
	      $(".add-item").click(function(){
	    	  var goodsId = $("#goods_id").val();
	    	  var quantity = $("#quantity").val();
	    	  var total = $("#total").val();
	    	  
	    	  $.ajax({
	  				url: "adminhome/add_item",
	  				data:{goodsId:goodsId, quantity:quantity, total:total},
	  				dataType:"text",
	  				success: function(response){
	  					var item = JSON.parse(response);
	  					console.log(item);
	  					t.row.add( [
	  				              item.goodsVo.goodsName,
	  				              item.goodsVo.price + "/" + item.goodsVo.uom,
	  				              item.quantity + " " + item.goodsVo.uom,
	  				              item.price,
	  				              "Delete"
	  				          ] ).draw( false );
	  				   
	  				          counter++;
	  				          
	  				    $("#totalAmount").text("Rs. " + item.totalOrderAmount);
	  				},
	  				error: function(response){
	  					stopLoading();
	  					alert("Something went wrong, Please contact to technical specialist.");
	  					console.log(response.status);
	  					console.log(response);
	  				}
	  				
	  			});
	    	  
	      })
	      
	      $(".order-proceed").click(function(){
	    	  $.ajax({
	    			url: "adminhome/add_address_for_order",
	    			dataType:"text",
	    			success: function(response){
	    				
	    				$("#main-container").html(response);
	    			},
	    			error: function(response){
	    				console.log(response.status);
	    				console.log(response);
	    			}
	    			
	    		});
	    	  
	      });
	   
  });
  
  </script>