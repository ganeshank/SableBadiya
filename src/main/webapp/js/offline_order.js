
		
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
			    	  console.log(ui.item.id);
			    	  $("#g_id").val(ui.item.id);
			      }
				});
			});
		    
		    $('#addGoods').on( 'click', function () {
		    	var goodsId = $("#g_id").val();
		    	var qtyId = $("#qty-goods").val();
		    	var price = $("#price").val();
		    	
		    	if(!validateGoodsInput()){
		    		return false;
		    	}
		    	
		    	$.ajax({
		    		url: "adminhome/offline_order_add_goods",
		    		data:{goodsId:goodsId, qtyId:qtyId, price:price},
		    		dataType:"text",
		    		success: function(response){
		    			table.row.add( [
   				            $("#goods_id").val(),
   				            $("#qty-goods :selected").text(),
   				            price,
   				            "<div><input class='btn delete-item' data-goods='"+goodsId+"' type='button' value='Delete'></div>"
   				        ] ).draw( false );
		    			
		    			$("#goods_id").val("");
		    			$("#g_id").val(0);
		    			$("#price").val("");
		    			$("#qty-goods").val("select");
		    			
		    		},
		    		error: function(response){
		    			console.log(response.status);
		    			console.log(response);
		    		}
		    		
		    	});
		 
		    } );
		    
		    $(document).off('click', '.delete-item').delegate(".delete-item","click",function() {
		    	var goodsId = $(this).data("goods");
		    	var thisKey = $(this);
		    	$.ajax({
		    		url: "adminhome/offline_order_item_delete",
		    		data:{goodsId:goodsId},
		    		dataType:"text",
		    		success: function(response){
		    			table
		    	        .row( thisKey.parents('tr') )
		    	        .remove()
		    	        .draw();
		    			
		    		},
		    		error: function(response){
		    			console.log(response.status);
		    			console.log(response);
		    		}
		    		
		    	});
		    });
		    
		    function validateGoodsInput(){
		    	var goodsId = $("#g_id").val();
		    	var qtyId = $("#qty-goods").val();
		    	var price = $("#price").val();
		    	
		    	if(goodsId==0){
		    		alert("Please select the correct goods name.");
		    		return false;
		    	}else if(qtyId=="select"){
		    		alert("Please select the quantity for goods.");
		    		return false;
		    	}else if(!(/^\d+$/.test(price))){
		    		alert("Please check the price. It should be a number");
		    		return false;
		    	}else{
		    		return true;
		    	}
		    	
		    }
		    
		    $("#create_order").click(function(){
		    	if($("#customer_name").val()==""){
		    		alert("Please fill customer name.");
		    		return false;
		    	}else if($("#mobile").val()==""){
		    		alert("Please fill contact number.");
		    		return false;
		    	}else if(!$("#mobile").val().match("/^\d{10}$/")){
		    		alert("Please enter 10 digit contact number.");
		    		return false;
		    	}else if($("#address").val()==""){
		    		alert("Please fill customer address.");
		    		return false;
		    	}else if($("#email").val()==""){
		    		alert("Please enter email id.");
		    		return false;
		    	}else if(!$("#email").val().match("/\S+@\S+\.\S+/")){
		    		alert("Please enter valid email id.");
		    		return false;
		    	}else if($("#order_date").val()==""){
		    		alert("Please enter order date.");
		    		return false;
		    	}else{
		    		$("#offline_order_form").submit();
		    	}
		    });
		    
		    

		} );
		