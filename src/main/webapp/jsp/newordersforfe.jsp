<body>
	<table id="example" class="display responsive nowrap" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Order Number</th>
                <th>Order Date</th>
                <th>Order Total</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Order Number</th>
                <th>Order Date</th>
                <th>Order Total</th>
                <th>Order Status</th>
                <th>Actions</th>
            </tr>
        </tfoot>
    </table>
    
    
    <button type="button" class="btn btn-info btn-sm order-update-button" data-toggle="modal" data-target="#myModal" style="display: none;"></button>
	  			
	  			<!-- Modal -->
				  <div class="modal fade" id="myModal" role="dialog" style="display: none;">
				    <div class="modal-dialog modal-lg">
				      <div class="modal-content">
				        <div class="modal-header">
				          <button type="button" class="close" data-dismiss="modal">&times;</button>
				          <h4 class="modal-title">Order Update</h4>
				        </div>
				        <div class="modal-body">
				        	<div id="order-dispatch-div">
								<p id="message"></p>
				        		<button type="button" class="btn btn-info btn-sm" onclick="updateOrderStatus();">Ok</button>				        	
				        	</div>
				        	<input type="hidden" id="cartIdForUpdate"/>
				        </div>
				        <div class="modal-footer">
				          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				        </div>
				      </div>
				    </div>
				  </div>
    
</body>
<script type="text/javascript">
var table;
$(document).ready(function() {
	table = $('#example').DataTable( {
        "ajax": "viewnewordersforfe",
        "columns": [
            { "data": "orderNumber" },
            { "data": "orderDate" },
            { "data": "totalAmount" },
            { "data": "cartStatusName" },
            { "data": "viewLink" }
        ],
        "fnDrawCallback": function (oSettings) {
        	$(".new-order-count").text(oSettings.aoData.length);
         }
    } );
} );

function viewNewOrder(cartId){
	
	$.ajax({
		url: "fehome/view_new_order_fe",
		data:{cartId:cartId},
		dataType:"text",
		success: function(response){
			console.log(response);
			$("#main-container").html(response);
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
		}
		
	});
}

function updateOrder(cartId){
	$.ajax({
		url: "fehome/get_order_status_for_update",
		data:{cartId:cartId},
		dataType:"text",
		success: function(response){
			console.log(response);
			if(response=="11"){
				$("#cartIdForUpdate").val(cartId);
				$(".order-update-button").click();
				
				$("#message").text("Order is received from the seller.Click ok button for update.");
			}else if(response=="5"){
				$("#cartIdForUpdate").val(cartId);
				$(".order-update-button").click();
				
				$("#message").text("Order is delivered to the custmomer.Click ok button for update.");
			}else{
				alert("Something went wrong, Please contact to customer support.")
			}
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
		}
		
	});
}

$("#order-deliver-select").change(function(){
	var value = $(this).val();
	if(value=="2"){
		$("#order-deliver-reject").show();
	}else{
		// do nothing
		$("#order-deliver-reject").hide();
	}
});


function updateOrderStatus(){
	var cartId = $("#cartIdForUpdate").val();
	startLoading();
	$.ajax({
		url: "fehome/update_order_status",
		data:{cartId:cartId},
		dataType:"text",
		success: function(response){
			$('#myModal').modal('toggle');
			alert("Successfully updated.");
			
			table.ajax.reload();
			stopLoading();
		},
		error: function(response){
			console.log(response.status);
			console.log(response);
			stopLoading();
		}
		
	});
}
</script>
</html>